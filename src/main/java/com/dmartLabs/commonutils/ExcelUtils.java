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
    //create delivery DT
//    public static int ActualTotalBoxes = 0;
//    public static List<String> EanList = new LinkedList<>();
//
//    //sorter craete Delivery
//    //DlvIemsSorterMain31Pojo dlvIemsSorterMainPojo;
//    static ArrayList<CreateDeliverySorter31Pojo> createDeliverySorterAl = new ArrayList<>();
//    public static CreateDeliverySorter31Pojo createdeliverySorterPojo = new CreateDeliverySorter31Pojo();
//    public static ArrayList<DlvIemsSorterMain31Pojo> dlvItemsSorterAL = new ArrayList<>();
//
//
//    @And("Assigning Main Mandatory fields to CreateOneDeliveryWithListOfItemsUsingDTInSorterExcelDT")
//    public void assigningMainMandatoryFieldsToCreateOneDeliveryWithListOfItemsUsingDTInSorterExcelDT(DataTable dataTable) {
//
//        Map<String, String> CreateDeliveryDT = dataTable.asMap(String.class, String.class);
//        String dlvNumber = GenricUtils.getRandomDeliveryNumber();
//        createdeliverySorterPojo.setDlvNumber(dlvNumber);
//
//        CommonUtilities.setDeliveryNumber(dlvNumber);
//
//        System.out.println(dlvNumber + "        random delivey number");
//        System.out.println(CommonUtilities.getDeliveryNumber() + "          common utilitites delivery number");
//
//        createdeliverySorterPojo.setProposedDlvDate(GenricUtils.getDate("yyyyMMdd"));
//        createdeliverySorterPojo.setSrcSiteId(CreateDeliveryDT.get("srcSiteId"));
//        CommonUtilities.setSrcSiteID(createdeliverySorterPojo.getSrcSiteId());
//
//        createdeliverySorterPojo.setDstSiteId(CreateDeliveryDT.get("dstSiteId"));
//        CommonUtilities.setDstSiteID(createdeliverySorterPojo.getDstSiteId());
//
//        createdeliverySorterPojo.setWhNumber(CreateDeliveryDT.get("whNumber"));
//        CommonUtilities.setWhNumber(createdeliverySorterPojo.getWhNumber());
//
//        createdeliverySorterPojo.setCreatedBy(CreateDeliveryDT.get("createdBy"));
//
//        createdeliverySorterPojo.setCreationDate(GenricUtils.getDate("yyyyMMdd"));
//        createdeliverySorterPojo.setCreationTime(GenricUtils.getTime("HHmmss"));
//        createdeliverySorterPojo.setTotalGdsMvtStat(CreateDeliveryDT.get("totalGdsMvtStat"));
//
//
//    }


    public static List<LinkedHashMap<String, Object>> ReadDataFromExcelDT(String filePath, String sheetName) throws
            IOException {

        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet1 = workbook.getSheet(sheetName);
        List<String> keys = new ArrayList<>();//getting all keys

        for (int i = 0; i < sheet1.getRow(0).getPhysicalNumberOfCells(); i++) {
            keys.add(sheet1.getRow(0).getCell(i).getStringCellValue());
        }


        //     List<LinkedHashMap<String, Object>> MainObjectAl = new ArrayList<>();
        LinkedHashMap<String, Object> MainObject = new LinkedHashMap<>();
        List<LinkedHashMap<String, Object>> DlvItemObjAl = new ArrayList<>();
        for (int i = 1; i < sheet1.getPhysicalNumberOfRows(); i++) {
            //  System.out.println(sheet1.getPhysicalNumberOfRows() + "=============>sheet1.getPhysicalNumberOfRows()");

            Row rowNum = sheet1.getRow(i);
            LinkedHashMap<String, Object> DlvItemObj = new LinkedHashMap<>();
            for (int j = 0; j < rowNum.getPhysicalNumberOfCells(); j++) {

                if (!(rowNum.equals(""))) {
                    //  System.out.println(rowNum.getPhysicalNumberOfCells() + "=============>rowNum.getPhysicalNumberOfCells()");
                    DataFormatter dataFormatter = new DataFormatter();
                    String cellValue = dataFormatter.formatCellValue(sheet1.getRow(i).getCell(j));
                    if (!(cellValue.equals(""))) {
                        DlvItemObj.put(keys.get(j), cellValue);
                    }

                }
            }
            DlvItemObjAl.add(DlvItemObj);

            if(sheet1.getRow(i).getCell(0).getStringCellValue().equals("end"))
            {
                break;
            }
        }

        try {
//            workbook.write(fos);
            fis.close();
//            fos.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return DlvItemObjAl;

    }

    public List<LinkedHashMap<String, Object>> getPurchaseOrderItems(String sheetName) {

        Sheet sheet = workbook.getSheet(sheetName);

        int totalRows = sheet.getPhysicalNumberOfRows();

        List<LinkedHashMap<String, Object>> allitemsDetails = new ArrayList<>();

        List<String> allKeys = new ArrayList<>();


        for (int i=0;i<sheet.getRow(0).getPhysicalNumberOfCells();i++){
            allKeys.add(sheet.getRow(0).getCell(i).getStringCellValue());
        }

        LinkedHashMap<String, Object> itemDetails;

        for (int i=1;i<sheet.getPhysicalNumberOfRows();i++) {
            itemDetails = new LinkedHashMap<>();
            if (sheet.getRow(i).getCell(0).getStringCellValue().equals("end")){
                break;
            }
            else{
                Row row = sheet.getRow(i);
                for (int j = 0; j < sheet.getRow(i).getPhysicalNumberOfCells(); j++) {
                    String values = df.formatCellValue(row.getCell(j));

                    if (!(values.equals(""))) {

                        if (values.equals("\"\"")) {
                            values = "";
                        }

                        if (values.equals("null")) {
                            values = null;
                        }

                        if (allKeys.get(j).equals("poDueDate")){
                            itemDetails.put(allKeys.get(j), GenricUtils.getPO_DueDate());
                        } else if ((allKeys.get(j).equals("quantity") || allKeys.get(j).equals("leQuantity") || allKeys.get(j).equals("mrp") || allKeys.get(j).equals("caselot") || allKeys.get(j).equals("weight"))){
                            itemDetails.put(allKeys.get(j), Integer.parseInt(values));
                        } else if (allKeys.get(j).equals("volume")) {
                            itemDetails.put(allKeys.get(j), Double.parseDouble(values));
                        } else if (allKeys.get(j).equals("isBlocked") || allKeys.get(j).equals("isDeleted")){
                            itemDetails.put(allKeys.get(j), Boolean.parseBoolean(values));
                        } else {
                            itemDetails.put(allKeys.get(j), values);
                        }
                    }
                }
            }
            allitemsDetails.add(itemDetails);
        }

        try {
            ExcelUtils.closeWorkbook();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allitemsDetails;
    }

    public List<LinkedHashMap<String, Object>> getPurchaseOrderItems(String sheetName, int requiredItems) {

        Sheet sheet = workbook.getSheet(sheetName);

        int totalRows = sheet.getPhysicalNumberOfRows();

        List<LinkedHashMap<String, Object>> allitemsDetails = new ArrayList<>();

        List<String> allKeys = new ArrayList<>();


        for (int i=0;i<sheet.getRow(0).getPhysicalNumberOfCells();i++){
            allKeys.add(sheet.getRow(0).getCell(i).getStringCellValue());
        }

        LinkedHashMap<String, Object> itemDetails;

        for (int i=1;i<sheet.getPhysicalNumberOfRows();i++) {
            itemDetails = new LinkedHashMap<>();
            if (sheet.getRow(i).getCell(0).getStringCellValue().equals("end") || i==requiredItems+1){
                break;
            }
            else{
                Row row = sheet.getRow(i);
                for (int j = 0; j < sheet.getRow(i).getPhysicalNumberOfCells(); j++) {
                    String values = df.formatCellValue(row.getCell(j));

                    if (!(values.equals(""))) {

                        if (values.equals("\"\"")) {
                            values = "";
                        }

                        if (values.equals("null")) {
                            values = null;
                        }

                        if (allKeys.get(j).equals("poDueDate")){
                            itemDetails.put(allKeys.get(j), GenricUtils.getPO_DueDate());
                        } else if ((allKeys.get(j).equals("quantity") || allKeys.get(j).equals("leQuantity") || allKeys.get(j).equals("mrp") || allKeys.get(j).equals("caselot") || allKeys.get(j).equals("weight"))){
                            itemDetails.put(allKeys.get(j), Integer.parseInt(values));
                        } else if (allKeys.get(j).equals("volume")) {
                            itemDetails.put(allKeys.get(j), Double.parseDouble(values));
                        } else if (allKeys.get(j).equals("isBlocked") || allKeys.get(j).equals("isDeleted")){
                            itemDetails.put(allKeys.get(j), Boolean.parseBoolean(values));
                        } else {
                            itemDetails.put(allKeys.get(j), values);
                        }
                    }
                }
            }
            allitemsDetails.add(itemDetails);
        }

        try {
            ExcelUtils.closeWorkbook();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allitemsDetails;
    }

    public static void main(String[] args) {
        ExcelUtils excelUtils = new ExcelUtils("Purchase Order Items.xlsx");
        List<LinkedHashMap<String, Object>> items = excelUtils.getPurchaseOrderItems("Items", 2);

        for (LinkedHashMap<String, Object> item:items){
            System.out.println(item);
            System.out.println();
        }
    }

}


