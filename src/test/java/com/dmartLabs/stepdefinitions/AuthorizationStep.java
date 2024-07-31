package com.dmartLabs.stepdefinitions;


import com.dmartLabs.commonutils.ExtentReportManager;
import com.dmartLabs.commonutils.GenricUtils;
import com.dmartLabs.commonutils.RequestGenerator;
import com.dmartLabs.config.ConStants;
import com.dmartLabs.config.PropertyReader;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

//import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.Bearer;
//import static com.dmartLabs.stepdefinitions.GetSlotsStep.PoList;
//import static com.dmartLabs.stepdefinitions.GetSlotsStep.poDueDate;

public class AuthorizationStep {
    LinkedHashMap<String, String> QueryParam = new LinkedHashMap<>();
    LinkedHashMap<String, String> headerPreCheck = new LinkedHashMap<>();
    LinkedHashMap<String,String>TokenHeader=new LinkedHashMap<>();
    LinkedHashMap<String, Object> MainObjPreCheck = new LinkedHashMap<>();
    LinkedHashMap<String, String> PinObject = new LinkedHashMap<>();
    LinkedHashMap<String,String>AuthObject=new LinkedHashMap<>();
    List<String> ScopeObject = new ArrayList<>();
    public String clientId;
    public String Pincode;
    LinkedHashMap<String, String> OtpObject = new LinkedHashMap<>();
    LinkedHashMap<String, String> TokenObject = new LinkedHashMap<>();
    String prechecknonce;
    String otpcode;
    String  AuthcodeNonce;
    public static String VendorBearerToken;
    // https://canary.id.dmartlink.com/api/precheck?client_id=b4204b8f-81ae-4d32-8724-06e4368123a1

    @When("pass precheck condition for client id")
    public void passPrecheckConditionForClientId(DataTable dataTable) {
        List<Map<String, String>> clientDt = dataTable.asMaps();
        headerPreCheck.put("origin", "https://canary.id.dmartlink.com");
        TokenHeader.put("origin","https://canary.dmartlink.com");

        for (int i = 0; i < clientDt.size(); i++) {
            clientId = clientDt.get(i).get("client_id");
            QueryParam.put("client_id", clientDt.get(i).get("client_id"));
            String UUid = GenricUtils.generateUUID();

            MainObjPreCheck.put("nonce", UUid);
            MainObjPreCheck.put("state", clientDt.get(i).get("state"));
            MainObjPreCheck.put("user_name", clientDt.get(i).get("user_name"));
            MainObjPreCheck.put("response_type", clientDt.get(i).get("response_type"));
            MainObjPreCheck.put("redirect_url", clientDt.get(i).get("redirect_url"));

            ScopeObject.add(clientDt.get(i).get("scope"));

            MainObjPreCheck.put("scope", ScopeObject);

        }

        RequestSpecification requestSpecification = RequestGenerator.getRequestWithQueryParamHeader(QueryParam, headerPreCheck, MainObjPreCheck).log().all();

        Response PrecheckResponse = requestSpecification.post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "preCheckEndpoint"));
        PrecheckResponse.then().log().all();
        prechecknonce = PrecheckResponse.jsonPath().get("nonce");
        ExtentReportManager.logJson(PrecheckResponse.prettyPrint());
        long responseTime = PrecheckResponse.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("PrecheckResponse " + responseTime + " ms");
        CommonUtilities.setResponseInstance(PrecheckResponse);

    }

    @And("Generate otp for particular client id")
    public void generateOtpForParticularClientId(DataTable dataTable) {
        List<Map<String, String>> OtpDt = dataTable.asMaps();
        for (int i = 0; i < OtpDt.size(); i++) {
            OtpObject.put("otp", OtpDt.get(i).get("otp"));
            OtpObject.put("nonce", prechecknonce);
        }

        RequestSpecification requestSpecification = RequestGenerator.getRequestWithQueryParamHeader(QueryParam, headerPreCheck, OtpObject).log().all();

        Response OtpResponse = requestSpecification.post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "OtpEndPoint"));
        OtpResponse.then().log().all();
        otpcode = OtpResponse.jsonPath().get("nonce");


        ExtentReportManager.logJson(OtpResponse.prettyPrint());
        long responseTime = OtpResponse.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("Response Time otpcode is " + responseTime + " ms");
        CommonUtilities.setResponseInstance(OtpResponse);
    }

    @And("enter pin to access the application")
    public void enterPinToAccessTheApplication(DataTable dataTable) {
        List<Map<String, String>> PinDT = dataTable.asMaps();
        for (int i = 0; i < PinDT.size(); i++) {
            PinObject.put("nonce", otpcode);
            PinObject.put("pin", PinDT.get(i).get("pin"));
        }

        RequestSpecification requestSpecification = RequestGenerator.getRequestWithQueryParamHeader(QueryParam, headerPreCheck, PinObject).log().all();

        Response PinResponse = requestSpecification.post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "PinEndPoint"));
        PinResponse.then().log().all();
        Pincode = PinResponse.jsonPath().get("nonce");


        ExtentReportManager.logJson(PinResponse.prettyPrint());
        long responseTime = PinResponse.getTimeIn(TimeUnit.MILLISECONDS);
        CommonUtilities.setResponseInstance(PinResponse);

    }

    @And("enter the Auth code to access the application")
    public void enterTheAuthCodeToAccessTheApplication(DataTable dataTable) {


        AuthObject.put("nonce",Pincode);

        RequestSpecification requestSpecification = RequestGenerator.getRequestWithQueryParamHeader(QueryParam, headerPreCheck, AuthObject).log().all();

        Response AuthcodeResponse = requestSpecification.post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "AuthcodeEndPoint"));
        AuthcodeResponse.then().log().all();
        AuthcodeNonce= AuthcodeResponse.jsonPath().get("authorization_code");
        ExtentReportManager.logJson(AuthcodeResponse.prettyPrint());
        long responseTime = AuthcodeResponse.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("AuthcodeNonce is " + responseTime + " ms");
        CommonUtilities.setResponseInstance(AuthcodeResponse);


    }


    @And("pass otp and generate bearer token")
    public void passOtpAndGenerateBearerToken(DataTable dataTable) {
        List<Map<String, String>> TokenDt = dataTable.asMaps();
        for (int i = 0; i < TokenDt.size(); i++) {

            TokenObject.put("code", AuthcodeNonce);
            TokenObject.put("grant_type", TokenDt.get(i).get("grant_type"));

        }

        RequestSpecification requestSpecification = RequestGenerator.getRequestWithQueryParamHeader(QueryParam, TokenHeader, TokenObject).log().all();

        Response BearerResponse = requestSpecification.post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "TokenEndPoint"));
        BearerResponse.then().log().all();
        VendorBearerToken = BearerResponse.jsonPath().get("accessToken");
        ExtentReportManager.logJson(BearerResponse.prettyPrint());
        long responseTime = BearerResponse.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("VendorBearerToken " + responseTime + " ms");
        CommonUtilities.setResponseInstance(BearerResponse);


    }
    public static LinkedHashMap<String, String> Bearertoken = new LinkedHashMap<>();
    public static String Auth;
    @And("save the  bearer token")
    public void saveTheBearerToken() {


        Auth = "Bearer" + " " + VendorBearerToken;
        //  Auth = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjRiMjhjMzY5LThhNTUtNGQ3Zi05M2YwLTExOGVkOTdmMmFjYSIsInR5cCI6IkpXVCIsInVzZSI6IkFUIn0.eyJleHAiOjE3MTkwNTQ1OTEsImlhdCI6MTcxODk2ODE5MSwiaXNzIjoiZG1hcnQiLCJqdGkiOiI2MzcxOWJjNC0zZTc2LTQ2OTEtOTM0My0xMWY4MWRhMWUzMDEiLCJuYmYiOjE3MTg5NjgxOTEsInN1YiI6IjI2ZjIyNzM0LTkxYzctNDQ3YS1hZmIwLTExM2NjMzQ4MTJlZiIsInZlciI6ImFhOGQ4YTRjYzEzYzE1NzQifQ.gfI_p5aFvXd__0lfIMR3TPgY0GjJ7LdbDdAw7inyZoIjNby0gOgwyk3rAqtyFPbKV1DrU8itQmRpjz-1n6wdmKxZ4CgVMxiio5RE4G9bSDt8HfzmepMjz7vAIRAu7hm2edzETQpe5My9yZK2lUOosgts0Lqh1RELr5-YD3XGZqg_iYz39LKxa_Cfi4ZVDmghedOEN7gOdgtBvLLnrxYEFHlg7c1gQvYwSHUZDLS6H9prrUUXZZJZLuEzwYGaa_cxa5Kx_l85Rfi3LWDNGCsa0ogob3jFYvjttz9yajEjiTMgvvzzGfw5eu6RdUOi39_YtcS0oYioHWZ0zpi7pq4r4Q";
        // "Authorization:
        Bearertoken.put("Authorization", Auth);
    }
}

