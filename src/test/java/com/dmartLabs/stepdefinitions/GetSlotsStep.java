package com.dmartLabs.stepdefinitions;


import com.dmartLabs.commonutils.ExtentReportManager;
import com.dmartLabs.commonutils.GenricUtils;
import com.dmartLabs.commonutils.RequestGenerator;
import com.dmartLabs.config.ConStants;
import com.dmartLabs.config.PropertyReader;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.dmartLabs.stepdefinitions.AuthorizationStep.Auth;
import static com.dmartLabs.stepdefinitions.AuthorizationStep.Bearertoken;
import static com.dmartLabs.stepdefinitions.CreateMultiplePurchaseOrderStep_HK.PoNumbersList;
import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.ponumbersbody;

public class GetSlotsStep {
    public static String ConfirmpoDueDate1;
    public static List<LinkedHashMap<String, Object>> ponum;
    public static String getslotsPonumber;
    public List<String> asnNumberIdList = new ArrayList<>();

    @When("whenever vendor created purchase order,Dc should give availability time")
    public void wheneverVendorCreatedPurchaseOrderDcShouldGiveAvailabilityTime() throws InterruptedException {

        int j;
        int k = 0;

        for (int i = 0; i < PoNumbersList.size(); i++) {
            System.out.println(PoNumbersList.get(i) + "=====GetSlotsConfirmApponment" + i);
            ExtentReportManager.logInfoDetails("GetSlotsConfirmApponment is - ");
            ExtentReportManager.logInfoDetails("poNumbersList " + PoNumbersList.get(i) + "=====GetSlotsConfirmApponment" + i);
        }

        System.out.println(PoNumbersList.size() + "====================>PoNumbersList.size()");

        for (j = 0; j < PoNumbersList.size(); ) {
            RequestGenerator requestGenerator = new RequestGenerator();
            Response GetSlotsResponse;
            Response CreateAppointmentResponse;
            String ConfirmpoDueDate;

            String getslotpoDueDate;
            LinkedHashMap<String, Object> GetSlotmainObject = new LinkedHashMap<>();
            List<LinkedHashMap<String, Object>> ResponseItemsList = CommonUtilities.getResponseItemsList();

            LinkedHashMap<String, String> Headers = new LinkedHashMap<>();

            String bearer = Bearertoken.get("Authorization");
            Headers.put("Authorization", bearer);
            Headers.put("requestid", GenricUtils.getRandomDeliveryNumber());


            List<LinkedHashMap<String, Object>> PoList1 = new ArrayList<>();

            LinkedHashMap<String, Object> poObject = new LinkedHashMap<>();
            List<LinkedHashMap<String, Object>> PoList = new ArrayList<>();
            ponum = ponumbersbody.get(PoNumbersList.get(j));

            List<LinkedHashMap<String, Object>> ItemsList = new ArrayList<>();
            List<String> PoItemNumberList = new ArrayList<>();
            LinkedHashMap<String, Object> ConfirmAppointmentMap = new LinkedHashMap<>();
            getslotsPonumber = PoNumbersList.get(j);

            for (int i = 0; i < ponum.size(); i++) {
                LinkedHashMap<String, Object> ItemsObject = new LinkedHashMap<>();
                String siteId = (String) ponum.get(i).get("siteId");
                String poItemNumber = (String) ponum.get(i).get("poItemNumber");
                PoItemNumberList.add(poItemNumber);
                double weight = (double) ponum.get(i).get("weight");
                double volume = (double) ponum.get(i).get("volume");
                Integer qty = (Integer) ponum.get(i).get("qty");
                weight = qty * weight / 1000;
                volume = qty * volume;
                Integer caselot = (Integer) ponum.get(i).get("caselot");
                int totalBoxes = qty / caselot;
                ItemsObject.put("poItemNumber", poItemNumber);
                ItemsObject.put("qty", qty);
                ItemsObject.put("weight", weight);
                ItemsObject.put("volume", volume);
                ItemsObject.put("boxes", totalBoxes);
                ItemsList.add(ItemsObject);
                if (ConfirmpoDueDate1 != null) {
                    ConfirmpoDueDate1 = ConfirmpoDueDate1;
                } else {
                    getslotpoDueDate = (String) ResponseItemsList.get(i).get("poDueDate");
                    ConfirmpoDueDate1 = getslotpoDueDate;
                }
            }
            poObject.put("poNumber", getslotsPonumber);
            poObject.put("items", ItemsList);
            PoList.add(poObject);
            GetSlotmainObject.put("date", ConfirmpoDueDate1);
            String id = CommonUtilities.getSiteId();
            GetSlotmainObject.put("siteId", id);
            GetSlotmainObject.put("po", PoList);
            GetSlotsResponse = RequestGenerator.getRequestWithheader(Headers, GetSlotmainObject).log().all().when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "GetSlotsEndPoint"));
            GetSlotsResponse.then().log().all();
            System.out.println(GetSlotsResponse.prettyPrint());
            CommonUtilities.setResponseInstance(GetSlotsResponse);
            Thread.sleep(1000);
        }
    }


    //===============================================================================================================================================================

//************************************************************************************************
//    ******************************************************************************************

    LinkedHashMap<String, Object> GetSlotmainObject;

    @When("slot is availble for particular time slot or not")


    public LinkedHashMap<String, Object> slotIsAvailbleForParticularTimeSlotOrNot(DataTable dataTable) {

        int j;
        int k = 0;
        List<String> calenderList = CommonUtilities.getcalenderResponseList();

        for (int i = 0; i < PoNumbersList.size(); i++) {
            System.out.println(PoNumbersList.get(i) + "=====GetSlotsConfirmApponment" + i);
            ExtentReportManager.logInfoDetails("GetSlotsConfirmApponment is - ");
            ExtentReportManager.logInfoDetails("poNumbersList " + PoNumbersList.get(i) + "=====GetSlotsConfirmApponment" + i);
        }

        System.out.println(PoNumbersList.size() + "====================>PoNumbersList.size()");

        for (j = 0; j < PoNumbersList.size(); ) {
            RequestGenerator requestGenerator = new RequestGenerator();
            Response GetSlotsResponse;
            Response CreateAppointmentResponse;
            String ConfirmpoDueDate;


            String getslotpoDueDate;

            GetSlotmainObject = new LinkedHashMap<>();
//due date taking
            List<LinkedHashMap<String, Object>> ResponseItemsList = CommonUtilities.getResponseItemsList();

            LinkedHashMap<String, String> Headers = new LinkedHashMap<>();

            //   String bearer = Bearertoken.get("Authorization");
            Headers.put("Authorization", Auth);
            Headers.put("requestid", GenricUtils.getRandomDeliveryNumber());


            List<LinkedHashMap<String, Object>> PoList1 = new ArrayList<>();

            LinkedHashMap<String, Object> poObject = new LinkedHashMap<>();
            List<LinkedHashMap<String, Object>> PoList = new ArrayList<>();
            // ponumbersbody= CommonUtilities.getPonumbersBody();
            ponum = ponumbersbody.get(PoNumbersList.get(j));

            List<LinkedHashMap<String, Object>> ItemsList = new ArrayList<>();
            List<String> PoItemNumberList = new ArrayList<>();
            LinkedHashMap<String, Object> ConfirmAppointmentMap = new LinkedHashMap<>();
            getslotsPonumber = PoNumbersList.get(j);
            Boolean isPalletizedBollean = null;
            for (int i = 0; i < ponum.size(); i++) {
                LinkedHashMap<String, Object> ItemsObject = new LinkedHashMap<>();

                String poItemNumber = null;
                double weight = 0;
                double volume = 0;
                Integer qty = 0;

                String siteId = (String) ponum.get(i).get("siteId");
//================================================================================
                poItemNumber = (String) ponum.get(i).get("poItemNumber");
                PoItemNumberList.add(poItemNumber);

                weight = (double) ponum.get(i).get("weight");

                volume = (double) ponum.get(i).get("volume");

                qty = (Integer) ponum.get(i).get("qty");

                //    qty= qty-24;
                weight = qty * weight / 1000;
                volume = qty * volume;

                Integer caselot = (Integer) ponum.get(i).get("caselot");
                int totalBoxes = qty / caselot;


                //============================================================================


                ItemsObject.put("poItemNumber", poItemNumber);
                ItemsObject.put("qty", qty);
                ItemsObject.put("weight", weight);
                ItemsObject.put("volume", volume);
                ItemsObject.put("boxes", totalBoxes);
                // "isPalletized": false
                List<Map<String, String>> palletDT = dataTable.asMaps();
                isPalletizedBollean = Boolean.valueOf(palletDT.get(j).get("isPalletized"));

                ItemsObject.put("isPalletized", isPalletizedBollean);

                ItemsList.add(ItemsObject);
                if (ConfirmpoDueDate1 != null) {
                    ConfirmpoDueDate1 = ConfirmpoDueDate1;
                } else {
                    //  getslotpoDueDate = (String) ResponseItemsList.get(i).get("poDueDate");
                    getslotpoDueDate = calenderList.get(0);

                    ConfirmpoDueDate1 = getslotpoDueDate;
                }
            }

            poObject.put("poNumber", getslotsPonumber);
            poObject.put("items", ItemsList);

            PoList.add(poObject);

            GetSlotmainObject.put("date", ConfirmpoDueDate1);
            String id = CommonUtilities.getSiteId();
            GetSlotmainObject.put("siteId", id);
            GetSlotmainObject.put("po", PoList);
            j++;

            if (j == PoNumbersList.size()) {
                break;
            }


        }
        return GetSlotmainObject;
    }



}

