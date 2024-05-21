package com.dmartLabs.stepdefinitions;

import com.dmartLabs.commonutils.ExcelUtils;
import com.dmartLabs.commonutils.ExtentReportManager;
import com.dmartLabs.commonutils.GenricUtils;
import com.dmartLabs.commonutils.RequestGenerator;
import com.dmartLabs.config.ConStants;
import com.dmartLabs.config.PropertyReader;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.dmartLabs.config.ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH;

public class CreatePurchaseOrderSteps {

    private LinkedHashMap<String, Object> requestPayload;
    private ExcelUtils excelUtils;
    private Response response;

    @And("Get the PO number to create a new purchase order")
    public void getThePONumberToCreateANewPurchaseOrder() {
        ExtentReportManager.logInfoDetails("Get the PO number to create a new purchase order");
        requestPayload = new LinkedHashMap<>();
        requestPayload.put("poNumber", GenricUtils.getPONumber());
    }

    @And("Company code for the purchase order is {string}")
    public void companyCodeForThePurchaseOrderIs(String companyCode) {
        ExtentReportManager.logInfoDetails("Company code for the purchase order is "+companyCode);
        requestPayload.put("companyCode", companyCode);
    }

    @And("Document type for the purchase order is {string}")
    public void documentTypeForThePurchaseOrderIs(String documentType) {
        ExtentReportManager.logInfoDetails("Document type for the purchase order is "+documentType);
        requestPayload.put("documentType", documentType);
    }

    @And("Get the po date for the purchase order")
    public void getThePoDateForThePurchaseOrder() {
        ExtentReportManager.logInfoDetails("Get the po date for the purchase order");
        requestPayload.put("poDate", GenricUtils.getUTC_Format_CurrentDateTime());
    }

    @And("Supplier for the purchase order is {string}")
    public void supplierForThePurchaseOrderIs(String supplier) {
        ExtentReportManager.logInfoDetails("Supplier for the purchase order is "+supplier);
        requestPayload.put("supplier", supplier);
    }

    @And("Buyer for the purchase order is {string}")
    public void buyerForThePurchaseOrderIs(String buyer) {
        ExtentReportManager.logInfoDetails("Buyer for the purchase order is "+buyer);
        requestPayload.put("buyer", buyer);
    }

    @And("Provide the Items details for which purchase order has to be created")
    public void provideTheItemsDetailsForWhichPurchaseOrderHasToBeCreated(DataTable dataTable) {
        ExtentReportManager.logInfoDetails("Provide the Items details for which purchase order has to be created");
        List<Map<String, Object>> items = dataTable.asMaps(String.class, Object.class);
        List<LinkedHashMap<String, Object>> expectedItems = new ArrayList<>();

        for (int i=0;i<items.size();i++){
            LinkedHashMap<String, Object> item = new LinkedHashMap<>();
            item.putAll(items.get(i));
            expectedItems.add(item);
            if (items.get(i).containsKey("quantity")) {
                String quantity = (String) items.get(i).get("quantity");
                expectedItems.get(i).put("quantity", Integer.parseInt(quantity));
            }if (items.get(i).containsKey("leQuantity")) {
                String leQuantity = (String) items.get(i).get("leQuantity");
                expectedItems.get(i).put("leQuantity", Integer.parseInt(leQuantity));
            }if (items.get(i).containsKey("mrp")) {
                String mrp = (String) items.get(i).get("mrp");
                expectedItems.get(i).put("mrp", Integer.parseInt(mrp));
            }if (items.get(i).containsKey("caselot")) {
                String caselot = (String) items.get(i).get("caselot");
                expectedItems.get(i).put("caselot", Integer.parseInt(caselot));
            }if (items.get(i).containsKey("weight")) {
                String weight = (String) items.get(i).get("weight");
                expectedItems.get(i).put("weight", Integer.parseInt(weight));
            }if (items.get(i).containsKey("volume")) {
                String volume = (String) items.get(i).get("volume");
                expectedItems.get(i).put("volume", Double.parseDouble(volume));
            }if (items.get(i).containsKey("isBlocked")) {
                String isBlocked = (String) items.get(i).get("isBlocked");
                expectedItems.get(i).put("isBlocked", Boolean.parseBoolean(isBlocked));
            }if (items.get(i).containsKey("isDeleted")) {
                String isDeleted = (String) items.get(i).get("isDeleted");
                expectedItems.get(i).put("isDeleted", Boolean.parseBoolean(isDeleted));
            }
            expectedItems.get(i).put("poDueDate", GenricUtils.getPO_DueDate());
            expectedItems.get(i).put("item", "");
        }
        requestPayload.put("items", expectedItems);
    }

    @And("And Give the Excel file to get the items {string}")
    public void andGiveTheExcelFileToGetTheItems(String excelFile) {
        ExtentReportManager.logInfoDetails("And Give the Excel file to get the items "+excelFile);
        excelUtils = new ExcelUtils(excelFile);
    }

    @And("Give the sheet name to get the items for which purchase order has to be created {string}")
    public void giveTheSheetNameToGetTheItemsForWhichPurchaseOrderHasToBeCreated(String sheetName) {
        ExtentReportManager.logInfoDetails("Give the sheet name to get the items for which purchase order has to be created");
        List<LinkedHashMap<String, Object>> items = excelUtils.getPurchaseOrderItems(sheetName);
        requestPayload.put("items",items);
    }

    @And("Give the sheet name {string} and the number of items required to create purchase order {int}")
    public void giveTheSheetNameAndTheNumberOfTasksToBeCreated(String sheetName, int requiredItems) {
        ExtentReportManager.logInfoDetails("Sheet name is "+sheetName+" and the total required items are "+requiredItems);
        List<LinkedHashMap<String, Object>> items = excelUtils.getPurchaseOrderItems(sheetName, requiredItems);
        requestPayload.put("items",items);
    }
    
    @When("Requester calls the purchase order api endpoint to create a new purchase order")
    public void requesterCallsThePurchaseOrderApiEndpointToCreateANewPurchaseOrder() {
        ExtentReportManager.logInfoDetails("Requester calls the purchase order api endpoint to create a new purchase order");
        RequestSpecification requestSpecification = RequestGenerator.getRequest(GenericSteps.userCredential, requestPayload).log().all();
        response = requestSpecification.put(PropertyReader.getProperty(ENDPOINTS_PATHS_PROPERTIES_PATH, "CREATE_PO_ENDPOINT")).then().log().all().extract().response();
        ExtentReportManager.logInfoDetails("Confirm Delivery Response Status Code is : "+response.getStatusCode());
        ExtentReportManager.logInfoDetails("Response Payload is - ");
        ExtentReportManager.logJson(response.prettyPrint());
        ExtentReportManager.logInfoDetails("Response Time is : "+response.getTimeIn(TimeUnit.MILLISECONDS)+" ms");
        CommonUtilities.setResponseInstance(response);
    }

}
