package com.dmartLabs.stepdefinitions;


import com.dmartLabs.commonutils.ExtentReportManager;
import com.dmartLabs.commonutils.GenricUtils;
import com.dmartLabs.commonutils.RequestGenerator;
import com.dmartLabs.config.ConStants;
import com.dmartLabs.config.PropertyReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.dmartLabs.stepdefinitions.AuthorizationStep.VendorBearerToken;
import static com.dmartLabs.stepdefinitions.CreateMultiplePurchaseOrderStep_HK.PoNumbersList;
//import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.Bearertoken;
import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.ponumbersbody;

public class getDCstep {

RequestGenerator requestGenerator=new RequestGenerator();

  //  LinkedHashMap<String, String> Bearertoken1 = new LinkedHashMap<>();
    @When("user call the  Get DC api user should get dc configuration")
    public void userCallTheGetDCApiUserShouldGetDcConfiguration() {


//        String Auth = "Bearer" + " " + VendorBearerToken;
//        //  Auth = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjRiMjhjMzY5LThhNTUtNGQ3Zi05M2YwLTExOGVkOTdmMmFjYSIsInR5cCI6IkpXVCIsInVzZSI6IkFUIn0.eyJleHAiOjE3MTkwNTQ1OTEsImlhdCI6MTcxODk2ODE5MSwiaXNzIjoiZG1hcnQiLCJqdGkiOiI2MzcxOWJjNC0zZTc2LTQ2OTEtOTM0My0xMWY4MWRhMWUzMDEiLCJuYmYiOjE3MTg5NjgxOTEsInN1YiI6IjI2ZjIyNzM0LTkxYzctNDQ3YS1hZmIwLTExM2NjMzQ4MTJlZiIsInZlciI6ImFhOGQ4YTRjYzEzYzE1NzQifQ.gfI_p5aFvXd__0lfIMR3TPgY0GjJ7LdbDdAw7inyZoIjNby0gOgwyk3rAqtyFPbKV1DrU8itQmRpjz-1n6wdmKxZ4CgVMxiio5RE4G9bSDt8HfzmepMjz7vAIRAu7hm2edzETQpe5My9yZK2lUOosgts0Lqh1RELr5-YD3XGZqg_iYz39LKxa_Cfi4ZVDmghedOEN7gOdgtBvLLnrxYEFHlg7c1gQvYwSHUZDLS6H9prrUUXZZJZLuEzwYGaa_cxa5Kx_l85Rfi3LWDNGCsa0ogob3jFYvjttz9yajEjiTMgvvzzGfw5eu6RdUOi39_YtcS0oYioHWZ0zpi7pq4r4Q";
//        // "Authorization:
//        Bearertoken1.put("Authorization", Auth);

        RequestSpecification requestSpecification = RequestGenerator.getRequest1(CommonUtilities.genericHeader()).log().all();

        Response GetDCresponse = requestSpecification.get(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "Get_Dc_config"));
        GetDCresponse.then().log().all();

      //  System.out.println(GetPOresponse.prettyPrint());

        ExtentReportManager.logInfoDetails("Response for get dock is - ");
        ExtentReportManager.logJson(GetDCresponse.prettyPrint());
        ExtentReportManager.logInfoDetails("Response status code for get dock is " + GetDCresponse.getStatusCode());
        long responseTime = GetDCresponse.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("Response Time to get a dock is " + responseTime + " ms");
      //  CommonUtilities.setResponseInstance(GetDCresponse);

    }


}

