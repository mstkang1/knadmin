package com.knpharm.knadmin.web;

import com.knpharm.knadmin.dto.StoreDto;
import com.knpharm.knadmin.excel.ExcelReader;
import com.knpharm.knadmin.service.store.StoreService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;


@Controller
@RequestMapping("store")
public class StoreController {


    private Logger logger = LoggerFactory.getLogger(StoreController.class);

    @Autowired
    private StoreService storeService;

    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.client.secret}")
    private String naverSecret;

    @Value("${naver.url.geocoding}")
    private String naverGeocodingUrl;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @RequestMapping("/excel")
    public  String store(Model model) {
        return "store/excel";
    }


    @ResponseBody
    @RequestMapping(value = "/storeList", method = RequestMethod.POST)
    public Map<String, Object>  storeList(@RequestBody StoreDto storeDto) throws Exception {

        Map<String, Object> rtnObj = new HashMap<>();

        List<StoreDto> storeList = storeService.selectStoreList(storeDto);
        //logger.info("storeList -> " + storeList.toString());

        rtnObj.put("storeList", storeList);
        return rtnObj;
    }

    @ResponseBody
    @RequestMapping(value = "/excelUpload", method = RequestMethod.POST)
    public Map<String, Object> excelUpload(
            @RequestParam("excelFile") MultipartFile excelFile) throws Exception {

        Map<String, Object> rtnObj = new HashMap<>();

        if (excelFile.isEmpty()) {
            rtnObj.put("result", "fail");
            rtnObj.put("message", "잘못된 접근입니다.");
            return rtnObj;
        } else {
            if (!excelFile.isEmpty()) {
                String originName = excelFile.getOriginalFilename();//파일이름
                String exten = originName.substring(originName.lastIndexOf("."), originName.length());//확장자

                if (!(exten.equalsIgnoreCase(".xls") || exten.equalsIgnoreCase(".xlsx"))) {
                    rtnObj.put("result", "fail");
                    rtnObj.put("message", "잘못된 형식의 파일입니다.(xls, xlsx만 가능)");
                    return rtnObj;
                }
                else {

                    String excelPath = uploadPath + File.separatorChar + "excel" + File.separatorChar;


                    File destFile = new File(excelPath + excelFile.getOriginalFilename());


                    try {
                        excelFile.transferTo(destFile);
                    } catch (IllegalStateException | IOException e) {
                        e.printStackTrace();
                    }



                    String filePath = excelPath + excelFile.getOriginalFilename();



                    ExcelReader excelReader = new ExcelReader();
                    System.out.println("********xlsx******");



                    String extention = StringUtils.getFilenameExtension(filePath);
                    List<StoreDto> storeList = new ArrayList<>();

                    if(extention.equals("xls")) {
                        storeList = excelReader.xlsToStoreList(filePath);
                    }else {
                        storeList = excelReader.xlsxToStoreList(filePath);
                    }

                    /*for(int i = 0; i <storeList.size(); i++){
                     StoreDto store = searchGeoCoding(storeList.get(i).getAddress());
                         storeList.get(i).setLongitude(store.getLongitude());
                         storeList.get(i).setLatitude(store.getLatitude());
                         storeService.insertStoreEach(storeList.get(i));
                    }*/
                    int cnt = 0;
                    DecimalFormat decFormat = new DecimalFormat("###,###");

                    try{
                        for(int i = 0; i <storeList.size(); i++){
                            StoreDto store = searchGeoCoding(storeList.get(i).getAddress());
                            storeList.get(i).setLongitude(store.getLongitude());
                            storeList.get(i).setLatitude(store.getLatitude());
                            storeService.insertStoreEach(storeList.get(i));
                            cnt++;
                        }
                        //storeService.insertStore(storeList);
                        storeService.changeStore();



                        rtnObj.put("result", "success");
                        rtnObj.put("message", decFormat.format(cnt) + "건이 정상적으로 업로드되었습니다.");
                        return rtnObj;
                    } catch (Exception ex) {
                        ex.printStackTrace();

                        storeService.rollbackStore();

                        rtnObj.put("result", "fail");
                        rtnObj.put("message", decFormat.format(cnt + 2) + "열에서 오류가 발생하였습니다.");
                        return rtnObj;
                    }
                    
                }
            }
        }



        //rtnObj.put("result", "success");
        return rtnObj;
    }

    private StoreDto searchGeoCoding(String address) {
        StoreDto store = new StoreDto();

        try {
            String addr = URLEncoder.encode(address, "UTF-8");

            // Geocoding 개요에 나와있는 API URL 입력.
            String apiURL = naverGeocodingUrl + "?query=" + addr;    // JSON

            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // Geocoding 개요에 나와있는 요청 헤더 입력.
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", naverClientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", naverSecret);

            // 요청 결과 확인. 정상 호출인 경우 200
            int responseCode = con.getResponseCode();

            BufferedReader br;

            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;

            StringBuffer response = new StringBuffer();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();

            JSONTokener tokener = new JSONTokener(response.toString());
            JSONObject object = new JSONObject(tokener);
            JSONArray arr = object.getJSONArray("addresses");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject temp = (JSONObject) arr.get(i);
                /*System.out.println("address : " + temp.get("roadAddress"));
                System.out.println("jibunAddress : " + temp.get("jibunAddress"));
                System.out.println("위도 : " + temp.get("y"));
                System.out.println("경도 : " + temp.get("x"));*/
                store.setLatitude(Float.parseFloat(temp.get("y").toString()));
                store.setLongitude(Float.parseFloat(temp.get("x").toString()));
            }

            return store;

            // JSON.simple 사용한 경우 아래와 같이 진행.
			/*JSONParser jpr = new JSONParser();
			JSONObject jarr = (JSONObject) jpr.parse(response.toString());
			JSONArray arr2 = (JSONArray) jarr.get("addresses");

			for (int i = 0; i < arr2.length(); i++) {
				JSONObject temp = (JSONObject) arr.get(i);
				System.out.println("address : " + temp.get("roadAddress"));
				System.out.println("jibunAddress : " + temp.get("jibunAddress"));
				System.out.println("위도 : " + temp.get("y"));
				System.out.println("경도 : " + temp.get("x"));
			}*/

        } catch (Exception e) {
            System.out.println(e);
            return store;
        }
    }
}
