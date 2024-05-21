package com.dmartLabs.commonutils;


import com.dmartLabs.pojo.CreateDeliverySorter31Pojo;
import io.cucumber.java.hu.Ha;
import org.apache.poi.ss.usermodel.*;
import org.json.JSONObject;
import rst.pdfbox.layout.text.Constants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class SampleExcelUtils extends Constants {

    //create Sorter delivery
    public static List<LinkedHashMap<String, Object>> ReadDataFromExcel(String filePath, String sheetName) throws IOException {

        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet1 = workbook.getSheet(sheetName);
        List<String> keys = new ArrayList<>();//27
        List<LinkedHashMap<String, Object>> allObj = new ArrayList<>();

        for (int i = 0; i < sheet1.getRow(0).getPhysicalNumberOfCells(); i++) {
            keys.add(sheet1.getRow(0).getCell(i).getStringCellValue());
        }

        DataFormatter dataFormatter = new DataFormatter();
        //  String key = df.formatCellValue(sheet.getRow(i).getCell(j));
        LinkedHashMap<String, Object> mapAL;
        System.out.println(sheet1.getPhysicalNumberOfRows() + "============> sheet1.getPhysicalNumberOfRows()");
        System.out.println();
        for (int i = 1; i < sheet1.getPhysicalNumberOfRows(); i++) {
            Row rowNum = sheet1.getRow(i);
            System.out.println(rowNum.getPhysicalNumberOfCells());
            mapAL = new LinkedHashMap<>();
            for (int j = 0; j < rowNum.getPhysicalNumberOfCells(); j++) {
//                String key1 = rowNum.getCell(j).getStringCellValue();
                String cellValue = dataFormatter.formatCellValue(sheet1.getRow(i).getCell(j));
                String value1 = cellValue;
                mapAL.put(keys.get(j), value1);
            }
            allObj.add(mapAL);
        }
        return allObj;
    }


//    public static void main(String[] args) throws IOException {
//        // String filePath = "C:\\Users\\Labs User's\\Desktop\\Duplicate\\CopyQF_Update\\QuickFill_ApiFramework\\src\\test\\resources\\features\\TestData\\Canary\\qfxl.xlsx";
//        //String sheetName = "qf";
//        String filePath = "C:\\Final Framework\\Sorter_API Framework_3_!5_2024\\src\\test\\resources\\TestData.Canary\\createDeliveryExcel.xlsx";
//        String sheetName = "Sheet1";
//
//        ArrayList<CreateDeliverySorter31Pojo> TotalObject = ExcelUtils.ReadDataFromExcelDT(filePath, sheetName);
//        System.out.println(TotalObject);
//
//
//
//
//    }
}