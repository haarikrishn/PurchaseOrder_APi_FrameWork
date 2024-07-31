//package com.dmartLabs.stepdefinitions;
//
//
//import com.dmartLabs.commonutils.ExtentReportManager;
//import com.dmartLabs.commonutils.GenricUtils;
//import com.dmartLabs.commonutils.RequestGenerator;
//import com.dmartLabs.config.ConStants;
//import com.dmartLabs.config.PropertyReader;
//import com.nimbusds.jose.AlgorithmFamily;
//import io.cucumber.datatable.DataTable;
//import io.cucumber.java.en.And;
//import io.cucumber.java.en.When;
//import io.restassured.response.Response;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//
//import static com.dmartLabs.stepdefinitions.AuthorizationStep.Bearertoken;
//
////import static com.dmartLabs.stepdefinitions.CreateMultiplePurchaseOrderStep_HK.PoNumbersList;
//
//public class OptimiseSlotsCalenderStep {
//
//    private List<String> PoNumbersList;
//    private String getslotsPonumber;
//    private LinkedHashMap<String, Object> GetSlotmainObject;
//    private List<LinkedHashMap<String, Object>> ItemsList;
//    private List<LinkedHashMap<String, Object>> ponum;
//    private String ConfirmpoDueDate1;
//
//    @When("whenever vendor created purchase order,Dc should give availability time,and confirm appointment")
//    public void wheneverVendorCreatedPurchaseOrderDcShouldGiveAvailabilityTimeAndConfirmAppointment(DataTable dataTable) throws InterruptedException {
//        initializePoNumbersList();
//
//        for (int j = 0; j < PoNumbersList.size(); j++) {
//            processPoNumber(j);
//        }
//    }
//
//    private void initializePoNumbersList() {
//        // Replace this logic with your actual initialization of PoNumbersList
//        PoNumbersList = new ArrayList<>();
//        PoNumbersList.add("1109");
//        PoNumbersList.add("1110");
//        // Add more entries as needed
//    }
//
//    private void processPoNumber(int j) throws InterruptedException {
//        RequestGenerator requestGenerator = new RequestGenerator();
//        Response GetSlotsResponse;
//        String bearer = Bearertoken.get("Authorization");
//        LinkedHashMap<String, Object> Headers = new LinkedHashMap<>();
//        Headers.put("Authorization", bearer);
//        Headers.put("requestid", GenricUtils.getRandomDeliveryNumber());
//
//        prepareItemsList(j);
//
//        preparePoObject(j);
//
//        prepareGetSlotmainObject();
//
//        GetSlotsResponse = RequestGenerator.getRequestWithheader(Headers, GetSlotmainObject)
//                .log().all().when()
//                .post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "GetSlotsEndPoint"));
//
//        GetSlotsResponse.then().log().all();
//        System.out.println(GetSlotsResponse.prettyPrint());
//
//        CommonUtilities.setResponseInstance(GetSlotsResponse);
//        Thread.sleep(1000);
//    }
//
//    private void prepareItemsList(int j) {
//        ItemsList = new ArrayList<>();
//        ponum = CommonUtilities.getResponseItemsList();
//
//        for (int i = 0; i < ponum.size(); i++) {
//            LinkedHashMap<String, Object> ItemsObject = new LinkedHashMap<>();
//            String siteId = (String) ponum.get(i).get("siteId");
//            String poItemNumber = (String) ponum.get(i).get("poItemNumber");
//            AlgorithmFamily<T> PoItemNumberList;
//            PoItemNumberList.add(poItemNumber);
//            double weight = (double) ponum.get(i).get("weight");
//            double volume = (double) ponum.get(i).get("volume");
//            Integer qty = (Integer) ponum.get(i).get("qty");
//            weight = qty * weight / 1000;
//            volume = qty * volume;
//            Integer caselot = (Integer) ponum.get(i).get("caselot");
//            int totalBoxes = qty / caselot;
//
//            ItemsObject.put("poItemNumber", poItemNumber);
//            ItemsObject.put("qty", qty);
//            ItemsObject.put("weight", weight);
//            ItemsObject.put("volume", volume);
//            ItemsObject.put("boxes", totalBoxes);
//
//            ItemsList.add(ItemsObject);
//
//            if (ConfirmpoDueDate1 != null) {
//                ConfirmpoDueDate1 = ConfirmpoDueDate1;
//            } else {
//                String getslotpoDueDate = (String) ponum.get(i).get("poDueDate");
//                ConfirmpoDueDate1 = getslotpoDueDate;
//            }
//        }
//    }
//
//    private void preparePoObject(int j) {
//        LinkedHashMap<Object, Object> poObject = new LinkedHashMap<>();
//        poObject.put("poNumber", PoNumbersList.get(j));
//        poObject.put("items", ItemsList);
//        PoList.add(poObject);
//    }
//
//    private void prepareGetSlotmainObject() {
//        GetSlotmainObject = new LinkedHashMap<>();
//        GetSlotmainObject.put("date", ConfirmpoDueDate1);
//        String id = CommonUtilities.getSiteId();
//        GetSlotmainObject.put("siteId", id);
//        GetSlotmainObject.put("po", PoList);
//    }
//
//    // Add more methods as needed
//
//}
