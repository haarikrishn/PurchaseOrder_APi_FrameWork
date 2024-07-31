package com.dmartLabs.stepdefinitions;


import com.dmartLabs.commonutils.ExcelUtils;
import com.dmartLabs.commonutils.ExtentReportManager;
import com.dmartLabs.commonutils.GenricUtils;
import com.dmartLabs.commonutils.RequestGenerator;
import com.dmartLabs.config.ConStants;
import com.dmartLabs.config.PropertyReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class CreatePurchaseOrderStep_HK {
    LinkedHashMap<String, Object> POMainObject = new LinkedHashMap<>();
    List<LinkedHashMap<String, Object>> articlesList = new ArrayList<>();

    LinkedHashMap<String, Object> SupplierMAp = new LinkedHashMap<>();
    List<LinkedHashMap<String, Object>> termsOfDlvList = new ArrayList<>();
    LinkedHashMap<String, Object> termsOfDlvMap = new LinkedHashMap<>();


    RequestGenerator requestGenerator = new RequestGenerator();
    Response CreatePOResponse;

    @When("create a PO with below test data")
    public void createAPOWithBelowTestData(DataTable dataTable) {
        String RandomPONumber = String.valueOf(GenricUtils.getRandomPONumber());
        POMainObject.put("poNumber", RandomPONumber);
        CommonUtilities.setPONumber(RandomPONumber);

        POMainObject.put("companyCode", "ASPL");
        POMainObject.put("documentType", "ZANB");
        POMainObject.put("poDate", GenricUtils.getDate("yyyyMMdd"));
        POMainObject.put("supplierCode", "0000108294");
        POMainObject.put("buyerCode", "H10");
        POMainObject.put("buyerEmail", "phiroze.lakhani@dmartindia.com");
        LinkedHashMap<String, Object> termsOfDlvMap1 = new LinkedHashMap<>();

        termsOfDlvMap1.put("tdLine", "line 1: This is line one of terms of delivery.");
        termsOfDlvMap1.put("tdFormat", "*");

        LinkedHashMap<String, Object> termsOfDlvMap2 = new LinkedHashMap<>();

        termsOfDlvMap2.put("tdLine", "line 2: This the line tow of terms of delivery.");
        termsOfDlvMap2.put("tdFormat", "*");

        termsOfDlvList.add(termsOfDlvMap1);
        termsOfDlvList.add(termsOfDlvMap2);

        POMainObject.put("termsOfDlv", termsOfDlvList);


        LinkedHashMap<String, Object> articlesMap = null;
        List<Map<String, Object>> articlesDT = dataTable.asMaps(String.class, String.class);
        for (int i = 0; i < articlesDT.size(); i++) {
            articlesMap = new LinkedHashMap<>();
            Map<String, Object> articlesOneByone = articlesDT.get(i);

            articlesMap.put("itemSeq", articlesOneByone.get("itemSeq"));
            articlesMap.put("poDueDate", GenricUtils.getDueDate("yyyyMMdd"));

            articlesMap.put("matNumber", articlesOneByone.get("matNumber"));
            articlesMap.put("ean", articlesOneByone.get("ean"));
            //    "dueDate": "2024-05-11T00:00:00.000+00:00",

            articlesMap.put("articleDesc", articlesOneByone.get("articleDesc"));
            articlesMap.put("matGroup", articlesOneByone.get("matGroup"));
            articlesMap.put("siteId", articlesOneByone.get("siteId"));
            CommonUtilities.setSiteId((String) articlesOneByone.get("siteId"));

            articlesMap.put("qty", articlesOneByone.get("qty"));
            articlesMap.put("uom", articlesOneByone.get("uom"));
            articlesMap.put("isDeleted", Boolean.parseBoolean((String) articlesOneByone.get("isDeleted")));
            articlesMap.put("caselot", articlesOneByone.get("caselot"));
            articlesMap.put("weight", articlesOneByone.get("weight"));
            articlesMap.put("volume", articlesOneByone.get("volume"));
            articlesMap.put("mrp", articlesOneByone.get("mrp"));


            articlesMap.put("basicPrice", articlesOneByone.get("basicPrice"));
            articlesMap.put("netPrice", articlesOneByone.get("netPrice"));
            articlesMap.put("tot", articlesOneByone.get("tot"));
            articlesMap.put("vendorDiscountPer", articlesOneByone.get("vendorDiscountPer"));
            articlesMap.put("vendorDiscountVal", articlesOneByone.get("vendorDiscountVal"));
            articlesMap.put("sgst", articlesOneByone.get("sgst"));
            articlesMap.put("cgst", articlesOneByone.get("cgst"));
            articlesMap.put("cess", articlesOneByone.get("cess"));
            articlesMap.put("landedPrice", articlesOneByone.get("landedPrice"));
            articlesMap.put("storageSection", articlesOneByone.get("storageSection"));
            articlesMap.put("stockPlacement", articlesOneByone.get("stockPlacement"));
            articlesMap.put("leQty", articlesOneByone.get("leQty"));
            articlesMap.put("hsn", articlesOneByone.get("hsn"));
            articlesList.add(articlesMap);
        }
        //    articlesList.add(articlesMap)  ;
        POMainObject.put("items", articlesList);

    }


    @And("user  trigger create PO Api purchase order should be created")
    public void userTriggerCreatePOApiPurchaseOrderShouldBeCreated() {

        Map<String, String> userCred = GenericSteps.userCredential;
        HashMap<String, String> userCredential = new HashMap<>();
        userCredential.putAll(userCred);
        userCredential.put("requestId", CommonUtilities.getPONumber());


        ExtentReportManager.logInfoDetails("Requester calls the outbound delivery endpoint to create new delivery for a wave");
        CreatePOResponse = requestGenerator.getRequest(userCredential, POMainObject).log().all()
                .when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CREATE_PO_ENDPOINT"));
        CreatePOResponse.then().log().all();
        ExtentReportManager.logInfoDetails("Response is - ");
        ExtentReportManager.logJson(CreatePOResponse.prettyPrint());
        ExtentReportManager.logInfoDetails("Response status code is " + CreatePOResponse.getStatusCode());
        long responseTime = CreatePOResponse.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");
        CommonUtilities.setResponseInstance(CreatePOResponse);

    }
//==============================================================================================
    //excel

    @When("user pass to  main mandatory fields to create po")
    public void userPassToMainMandatoryFieldsToCreatePo(DataTable dataTable) {
        List<Map<String, String>> MainDataDT = dataTable.asMaps();

        for (int i = 0; i < MainDataDT.size(); i++) {

            String RandomPONumber = String.valueOf(GenricUtils.getRandomPONumber());
            POMainObject.put("poNumber", RandomPONumber);
            CommonUtilities.setPONumber(RandomPONumber);
            POMainObject.put("companyCode", MainDataDT.get(i).get("companyCode"));
            POMainObject.put("documentType", MainDataDT.get(i).get("documentType"));
            POMainObject.put("poDate", GenricUtils.getDate("yyyyMMdd"));
            POMainObject.put("supplierCode", MainDataDT.get(i).get("supplierCode"));
            POMainObject.put("buyerCode", MainDataDT.get(i).get("buyerCode"));
            POMainObject.put("buyerEmail", MainDataDT.get(i).get("buyerEmail"));
            LinkedHashMap<String, Object> termsOfDlvMap1 = new LinkedHashMap<>();

            termsOfDlvMap1.put("tdLine", MainDataDT.get(i).get("tdLine"));
            termsOfDlvMap1.put("tdFormat", MainDataDT.get(i).get("tdFormat"));

            LinkedHashMap<String, Object> termsOfDlvMap2 = new LinkedHashMap<>();

            termsOfDlvMap2.put("tdLine1", MainDataDT.get(i).get("tdLine1"));
            termsOfDlvMap2.put("tdFormat1", MainDataDT.get(i).get("tdFormat1"));

            termsOfDlvList.add(termsOfDlvMap1);
            termsOfDlvList.add(termsOfDlvMap2);

            POMainObject.put("termsOfDlv", termsOfDlvList);

        }
    }


    @And("user add to  list of articles  {string} {string} {string} {string}to create po")
    public void userAddToListOfArticlesToCreatePo(String excelFile, String SheetName, String requiredTasks, String siteId) throws JsonProcessingException {
// | excelFile | SheetName | requiredTasks |

        ExcelUtils excelUtils = new ExcelUtils(excelFile);
        int requiredTasks1 = Integer.parseInt(requiredTasks);
        List<LinkedHashMap<String, Object>> POItems = excelUtils.getPOItems(SheetName, requiredTasks1, siteId);
//=========================================================================================
//        JsonPath js = new JsonPath(String.valueOf(POItems));
//        System.out.print(js.prettyPrint());
//import com.fasterxml.jackson.databind.ObjectMapper;

// Assuming you have ObjectMapper initialized somewhere in your code
        ObjectMapper objectMapper = new ObjectMapper();

// Convert POItems to JSON string
        String json = objectMapper.writeValueAsString(POItems);

// Now use JsonPath to parse the JSON string
        JsonPath js = new JsonPath(json);
        //     System.out.print(js.prettyPrint());


        //==================================================================================
        POMainObject.put("items", POItems);


    }

    @And("user send request to the server using create Po Api")
    public void userSendRequestToTheServerUsingCreatePoApi() {

        ExtentReportManager.logInfoDetails("Requester calls the outbound delivery endpoint to create new delivery for a wave");
        Map<String, String> userCred = GenericSteps.userCredential;
        HashMap<String, String> userCredential = new HashMap<>();
        userCredential.putAll(userCred);
        userCredential.put("requestId", CommonUtilities.getPONumber());


        CreatePOResponse = requestGenerator.getRequest(userCredential, POMainObject).log().all()
                .when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CREATE_PO_ENDPOINT"));
        CreatePOResponse.then().log().all();
        ExtentReportManager.logInfoDetails("Response is - ");
        ExtentReportManager.logJson(CreatePOResponse.prettyPrint());
        ExtentReportManager.logInfoDetails("Response status code is " + CreatePOResponse.getStatusCode());
        long responseTime = CreatePOResponse.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");
        CommonUtilities.setResponseInstance(CreatePOResponse);


    }
//=====================================================================================================

    //Data table

List<String>PoNumbersList=new ArrayList<>();
    List<LinkedHashMap<String, Object>> PoArticlesList = new ArrayList<>();


    @And("creating purchase order with different suppliercodeDT")
    public void creatingPurchaseOrderWithDifferentSuppliercodeDT(DataTable dataTable) {
        List<Map<String, String>> suppliercodeDT = dataTable.asMaps();

        for (int i = 0; i < suppliercodeDT.size(); i++) {
            POMainObject.put("supplierCode", suppliercodeDT.get(i).get("supplierCode"));
        }

    }



    @When("user pass to  main mandatory fields to create a multiple purchase ordersDT")
    public void userPassToMainMandatoryFieldsToCreateAMultiplePurchaseOrdersDT(DataTable dataTable) {
        List<Map<String, String>> MainDataDT = dataTable.asMaps();
        List<LinkedHashMap<String,Object>>termsOfDlvList=new ArrayList<>();
        for (int i = 0; i < MainDataDT.size(); i++) {

            String RandomPONumber = String.valueOf(GenricUtils.getRandomPONumber());
            POMainObject.put("poNumber", RandomPONumber);
            CommonUtilities.setPONumber(RandomPONumber);
            PoNumbersList.add(RandomPONumber);

            POMainObject.put("companyCode", MainDataDT.get(i).get("companyCode"));
            POMainObject.put("documentType", MainDataDT.get(i).get("documentType"));
            POMainObject.put("poDate", GenricUtils.getDate("yyyyMMdd"));

            //     POMainObject.put("supplierCode", MainDataDT.get(i).get("supplierCode"));

            String supplierCodeMap = (String) POMainObject.get("supplierCode");
           // SiteIdsList.add(supplierCodeMap);

            POMainObject.put("buyerCode", MainDataDT.get(i).get("buyerCode"));
            POMainObject.put("buyerEmail", MainDataDT.get(i).get("buyerEmail"));
            LinkedHashMap<String, Object> termsOfDlvMap1 = new LinkedHashMap<>();

            termsOfDlvMap1.put("tdLine", MainDataDT.get(i).get("tdLine"));
            termsOfDlvMap1.put("tdFormat", MainDataDT.get(i).get("tdFormat"));

            LinkedHashMap<String, Object> termsOfDlvMap2 = new LinkedHashMap<>();

            termsOfDlvMap2.put("tdLine1", MainDataDT.get(i).get("tdLine1"));
            termsOfDlvMap2.put("tdFormat1", MainDataDT.get(i).get("tdFormat1"));

            termsOfDlvList.add(termsOfDlvMap1);
            termsOfDlvList.add(termsOfDlvMap2);

            POMainObject.put("termsOfDlv", termsOfDlvList);

        }
    }


    @And("user add to  list of articles   to create a  purchase orders using Data table")
    public void userAddToListOfArticlesToCreateAPurchaseOrdersUsingDataTable(DataTable dataTable) {

        LinkedHashMap<String, Object> PoArticlesMap = null;
        List<Map<String, Object>> POArticlesDT = dataTable.asMaps(String.class, String.class);
        for (int i = 0; i < POArticlesDT.size(); i++) {

            PoArticlesMap = new LinkedHashMap<>();
            PoArticlesMap.put("itemSeq", (String) POArticlesDT.get(i).get("itemSeq"));
            System.out.println(POArticlesDT.get(i).get("poDueDate"));

            if (POArticlesDT.get(i).get("poDueDate") == null) {
                PoArticlesMap.put("poDueDate", GenricUtils.getDueDate("yyyyMMdd"));
            }


            PoArticlesMap.put("matNumber", (String) POArticlesDT.get(i).get("matNumber"));
            PoArticlesMap.put("ean", (String) POArticlesDT.get(i).get("ean"));
            PoArticlesMap.put("articleDesc", (String) POArticlesDT.get(i).get("articleDesc"));
            PoArticlesMap.put("matGroup", (String) POArticlesDT.get(i).get("matGroup"));
            PoArticlesMap.put("siteId", (String) POArticlesDT.get(i).get("siteId"));
            PoArticlesMap.put("qty", (String) POArticlesDT.get(i).get("qty"));
            PoArticlesMap.put("uom", (String) POArticlesDT.get(i).get("uom"));

            String isDeletedString = (String) POArticlesDT.get(i).get("isDeleted");
            boolean isDeletedBoolean = Boolean.parseBoolean(isDeletedString);

            PoArticlesMap.put("isDeleted", isDeletedBoolean);


            PoArticlesMap.put("caselot", POArticlesDT.get(i).get("caselot"));
            PoArticlesMap.put("weight", POArticlesDT.get(i).get("weight"));
            PoArticlesMap.put("volume", POArticlesDT.get(i).get("volume"));

            PoArticlesMap.put("mrp", POArticlesDT.get(i).get("mrp"));
            PoArticlesMap.put("basicPrice", POArticlesDT.get(i).get("basicPrice"));
            PoArticlesMap.put("netPrice", POArticlesDT.get(i).get("netPrice"));

            PoArticlesMap.put("tot", POArticlesDT.get(i).get("tot"));
            PoArticlesMap.put("vendorDiscountPer", POArticlesDT.get(i).get("vendorDiscountPer"));
            PoArticlesMap.put("vendorDiscountVal", POArticlesDT.get(i).get("vendorDiscountVal"));

            PoArticlesMap.put("sgst", POArticlesDT.get(i).get("sgst"));
            PoArticlesMap.put("cgst", POArticlesDT.get(i).get("cgst"));
            PoArticlesMap.put("cess", POArticlesDT.get(i).get("cess"));

            PoArticlesMap.put("landedPrice", POArticlesDT.get(i).get("landedPrice"));
            PoArticlesMap.put("storageSection", POArticlesDT.get(i).get("storageSection"));
            PoArticlesMap.put("stockPlacement", POArticlesDT.get(i).get("stockPlacement"));

            PoArticlesMap.put("leQty", POArticlesDT.get(i).get("leQty"));
            PoArticlesMap.put("hsn", POArticlesDT.get(i).get("hsn"));
            PoArticlesList.add(PoArticlesMap);
        }

        POMainObject.put("items", PoArticlesList);


    }


    @And("user send multiple requests to server using create Po APiDT")
    public void userSendMultipleRequestsToServerUsingCreatePoAPiDT() throws InterruptedException {

        ExtentReportManager.logInfoDetails("Requester calls the outbound delivery endpoint to create new delivery for a wave");
        Map<String, String> userCred = GenericSteps.userCredential;
        HashMap<String, String> userCredential = new HashMap<>();
        userCredential.putAll(userCred);
        userCredential.put("requestId", GenricUtils.getRandomDeliveryNumber());

        CreatePOResponse = requestGenerator.getRequest(userCredential, POMainObject).log().all()
                .when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CREATE_PO_ENDPOINT"));
        CreatePOResponse.then().log().all();
        ExtentReportManager.logInfoDetails("Response is - ");
        ExtentReportManager.logJson(CreatePOResponse.prettyPrint());
        ExtentReportManager.logInfoDetails("Response status code is " + CreatePOResponse.getStatusCode());
        long responseTime = CreatePOResponse.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");
        CommonUtilities.setResponseInstance(CreatePOResponse);
        Thread.sleep(500);
        // CommonUtilities.setPOResponseMap((LinkedHashMap<String, Object>) CreatePOResponse);

    }


//===================================================================================================================

    //scenarioOutline
    //   List<LinkedHashMap<String, Object>> PoArticlesList = new ArrayList<>();

    @And("user add to  list of articles  {string} {string} {string} {string} {string} {string} {string}	{string}	{string}	{string}	{string}	{string}	{string}	{string}	{string}	{string} {string} {string}	{string}	{string}	{string}	{string}	{string}	{string}	{string}	{string} {string} to create a multiple purchase orders")
    public void userAddToListOfArticlesToCreateAMultiplePurchaseOrders(String itemSeq, String poDueDate, String
            matNumber, String ean, String articleDesc, String matGroup, String siteId, String qty, String uom, String
                                                                               isDeleted, String caselot, String weight, String volume, String mrp, String basicPrice, String netPrice, String
                                                                               tot, String vendorDiscountPer, String vendorDiscountVal, String sgst, String cgst, String cess, String
                                                                               landedPrice, String storageSection, String stockPlacement, String leQty, String hsn) {


    }



}


//============================================================================================================================
//@When("create a PO with below test data")
//public void createAPOWithBelowTestData(DataTable dataTable) {
//    POMainObject.put("poNumber", GenricUtils.getRandomPONumber());
//    POMainObject.put("poCreationDate",GenricUtils.isodate());
//    POMainObject.put("poStatus","NEW");
//    POMainObject.put("documentType","Normal");
//    POMainObject.put("companyCode","HUL");
//    List<Map<String, Object>> articlesDT = dataTable.asMaps(String.class, String.class);
//    for(int i=0;i<articlesDT.size();i++)
//    {
//        Map<String, Object> articlesOneByone = articlesDT.get(i);
//
//        articlesMap.put("ean",articlesOneByone.get("ean"));
//        articlesMap.put("material",articlesOneByone.get("material"));
//        articlesMap.put("dstSiteId",articlesOneByone.get("dstSiteId"));
//        //    "dueDate": "2024-05-11T00:00:00.000+00:00",
//        articlesMap.put("dueDate",GenricUtils.isodate());
//        articlesMap.put("matDesc",articlesOneByone.get("matDesc"));
//        articlesMap.put("matlGroup",articlesOneByone.get("matlGroup"));
//        articlesMap.put("shortText",articlesOneByone.get("shortText"));
//        articlesMap.put("delIndicator",articlesOneByone.get("delIndicator"));
//        articlesMap.put("pInt",articlesOneByone.get("pInt"));
//        articlesMap.put("quantity",articlesOneByone.get("quantity"));
//        articlesMap.put("leQuantity",articlesOneByone.get("leQuantity"));
//        articlesMap.put("mrp",articlesOneByone.get("mrp"));
//        articlesMap.put("caselot",articlesOneByone.get("caselot"));
//        articlesMap.put("weight",articlesOneByone.get("weight"));
//        articlesMap.put("volume",articlesOneByone.get("volume"));
//        articlesMap.put("isBlocked",Boolean.parseBoolean((String) articlesOneByone.get("isBlocked")));
//        articlesMap.put("isDeleted",Boolean.parseBoolean((String) articlesOneByone.get("isDeleted")));
//        articlesList.add(articlesMap);
//    }
//    POMainObject.put("articles",articlesList);
//
//    SupplierMAp.put("name","HUL");
//    SupplierMAp.put("email","hul@gmail.com");
//    SupplierMAp.put("address","Hindustan Unilever Limited,Unilever House,B. D. Sawant Marg,Chakala, Andheri (E),Mumbai - 400 099.");
//    SupplierMAp.put("phone","7887678656");
//    SupplierMAp.put("supplierCode","H0022");
//    SupplierMAp.put("vendorCode","1001");
//    POMainObject.put("supplierDetails",SupplierMAp);
//
//}