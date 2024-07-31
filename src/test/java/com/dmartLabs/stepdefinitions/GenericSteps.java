package com.dmartLabs.stepdefinitions;

import com.dmartLabs.commonutils.ExtentReportManager;
import com.dmartLabs.commonutils.GenricUtils;
import com.dmartLabs.config.PropertyReader;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import rst.pdfbox.layout.text.Constants;

import java.util.HashMap;
import java.util.Map;

import static com.dmartLabs.config.ConStants.JSON_SCHEMA_VALIDATION_PATH;

public class GenericSteps extends Constants {

    public static String username;
    public static String password;
    public static Map<String, String> userCredential;
    public static HashMap<String, String> totalObj = new HashMap<>();

    @Given("Send Generic data using Feature File")
    public void sendGenericDataUsingFeatureFile(DataTable dataTable) {

        userCredential = dataTable.asMap(String.class, String.class);
        username = userCredential.get("username");
        password = userCredential.get("password");

    }

    @Given("Give Username and Password to get Access Token")
    public void giveUsernameAndPasswordToGetAccessToken(DataTable dataTable) {
        ExtentReportManager.logInfoDetails("Give Username and Password to get Access Token");
        userCredential = dataTable.asMap(String.class, String.class);


    }

    @Given("Give Username and Password for Access Token")
    public void giveUsernameAndPasswordForAccessToken(DataTable dataTable) {
        ExtentReportManager.logInfoDetails("Give Username and Password for Access Token");
        userCredential = dataTable.asMap(String.class, String.class);

    }

    @Then("verify that status code be equal to {string}")
    public void verifyThatStatusCodeBeEqualTo(String expectedStatusCode) {
        ExtentReportManager.logInfoDetails("verify that status code is : " + expectedStatusCode);
        int statucode = Integer.parseInt(String.valueOf(CommonUtilities.getResponseInstance().getStatusCode()));
        if (statucode == Integer.parseInt(expectedStatusCode)) {
            ExtentReportManager.logPassDetails("Passed");
            ExtentReportManager.logInfoDetails("Expected Status Code is " + statucode + " and the Actual Status Code is " + CommonUtilities.getResponseInstance().getStatusCode());
        } else {
            ExtentReportManager.logFailureDetails("Failed");
            ExtentReportManager.logInfoDetails("Expected status code is " + expectedStatusCode + " and Actual status code is " + CommonUtilities.getResponseInstance().getStatusCode());
        }
        CommonUtilities.getResponseInstance().then().assertThat().statusCode(Integer.parseInt(expectedStatusCode));
    }

    @Then("Verify that status code be equal to {int}")
    public void verifyThatStatusCodeBeEqual(int expectedStatusCode) {
        ExtentReportManager.logInfoDetails("Verify that status code be equal to " + expectedStatusCode);

        int actualStatuscode = CommonUtilities.getResponseInstance().getStatusCode();
        if (actualStatuscode == expectedStatusCode) {
            ExtentReportManager.logPassDetails("Passed");
            ExtentReportManager.logInfoDetails("Expected Status Code is " + expectedStatusCode + " and the Actual Status Code is " + CommonUtilities.getResponseInstance().getStatusCode());
        } else {
            ExtentReportManager.logFailureDetails("Failed");
            ExtentReportManager.logInfoDetails("Expected status code is " + expectedStatusCode + " but the Actual status code is " + CommonUtilities.getResponseInstance().getStatusCode());
        }

        CommonUtilities.getResponseInstance().then().assertThat().statusCode(expectedStatusCode);
    }


    @Then("verify that schema should be equal {string}")
    public void verifyThatSchemaShouldBeEqual(String schemaFile) {

        ExtentReportManager.logInfoDetails("Verify that schema should be equal " + schemaFile);
        CommonUtilities.getResponseInstance().then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(PropertyReader.fileReaders(JSON_SCHEMA_VALIDATION_PATH, schemaFile)));
        JsonSchemaValidator schema = JsonSchemaValidator.matchesJsonSchema(PropertyReader.fileReaders(JSON_SCHEMA_VALIDATION_PATH, schemaFile));
        ExtentReportManager.logInfoDetails(" Schema is pass ");

    }

    @Then("i validate that response time should be less then ms")
    public void iValidateThatResponseTimeShouldBeLessThenMs() {
//        ExtentReportManager.logInfoDetails("I get response Time is " + CommonUtilities.getResponseInstance().getTime() + "ms");
//        ExtentReportManager.logInfoDetails("API URL IS "+CommonUtilities.getResponseInstance().getHeader("Request URI"));
//        ExtentReportManager.logHeaders(CommonUtilities.getResponseInstance().getHeaders().asList());


//        System.out.println("==================> Response time " + CommonUtilities.getResponseInstance().getTime() + "ms");
//        int acthualTime = (int) CommonUtilities.getResponseInstance().getTime();
//        System.out.println("==================> achual " + acthualTime);
//        int MinimumTime = Integer.parseInt(PropertyReader.getProperty(ENDPOINTS_PATHS_PROPERTIES_PATH, "MinimumTime"));
//        int MedianTime = Integer.parseInt(PropertyReader.getProperty(ENDPOINTS_PATHS_PROPERTIES_PATH, "MedianTime"));
//        int MaximumTime = Integer.parseInt(PropertyReader.getProperty(ENDPOINTS_PATHS_PROPERTIES_PATH, "MaximumTime"));
//        System.out.println(MinimumTime + "---------" + MedianTime + "--------------" + MaximumTime);
//
//        if (acthualTime < MinimumTime) {
//            ExtentReportManager.logPassDetails("Passed");
//            ExtentReportManager.logPassDetails("MinimumTime time taken is " + MinimumTime + " and pass and actual time is " + acthualTime + "ms");
//            System.out.println("MinimumTime time taken and pass and actual time is" + acthualTime);
//        } else if ((acthualTime < MedianTime) && (acthualTime > MinimumTime)) {
//
//            ExtentReportManager.logWarningDetails("Passed");
//            ExtentReportManager.logWarningDetails("MedianTime time is " + MedianTime + " and actual time taken is " + acthualTime + "ms");
//            System.out.println("MedianTime time taken and pass and actual time is " + acthualTime);
//        } else if (acthualTime < MaximumTime && (acthualTime > MedianTime)) {
//            ExtentReportManager.logDeepWarningDetails("Passed");
//            ExtentReportManager.logDeepWarningDetails("MaximumTime time is " + MaximumTime + " taken and pass and actual time is " + acthualTime + "ms");
//            System.out.println("MaximumTime time taken and pass and pass and actual time is " + acthualTime);
//        } else if (acthualTime > MaximumTime) {
//            ExtentReportManager.logFailedDetails("Pass");
//            System.out.println("Response Time Failed Expected time is " + acthualTime + " But Acual is " + acthualTime + "ms");
//            ExtentReportManager.logFailedDetails("Expected is " + MaximumTime + " But Actual is " + acthualTime + "ms");
//        }
//=============================================================================

    }

    @Given("Give Username and Password and change requestId")
    public void giveUsernameAndPasswordAndChangeRequestId(DataTable dataTable) {
        String Randomerequest = GenricUtils.getRandomDeliveryNumber() + "-" + 1;
        ;
        Map<String, String> RequestDT = dataTable.asMap(String.class, String.class);
        String usernamevalue = RequestDT.get("username");
        String passwordValue = RequestDT.get("password");
        String requestIdValue = RequestDT.get("requestId");
        totalObj.put("username", usernamevalue);
        totalObj.put("password", passwordValue);
        try {
            if (requestIdValue.equals("")) {
                totalObj.put("requestId", Randomerequest);
            } else {
                totalObj.put("requestId", requestIdValue);
            }
        } catch (Exception e) {
            totalObj.put("requestId", Randomerequest);
        }


    }

    @And("validate schema for {string} file")
    public void validateSchemaForFile(String schemaFile) {
        try {
            ExtentReportManager.logInfoDetails("verify that schema should be equal " + schemaFile + "for po");
            CommonUtilities.getResponseInstance().then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(PropertyReader.fileReaders(JSON_SCHEMA_VALIDATION_PATH, schemaFile)));
            //    //src/main/java/com/dmartLabs/config/schema/po.json
            JsonSchemaValidator schema = JsonSchemaValidator.matchesJsonSchema(PropertyReader.fileReaders(JSON_SCHEMA_VALIDATION_PATH, schemaFile));
            ExtentReportManager.logInfoDetails(" Schema is pass " + schema);
            System.out.println("===========================>" + "schema validation successful");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage()+"==================>error");
        }
    }


}