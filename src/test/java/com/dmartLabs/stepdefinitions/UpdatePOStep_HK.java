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

import static com.dmartLabs.stepdefinitions.CreateMultiplePurchaseOrderStep_HK.*;

public class UpdatePOStep_HK {

    RequestGenerator requestGenerator = new RequestGenerator();
    public   List<String>SiteIdsList=new ArrayList<>();
    public   List<String>SupplierCodeList=new ArrayList<>();
;  List<Response> responseList = new ArrayList<>();
    LinkedHashMap<String,Object>UpdatePOMainObject1=new LinkedHashMap<>();
    Response UpdatePOResponse;
   public static int j = 0;

    @And("creating purchase order with different suppliercode {string} to updatepo")
    public void creatingPurchaseOrderWithDifferentSuppliercodeToUpdatepo(String supplierCode) {


        UpdatePOMainObject1.put("supplierCode", supplierCode);
        SupplierCodeList.add(supplierCode);
        CommonUtilities.setSuppliercode(supplierCode);

    }

    @When("user pass to  main mandatory fields to update a multiple purchase orders for updatepo")
    public void userPassToMainMandatoryFieldsToUpdateAMultiplePurchaseOrdersForUpdatepo(DataTable dataTable) {
        List<Map<String, String>> MainDataDT = dataTable.asMaps();
//supplier code  site id   po number
        String UpdatePONumber = null;
        for (int k= 0; k< PoNumbersList.size();k++ ) {
            System.out.println(PoNumbersList.get(k)+"=====update"+k);
            ExtentReportManager.logInfoDetails("poNumbersList is - ");
            ExtentReportManager.logInfoDetails("poNumbersList " + PoNumbersList.get(k)+"=====update"+k);
        }



        for (int i = 0; i < MainDataDT.size(); i++) {


       
            for (j = j; j < PoNumbersList.size(); ) {
                List<LinkedHashMap<String, Object>> termsOfDlvList = new ArrayList<>();

                UpdatePONumber = PoNumbersList.get(j);

                UpdatePOMainObject1.put("poNumber", UpdatePONumber);


                UpdatePOMainObject1.put("companyCode", MainDataDT.get(i).get("companyCode"));
                UpdatePOMainObject1.put("documentType", MainDataDT.get(i).get("documentType"));
                UpdatePOMainObject1.put("poDate", GenricUtils.getDate("yyyyMMdd"));

            //    String UpdatesupplierCode = SupplierCodeList.get(j);

             //   UpdatePOMainObject1.put("supplierCode", UpdatesupplierCode);


                String supplierCodeMap = (String) UpdatePOMainObject1.get("supplierCode");
                SupplierCodeList.add(supplierCodeMap);

                UpdatePOMainObject1.put("buyerCode", MainDataDT.get(i).get("buyerCode"));
                UpdatePOMainObject1.put("buyerEmail", MainDataDT.get(i).get("buyerEmail"));
                LinkedHashMap<String, Object> termsOfDlvMap1 = new LinkedHashMap<>();

                termsOfDlvMap1.put("tdLine", MainDataDT.get(i).get("tdLine"));
                termsOfDlvMap1.put("tdFormat", MainDataDT.get(i).get("tdFormat"));

                LinkedHashMap<String, Object> termsOfDlvMap2 = new LinkedHashMap<>();

                termsOfDlvMap2.put("tdLine1", MainDataDT.get(i).get("tdLine1"));
                termsOfDlvMap2.put("tdFormat1", MainDataDT.get(i).get("tdFormat1"));

                termsOfDlvList.add(termsOfDlvMap1);
                termsOfDlvList.add(termsOfDlvMap2);

                UpdatePOMainObject1.put("termsOfDlv", termsOfDlvList);
                j++;
                break;
            }


        }
    }


    @And("user add to  list of articles  {string} {string} {string} {string}to update a multiple purchase orders")
    public void userAddToListOfArticlesToUpdateAMultiplePurchaseOrders(String excelFile, String SheetName, String requiredTasks, String siteId) throws JsonProcessingException {

        ExcelUtils excelUtils = new ExcelUtils(excelFile);
        int requiredTasks1 = Integer.parseInt(requiredTasks);
        List<LinkedHashMap<String, Object>> updatePOItems = excelUtils.getPOItems(SheetName, requiredTasks1, siteId);


        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(updatePOItems);
        JsonPath js = new JsonPath(json);
        UpdatePOMainObject1.put("items", updatePOItems);

    }


    @And("user send multiple requests to server using Update Po APi")
    public void userSendMultipleRequestsToServerUsingUpdatePoAPi() throws InterruptedException {

Thread.sleep(500);
        Map<String, String> userCred = GenericSteps.userCredential;
        HashMap<String, String> userCredential = new HashMap<>();
        userCredential.putAll(userCred);
        userCredential.put("requestId", GenricUtils.getRandomDeliveryNumber());


        UpdatePOResponse = requestGenerator.getRequestpo(userCredential, UpdatePOMainObject1).log().all()
                .when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CREATE_PO_ENDPOINT"));
        UpdatePOResponse.then().log().all();

        System.out.println(UpdatePOResponse.prettyPrint());

        ExtentReportManager.logInfoDetails("Response is - ");
        ExtentReportManager.logJson(UpdatePOResponse.prettyPrint());
        ExtentReportManager.logInfoDetails("Response status code is " + UpdatePOResponse.getStatusCode());
        long responseTime = UpdatePOResponse.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");
        responseList.add(UpdatePOResponse);

      CommonUtilities.setResponseInstance(UpdatePOResponse);

        TimeUnit.SECONDS.sleep(1); //
    }



}