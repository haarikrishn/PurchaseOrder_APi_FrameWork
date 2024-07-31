package com.dmartLabs.stepdefinitions;

import com.dmartLabs.commonutils.ExtentReportManager;
import com.dmartLabs.commonutils.GenricUtils;
import com.dmartLabs.commonutils.RequestGenerator;
import com.dmartLabs.config.ConStants;
import com.dmartLabs.config.PropertyReader;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import rst.pdfbox.layout.text.Constants;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GenerateAccessTokenSteps extends Constants implements ConStants {

    RequestGenerator requestGenerator = new RequestGenerator();
    Response response;
    public static String accessToken;
    public static String deliveryPlannerAccessToken;
    public static String dispatcherPlannerAccessToken;
    public static String pickerAccessToken;
    public static String dispatcherAccessToken;
    public static String pickPlannerAccessToken;
    public static String checkinOfficerAccessToken;
    public static String allRolesAccessToken;

    @When("Send username and password to get accessToken")
    public String sendUsernameAndPasswordToGetAccessToken() {
        ExtentReportManager.logInfoDetails("Send username and password to get accessToken");
        response = requestGenerator.getRequest(GenericSteps.userCredential).log().all()
                .when().post(PropertyReader.getProperty(ENDPOINTS_PATHS_PROPERTIES_PATH, "AUTHENTICATION_ENDPOINT"));
        response.then().log().all();
        accessToken = response.jsonPath().get("accessToken");

        ExtentReportManager.logInfoDetails("Authentication API Response is : ");
        ExtentReportManager.logJson(response.prettyPrint());
        ExtentReportManager.logInfoDetails("Response Status Code for Authentication is " + response.getStatusCode());
        long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("Response Time is "+responseTime+" ms");
        CommonUtilities.setResponseInstance(response);

        if (response.getStatusCode()==200){

            String clientId = GenricUtils.getClientID_FromAccessToken(accessToken);
            CommonUtilities.setClientId(clientId);
            String siteId = GenricUtils.getSiteID_FromAccessToken(accessToken);
            CommonUtilities.setSiteId(siteId);

            List<String> roles = GenricUtils.getRolesFromAccessToken(accessToken);
            if (roles.size()==1 && roles.get(0).equals("DELIVERY PLANNER")){
                deliveryPlannerAccessToken = accessToken;
                return deliveryPlannerAccessToken;
            }
            else if(roles.size()==1 && roles.get(0).equals("DISPATCHER PLANNER")){
                dispatcherPlannerAccessToken = accessToken;
                return dispatcherPlannerAccessToken;
            }
            else if (roles.size()==1 && roles.get(0).equals("DISPATCHER")){
                dispatcherAccessToken = accessToken;
                return dispatcherAccessToken;
            }
            else if (roles.size()==1 && roles.get(0).equals("PICKER")){
                pickerAccessToken = accessToken;
                return pickerAccessToken;
            }
            else if (roles.size()==1 && roles.get(0).equals("PICK PLANNER")){
                pickPlannerAccessToken = accessToken;
                return pickPlannerAccessToken;
            }
            else if (roles.size()==1 && roles.get(0).equals("CHECKIN OFFICER")){
                checkinOfficerAccessToken = accessToken;
                return checkinOfficerAccessToken;
            }
            else if (roles.size()>=1){
                allRolesAccessToken = accessToken;
                return allRolesAccessToken;
            }
        }
        return "";
    }

    @When("Send username and password from Feature File to get accessToken")
    public String sendUsernameAndPasswordFromFeatureFileToGetAccessToken(DataTable dataTable) {
        ExtentReportManager.logInfoDetails("Send username and password to get Access Token");

        Map<String, String> userCredential = dataTable.asMap(String.class, String.class);

        response = requestGenerator.getRequest(userCredential).log().all()
                .when().post(PropertyReader.getProperty(ENDPOINTS_PATHS_PROPERTIES_PATH, "AUTHENTICATION_ENDPOINT"));
        response.then().log().all();
        accessToken = response.jsonPath().get("accessToken");
        ExtentReportManager.logInfoDetails("Authentication API Response is : ");
        ExtentReportManager.logJson(response.prettyPrint());
        ExtentReportManager.logInfoDetails("Response status code is " + response.getStatusCode());
        long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("Response Time is "+responseTime+" ms");
        CommonUtilities.setResponseInstance(response);

        if (response.statusCode()==200){
            List<String> roles = GenricUtils.getRolesFromAccessToken(accessToken);
            if (roles.contains("DELIVERY PLANNER")){
                deliveryPlannerAccessToken = accessToken;
                return deliveryPlannerAccessToken;
            }
        }
        return "";
    }


}
