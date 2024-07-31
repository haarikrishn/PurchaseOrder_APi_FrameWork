package com.dmartLabs.stepdefinitions;


import com.dmartLabs.commonutils.ExtentReportManager;
import com.dmartLabs.commonutils.GenricUtils;
import com.dmartLabs.commonutils.RequestGenerator;
import com.dmartLabs.config.ConStants;
import com.dmartLabs.config.PropertyReader;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.dmartLabs.stepdefinitions.CreateMultiplePurchaseOrderStep_HK.PoNumbersList;
import static com.dmartLabs.stepdefinitions.GenericSteps.userCredential;
import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.ponumbersbody;
import static com.dmartLabs.stepdefinitions.GetSlotsConfirmApponmentStep.*;
import static com.dmartLabs.stepdefinitions.validateGetSlots2ConfirmApponmentStep.*;

public class CreateGrnStep {
    private static String inwardNumber = "5242688";
    private static String userId = "Harikrish";

    public static List<String> grnList = new ArrayList<>();
    List<LinkedHashMap<String, Object>> GrnObjectlist = new ArrayList<>();
    LinkedHashMap<String, Object> ItemsObject = new LinkedHashMap<>();
    private static List<LinkedHashMap<String, Object>> ItemsList = new ArrayList<>();

    RequestGenerator requestGenerator = new RequestGenerator();
    private static LinkedHashMap<String, Object> GrnObject = new LinkedHashMap<>();
    private static Response CreateGrnResponse;

    @When("after appontment scheduled create Grn for particular po number")
    public void afterAppontmentScheduledCreateGrnForParticularPoNumber(DataTable dataTable) {
        List<Map<String, String>> GrnDT = dataTable.asMaps();


        for (int k = 0; k < GrnDT.size(); k++) {
            GrnObject = new LinkedHashMap<>();

            GrnObject.put("postingDate", GenricUtils.getDueDate("yyyyMMdd"));

            GrnObject.put("grnDate", GenricUtils.getDueDate("yyyyMMdd"));


            String grn = String.valueOf(GenricUtils.getGrnNumber(CommonUtilities.getSiteId()));
            String[] Grnsplit = grn.split("/");
            String grnArray1 = Grnsplit[1];
            String grnArray2 = Grnsplit[2];
            String grnArray = grnArray1 + grnArray2;
            GrnObject.put("invoiceNumber", GenricUtils.getInvoiceNumber());
            GrnObject.put("inwardNumber", GrnDT.get(k).get("inwardNumber"));
            GrnObject.put("userId", GrnDT.get(k).get("userId"));

            GrnObject.put("headerText", grnArray);
            GrnObject.put("grnNumber", grn);


            for (int j = 0; j < PoNumbersList.size(); j++) {
                List<LinkedHashMap<String, Object>> ItemsList = new ArrayList<>();
                LinkedHashMap<String, Object> poObject = new LinkedHashMap<>();

                List<LinkedHashMap<String, Object>> ponum = ponumbersbody.get(PoNumbersList.get(j));

                String poNumber = PoNumbersList.get(j);

                for (int i = 0; i < ponum.size(); i++) {
                    LinkedHashMap<String, Object> ItemsObject = new LinkedHashMap<>();

//  "batchNumber": "ABC",  "movementType": "101",
                    String matNumber = (String) ponum.get(i).get("matNumber");
                    String siteId = (String) ponum.get(i).get("siteId");
                    String storageLoc = (String) ponum.get(i).get("stockPlacement");
                    String batchNumber = (String) ponum.get(i).get("matNumber");
                    String movementType = String.valueOf(101);
                    Integer qty;
                    qty = (Integer) ponum.get(i).get("qty");
                    String unit = (String) ponum.get(i).get("uom");
                    Object itemSeq1 = ponum.get(i).get("itemSeq");

                    ItemsObject.put("poNumber", poNumber);
                    ItemsObject.put("matNumber", matNumber);
                    ItemsObject.put("siteId", siteId);
                    ItemsObject.put("storageLoc", storageLoc);
                    ItemsObject.put("batchNumber", batchNumber);
                    ItemsObject.put("movementType", movementType);
                    ItemsObject.put("grnQty", qty);
                    ItemsObject.put("itemSeq", itemSeq1);
                    ItemsObject.put("inwardIndicator", "C");
                    ItemsList.add(ItemsObject);
                }
                GrnObject.put("items", ItemsList);


                Map<String, String> userCred = userCredential;
                HashMap<String, String> userCredential = new HashMap<>();
                userCredential.putAll(userCred);
                userCredential.put("requestId", GenricUtils.getRandomDeliveryNumber());

                Response CreateGrnResponse = requestGenerator.getRequest(userCredential, GrnObject).log().all()
                        .when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CreateGrnEndPoint"));
                CreateGrnResponse.then().log().all();
                ExtentReportManager.logInfoDetails("Response is - ");
                ExtentReportManager.logJson(CreateGrnResponse.prettyPrint());
                ExtentReportManager.logInfoDetails("Response status code is " + CreateGrnResponse.getStatusCode());
                long responseTime = CreateGrnResponse.getTimeIn(TimeUnit.MILLISECONDS);
                ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");
                CommonUtilities.setResponseInstance(CreateGrnResponse);

            }
        }
    }

    //===============================================================================================================
//consider

    @When("after appontment scheduled create Grn for particular po number for number of iterations")
    public void afterAppontmentScheduledCreateGrnForParticularPoNumberForNumberOfIterations() throws InterruptedException {

        Thread.sleep(1000);
        //  List<Map<String, String>> GrnDT = dataTable1.asMaps();
        GrnObject = new LinkedHashMap<>();
        GrnObject.put("postingDate", GenricUtils.getDate("yyyyMMdd"));
        String originalDateString = ConfirmpoDueDate1;
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(originalDateString);
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String ConfirmformattedDate = localDateTime.format(formatter);
        GrnObject.put("grnDate", ConfirmformattedDate);


        String grn = String.valueOf(GenricUtils.getGrnNumber(CommonUtilities.getSiteId()));
        String[] Grnsplit = grn.split("/");
        String grnArray1 = Grnsplit[1];
        String grnArray2 = Grnsplit[2];
        String grnArray = grnArray1 + grnArray2;
        grnList.add(grnArray);
        GrnObject.put("invoiceNumber", GenricUtils.getInvoiceNumber());
        GrnObject.put("inwardNumber", inwardNumber);
        GrnObject.put("userId", userId);
        GrnObject.put("headerText", grnArray);
        GrnObject.put("grnNumber", grn);

        for (int k = 0; k < PoNumbersList.size(); k++) {
            for (int i = 0; i < ponum.size(); i++) {
                LinkedHashMap<String, Object> ItemsObject = new LinkedHashMap<>();

//  "batchNumber": "ABC",  "movementType": "101",
                String matNumber = (String) ponum.get(i).get("matNumber");
                String siteId = (String) ponum.get(i).get("siteId");
                String storageLoc = (String) ponum.get(i).get("stockPlacement");
                String batchNumber = (String) ponum.get(i).get("matNumber");
                String movementType = String.valueOf(101);
                Integer qty;
                qty = (Integer) ponum.get(i).get("qty");
                String unit = (String) ponum.get(i).get("uom");
                Object itemSeq1 = ponum.get(i).get("itemSeq");

                ItemsObject.put("poNumber", PoNumbersList.get(k));
                ItemsObject.put("matNumber", matNumber);
                ItemsObject.put("siteId", siteId);
                ItemsObject.put("storageLoc", storageLoc);
                ItemsObject.put("batchNumber", batchNumber);
                ItemsObject.put("movementType", movementType);
                ItemsObject.put("grnQty", qty);
                ItemsObject.put("unit", unit);
                ItemsObject.put("itemSeq", itemSeq1);
                ItemsObject.put("inwardIndicator", "C");
                ItemsList.add(ItemsObject);
            }
        }

        GrnObject.put("items", ItemsList);


        Map<String, String> userCred = userCredential;
        HashMap<String, String> userCredential = new HashMap<>();
        userCredential.putAll(userCred);
        userCredential.put("requestId", GenricUtils.getRandomDeliveryNumber());

        Response CreateGrnResponse = requestGenerator.getRequestpo(userCredential, GrnObject).log().all()
                .when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CreateGrnEndPoint"));
        CreateGrnResponse.then().log().all();

        //  CreateGrnResponse.jsonPath().get("");
        ExtentReportManager.logInfoDetails("Response is - ");
        ExtentReportManager.logJson(CreateGrnResponse.prettyPrint());
        ExtentReportManager.logInfoDetails("Response status code is " + CreateGrnResponse.getStatusCode());
        long responseTime = CreateGrnResponse.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");
        Thread.sleep(500);
        //  CommonUtilities.setResponseInstance(CreateGrnResponse);

    }

//=============================================================================================================

    //grn validate
    public static LinkedHashMap<String, Object> ReturnGrnObject(LinkedHashMap<String, Object> GrnObject, int j) throws InterruptedException {
        Thread.sleep(1000);
        //  List<Map<String, String>> GrnDT = dataTable1.asMaps();
        GrnObject = new LinkedHashMap<>();
        GrnObject.put("postingDate", GenricUtils.getDate("yyyyMMdd"));
        String originalDateString = ConfirmpoDueDate2;
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(originalDateString);
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String ConfirmformattedDate = localDateTime.format(formatter);
        GrnObject.put("grnDate", ConfirmformattedDate);


        String grn = String.valueOf(GenricUtils.getGrnNumber(CommonUtilities.getSiteId()));
        String[] Grnsplit = grn.split("/");
        String grnArray1 = Grnsplit[1];
        String grnArray2 = Grnsplit[2];
        String grnArray = grnArray1 + grnArray2;
        grnList.add(grnArray);
        GrnObject.put("invoiceNumber", GenricUtils.getInvoiceNumber());
        GrnObject.put("inwardNumber", inwardNumber);
        GrnObject.put("userId", userId);
        GrnObject.put("headerText", grnArray);
        GrnObject.put("grnNumber", grn);

        for (j = j; j < PoNumbersList.size();) {
            ItemsList=new ArrayList<>();
            for (int i = 0; i < ponum1.size(); i++) {
                LinkedHashMap<String, Object> ItemsObject = new LinkedHashMap<>();

//  "batchNumber": "ABC",  "movementType": "101",
                String matNumber = (String) ponum1.get(i).get("matNumber");
                String siteId = (String) ponum1.get(i).get("siteId");
                String storageLoc = (String) ponum1.get(i).get("stockPlacement");
                String batchNumber = (String) ponum1.get(i).get("matNumber");
                String movementType = String.valueOf(101);
                Integer qty;
                qty = (Integer) ponum1.get(i).get("qty");
                String unit = (String) ponum1.get(i).get("uom");
                Object itemSeq1 = ponum1.get(i).get("itemSeq");

                ItemsObject.put("poNumber", PoNumbersList.get(j));
                ItemsObject.put("matNumber", matNumber);
                ItemsObject.put("siteId", siteId);
                ItemsObject.put("storageLoc", storageLoc);
                ItemsObject.put("batchNumber", batchNumber);
                ItemsObject.put("movementType", movementType);
                ItemsObject.put("grnQty", qty);
                ItemsObject.put("unit", unit);
                ItemsObject.put("itemSeq", itemSeq1);
                ItemsObject.put("inwardIndicator", "C");
                ItemsList.add(ItemsObject);
            }
            j++;
            break;
        }
        GrnObject.put("items", ItemsList);
        return GrnObject;

    }

    public static HashMap<String, String> ReturnUserCredential() {
        Map<String, String> userCred = userCredential;
        HashMap<String, String> userCredential = new HashMap<>();
        userCredential.putAll(userCred);
        userCredential.put("requestId", GenricUtils.getRandomDeliveryNumber());
        return userCredential;
    }


    public static Response ReturnGRNResponse(LinkedHashMap<String, Object> GrnObject, int j) throws InterruptedException {

        while (j < PoNumbersList.size()) {
            // Update the GRN object for the current PO number
            GrnObject = ReturnGrnObject(GrnObject, j);

            // Generate the request and get the response
            CreateGrnResponse = RequestGenerator.getRequestpo(CreateGrnStep.ReturnUserCredential(), GrnObject)
                    .log().all()
                    .when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CreateGrnEndPoint"));

            // Log details about the response
            CreateGrnResponse.then().log().all();
            ExtentReportManager.logInfoDetails("Response is - ");
            ExtentReportManager.logJson(CreateGrnResponse.prettyPrint());
            ExtentReportManager.logInfoDetails("Response status code is " + CreateGrnResponse.getStatusCode());
            long responseTime = CreateGrnResponse.getTimeIn(TimeUnit.MILLISECONDS);
            ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");

            // Exit after the first execution
            break;
        }

        return CreateGrnResponse;
    }
}

//================================================================================================================================

























/*
    @And("Send Grn request to the server using api")
    public void sendGrnRequestToTheServerUsingApi() {

        Map<String, String> userCred = GenericSteps.userCredential;
        HashMap<String, String> userCredential = new HashMap<>();
        userCredential.putAll(userCred);
        userCredential.put("requestId", GenricUtils.getRandomDeliveryNumber());

        Response CreateGrnResponse = requestGenerator.getRequest(userCredential, GrnObject).log().all()
                .when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CreateGrnEndPoint"));
        CreateGrnResponse.then().log().all();
        ExtentReportManager.logInfoDetails("Response is - ");
        ExtentReportManager.logJson(CreateGrnResponse.prettyPrint());
        ExtentReportManager.logInfoDetails("Response status code is " + CreateGrnResponse.getStatusCode());
        long responseTime = CreateGrnResponse.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");
        CommonUtilities.setResponseInstance(CreateGrnResponse);

    }

    @When("after appontment scheduled create Grn for particular po number")
    public void afterAppontmentScheduledCreateGrnForParticularPoNumber(DataTable dataTable) {
        List<Map<String, String>> GrnDT = dataTable.asMaps();
        for (int k = 0; k < GrnDT.size(); k++) {
            GrnObject = new LinkedHashMap<>();

            GrnObject.put("postingDate", GenricUtils.getDueDate("yyyyMMdd"));
            GrnObject.put("grnDate", GenricUtils.getDueDate("yyyyMMdd"));
            String grn = String.valueOf(GenricUtils.getGrnNumber(CommonUtilities.getSiteId()));
            String[] Grnsplit = grn.split("/");
            String grnArray1 = Grnsplit[1];
            String grnArray2 = Grnsplit[2];
            String grnArray = grnArray1 + grnArray2;
            GrnObject.put("invoiceNumber", GenricUtils.getInvoiceNumber());
            GrnObject.put("inwardNumber", GrnDT.get(k).get("inwardNumber"));
            GrnObject.put("userId", GrnDT.get(k).get("userId"));

            GrnObject.put("headerText", grnArray);
            GrnObject.put("grnNumber", grn);


  List<LinkedHashMap<String, Object>> ItemsList = new ArrayList<>();
                LinkedHashMap<String, Object> poObject = new LinkedHashMap<>();

            for (int j = 0; j < PoNumbersList.size(); j++) {


                List<LinkedHashMap<String, Object>> ponum = ponumbersbody.get(PoNumbersList.get(j));

                String poNumber = PoNumbersList.get(j);

                for (int i = 0; i < ponum.size(); i++) {
                    LinkedHashMap<String, Object> ItemsObject = new LinkedHashMap<>();

//  "batchNumber": "ABC",  "movementType": "101",
                    String matNumber = (String) ponum.get(i).get("matNumber");
                    String siteId = (String) ponum.get(i).get("siteId");
                    String storageLoc = (String) ponum.get(i).get("stockPlacement");
                    String batchNumber = (String) ponum.get(i).get("matNumber");
                    String movementType = String.valueOf(101);
                    Integer qty;
                    qty = (Integer) ponum.get(i).get("qty");
                    String unit = (String) ponum.get(i).get("uom");
                    Object itemSeq1 = ponum.get(i).get("itemSeq");

                    ItemsObject.put("poNumber", poNumber);
                    ItemsObject.put("matNumber", matNumber);
                    ItemsObject.put("siteId", siteId);
                    ItemsObject.put("storageLoc", storageLoc);
                    ItemsObject.put("batchNumber", batchNumber);
                    ItemsObject.put("movementType", movementType);
                    ItemsObject.put("grnQty", qty);
                    ItemsObject.put("itemSeq", itemSeq1);
                    ItemsObject.put("inwardIndicator", "C");
                    ItemsList.add(ItemsObject);
                }
                GrnObject.put("items", ItemsList);
            }

        }

    }




 */
