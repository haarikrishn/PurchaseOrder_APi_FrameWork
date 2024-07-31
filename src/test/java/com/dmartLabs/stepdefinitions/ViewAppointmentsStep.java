package com.dmartLabs.stepdefinitions;


import com.dmartLabs.commonutils.ExtentReportManager;
import com.dmartLabs.commonutils.RequestGenerator;
import com.dmartLabs.config.ConStants;
import com.dmartLabs.config.PropertyReader;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.dmartLabs.config.ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH;
//import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.Bearer;
import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.ResponsePoDate;
//import static com.dmartLabs.stepdefinitions.GetSlotsStep.PoList;
//import static com.dmartLabs.stepdefinitions.GetSlotsStep.poDueDate;

public class ViewAppointmentsStep {

    LinkedHashMap<String, Object> ViewAppintmentMainObject = new LinkedHashMap<>();
    LinkedHashMap<String, String> viewFilterObject = new LinkedHashMap<>();
    Response ViewAppontmentResponse;
    String asnNumber;
    String appointmentId;
    String dstSiteId1;
    List<String> ExpectedpoNumbers;
    RequestGenerator requestGenerator = new RequestGenerator();

    @When("whenever vendor created purchase order,Dc should give availability time,and confirm appointment status and after view the appontment")
    public void wheneverVendorCreatedPurchaseOrderDcShouldGiveAvailabilityTimeAndConfirmAppointmentStatusAndAfterViewTheAppontment(DataTable dataTable) {
        String columnKey = null;
        String orderBy = null;
        String dstSiteId = null;

        List<Map<String, String>> ViewAppontmentDT = dataTable.asMaps(String.class, String.class);
        for (int i = 0; i < ViewAppontmentDT.size(); i++) {
            columnKey = ViewAppontmentDT.get(i).get("columnKey");
            orderBy = ViewAppontmentDT.get(i).get("orderBy");
            dstSiteId = ViewAppontmentDT.get(i).get("dstSiteId");
        }
        viewFilterObject.put("columnKey", columnKey);
        viewFilterObject.put("orderBy", orderBy);

        ViewAppintmentMainObject.put("sortFilter", viewFilterObject);

        ViewAppintmentMainObject.put("dstSiteId", dstSiteId);

        ViewAppintmentMainObject.put("startDate", ResponsePoDate);

//        ViewAppintmentMainObject.put("endDate", poDueDate);

        //==========================================

        Response ConfirmResponse = CommonUtilities.getResponseInstance();
        LinkedHashMap<String, Object> confirmViewAppointment = ConfirmResponse.as(new TypeRef<LinkedHashMap<String, Object>>() {
        });

        asnNumber = (String) confirmViewAppointment.get("asnNumber");
        appointmentId = (String) confirmViewAppointment.get("appointmentId");
        dstSiteId1 = (String) confirmViewAppointment.get("dstSiteId");
        ExpectedpoNumbers = (List<String>) confirmViewAppointment.get("poNumber");
        String status = (String) confirmViewAppointment.get("status");

        System.out.println(asnNumber + "=============>ExpectedAsnNumber");
        System.out.println(appointmentId + "=============>ExpectedappointmentId");
        System.out.println(dstSiteId1 + "=============>ExpecteddstSiteId1");
        System.out.println(status + "==============>ASN status");
        for (String poNumbers : ExpectedpoNumbers) {
            System.out.println(poNumbers + "======>ExpectedpoNumber");
        }

    }

    @And("send reuest to server using view Appontment Api")
    public void sendReuestToServerUsingViewAppontmentApi() {
        RequestSpecification viewAppontmentRequest = requestGenerator.getRequest(CommonUtilities.genericHeader(), ViewAppintmentMainObject);
        viewAppontmentRequest.log().all();
        ViewAppontmentResponse = viewAppontmentRequest.when().post(PropertyReader.getProperty(ENDPOINTS_PATHS_PROPERTIES_PATH, "ViewAppontment"));
        // AdjustResponse.then().log().all();
        ExtentReportManager.logJson("Response is " + ViewAppontmentResponse.getBody().prettyPrint());
        ExtentReportManager.logInfoDetails("Response status code is " + ViewAppontmentResponse.getStatusCode());
        long responseTime = ViewAppontmentResponse.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");
        CommonUtilities.setResponseInstance(ViewAppontmentResponse);


    }

    @And("verify the asnNumber appointmentId  and poNumber")
    public void verifyTheAsnNumberAppointmentIdAndPoNumber() {



        List<HashMap<String, Object>> appointmentsList = ViewAppontmentResponse.jsonPath().get("appointments");
        for (int k = 0; k < appointmentsList.size(); k++) {

            if (appointmentsList.get(k).get("asnNumber").equals(asnNumber)) {

                if (appointmentsList.get(k).get("asnNumber").equals(asnNumber)) {
                    ExtentReportManager.logInfoDetails("verifying actualAsnNumber");
                    ExtentReportManager.logPassDetails("Passed");
                  //  ExtentReportManager.logInfoDetails("actualAsnNumber is " + actualAsnNumber + " and the asnNumber is " + asnNumber);
                    Assert.assertEquals(appointmentsList.get(k).get("asnNumber"), asnNumber);
                    System.out.println(asnNumber + " ===================>" + "asnNumber validation successful");

                } else {
                    ExtentReportManager.logFailureDetails("Failed");
               //     ExtentReportManager.logInfoDetails("actualAsnNumber is  " + actualAsnNumber + " and the asnNumber is " + asnNumber);
                }


                if (appointmentsList.get(k).get("appointmentId").equals(appointmentId)) {
                    ExtentReportManager.logInfoDetails("verifying appointmentId");
                    ExtentReportManager.logPassDetails("Passed");
                    ExtentReportManager.logInfoDetails("actualAppointmentId is " + appointmentsList.get(k).get("appointmentId") + " and the appointmentId is " + appointmentId);
                    Assert.assertEquals(appointmentsList.get(k).get("appointmentId"), appointmentId);
                    System.out.println(appointmentsList.get(k).get("appointmentId") + " ===================>" + "appointmentId validation successful");

                } else {
                    ExtentReportManager.logFailureDetails("Failed");
                    ExtentReportManager.logInfoDetails("actualAppointmentId is  " + appointmentsList.get(k).get("appointmentId") + " and the appointmentId is " + appointmentId);
                }

                List<String> ActualPOnumbers = (List<String>) appointmentsList.get(k).get("poNumber");


                for (int m = 0; m < ActualPOnumbers.size(); m++) {
                    for (int n = 0; n < ExpectedpoNumbers.size(); n++) {
                        if (ActualPOnumbers.get(m).equals(ExpectedpoNumbers.get(n))) {
                            ExtentReportManager.logInfoDetails("verifying ActualpoNumber");
                            ExtentReportManager.logPassDetails("Passed");
                          //  ExtentReportManager.logInfoDetails("ExpectedpoNumbers is " + ExpectedpoNumbers + " and the ActualpoNumber is " + ActualpoNumber);
                            //     Assert.assertEquals(ExpectedpoNumbers, ActualpoNumber);
                            System.out.println(ExpectedpoNumbers + " ===================>" + "ActualpoNumber validation successful");

                        }
//                        else {
//                            ExtentReportManager.logFailureDetails("Failed");
//                        //    ExtentReportManager.logInfoDetails("ExpectedpoNumbers is  " + ExpectedpoNumbers + " and the ActualpoNumber is " + ActualpoNumber);
//                        }

                    }
                }


                if (dstSiteId1.equals(appointmentsList.get(k).get("dstSiteId"))) {
                    ExtentReportManager.logInfoDetails("verifying dstSiteId1");
                    ExtentReportManager.logPassDetails("Passed");
                    ExtentReportManager.logInfoDetails("dstSiteId1 is " + dstSiteId1 + " and the actualDstSiteId is " + appointmentsList.get(k).get("dstSiteId"));
                    Assert.assertEquals(dstSiteId1, appointmentsList.get(k).get("dstSiteId"));
                    System.out.println(dstSiteId1 + " ===================>" + "dstSiteId1 validation successful");

                } else {
                    ExtentReportManager.logFailureDetails("Failed");
                    ExtentReportManager.logInfoDetails("dstSiteId1 is  " + dstSiteId1 + " and the actualDstSiteId is " + appointmentsList.get(k).get("dstSiteId"));
                }


            }

        }
    }
}

