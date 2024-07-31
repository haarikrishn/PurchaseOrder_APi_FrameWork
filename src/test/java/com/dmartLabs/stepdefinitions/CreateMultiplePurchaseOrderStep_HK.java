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

public class CreateMultiplePurchaseOrderStep_HK {
   public LinkedHashMap<String,Object>POMainObject=new LinkedHashMap<>();
   public  List<LinkedHashMap<String,Object>>articlesList=new ArrayList<>();
   
   public LinkedHashMap<String,Object>SupplierMAp=new LinkedHashMap<>();

public    LinkedHashMap<String,Object>termsOfDlvMap=new LinkedHashMap<>();

public  static  List<String>PoNumbersList=new ArrayList<>();
public   List<String>SiteIdsList=new ArrayList<>();
public   List<String>SupplierCodeList=new ArrayList<>();


RequestGenerator requestGenerator=new RequestGenerator();
    Response CreatePOResponse;

//==============================================================================================
    //excel


    @And("creating purchase order with different suppliercode {string}")
    public void creatingPurchaseOrderWithDifferentSuppliercode(String supplierCode) {
        POMainObject.put("supplierCode", supplierCode);
        SupplierCodeList.add(supplierCode);
        CommonUtilities.setSuppliercode(supplierCode);

    }


    @When("user pass to  main mandatory fields to create a multiple purchase orders")
    public void userPassToMainMandatoryFieldsToCreateAMultiplePurchaseOrders(DataTable dataTable) {
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
            SiteIdsList.add(supplierCodeMap);

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
    @And("user add to  list of articles  {string} {string} {string} {string}to create a multiple purchase orders")
    public void userAddToListOfArticlesToCreateAMultiplePurchaseOrders(String excelFile, String SheetName, String requiredTasks,String siteId) throws JsonProcessingException {
        ExcelUtils excelUtils = new ExcelUtils(excelFile);
        int requiredTasks1 = Integer.parseInt(requiredTasks);
        List<LinkedHashMap<String, Object>> POItems = excelUtils.getPOItems(SheetName, requiredTasks1,siteId);
        SiteIdsList.add(siteId);
        CommonUtilities.setSiteId(siteId);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(POItems);
        JsonPath js = new JsonPath(json);
        //js.prettyPrint();
        POMainObject.put("items",POItems);
    }

    @And("user send multiple requests to server using create Po APi")
    public void userSendMultipleRequestsToServerUsingCreatePoAPi() throws InterruptedException {
Thread.sleep(500);
        ExtentReportManager.logInfoDetails("Requester calls the outbound delivery endpoint to create new delivery for a wave");
        Map<String, String> userCred = GenericSteps.userCredential;
        HashMap<String, String> userCredential = new HashMap<>();
        userCredential.putAll(userCred);
        userCredential.put("requestId", GenricUtils.getRandomDeliveryNumber());

        CreatePOResponse = requestGenerator.getRequestpo(userCredential, POMainObject).log().all()
                .when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CREATE_PO_ENDPOINT"));
        CreatePOResponse.then().log().all();
        ExtentReportManager.logInfoDetails("Response is - ");
        ExtentReportManager.logJson(CreatePOResponse.prettyPrint());
        ExtentReportManager.logInfoDetails("Response status code is " + CreatePOResponse.getStatusCode());
        long responseTime = CreatePOResponse.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");
 CommonUtilities.setResponseInstance(CreatePOResponse);

        TimeUnit.SECONDS.sleep(1); //
       // CommonUtilities.setPOResponseMap((LinkedHashMap<String, Object>) CreatePOResponse);

    }


}


