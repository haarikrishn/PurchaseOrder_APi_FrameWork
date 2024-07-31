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
import io.restassured.common.mapper.TypeRef;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.dmartLabs.stepdefinitions.AuthorizationStep.Bearertoken;
//import static com.dmartLabs.stepdefinitions.AuthorizationStep.VendorBearerToken;
import static com.dmartLabs.stepdefinitions.CreateMultiplePurchaseOrderStep_HK.PoNumbersList;

public class GetPOSStep_HK {


    LinkedHashMap<String, Object> Queryparam = new LinkedHashMap<>();
    //public static LinkedHashMap<String, String> Bearertoken = new LinkedHashMap<>();
    List<String> PoNumberList1 = new ArrayList<>();
    List<String> PoDatList1 = new ArrayList<>();
    public static List<LinkedHashMap<String, Object>> AddAllItemsTList = new ArrayList<>();

    public static LinkedHashMap<String, List<LinkedHashMap<String, Object>>> ponumbersbody = new LinkedHashMap<>();
    public static List<String> GetpoItemNumberList = new ArrayList<>();
    ;
    public static String ResponsePoDate;

    Response GetPOresponse;
    String poStatus;

    @When("user call GetAllPO Api user should get all pos in DC")
    public void userCallGetAllPOApiUserShouldGetAllPosInDC() throws InterruptedException {

        Thread.sleep(1000);
        // Thread.sleep(6500);
        for (int i = 0; i < PoNumbersList.size(); i++) {
            System.out.println(PoNumbersList.get(i) + "======getpo" + i);
        }

        for (int i = 0; i < PoNumbersList.size(); i++) {

            String PoNumberONeByOne = PoNumbersList.get(i);

            Queryparam.put("poNumber", PoNumberONeByOne);
            //   Thread.sleep(4500);
            GetPOresponse = RequestGenerator.getRequestWithQueryParams123(Bearertoken, Queryparam).when().get(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "Get_PO_EndPoint"));

            //     System.out.println("be===================================================================================================");

//            String get = GetPOresponse.toString();
//            JsonPath jsonPath=new JsonPath(get);
//            jsonPath.prettyPrint();
            GetPOresponse.then().log().all();
            CommonUtilities.setResponseInstance(GetPOresponse);
            //        System.out.println("before=================================================================================================");
            ExtentReportManager.logJson(GetPOresponse.prettyPrint());

            long responseTime = GetPOresponse.getTimeIn(TimeUnit.MILLISECONDS);
            ExtentReportManager.logInfoDetails("Response Time to get a dock is " + responseTime + " ms");

            CommonUtilities.setpoResponse(GetPOresponse);

            Response GetPoResponse1 = CommonUtilities.getpoResponse();
            LinkedHashMap<String, Object> GetPoResponse1Map = GetPoResponse1.as(new TypeRef<LinkedHashMap<String, Object>>() {
            });
            String ResponsePonumber = (String) GetPoResponse1Map.get("poNumber");
            PoNumberList1.add(ResponsePonumber);

            ResponsePoDate = (String) GetPoResponse1Map.get("poDate");
            PoDatList1.add(ResponsePoDate);
            poStatus = (String) GetPoResponse1Map.get("poStatus");

            List<LinkedHashMap<String, Object>> GetPoResponse1MapList = (List<LinkedHashMap<String, Object>>) GetPoResponse1Map.get("items");
            Thread.sleep(500);

            ponumbersbody.put(PoNumberONeByOne, GetPoResponse1MapList);
            CommonUtilities.setPonumbersBody(ponumbersbody);

//            String GetpoItemNumber = (String) ponumbersbody.get(PoNumbersList.get(i)).get(i).get("poItemNumber");
//            GetpoItemNumberList.add(GetpoItemNumber) ;


            //    System.out.println(CommonUtilities.getPonumbersBody().toString() + "======================================CommonUtilities.getPonumbersBody().toString");
            AddAllItemsTList.addAll(GetPoResponse1MapList);
            CommonUtilities.setResponseItemsList(GetPoResponse1MapList);

            Thread.sleep(1000);
        }

        for (int k1 = 0; k1 < ponumbersbody.size(); k1++) {
            List<LinkedHashMap<String, Object>> ponum = ponumbersbody.get(PoNumbersList.get(k1));
            for (int k2 = 0; k2 < ponum.size(); k2++) {
                String GetpoItemNumber = (String) ponum.get(k2).get("poItemNumber");
                GetpoItemNumberList.add(GetpoItemNumber);
            }

        }

    }

    //ctrl+Alt+L
//    @When("user call GetAllPO Api user should get all pos in DC")
//    public void userCallGetAllPOApiUserShouldGetAllPosInDC() throws InterruptedException {
//
//
//        for (int i = 0; i < PoNumbersList.size(); i++) {
//            System.out.println(PoNumbersList.get(i) + "======getpo" + i);
//        }
//
//        for (int i = 0; i < PoNumbersList.size(); i++) {
//
//            String PoNumberONeByOne = PoNumbersList.get(i);
//
//            Queryparam.put("poNumber", PoNumberONeByOne);
//
//            //  RequestSpecification requestSpecification = RequestGenerator.getRequestWithQueryParams(CommonUtilities.genericHeader(), Queryparam).log().all();
////https://canary.api.meradmart.com/api/dlnk/asn/po/v2?poNumber=55187982645
//
//            Thread.sleep(4500);
//            RequestSpecification requestSpecification = RequestGenerator.getRequestWithQueryParams123(Bearertoken, Queryparam).log().all();
//
//            GetPOresponse = requestSpecification.get(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "Get_PO_EndPoint"));
//            GetPOresponse.then().log().all();
//            ExtentReportManager.logJson(GetPOresponse.prettyPrint());
//
//            long responseTime = GetPOresponse.getTimeIn(TimeUnit.MILLISECONDS);
//            ExtentReportManager.logInfoDetails("Response Time to get a dock is " + responseTime + " ms");
//
//            CommonUtilities.setpoResponse(GetPOresponse);
//            Response GetPoResponse1 = CommonUtilities.getpoResponse();
//
//            String ExpectedvendorCode = GetPoResponse1.jsonPath().get("supplierDetails.vendorCode");
//            String ActualVendorcode = CommonUtilities.getSuppliercode();
//
//            LinkedHashMap<String, Object> GetPoResponse1Map = GetPoResponse1.as(new TypeRef<LinkedHashMap<String, Object>>() {
//            });
//            //  LinkedHashMap<String, Object> GetporesponseValidate = CommonUtilities.getCreatePoMAp();
//            String ResponsePonumber = (String) GetPoResponse1Map.get("poNumber");
//            PoNumberList1.add(ResponsePonumber);
//
//            ResponsePoDate = (String) GetPoResponse1Map.get("poDate");
//            PoDatList1.add(ResponsePoDate);
//            poStatus = (String) GetPoResponse1Map.get("poStatus");
//
//            List<HashMap<String, Object>> ListOfitems = GetPoResponse1.jsonPath().get("items");
//            for (int n = 0; n < ListOfitems.size(); n++) {
//
//                String expectedSiteId = (String) ListOfitems.get(n).get("siteId");
//                //    expectedSiteId = (String) GetPoResponse1Map.get("siteId");
//                String ActualSiteId = CommonUtilities.getSiteId();
//                if (expectedSiteId.equals(ActualSiteId)) {
//                    ExtentReportManager.logInfoDetails("verifying ActualSiteId");
//                    ExtentReportManager.logPassDetails("Passed");
//                    ExtentReportManager.logInfoDetails("expectedSiteId is " + expectedSiteId + " and the ActualSiteId is " + ActualSiteId);
//                    Assert.assertEquals(expectedSiteId, ActualSiteId);
//                    //      System.out.println(expectedSiteId + " ===================>" + "expectedSiteId validation successful");
//
//
//                } else {
//                    ExtentReportManager.logFailureDetails("Failed");
//                    ExtentReportManager.logInfoDetails("expectedSiteId is  " + expectedSiteId + " and the ActualSiteId is " + ActualSiteId);
//                }
//            }
//
//            String Actualponumber = PoNumbersList.get(i);
//
//
//            if (PoNumberONeByOne.equals(ResponsePonumber)) {
//                ExtentReportManager.logInfoDetails("verifying ResponsePonumber");
//                ExtentReportManager.logPassDetails("Passed");
//                ExtentReportManager.logInfoDetails("ExpectedPonumber is " + PoNumberONeByOne + " and the Actualponumber is " + ResponsePonumber);
//                Assert.assertEquals(PoNumberONeByOne, ResponsePonumber);
//                System.out.println(PoNumberONeByOne + " ===================>" + "ActualpoNumber validation successful");
//
//                System.out.println("============================================================");
//                System.out.println(PoNumberONeByOne + "===============>" + poStatus);
//                ExtentReportManager.logPassDetails(PoNumberONeByOne + "===============>" + poStatus);
//
//
//            } else {
//                ExtentReportManager.logFailureDetails("Failed");
//                ExtentReportManager.logInfoDetails("ExpectedPonumber is  " + PoNumberONeByOne + " and the ActualpoNumber is " + ResponsePonumber);
//            }
//
//            if (ExpectedvendorCode.equals(ActualVendorcode)) {
//                ExtentReportManager.logInfoDetails("verifying ActualVendorcode");
//                ExtentReportManager.logPassDetails("Passed");
//                ExtentReportManager.logInfoDetails("ExpectedvendorCode is " + ExpectedvendorCode + " and the ActualVendorcode is " + ActualVendorcode);
//                Assert.assertEquals(ExpectedvendorCode, ActualVendorcode);
//                System.out.println(ExpectedvendorCode + " ===================>" + "ExpectedvendorCode validation successful");
//
//
//            } else {
//                ExtentReportManager.logFailureDetails("Failed");
//                ExtentReportManager.logInfoDetails("ExpectedvendorCode is  " + ExpectedvendorCode + " and the ActualVendorcode is " + ActualVendorcode);
//            }
//
//
//            Object ResponsevendorCode = GetPoResponse1Map.get("supplierDetails.vendorCode");
//            List<LinkedHashMap<String, Object>> GetPoResponse1MapList = (List<LinkedHashMap<String, Object>>) GetPoResponse1Map.get("items");
//
//            //   String GetPOpoDueDate = GetPoResponse1MapList.get(i).get("poDueDate");
//            // List<LinkedHashMap<String, Object>>value=new ArrayList<>();
//            Thread.sleep(500);
//
//            ponumbersbody.put(PoNumberONeByOne, GetPoResponse1MapList);
//
//            CommonUtilities.setPonumbersBody(ponumbersbody);
//            System.out.println(CommonUtilities.getPonumbersBody().toString() + "======================================CommonUtilities.getPonumbersBody().toString");
////
//            AddAllItemsTList.addAll(GetPoResponse1MapList);
//            //   AddAllItemsTList.add((LinkedHashMap<String, Object>) GetPoResponse1MapList);
//
//
//            CommonUtilities.setResponseItemsList(GetPoResponse1MapList);
//            Thread.sleep(1000);
//
//        }
//
//    }

//================================================================================================
    //get calender
//LinkedHashMap<String,List<LinkedHashMap<String,Object>>>  ponumbersbody=CommonUtilities.getPonumbersBody();

    @When("user call the get calender api")
    public void userCallTheGetCalenderApi() {

        LinkedHashMap<String, Object> CalenderPoMainObject1 = new LinkedHashMap<>();

        List<LinkedHashMap<String, Object>> CalenderPoMainList = new ArrayList<>();

        LinkedHashMap<String, Object> ArticlesMainObject;


        for (int i = 0; i < PoNumbersList.size(); i++) {
            LinkedHashMap<String, Object> CalenderPoMainObject = new LinkedHashMap<>();
            ;
            CalenderPoMainObject.put("poNumber", PoNumbersList.get(i));
            List<LinkedHashMap<String, Object>> ArticlesMainList = new ArrayList<>();
            ponumbersbody = CommonUtilities.getPonumbersBody();


            List<LinkedHashMap<String, Object>> poNumbercalenderItems = ponumbersbody.get(PoNumbersList.get(i));
            for (int j = 0; j < poNumbercalenderItems.size(); j++) {

                ArticlesMainObject = new LinkedHashMap<>();
                ArticlesMainObject.put("poItemNumber", poNumbercalenderItems.get(j).get("poItemNumber"));
                ArticlesMainObject.put("qty", poNumbercalenderItems.get(j).get("qty"));

                ArticlesMainList.add(ArticlesMainObject);
            }
            CalenderPoMainObject.put("articles", ArticlesMainList);
            CalenderPoMainList.add(CalenderPoMainObject);
        }

        CalenderPoMainObject1.put("po", CalenderPoMainList);


        RequestSpecification requestSpecification = RequestGenerator.getRequest(Bearertoken, CalenderPoMainObject1).log().all();

        Response GetCalenderResponse = requestSpecification.post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "GetCalenderEndPoint"));
        GetCalenderResponse.then().log().all();
        List<String> GetCalenderResponseTypeCast = GetCalenderResponse.as(new TypeRef<List<String>>() {
        });

        CommonUtilities.setcalenderResponseList(GetCalenderResponseTypeCast);

        CommonUtilities.setResponseInstance(GetCalenderResponse);

        ExtentReportManager.logJson(GetCalenderResponse.prettyPrint());
        //   ExtentReportManager.logInfoDetails("Response status code for get dock is " + GetCalenderResponse.getStatusCode());
        long responseTime = GetCalenderResponse.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("Response Time to get a calender is " + responseTime + " ms");

        //    System.out.println(CommonUtilities.getcalenderResponseList() + "============CommonUtilities.getcalenderResponseList()");
    }
}
//===============================================================================================
