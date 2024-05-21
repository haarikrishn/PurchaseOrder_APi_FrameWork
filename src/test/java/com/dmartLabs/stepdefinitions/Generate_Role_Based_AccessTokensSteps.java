package com.dmartLabs.stepdefinitions;

import com.dmartLabs.commonutils.ExtentReportManager;
import com.dmartLabs.commonutils.GenricUtils;
import com.dmartLabs.commonutils.RequestGenerator;
import com.dmartLabs.config.ConStants;
import com.dmartLabs.config.PropertyReader;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Generate_Role_Based_AccessTokensSteps implements ConStants {

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

    @When("Send username {string} and password {string} to get Access Token from Authentication Endpoint.")
    public void sendUsernameAndPasswordToGetAccessTokenFromAuthenticationEndpoint(String username, String password) {
        ExtentReportManager.logInfoDetails("Send username and password to get accessToken");
        Map<String, String> userCredintials = new HashMap<>();
        userCredintials.put("username", username);
        userCredintials.put("password", password);
//        response = requestGenerator.getRequest(GenericSteps.userCredential).log().all()
        response = requestGenerator.getRequest(userCredintials).log().all()
                .when().post(PropertyReader.getProperty(ENDPOINTS_PATHS_PROPERTIES_PATH, "AUTHENTICATION_ENDPOINT"));
        response.then().log().all();
        accessToken = response.jsonPath().get("accessToken");
        ExtentReportManager.logInfoDetails("Response Body is ");

        ExtentReportManager.logInfoDetails(response.prettyPrint());
//        ExtentReportManager.logJson(response.prettyPrint());
        ExtentReportManager.logInfoDetails("Response status code is " + response.getStatusCode());
        long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("Response Time is "+responseTime+" ms");
        CommonUtilities.setResponseInstance(response);

        if (response.statusCode()==200){
            List<String> roles = GenricUtils.getRolesFromAccessToken(accessToken);
            System.out.println("Length of Roles is ================> "+roles.size());
            System.out.println("Role is =============================> "+roles.get(0));

            if (roles.size()>=1){
                allRolesAccessToken = accessToken;
                System.out.println("allRolesAccessToken is =========> "+pickPlannerAccessToken);
            }
            else if (roles.size()==1 && roles.get(0).equals("DELIVERY PLANNER")){
                deliveryPlannerAccessToken = accessToken;
                System.out.println("deliveryPlanner Access token is =========> "+deliveryPlannerAccessToken);
            }
            else if(roles.size()==1 && roles.get(0).equals("DISPATCHER PLANNER")){
                dispatcherPlannerAccessToken = accessToken;
                System.out.println("dispatcherPlanner Access token is =========> "+dispatcherPlannerAccessToken);
            }
            else if (roles.size()==1 && roles.get(0).equals("DISPATCHER")){
                dispatcherAccessToken = accessToken;
                System.out.println("dispatcher Access token is =========> "+dispatcherAccessToken);
            }
            else if (roles.size()==1 && roles.get(0).equals("PICKER")){
                pickerAccessToken = accessToken;
                System.out.println("pickerAccessToken is =========> "+pickerAccessToken);
            }
            else if (roles.size()==1 && roles.get(0).equals("PICK PLANNER")){
                pickPlannerAccessToken = accessToken;
                System.out.println("pickPlannerAccessToken is =========> "+pickPlannerAccessToken);
            }
            else if (roles.size()==1 && roles.get(0).equals("CHECKIN OFFICER")){
                checkinOfficerAccessToken = accessToken;
                System.out.println("pickPlannerAccessToken is =========> "+pickPlannerAccessToken);
            }

        }
    }

    @Then("Validate that status code is {int}")
    public void validateThatStatusCodeIs(int expectedStatusCode) {

        ExtentReportManager.logInfoDetails("Validate that status code is 200");
        if (response.getStatusCode()==expectedStatusCode){
            ExtentReportManager.logPassDetails("Passed");
            ExtentReportManager.logInfoDetails("Expected status code is " +expectedStatusCode + " and the Actual status code is " +response.getStatusCode());
        }
        else {
            ExtentReportManager.logPassDetails("Failed");
            ExtentReportManager.logInfoDetails("Expected status code is " +expectedStatusCode + " and the Actual status code is " +response.getStatusCode());
        }

        Assert.assertEquals(response.statusCode(), expectedStatusCode);

    }

}
