package com.knpharm.knadmin.web;

import com.knpharm.knadmin.dto.PopupDto;
import com.knpharm.knadmin.dto.ProductDto;
import com.knpharm.knadmin.service.product.ProductService;
import com.knpharm.knadmin.service.store.StoreService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductController {

    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreService storeService;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @RequestMapping(value = "/list/{brandCode}")
    public  String list(@PathVariable("brandCode") String brandCode, Model model) throws Exception {

        List<ProductDto> productList = productService.selectProductList(brandCode);

        model.addAttribute("menuCode", "product");
        model.addAttribute("brandCode", brandCode);

        model.addAttribute("productList", productList);

        return "product/list";
    }

    @RequestMapping(value = "/write/{brandCode}")
    public  String write(@PathVariable("brandCode") String brandCode, Model model) throws Exception {

        model.addAttribute("menuCode", "product");
        model.addAttribute("brandCode", brandCode);

        return "product/write";
    }

    @RequestMapping(value = "/edit/{brandCode}/{productName}")
    public  String edit(@PathVariable("brandCode") String brandCode, @PathVariable("productName") String productName, Model model) throws Exception {

        ProductDto product = productService.selectProduct(productName);

        model.addAttribute("menuCode", "product");
        model.addAttribute("brandCode", brandCode);
        model.addAttribute("productName", productName);
        model.addAttribute("product", product);

        return "product/edit";
    }

    @ResponseBody
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public Map<String, Object> modify(
            ProductDto product,@RequestParam("productFile") MultipartFile[] files ) throws Exception {

        Map<String, Object> rtnObj = new HashMap<>();

        if(!product.getProductName().equals(product.getOldProductName())) {
            ProductDto oldProcuct = productService.selectProduct(product.getProductName());
            if(oldProcuct != null) {
                rtnObj.put("result", "fail");
                rtnObj.put("message", "이미 등록된 제품명입니다.");
                return rtnObj;
            }
        }


        if (files.length < 0) {
            rtnObj.put("result", "fail");
            rtnObj.put("message", "잘못된 접근입니다.");
            return rtnObj;
        } else {
            MultipartFile productFile = files[0];
            if (!productFile.isEmpty()) {
                String originName = productFile.getOriginalFilename();//파일이름
                String exten = originName.substring(originName.lastIndexOf("."), originName.length());//확장자
                String random = "";//랜덤값
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS");
                int rndNum = (int) (Math.random() * 100000);
                random = sdf.format(new Date(System.currentTimeMillis())) + "_" + rndNum + exten;
                productFile.transferTo(new File(random));

                product.setProductOrgFileName(originName);
                product.setProductSaveFileName(random);

                if (!(exten.equalsIgnoreCase(".jpg") || exten.equalsIgnoreCase(".jpeg") || exten.equalsIgnoreCase(".gif") ||
                        exten.equalsIgnoreCase(".png") || exten.equalsIgnoreCase(".bmp") ||
                        exten.equalsIgnoreCase(".pdf") || exten.equalsIgnoreCase(".zip") || exten.equalsIgnoreCase(".docx") ||
                        exten.equalsIgnoreCase(".doc"))) {
                    rtnObj.put("result", "fail");
                    rtnObj.put("message", "잘못된 형식의 파일입니다.(jpg, jpeg, gif, png, bmp, pdf, zip, doc, docx만 가능)");
                    return rtnObj;
                }
            }
        }

        logger.info("product -> " + product.toString());
        productService.updateProduct(product);
        storeService.updateStoreProduct(product);

        rtnObj.put("result", "success");
        return rtnObj;
    }


    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Map<String, Object> create(
            ProductDto product,@RequestParam("productFile") MultipartFile[] files ) throws Exception {

        Map<String, Object> rtnObj = new HashMap<>();

        ProductDto oldProcuct = productService.selectProduct(product.getProductName());
        if(oldProcuct != null) {
            rtnObj.put("result", "fail");
            rtnObj.put("message", "이미 등록된 제품명입니다.");
        } else {
            if(files.length < 0) {
                rtnObj.put("result", "fail");
                rtnObj.put("message", "잘못된 접근입니다.");
                return rtnObj;
            } else {
                MultipartFile productFile = files[0];
                if(!productFile.isEmpty()) {
                    String originName = productFile.getOriginalFilename();//파일이름
                    String exten = originName.substring(originName.lastIndexOf("."),originName.length());//확장자
                    String random = "";//랜덤값
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS");
                    int rndNum = (int)(Math.random()*100000);
                    random = sdf.format(new Date(System.currentTimeMillis()))+"_"+rndNum+exten;
                    productFile.transferTo(new File(random));

                    product.setProductOrgFileName(originName);
                    product.setProductSaveFileName(random);

                    if(!(exten.equalsIgnoreCase(".jpg") || exten.equalsIgnoreCase(".jpeg") || exten.equalsIgnoreCase(".gif") ||
                            exten.equalsIgnoreCase(".png") || exten.equalsIgnoreCase(".bmp") ||
                            exten.equalsIgnoreCase(".pdf") || exten.equalsIgnoreCase(".zip") || exten.equalsIgnoreCase(".docx") ||
                            exten.equalsIgnoreCase(".doc"))) {
                        rtnObj.put("result", "fail");
                        rtnObj.put("message", "잘못된 형식의 파일입니다.(jpg, jpeg, gif, png, bmp, pdf, zip, doc, docx만 가능)");
                        return rtnObj;
                    }
                }
            }

            logger.info("product -> " + product.toString());

            productService.insertProduct(product);

            rtnObj.put("result", "success");
        }



        return rtnObj;
    }

    @ResponseBody
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public Map<String, Object> remove(ProductDto product) throws Exception {

        Map<String, Object> rtnObj = new HashMap<>();

        productService.deleteProduct(product.getOldProductName());

        storeService.deleteStore(product.getOldProductName());

        rtnObj.put("result", "success");

        return rtnObj;
    }

    @RequestMapping("/download/{productName}")
    public void process(@PathVariable(name = "productName") String productName, HttpServletResponse response) throws Exception {
        try {
            // 서비스를 통해 첨부파일 가져오기
            ProductDto product = productService.selectProduct(productName);


            String originalName = "";
            String filePath = uploadPath + File.separatorChar;
            String fileName = "";

            // 파일명에 한글이 있는경우 처리
            originalName = new String(product.getProductOrgFileName().getBytes("utf-8"), "iso-8859-1");
            fileName = product.getProductSaveFileName();

            String path = filePath + fileName;

            File file = new File(path);
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

            //형식을 모르는 파일첨부용 contentType
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + originalName); // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더


            //파일복사
            FileCopyUtils.copy(in, response.getOutputStream());
            in.close();
            response.getOutputStream().flush();
            response.getOutputStream().close();

        } catch (Exception e) {
            throw new Exception("download error");

        }
    }

}
