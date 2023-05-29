package com.knpharm.knadmin.excel;

import com.knpharm.knadmin.dto.StoreDto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelReader {
    /**
     * XLS 파일을 분석하여 List<Info> 객체로 반환
     * @param filePath
     * @return
     */
    @SuppressWarnings("resource")
    public List<StoreDto> xlsToStoreList(String filePath){

        //반환할 객체를 생성
        List<StoreDto> list = new ArrayList<StoreDto>();

        FileInputStream fis = null;
        HSSFWorkbook workbook = null; //xsl파일

        Map<String, String> brandMap = new HashMap<>();
        brandMap.put("미놀", "M");
        brandMap.put("자하생력", "J");
        brandMap.put("피엠", "P");

        try {

            fis = new FileInputStream(filePath);
            // HSSFWorkbook은 엑셀파일 전체 내용을 담고 있는 객체
            workbook = new HSSFWorkbook(fis);

            //탐색에 사용할 sheet,Row,Cell객체

            HSSFSheet curSheet; //xsl파일
            HSSFRow curRow;
            HSSFCell curCell;   //xsl파일
            StoreDto store;


            //Sheet 탐색 for문  만약 시트가 여러개라면 제일바깥 for문을 살리면 된다.

//            for(int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets();sheetIndex++) {
            //현재 sheet반환
            curSheet = workbook.getSheetAt(0);
            //row탐색 for문
            for(int rowIndex=0;rowIndex<curSheet.getPhysicalNumberOfRows();rowIndex++) {
                //row 0은 헤더정보이기 때문에 무시
                if(rowIndex != 0) {
                    //현재 row반환
                    curRow = curSheet.getRow(rowIndex);
                    store = new StoreDto();
                    String value;

                    //row의 첫 번째 cell값이 비어있지 않은 경우만 cell탐색
                    if(!"".equals(curRow.getCell(0).getStringCellValue())) {

                        //cell탐색 for문
                        for(int cellIndex=0; cellIndex<curRow.getPhysicalNumberOfCells();cellIndex++ ) {
                            curCell = curRow.getCell(cellIndex);
                            if(true) {
                                value ="";
                                //cell스타일이 다르더라도 String으로 반환 받음
                                switch(curCell.getCellType()) {
                                    case HSSFCell.CELL_TYPE_FORMULA:
                                        value = curCell.getCellFormula();
                                        break;
                                    case HSSFCell.CELL_TYPE_NUMERIC:
                                        value = curCell.getNumericCellValue()+"";
                                        break;
                                    case HSSFCell.CELL_TYPE_STRING:
                                        value = curCell.getStringCellValue()+"";
                                        break;
                                    case HSSFCell.CELL_TYPE_BLANK:
                                        value = curCell.getBooleanCellValue()+"";
                                        break;
                                    case HSSFCell.CELL_TYPE_ERROR:
                                        value = curCell.getErrorCellValue()+"";
                                        break;
                                    default:
                                        value = new String();
                                        break;
                                }

                                //현재  column index에 따라서 vo에 입력

                                switch(cellIndex) {
                                    case 0: //Brand
                                        store.setBrandCode(brandMap.get(0).toString());
                                        break;
                                    case 1://Product
                                        store.setProductName(value);
                                        break;
                                    case 2://PHCode
                                        store.setPhCode(value);
                                        break;
                                    case 3: //Store
                                        store.setStoreName(value);
                                        break;
                                    case 4: //Location1
                                        store.setLocation1(value);
                                        break;
                                    case 5: //Location2
                                        store.setLocation2(value);
                                        break;
                                    case 6: //Address
                                        store.setAddress(value);
                                    case 7: //Address
                                        store.setPhone(value);
                                    default:
                                        break;
                                }
                            }
                        }
                        //cell탐색이후 vo추가
                        list.add(store);
                    }
                }
            }
//            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {


            try {
                //사용한 자원은 finally 에서 해제
                if(workbook != null) workbook.close();
                if(fis != null) fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return list;

    }


    /**
     * XLSX 파일을 분석하여 List<StatisticrVo> 객체로 반환
     * @param filePath
     * @return
     */

    public List<StoreDto> xlsxToStoreList(String filePath){
        //반환할 객체를 생성
        List<StoreDto> list = new ArrayList<StoreDto>();

        FileInputStream fis = null;
        XSSFWorkbook workbook = null; //xlsx파일

        Map<String, String> brandMap = new HashMap<>();
        brandMap.put("미놀", "M");
        brandMap.put("자하생력", "J");
        brandMap.put("피엠", "P");

        try {
            fis = new FileInputStream(filePath);
            //HSSFWorkbook은 엑셀파일 전체 내용을 담고 있는 객체
            workbook = new XSSFWorkbook(fis);


            //탐색에 사용할 Sheet, Row ,Cell 객체
            XSSFSheet curSheet;
            XSSFRow curRow;
            XSSFCell curCell;
            StoreDto store;

            //Sheet 탐색 for문 SHEET가 하나이기때문에 바깥포문은 주석
//            for(int sheetIndex =0 ; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            //현재 Sheet반환
//                curSheet = workbook.getSheetAt(sheetIndex);
            curSheet = workbook.getSheetAt(0);
            //row 탐새 for문
            for(int rowIndex=0;rowIndex < curSheet.getPhysicalNumberOfRows();rowIndex++) {
                //row 0은 헤더정보이기 때문에 무시
                if(rowIndex != 0) {
                    //현재 row반환
                    curRow = curSheet.getRow(rowIndex);
                    store = new StoreDto();
                    String value;

                    //row의 첫번째 cell값이 비어있지 않은 경우만 cell탐색
                    if(!"".equals(curRow.getCell(0).getStringCellValue())) {

                        // cell탐색 for문
                        for(int cellIndex = 0 ; cellIndex < curRow.getPhysicalNumberOfCells(); cellIndex++) {
                            curCell = curRow.getCell(cellIndex);

                            if(true) {
                                value = "";
                                //cell스타일이 다르더라도 String 으로 반환받음
                                switch (curCell.getCellType()){
                                    case XSSFCell.CELL_TYPE_FORMULA:
                                        value = curCell.getCellFormula();
                                        break;
                                    case XSSFCell.CELL_TYPE_NUMERIC:
                                        value = curCell.getNumericCellValue()+"";
                                        break;
                                    case XSSFCell.CELL_TYPE_STRING:
                                        value = curCell.getStringCellValue()+"";
                                        break;
                                    case XSSFCell.CELL_TYPE_BLANK:
                                        value = curCell.getBooleanCellValue()+"";
                                        break;
                                    case XSSFCell.CELL_TYPE_ERROR:
                                        value = curCell.getErrorCellValue()+"";
                                        break;
                                    default:
                                        value = new String();
                                        break;
                                }

                                //현재 column index에 따라서 vo에 입력

                                switch(cellIndex) {
                                    case 0: //Brand
                                        store.setBrandCode(brandMap.get(value).toString());
                                        break;
                                    case 1://Product
                                        store.setProductName(value);
                                        break;
                                    case 2://PHCode
                                        store.setPhCode(value);
                                        break;
                                    case 3: //Store
                                        store.setStoreName(value);
                                        break;
                                    case 4: //Location1
                                        store.setLocation1(value);
                                        break;
                                    case 5: //Location2
                                        store.setLocation2(value);
                                        break;
                                    case 6: //Address
                                        store.setAddress(value);
                                    case 7: //Address
                                        store.setPhone(value);
                                    default:
                                        break;
                                }
                            }
                        }
                        //cell탐색 이후 vo추가
                        list.add(store);
                    }
                }
//            }
            }
            return list;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
