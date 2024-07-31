package com.dmartLabs.commonutils;


import com.dmartLabs.config.ConStants;

import org.apache.poi.ss.usermodel.*;
import rst.pdfbox.layout.text.Constants;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


public class ExcelUtils extends Constants {


    static FileInputStream fis;
    static Workbook workbook;
    static DataFormatter df;
    static FileOutputStream fos;
    static ExcelUtils excelUtils;

    public ExcelUtils(String fileName) {
        try {
            fis = new FileInputStream(ConStants.EXCEL_FILE_PATH + fileName);
            workbook = WorkbookFactory.create(fis);
            df = new DataFormatter();
//            fos = new FileOutputStream(ConStants.EXCEL_FILE_PATH + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public static void closeWorkbook() {
        try {
//            workbook.write(fos);
            fis.close();
//            fos.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //=======================================================================================================================
    public List<LinkedHashMap<String, Object>> getPOItems(String SheetName, int requiredTasks1,String siteId) {

        Sheet sheet = workbook.getSheet(SheetName);

        int totalRows = sheet.getPhysicalNumberOfRows();

        //LinkedHashMap<String,Object>POMainObject=new LinkedHashMap<>();
        LinkedList<LinkedHashMap<String, Object>> articlesList = new LinkedList<>();

        LinkedList<String> allKeys = new LinkedList<>();

        for (int i = 0; i < sheet.getRow(0).getPhysicalNumberOfCells(); i++) {
            allKeys.add(sheet.getRow(0).getCell(i).getStringCellValue());
        }
//        for (String key : allKeys) {
//            System.out.println(key);
//        }

        LinkedHashMap<String, Object> itemDetails;
//ctrl+alt+l
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            itemDetails = new LinkedHashMap<>();
            if (sheet.getRow(i).getCell(0).getStringCellValue().equals("end") || i == (requiredTasks1 + 1)) {
                break;
            } else {
                Row row = sheet.getRow(i);

                for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
            //        System.out.println(row.getPhysicalNumberOfCells()+"===========>row.getPhysicalNumberOfCells()");

                    String values = df.formatCellValue(row.getCell(j));
                    //System.out.println(values + "===============>values");
                    if (values.equals("")&&allKeys.get(j).equals("poDueDate")) {
                        itemDetails.put(allKeys.get(j), GenricUtils.getDueDate("yyyyMMdd"));
                    }

                    else if (values.equals("true") || values.equals("false") /**"&& allKeys.get(j).equals("isDeleted")"*/) {

                        //Boolean booleanValue = Boolean.parseBoolean(values);
                      itemDetails.put(allKeys.get(j),Boolean.parseBoolean(values));

                    }
                    else if (values.equals("")&&allKeys.get(j).equals("siteId")) {
                        itemDetails.put(allKeys.get(j), siteId);
                    }
                    else if (!values.equals("")) {
                        itemDetails.put(allKeys.get(j), values);
                    }


                }
           //     articlesList.add(itemDetails);
            }

            try {
                ExcelUtils.closeWorkbook();
            } catch (Exception e) {
                e.printStackTrace();
            }
            articlesList.add(itemDetails);
        }

        //   POMainObject.put("items",articlesList);
        return articlesList;

    }
}

/*
 LinkedList<String> allKeys = new LinkedList<>();

        for (int i = 0; i < sheet.getRow(0).getPhysicalNumberOfCells(); i++) {
            allKeys.add(sheet.getRow(0).getCell(i).getStringCellValue());
        }


   for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            itemDetails = new LinkedHashMap<>();

                Row row = sheet.getRow(i);

                for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {


 String values = df.formatCellValue(row.getCell(j));

    itemDetails.put(allKeys.get(j), values);




 */
