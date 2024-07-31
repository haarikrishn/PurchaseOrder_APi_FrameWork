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
import org.testng.Assert;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;


import static com.dmartLabs.stepdefinitions.AuthorizationStep.Auth;
import static com.dmartLabs.stepdefinitions.AuthorizationStep.Bearertoken;
import static com.dmartLabs.stepdefinitions.CreateGrnStep.grnList;
import static com.dmartLabs.stepdefinitions.CreateMultiplePurchaseOrderStep_HK.PoNumbersList;
import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.GetpoItemNumberList;
import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.ponumbersbody;
//import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.Bearertoken;
//import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.ponumbersbody;


public class GetSlotsConfirmApponmentStep {

    public static String ConfirmpoDueDate1;
    public static List<LinkedHashMap<String, Object>> ponum;
    public static String getslotsPonumber;
    public List<String> asnNumberIdList = new ArrayList<>();
    //    private  static LinkedHashMap<String,List<LinkedHashMap<String,Object>>>ponumbersbody;
    private static LinkedHashMap<String, List<LinkedHashMap<String, Object>>> ponumbersbody = CommonUtilities.getPonumbersBody();

    @When("whenever vendor created purchase order,Dc should give availability time,and confirm appointment")
    public void wheneverVendorCreatedPurchaseOrderDcShouldGiveAvailabilityTimeAndConfirmAppointment(DataTable dataTable) throws InterruptedException {

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
//due date taking
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
            //==============================================================
//            for(int b=0;b<ponumbersbody.size();b++)
//            {
//                ponumbersbody.get(b).g;
//            }


            //==========================================================
            for (int i = 0; i < ponum.size(); i++) {
                LinkedHashMap<String, Object> ItemsObject = new LinkedHashMap<>();


                String siteId = (String) ponum.get(i).get("siteId");

                String poItemNumber = (String) ponum.get(i).get("poItemNumber");
                PoItemNumberList.add(poItemNumber);
//=======================================================================
                double weight = (double) ponum.get(i).get("weight");

                double volume = (double) ponum.get(i).get("volume");

                Integer qty = (Integer) ponum.get(i).get("qty");

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
            //GetSlotsResponse.getBody().as(HashMap.class)

            CommonUtilities.setResponseInstance(GetSlotsResponse);
            Thread.sleep(1000);
            //==============================================================================================

            //confirmappontment


            int iterations = 0;
            LocalTime currentSlotStart = null;
            //    int unloadingRate;
            Response ConfirmGetslotResponse = CommonUtilities.getResponseInstance();
            LinkedHashMap<String, Object> GetslotResponse1 = ConfirmGetslotResponse.as(new TypeRef<LinkedHashMap<String, Object>>() {
            });

            String workingHoursstartTime = ConfirmGetslotResponse.jsonPath().get("workingHours.startTime");//6.00
            String workingHoursendTime = ConfirmGetslotResponse.jsonPath().get("workingHours.endTime");//18.00
//            try {

            int unloadingRate = ConfirmGetslotResponse.jsonPath().get("unloadingRate");
//            } catch (Exception ne) {
//                unloadingRate = 30;
//            }
            List<Map<String, Object>> unAvailableSlotList = ConfirmGetslotResponse.jsonPath().get("unAvailableSlot");


            LinkedHashMap<String, Object> VehicleMap = new LinkedHashMap<>();
            LinkedHashMap<String, Object> driverDetailsMap = new LinkedHashMap<>();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");


            LocalTime startTime = LocalTime.parse(workingHoursstartTime);
            LocalTime endTime = LocalTime.parse(workingHoursendTime);


            List<LinkedHashMap<String, String>> unAvailableSlotstartAndEndTimeMApList = new ArrayList<>();


            List<Map<String, String>> halfHourSlots = new ArrayList<>();
            try {

                for (int C = 0; C < unAvailableSlotList.size(); C++) {
                    LinkedHashMap<String, String> unAvailableSlotstartAndEndTimeMAp = new LinkedHashMap<>();
                    String unAvailableSlotstartTime = (String) unAvailableSlotList.get(C).get("startTime");
                    String unAvailableSlotendTime = (String) unAvailableSlotList.get(C).get("endTime");

                    unAvailableSlotstartAndEndTimeMAp.put("startTime", unAvailableSlotstartTime);
                    unAvailableSlotstartAndEndTimeMAp.put("endTime", unAvailableSlotendTime);

                    unAvailableSlotstartAndEndTimeMApList.add(unAvailableSlotstartAndEndTimeMAp);

                }


                //========================================================================================


                List<LinkedHashMap<String, String>> CompareendTimec4list = null;
                LocalTime endTimec5Local = null;

                for (int c4 = 0; c4 < unAvailableSlotList.size(); c4++) {
                    LinkedHashMap<String, String> CompareendTimec4Map = new LinkedHashMap<>();
                    CompareendTimec4list = new ArrayList<>();
                    String ComparestartTimec4 = (String) unAvailableSlotList.get(c4).get("startTime");
                    String CompareendTimec4 = (String) unAvailableSlotList.get(c4).get("endTime");
                    CompareendTimec4Map.put("endTime", CompareendTimec4);
                    CompareendTimec4list.add(CompareendTimec4Map);
                }
                for (int c5 = 0; c5 < CompareendTimec4list.size(); c5++) {
                    String endTimec5 = CompareendTimec4list.get(c5).get("endTime");
                    endTimec5Local = LocalTime.parse(endTimec5);
                    currentSlotStart = endTimec5Local;
                }
                if (unloadingRate <= 30) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
                    System.out.println(iterations + "=========================>endTimec5Local");
                }
                if (unloadingRate > 31 && unloadingRate <= 60) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 60);
                    System.out.println(iterations + "=========================>endTimec5Local");
                }
//                if (unloadingRate > 1 && unloadingRate < 5000) {
//                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 60);
//                    System.out.println(iterations + "=========================>endTimec5Local");
//                }


                for (int c1 = 0; c1 <= iterations - 2; c1++) {
                    if (unloadingRate <= 30) {
                        LocalTime currentSlotEnd = currentSlotStart.plusMinutes(30);

                        Map<String, String> slotMap = new HashMap<>();
                        slotMap.put("startTime", currentSlotStart.format(formatter));
                        slotMap.put("endTime", currentSlotEnd.format(formatter));

                        halfHourSlots.add(slotMap);
                        currentSlotStart = currentSlotStart.plusMinutes(30);

                    }
                    if (unloadingRate > 30 && unloadingRate < 1000) {
                        LocalTime currentSlotEnd = currentSlotStart.plusMinutes(60);

                        Map<String, String> slotMap = new HashMap<>();
                        slotMap.put("startTime", currentSlotStart.format(formatter));
                        slotMap.put("endTime", currentSlotEnd.format(formatter));

                        halfHourSlots.add(slotMap);
                        currentSlotStart = currentSlotStart.plusMinutes(60);


                    }


                }
                try {

                    if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) && halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {

                        System.out.println(halfHourSlots.get(k).get("startTime") + "=halfHourSlots.get(k).get(\"startTime\")" + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + "halfHourSlots.get(halfHourSlots.size() - 1)");

                        if (unloadingRate <= 30) {
                            iterations = 0;

                            iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
                            String poDueDateIteration = ConfirmpoDueDate1;
                            LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
                            LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
                            String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
                            DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
                            String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
                            ConfirmpoDueDate1 = poDueDateIterationString24;
                            currentSlotStart = startTime;
                            k = 0;


                        }
                        if (unloadingRate > 30 && unloadingRate < 1000) {
                            iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 60);
                            String poDueDateIteration = ConfirmpoDueDate1;
                            LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
                            LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
                            String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
                            DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
                            String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
                            ConfirmpoDueDate1 = poDueDateIterationString24;
                            currentSlotStart = startTime;
                            k = 0;


                        }


                    }
                } catch (IndexOutOfBoundsException ie123) {
                    System.out.println("ie123 inside catch");
                    if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size()).get("startTime")) && halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size()).get("endTime"))) {

                        System.out.println(halfHourSlots.get(k).get("startTime") + "=halfHourSlots.get(k).get(\"startTime\")" + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + "halfHourSlots.get(halfHourSlots.size() - 1)");

                        if (unloadingRate <= 30) {
                            iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
                            String poDueDateIteration = ConfirmpoDueDate1;
                            LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
                            LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
                            String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
                            DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
                            String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
                            ConfirmpoDueDate1 = poDueDateIterationString24;
                            currentSlotStart = startTime;
                            k = 0;
                        }
                        if (unloadingRate > 30 && unloadingRate < 1000) {
                            iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 60);
                            String poDueDateIteration = ConfirmpoDueDate1;
                            LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
                            LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
                            String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
                            DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
                            String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
                            ConfirmpoDueDate1 = poDueDateIterationString24;
                            currentSlotStart = startTime;
                            k = 0;


                        }

                    }

                }
                for (int c2 = 0; c2 < unAvailableSlotstartAndEndTimeMApList.size(); c2++) {
                    String unAvailablestartTime = unAvailableSlotstartAndEndTimeMApList.get(c2).get("startTime");
                    String unAvailableendTime = unAvailableSlotstartAndEndTimeMApList.get(c2).get("endTime");

                    LocalTime unAvailableStartTimeLocal = LocalTime.parse(unAvailablestartTime);
                    LocalTime unAvailableendTimeLocal = LocalTime.parse(unAvailableendTime);

                    for (int c3 = 0; c3 < halfHourSlots.size() - 1; c3++) {

                        String halfHourSlotsstartTime = halfHourSlots.get(c3).get("startTime");
                        String halfHourSlotsendTime = halfHourSlots.get(c3).get("endTime");

                        LocalTime halfHourSlotsstartTimeLocal = LocalTime.parse(halfHourSlotsstartTime);
                        LocalTime halfHourSlotsendTimeLocal = LocalTime.parse(halfHourSlotsendTime);

                        if (halfHourSlotsstartTime.equals(unAvailablestartTime)) {
                            System.out.println("================================================>");
                            System.out.println("inside the if()");
                            halfHourSlots.remove(halfHourSlots.get(c3));
                            System.out.println("inside remove");

                            break;
                        }

                    }
                }
                //   halfHourSlots.remove(halfHourSlots.remove(halfHourSlots.size() - 1));
                System.out.println("available times ===========================================> ");
                System.out.println(halfHourSlots);


            } catch (NullPointerException e) {

                if (unloadingRate <= 30) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                }

                if (unloadingRate > 31 && unloadingRate < 5000) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 60);
                    System.out.println(iterations + "=========================>startTime");

                    currentSlotStart = startTime;
                }
                for (int c1 = 0; c1 <= iterations - 2; c1++) {
                    if (unloadingRate <= 30) {
                        LocalTime currentSlotEnd = currentSlotStart.plusMinutes(30);

                        Map<String, String> slotMap = new HashMap<>();
                        slotMap.put("startTime", currentSlotStart.format(formatter));
                        slotMap.put("endTime", currentSlotEnd.format(formatter));

                        halfHourSlots.add(slotMap);
                        currentSlotStart = currentSlotStart.plusMinutes(30);

                    }
                    if (unloadingRate > 30 && unloadingRate < 1000) {
                        LocalTime currentSlotEnd = currentSlotStart.plusMinutes(60);

                        Map<String, String> slotMap = new HashMap<>();
                        slotMap.put("startTime", currentSlotStart.format(formatter));
                        slotMap.put("endTime", currentSlotEnd.format(formatter));

                        halfHourSlots.add(slotMap);
                        currentSlotStart = currentSlotStart.plusMinutes(60);


                    }
                }
                //  halfHourSlots.remove(halfHourSlots.remove(halfHourSlots.size() - 1));

                //   System.out.println(halfHourSlots.get(j).get("startTime") + "====" + halfHourSlots.get(halfHourSlots.size() - 2).get("startTime"));
                try {


                    if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) && halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {

                        System.out.println(halfHourSlots.get(k).get("startTime") + "=halfHourSlots.get(k).get(\"startTime\")" + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + "halfHourSlots.get(halfHourSlots.size() - 1)");

                        if (unloadingRate <= 30) {

                            iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
                            String poDueDateIteration = ConfirmpoDueDate1;
                            LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
                            LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
                            String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
                            DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
                            String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
                            ConfirmpoDueDate1 = poDueDateIterationString24;
                            currentSlotStart = startTime;
                            k = 0;
                        }


                        if (unloadingRate > 30 && unloadingRate < 1000) {
                            iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 60);
                            String poDueDateIteration = ConfirmpoDueDate1;
                            LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
                            LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
                            String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
                            DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
                            String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
                            ConfirmpoDueDate1 = poDueDateIterationString24;
                            currentSlotStart = startTime;
                            k = 0;


                        }
                    }
                } catch (IndexOutOfBoundsException ie12) {

                    ie12.printStackTrace();
                    System.out.println("IndexOutOfBoundsException occurred: " + ie12.getMessage());
                }
            }


            List<Map<String, String>> vehicleData = dataTable.asMaps();


            String truckType = vehicleData.get(j).get("truckType");
            String truckNumber = vehicleData.get(j).get("truckNumber");
            String driverName = vehicleData.get(j).get("driverName");
            String driverNumber = vehicleData.get(j).get("driverNumber");
            String dstSiteId = vehicleData.get(j).get("dstSiteId");

            VehicleMap.put("truckType", truckType);
            VehicleMap.put("truckNumber", truckNumber);

            driverDetailsMap.put("driverName", driverName);
            driverDetailsMap.put("driverNumber", driverNumber);

            VehicleMap.put("driverDetails", driverDetailsMap);


            ConfirmAppointmentMap.put("vehicle", VehicleMap);

            ConfirmAppointmentMap.put("isPalletized", false);


            ConfirmAppointmentMap.put("appnDate", ConfirmpoDueDate1);
//            }


//            ConfirmAppointmentMap.put("appnStartTime", halfHourSlots.get(k).get("startTime"));
//            ConfirmAppointmentMap.put("appnEndTime", halfHourSlots.get(k).get("endTime"));

            try {

                ConfirmAppointmentMap.put("appnStartTime", halfHourSlots.get(k).get("startTime"));
                ConfirmAppointmentMap.put("appnEndTime", halfHourSlots.get(k).get("endTime"));


                if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) && halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {
                    System.out.println(halfHourSlots.get(k).get("startTime") + "=halfHourSlots.get(k).get(\"startTime\")" + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + "halfHourSlots.get(halfHourSlots.size() - 1)");

                    System.out.println("clear");
                    halfHourSlots.clear();

                    halfHourSlots = new ArrayList<>();


                }
            } catch (IndexOutOfBoundsException ie14) {
                ie14.printStackTrace();
                System.out.println("IndexOutOfBoundsException occurred: " + ie14.getMessage());

            }

            ConfirmAppointmentMap.put("dstSiteId", dstSiteId);

            List<LinkedHashMap<String, Object>> ItemsList1 = new ArrayList<>();


            for (int c5 = 0; c5 < ponum.size(); c5++) {
                LinkedHashMap<String, Object> ItemsObjct1 = new LinkedHashMap<>();

                String poItemNumber = (String) ponum.get(c5).get("poItemNumber");
                int qty = (int) ponum.get(c5).get("qty");
                //    qty= qty-24;
                int caselot = (int) ponum.get(c5).get("caselot");
                int boxes = qty / caselot;

                double weight = (double) ponum.get(c5).get("weight");
                double volume = (double) ponum.get(c5).get("volume");


                weight = qty * weight / 1000;
                volume = qty * volume;

                ItemsObjct1.put("poItemNumber", poItemNumber);
                ItemsObjct1.put("qty", qty);
                ItemsObjct1.put("weight", weight);
                ItemsObjct1.put("volume", volume);
                ItemsObjct1.put("boxes", boxes);
                ItemsList1.add(ItemsObjct1);

            }
            LinkedHashMap<String, Object> Poobject1 = new LinkedHashMap<>();
            Poobject1.put("poNumber", getslotsPonumber);
            Poobject1.put("items", ItemsList1);
            PoList1.add(Poobject1);

            ConfirmAppointmentMap.put("po", PoList1);

            CreateAppointmentResponse = RequestGenerator.getRequestWithheader(Headers, ConfirmAppointmentMap).log().all().when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CreateAppointment"));
            CreateAppointmentResponse.then().log().all();

            String asnNumberResponse = CreateAppointmentResponse.jsonPath().get("asnNumber").toString();
            String appointmentId = CreateAppointmentResponse.jsonPath().get("appointmentId").toString();
            asnNumberIdList.add(asnNumberResponse + "==>asnNumber " + appointmentId + "===>appointmentId " + j);


            System.out.println(CreateAppointmentResponse.prettyPrint());
            ExtentReportManager.logInfoDetails("Response is - ");
            ExtentReportManager.logJson(CreateAppointmentResponse.prettyPrint());
            ExtentReportManager.logInfoDetails("Response status code is " + CreateAppointmentResponse.getStatusCode());
            long responseTime = CreateAppointmentResponse.getTimeIn(TimeUnit.MILLISECONDS);
            ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");

            //  Thread.sleep(500);

            j++;
            k++;
//call the GRN method to create GRN for particular appontment
            CreateGrnStep createGrnStep = new CreateGrnStep();
            createGrnStep.afterAppontmentScheduledCreateGrnForParticularPoNumberForNumberOfIterations();


            if (j == PoNumbersList.size()) {
                break;
            }


            Thread.sleep(1000);
        }

        for (int q = 0; q < asnNumberIdList.size(); q++) {
            System.out.println(asnNumberIdList.get(q) + "============>asnNumber + ==> appointmentId" + q);
            ExtentReportManager.logInfoDetails("asnNumberIdList is " + asnNumberIdList.get(q) + "============>asnNumber + ==> appointmentId" + q);
            System.out.println(grnList.get(q) + "=================>grn number" + q);
            ExtentReportManager.logInfoDetails("grnNumberIdList is " + grnList.get(q) + "============>grnnumber" + q);

        }


        System.out.println(asnNumberIdList.size() + "==========>asnNumberIdList.size");
        ExtentReportManager.logInfoDetails(asnNumberIdList.size() + "==========>asnNumberIdList.size");

        System.out.println(grnList.size() + "==========>grnList.size");
        ExtentReportManager.logInfoDetails(grnList.size() + "==========>grnList.size");

    }


    //===============================================================================================================================
//    Case-||
    //***********************nested if condition*************************************************
    @And("whenever vendor created purchase order,Dc should give availability time,and confirm appointment and create a GRN")
    public void wheneverVendorCreatedPurchaseOrderDcShouldGiveAvailabilityTimeAndConfirmAppointmentAndCreateAGRN(DataTable dataTable) throws InterruptedException {
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
//due date taking
            List<LinkedHashMap<String, Object>> ResponseItemsList = CommonUtilities.getResponseItemsList();

            LinkedHashMap<String, String> Headers = new LinkedHashMap<>();

            //   String bearer = Bearertoken.get("Authorization");
            Headers.put("Authorization", Auth);
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
//=======================================================================
                double weight = (double) ponum.get(i).get("weight");

                double volume = (double) ponum.get(i).get("volume");

                Integer qty = (Integer) ponum.get(i).get("qty");

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
            //    System.out.println(GetSlotsResponse.prettyPrint());
            //GetSlotsResponse.getBody().as(HashMap.class)

            CommonUtilities.setResponseInstance(GetSlotsResponse);
            Thread.sleep(1000);
            //==============================================================================================

            //confirmappontment

            int iterations = 0;
            LocalTime currentSlotStart = null;
            Integer unloadingRate = 0;
            Response ConfirmGetslotResponse = CommonUtilities.getResponseInstance();
            LinkedHashMap<String, Object> GetslotResponse1 = ConfirmGetslotResponse.as(new TypeRef<LinkedHashMap<String, Object>>() {
            });

            String workingHoursstartTime = ConfirmGetslotResponse.jsonPath().get("workingHours.startTime");//6.00
            String workingHoursendTime = ConfirmGetslotResponse.jsonPath().get("workingHours.endTime");//18.00
            if (unloadingRate != null) {
                unloadingRate = ConfirmGetslotResponse.jsonPath().get("unloadingRate");
            } else {
                System.out.println("***********po number not found*************************");
            }

            List<Map<String, Object>> unAvailableSlotList = ConfirmGetslotResponse.jsonPath().get("unAvailableSlot");
            LinkedHashMap<String, Object> VehicleMap = new LinkedHashMap<>();
            LinkedHashMap<String, Object> driverDetailsMap = new LinkedHashMap<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime startTime = LocalTime.parse(workingHoursstartTime);
            LocalTime endTime = LocalTime.parse(workingHoursendTime);
            List<LinkedHashMap<String, String>> unAvailableSlotstartAndEndTimeMApList = new ArrayList<>();
            List<Map<String, String>> halfHourSlots = new ArrayList<>();
            try {
                for (int C = 0; C < unAvailableSlotList.size(); C++) {
                    LinkedHashMap<String, String> unAvailableSlotstartAndEndTimeMAp = new LinkedHashMap<>();
                    String unAvailableSlotstartTime = (String) unAvailableSlotList.get(C).get("startTime");
                    String unAvailableSlotendTime = (String) unAvailableSlotList.get(C).get("endTime");
                    unAvailableSlotstartAndEndTimeMAp.put("startTime", unAvailableSlotstartTime);
                    unAvailableSlotstartAndEndTimeMAp.put("endTime", unAvailableSlotendTime);
                    unAvailableSlotstartAndEndTimeMApList.add(unAvailableSlotstartAndEndTimeMAp);
                }
                List<LinkedHashMap<String, String>> CompareendTimec4list = null;
                LocalTime endTimec5Local = null;

                for (int c4 = 0; c4 < unAvailableSlotList.size(); c4++) {
                    LinkedHashMap<String, String> CompareendTimec4Map = new LinkedHashMap<>();
                    CompareendTimec4list = new ArrayList<>();
                    String ComparestartTimec4 = (String) unAvailableSlotList.get(c4).get("startTime");
                    String CompareendTimec4 = (String) unAvailableSlotList.get(c4).get("endTime");
                    CompareendTimec4Map.put("endTime", CompareendTimec4);
                    CompareendTimec4list.add(CompareendTimec4Map);
                }
                for (int c5 = 0; c5 < CompareendTimec4list.size(); c5++) {
                    String endTimec5 = CompareendTimec4list.get(c5).get("endTime");
                    endTimec5Local = LocalTime.parse(endTimec5);
                    currentSlotStart = endTimec5Local;
                }
                //=============================================================================================================================
                if (unloadingRate <= 30) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
                    System.out.println(iterations + "=========================>endTimec30Local");
                } else if (unloadingRate > 30 && unloadingRate <= 60) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 60);
                    System.out.println(iterations + "=========================>endTimec60Local");
                } else if (unloadingRate > 60 && unloadingRate <= 90) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 90);
                    System.out.println(iterations + "=========================>endTimec90Local");
                } else if (unloadingRate > 90 && unloadingRate <= 120) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 120);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else if (unloadingRate > 120 && unloadingRate <= 150) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 150);
                    System.out.println(iterations + "=========================>endTimec150Local");
                } else if (unloadingRate > 150 && unloadingRate <= 180) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 180);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else if (unloadingRate > 180 && unloadingRate <= 210) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 210);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else if (unloadingRate > 210 && unloadingRate <= 240) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 240);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else {
                    System.out.println("************Time slot not avaialble**********************************");
                }
                Map<Integer, Integer> rateDurationMap = new HashMap<>();
                rateDurationMap.put(0, 30);     // unloadingRate <= 30
                rateDurationMap.put(31, 60);    // 31 < unloadingRate <= 60
                rateDurationMap.put(61, 90);    // 61 < unloadingRate <= 90
                rateDurationMap.put(91, 120);   // 91 < unloadingRate <= 120
                rateDurationMap.put(121, 150);  // 121 < unloadingRate <= 150
                rateDurationMap.put(151, 180);  // 151 < unloadingRate <= 180
                rateDurationMap.put(181, 210);
                rateDurationMap.put(211, 240);


                for (int c1 = 0; c1 <= iterations - 2; c1++) {
                    int duration = 0;
                    for (Map.Entry<Integer, Integer> entry : rateDurationMap.entrySet()) {
                        if (unloadingRate == entry.getValue()) {
                            duration = entry.getValue();
                            break;
                        }
                    }
                    LocalTime currentSlotEnd = currentSlotStart.plusMinutes(duration);
                    Map<String, String> slotMap = new HashMap<>();
                    slotMap.put("startTime", currentSlotStart.format(formatter));
                    slotMap.put("endTime", currentSlotEnd.format(formatter));
                    halfHourSlots.add(slotMap);
                    currentSlotStart = currentSlotEnd;
                }
                //=============================================================================================================
                //k >= 0 && halfHourSlots.size() > 0 &&
                if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) &&
                        halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {
                    System.out.println(halfHourSlots.get(k).get("startTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + " and " +
                            halfHourSlots.get(k).get("endTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"));
                    // Update ConfirmpoDueDate1
                    String poDueDateIteration = ConfirmpoDueDate1;
                    LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
                    LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
                    String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
                    DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
                    String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
                    ConfirmpoDueDate1 = poDueDateIterationString24;
                    // Reset currentSlotStart and k
                    currentSlotStart = startTime;
                    k = 0;
                }
                for (int c2 = 0; c2 < unAvailableSlotstartAndEndTimeMApList.size(); c2++) {
                    String unAvailablestartTime = unAvailableSlotstartAndEndTimeMApList.get(c2).get("startTime");
                    String unAvailableendTime = unAvailableSlotstartAndEndTimeMApList.get(c2).get("endTime");
                    LocalTime unAvailableStartTimeLocal = LocalTime.parse(unAvailablestartTime);
                    LocalTime unAvailableendTimeLocal = LocalTime.parse(unAvailableendTime);
                    for (int c3 = 0; c3 < halfHourSlots.size() - 1; c3++) {
                        String halfHourSlotsstartTime = halfHourSlots.get(c3).get("startTime");
                        String halfHourSlotsendTime = halfHourSlots.get(c3).get("endTime");
                        LocalTime halfHourSlotsstartTimeLocal = LocalTime.parse(halfHourSlotsstartTime);
                        LocalTime halfHourSlotsendTimeLocal = LocalTime.parse(halfHourSlotsendTime);
                        if (halfHourSlotsstartTime.equals(unAvailablestartTime)) {
                            System.out.println("================================================>");
                            System.out.println("inside the if()");
                            halfHourSlots.remove(halfHourSlots.get(c3));
                            System.out.println("inside remove");
                            break;
                        }
                    }
                }
                System.out.println("available times ===========================================> ");
                System.out.println(halfHourSlots);
            } catch (NullPointerException e) {
                if (unloadingRate <= 30) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else if (unloadingRate > 30 && unloadingRate <= 60) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 60);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else if (unloadingRate > 60 && unloadingRate <= 90) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 90);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else if (unloadingRate > 90 && unloadingRate <= 120) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 120);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else if (unloadingRate > 120 && unloadingRate <= 150) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 150);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else if (unloadingRate > 150 && unloadingRate <= 180) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 180);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else if (unloadingRate > 180 && unloadingRate <= 210) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 210);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else if (unloadingRate > 210 && unloadingRate <= 240) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 240);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else {
                    System.out.println("************Time slot not avaialble**********************************");
                }

                Map<Integer, Integer> rateDurationMap = new HashMap<>();
                rateDurationMap.put(0, 30);     // unloadingRate <= 30
                rateDurationMap.put(31, 60);    // 31 < unloadingRate <= 60
                rateDurationMap.put(61, 90);    // 61 < unloadingRate <= 90
                rateDurationMap.put(91, 120);   // 91 < unloadingRate <= 120
                rateDurationMap.put(121, 150);  // 121 < unloadingRate <= 150
                rateDurationMap.put(151, 180);  // 151 < unloadingRate <= 180
                rateDurationMap.put(181, 210);
                rateDurationMap.put(211, 240);

                for (int c1 = 0; c1 <= iterations - 2; c1++) {
                    int duration = 0;
                    for (Map.Entry<Integer, Integer> entry : rateDurationMap.entrySet()) {
                        if (unloadingRate == entry.getValue()) {
                            duration = entry.getValue();
                            break;
                        }
                    }
                    LocalTime currentSlotEnd = currentSlotStart.plusMinutes(duration);
                    Map<String, String> slotMap = new HashMap<>();
                    slotMap.put("startTime", currentSlotStart.format(formatter));
                    slotMap.put("endTime", currentSlotEnd.format(formatter));
                    halfHourSlots.add(slotMap);
                    currentSlotStart = currentSlotEnd;
                }
                try {
                    if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) &&
                            halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {
                        System.out.println(halfHourSlots.get(k).get("startTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + " and " +
                                halfHourSlots.get(k).get("endTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"));
                        String poDueDateIteration = ConfirmpoDueDate1;
                        LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
                        LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
                        String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
                        DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
                        String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
                        ConfirmpoDueDate1 = poDueDateIterationString24;
                        // Reset currentSlotStart and k
                        currentSlotStart = startTime;
                        k = 0;
                    }
                } catch (IndexOutOfBoundsException ie12) {
                    ie12.printStackTrace();
                    System.out.println("IndexOutOfBoundsException occurred: " + ie12.getMessage());
                }
            }
            List<Map<String, String>> vehicleData = dataTable.asMaps();
            String truckType = vehicleData.get(j).get("truckType");
            String truckNumber = vehicleData.get(j).get("truckNumber");
            String driverName = vehicleData.get(j).get("driverName");
            String driverNumber = vehicleData.get(j).get("driverNumber");
            String dstSiteId = vehicleData.get(j).get("dstSiteId");
            VehicleMap.put("truckType", truckType);
            VehicleMap.put("truckNumber", truckNumber);
            driverDetailsMap.put("driverName", driverName);
            driverDetailsMap.put("driverNumber", driverNumber);
            VehicleMap.put("driverDetails", driverDetailsMap);
            ConfirmAppointmentMap.put("vehicle", VehicleMap);
            ConfirmAppointmentMap.put("isPalletized", false);
            ConfirmAppointmentMap.put("appnDate", ConfirmpoDueDate1);
            try {
                ConfirmAppointmentMap.put("appnStartTime", halfHourSlots.get(k).get("startTime"));
                ConfirmAppointmentMap.put("appnEndTime", halfHourSlots.get(k).get("endTime"));
                if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) && halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {
                    System.out.println(halfHourSlots.get(k).get("startTime") + "=halfHourSlots.get(k).get(\"startTime\")" + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + "halfHourSlots.get(halfHourSlots.size() - 1)");
                    System.out.println("clear");
                    halfHourSlots.clear();
                    halfHourSlots = new ArrayList<>();
                }
            } catch (IndexOutOfBoundsException ie14) {
                ie14.printStackTrace();
                System.out.println("IndexOutOfBoundsException occurred: " + ie14.getMessage());
            }
            ConfirmAppointmentMap.put("dstSiteId", dstSiteId);
            List<LinkedHashMap<String, Object>> ItemsList1 = new ArrayList<>();
            for (int c5 = 0; c5 < ponum.size(); c5++) {
                LinkedHashMap<String, Object> ItemsObjct1 = new LinkedHashMap<>();
                String poItemNumber = (String) ponum.get(c5).get("poItemNumber");
                int qty = (int) ponum.get(c5).get("qty");
                //    qty= qty-24;
                int caselot = (int) ponum.get(c5).get("caselot");
                int boxes = qty / caselot;

                double weight = (double) ponum.get(c5).get("weight");
                double volume = (double) ponum.get(c5).get("volume");
                weight = qty * weight / 1000;
                volume = qty * volume;

                ItemsObjct1.put("poItemNumber", poItemNumber);
                ItemsObjct1.put("qty", qty);
                ItemsObjct1.put("weight", weight);
                ItemsObjct1.put("volume", volume);
                ItemsObjct1.put("boxes", boxes);
                ItemsList1.add(ItemsObjct1);

            }
            LinkedHashMap<String, Object> Poobject1 = new LinkedHashMap<>();
            Poobject1.put("poNumber", getslotsPonumber);
            Poobject1.put("items", ItemsList1);
            PoList1.add(Poobject1);

            ConfirmAppointmentMap.put("po", PoList1);

            CreateAppointmentResponse = RequestGenerator.getRequestWithheader(Headers, ConfirmAppointmentMap).log().all().when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CreateAppointment"));
            CreateAppointmentResponse.then().log().all();

            String asnNumberResponse = CreateAppointmentResponse.jsonPath().get("asnNumber").toString();
            String appointmentId = CreateAppointmentResponse.jsonPath().get("appointmentId").toString();
            asnNumberIdList.add(asnNumberResponse + "==>asnNumber " + appointmentId + "===>appointmentId " + j);

            System.out.println(CreateAppointmentResponse.prettyPrint());
            ExtentReportManager.logInfoDetails("Response is - ");
            ExtentReportManager.logJson(CreateAppointmentResponse.prettyPrint());
            ExtentReportManager.logInfoDetails("Response status code is " + CreateAppointmentResponse.getStatusCode());
            long responseTime = CreateAppointmentResponse.getTimeIn(TimeUnit.MILLISECONDS);
            ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");

            //  Thread.sleep(500);

            j++;
            k++;
//call the GRN method to create GRN for particular appontment
            CreateGrnStep createGrnStep = new CreateGrnStep();
            createGrnStep.afterAppontmentScheduledCreateGrnForParticularPoNumberForNumberOfIterations();

            if (j == PoNumbersList.size()) {
                break;
            }

            Thread.sleep(1000);
        }

        for (int q = 0; q < asnNumberIdList.size(); q++) {
            System.out.println(asnNumberIdList.get(q) + "============>asnNumber + ==> appointmentId" + q);
            ExtentReportManager.logInfoDetails("asnNumberIdList is " + asnNumberIdList.get(q) + "============>asnNumber + ==> appointmentId" + q);
            System.out.println(grnList.get(q) + "=================>grn number" + q);
            ExtentReportManager.logInfoDetails("grnNumberIdList is " + grnList.get(q) + "============>grnnumber" + q);
        }
        System.out.println(asnNumberIdList.size() + "==========>asnNumberIdList.size");
        ExtentReportManager.logInfoDetails(asnNumberIdList.size() + "==========>asnNumberIdList.size");
        System.out.println(grnList.size() + "==========>grnList.size");
        ExtentReportManager.logInfoDetails(grnList.size() + "==========>grnList.size");
    }
    //*******************************************************************************************************************************8


//calender

    private static int cl = 1;

    @And("using calender date whenever vendor created purchase order,Dc should give availability time,and confirm appointment and create a GRN")
    public void usingCalenderDateWheneverVendorCreatedPurchaseOrderDcShouldGiveAvailabilityTimeAndConfirmAppointmentAndCreateAGRN(DataTable dataTable) throws InterruptedException {

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

            LinkedHashMap<String, Object> GetSlotmainObject = new LinkedHashMap<>();
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


            GetSlotsResponse = RequestGenerator.getRequestWithheader(Headers, GetSlotmainObject).log().all().when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "GetSlotsEndPoint"));
            GetSlotsResponse.then().log().all();

            CommonUtilities.setResponseInstance(GetSlotsResponse);
            Thread.sleep(1000);
            //==============================================================================================

            //confirmappontment

            int iterations = 0;
            LocalTime currentSlotStart = null;
            Integer unloadingRate = 0;
            Response ConfirmGetslotResponse = CommonUtilities.getResponseInstance();
            LinkedHashMap<String, Object> GetslotResponse1 = ConfirmGetslotResponse.as(new TypeRef<LinkedHashMap<String, Object>>() {
            });

            String workingHoursstartTime = ConfirmGetslotResponse.jsonPath().get("workingHours.startTime");//6.00
            String workingHoursendTime = ConfirmGetslotResponse.jsonPath().get("workingHours.endTime");//18.00
            if (unloadingRate != null) {
                unloadingRate = ConfirmGetslotResponse.jsonPath().get("unloadingRate");
            } else {
                System.out.println("***********po number not found+ articles qty mismatch*************************");
            }

            List<Map<String, Object>> unAvailableSlotList = ConfirmGetslotResponse.jsonPath().get("unAvailableSlot");
            LinkedHashMap<String, Object> VehicleMap = new LinkedHashMap<>();
            LinkedHashMap<String, Object> driverDetailsMap = new LinkedHashMap<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime startTime = LocalTime.parse(workingHoursstartTime);
            LocalTime endTime = LocalTime.parse(workingHoursendTime);
            List<LinkedHashMap<String, String>> unAvailableSlotstartAndEndTimeMApList = new ArrayList<>();
            List<Map<String, String>> halfHourSlots = new ArrayList<>();
            try {
                for (int C = 0; C < unAvailableSlotList.size(); C++) {
                    LinkedHashMap<String, String> unAvailableSlotstartAndEndTimeMAp = new LinkedHashMap<>();
                    String unAvailableSlotstartTime = (String) unAvailableSlotList.get(C).get("startTime");
                    String unAvailableSlotendTime = (String) unAvailableSlotList.get(C).get("endTime");
                    unAvailableSlotstartAndEndTimeMAp.put("startTime", unAvailableSlotstartTime);
                    unAvailableSlotstartAndEndTimeMAp.put("endTime", unAvailableSlotendTime);
                    unAvailableSlotstartAndEndTimeMApList.add(unAvailableSlotstartAndEndTimeMAp);
                }
                List<LinkedHashMap<String, String>> CompareendTimec4list = null;
                LocalTime endTimec5Local = null;

                for (int c4 = 0; c4 < unAvailableSlotList.size(); c4++) {
                    LinkedHashMap<String, String> CompareendTimec4Map = new LinkedHashMap<>();
                    CompareendTimec4list = new ArrayList<>();
                    String ComparestartTimec4 = (String) unAvailableSlotList.get(c4).get("startTime");
                    String CompareendTimec4 = (String) unAvailableSlotList.get(c4).get("endTime");
                    CompareendTimec4Map.put("endTime", CompareendTimec4);
                    CompareendTimec4list.add(CompareendTimec4Map);
                }
                for (int c5 = 0; c5 < CompareendTimec4list.size(); c5++) {
                    String endTimec5 = CompareendTimec4list.get(c5).get("endTime");
                    endTimec5Local = LocalTime.parse(endTimec5);
                    currentSlotStart = endTimec5Local;
                }
                //=============================================================================================================================
                if (unloadingRate <= 30) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
                    System.out.println(iterations + "=========================>endTimec30Local");
                } else if (unloadingRate > 30 && unloadingRate <= 60) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 60);
                    System.out.println(iterations + "=========================>endTimec60Local");
                } else if (unloadingRate > 60 && unloadingRate <= 90) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 90);
                    System.out.println(iterations + "=========================>endTimec90Local");
                } else if (unloadingRate > 90 && unloadingRate <= 120) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 120);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else if (unloadingRate > 120 && unloadingRate <= 150) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 150);
                    System.out.println(iterations + "=========================>endTimec150Local");
                } else if (unloadingRate > 150 && unloadingRate <= 180) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 180);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else if (unloadingRate > 180 && unloadingRate <= 210) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 210);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else if (unloadingRate > 210 && unloadingRate <= 240) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 240);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else if (unloadingRate > 240 && unloadingRate <= 270) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 270);
                    System.out.println(iterations + "=========================>endTimec150Local");
                } else if (unloadingRate > 270 && unloadingRate <= 300) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 300);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else if (unloadingRate > 300 && unloadingRate <= 330) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 330);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else if (unloadingRate > 330 && unloadingRate <= 360) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 360);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else if (unloadingRate > 360 && unloadingRate <= 390) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 390);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else if (unloadingRate > 390 && unloadingRate <= 420) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 420);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else {
                    System.out.println("************Time slot not avaialble**********************************" + unloadingRate);
                }

                //=========================================================================================================

                List<Integer> rateDurationList = new ArrayList<>();
                rateDurationList.add(30);
                rateDurationList.add(60);
                rateDurationList.add(90);
                rateDurationList.add(120);
                rateDurationList.add(150);
                rateDurationList.add(180);
                rateDurationList.add(210);
                rateDurationList.add(240);


                for (int c1 = 0; c1 <= iterations - 2; c1++) {
                    int duration = 0;
                    for (int c2 = 0; c2 < rateDurationList.size(); c2++) {
                        if (unloadingRate == rateDurationList.get(c2)) {
                            duration = rateDurationList.get(c2);
                            break;
                        }
                    }

                    LocalTime currentSlotEnd = currentSlotStart.plusMinutes(duration);
                    Map<String, String> slotMap = new HashMap<>();
                    slotMap.put("startTime", currentSlotStart.format(formatter));
                    slotMap.put("endTime", currentSlotEnd.format(formatter));
                    halfHourSlots.add(slotMap);
                    currentSlotStart = currentSlotEnd;

                }

                if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) &&
                        halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {
                    System.out.println(halfHourSlots.get(k).get("startTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + " and " +
                            halfHourSlots.get(k).get("endTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"));

                    for (cl = cl; cl < calenderList.size(); ) {
                        ConfirmpoDueDate1 = calenderList.get(cl);
                        // Reset currentSlotStart and k
                        currentSlotStart = startTime;
                        k = 0;
                        cl++;
                        break;
                    }
                }
                for (int c2 = 0; c2 < unAvailableSlotstartAndEndTimeMApList.size(); c2++) {
                    String unAvailablestartTime = unAvailableSlotstartAndEndTimeMApList.get(c2).get("startTime");
                    String unAvailableendTime = unAvailableSlotstartAndEndTimeMApList.get(c2).get("endTime");
                    LocalTime unAvailableStartTimeLocal = LocalTime.parse(unAvailablestartTime);
                    LocalTime unAvailableendTimeLocal = LocalTime.parse(unAvailableendTime);
                    for (int c3 = 0; c3 < halfHourSlots.size() - 1; c3++) {
                        String halfHourSlotsstartTime = halfHourSlots.get(c3).get("startTime");
                        String halfHourSlotsendTime = halfHourSlots.get(c3).get("endTime");
                        LocalTime halfHourSlotsstartTimeLocal = LocalTime.parse(halfHourSlotsstartTime);
                        LocalTime halfHourSlotsendTimeLocal = LocalTime.parse(halfHourSlotsendTime);
                        if (halfHourSlotsstartTime.equals(unAvailablestartTime)) {
                            System.out.println("================================================>");
                            System.out.println("inside the if()");
                            halfHourSlots.remove(halfHourSlots.get(c3));
                            System.out.println("inside remove");
                            break;
                        }
                    }
                }
                System.out.println("available times ===========================================> ");
                System.out.println(halfHourSlots);
            } catch (NullPointerException e) {
                if (unloadingRate <= 30) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else if (unloadingRate > 30 && unloadingRate <= 60) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 60);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else if (unloadingRate > 60 && unloadingRate <= 90) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 90);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else if (unloadingRate > 90 && unloadingRate <= 120) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 120);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else if (unloadingRate > 120 && unloadingRate <= 150) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 150);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else if (unloadingRate > 150 && unloadingRate <= 180) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 180);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else if (unloadingRate > 180 && unloadingRate <= 210) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 210);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else if (unloadingRate > 210 && unloadingRate <= 240) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 240);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else {
                    System.out.println("************Time slot not avaialble**********************************");
                }

                List<Integer> rateDurationList = new ArrayList<>();
                rateDurationList.add(30);
                rateDurationList.add(60);
                rateDurationList.add(90);
                rateDurationList.add(120);
                rateDurationList.add(150);
                rateDurationList.add(180);
                rateDurationList.add(210);
                rateDurationList.add(240);
                for (int c1 = 0; c1 <= iterations - 2; c1++) {
                    int duration = 0;
                    for (int c2 = 0; c2 < rateDurationList.size(); c2++) {
                        if (unloadingRate == rateDurationList.get(c2)) {
                            duration = rateDurationList.get(c2);
                            break;
                        }
                    }
                    LocalTime currentSlotEnd = currentSlotStart.plusMinutes(duration);
                    Map<String, String> slotMap = new HashMap<>();
                    slotMap.put("startTime", currentSlotStart.format(formatter));
                    slotMap.put("endTime", currentSlotEnd.format(formatter));
                    halfHourSlots.add(slotMap);
                    currentSlotStart = currentSlotEnd;
                }
                try {

                    if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) &&
                            halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {
                        System.out.println(halfHourSlots.get(k).get("startTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + " and " +
                                halfHourSlots.get(k).get("endTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"));
                        for (cl = cl; cl < calenderList.size(); ) {
                            ConfirmpoDueDate1 = calenderList.get(cl);
                            // Reset currentSlotStart and k
                            currentSlotStart = startTime;
                            k = 0;

                            cl++;
                            break;
                        }
                    }
                } catch (IndexOutOfBoundsException ie12) {
                    ie12.printStackTrace();
                    System.out.println("IndexOutOfBoundsException occurred: " + ie12.getMessage());
                }
            }
            List<Map<String, String>> vehicleData = dataTable.asMaps();
            String truckType = vehicleData.get(j).get("truckType");
            String truckNumber = vehicleData.get(j).get("truckNumber");
            String driverName = vehicleData.get(j).get("driverName");
            String driverNumber = vehicleData.get(j).get("driverNumber");
            String dstSiteId = vehicleData.get(j).get("dstSiteId");
            VehicleMap.put("truckType", truckType);
            VehicleMap.put("truckNumber", truckNumber);
            driverDetailsMap.put("driverName", driverName);
            driverDetailsMap.put("driverNumber", driverNumber);
            VehicleMap.put("driverDetails", driverDetailsMap);
            ConfirmAppointmentMap.put("vehicle", VehicleMap);

            ConfirmAppointmentMap.put("isPalletized", isPalletizedBollean);
            ConfirmAppointmentMap.put("appnDate", ConfirmpoDueDate1);


            try {
                ConfirmAppointmentMap.put("appnStartTime", halfHourSlots.get(k).get("startTime"));
                ConfirmAppointmentMap.put("appnEndTime", halfHourSlots.get(k).get("endTime"));
                if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) && halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {
                    System.out.println(halfHourSlots.get(k).get("startTime") + "=halfHourSlots.get(k).get(\"startTime\")" + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + "halfHourSlots.get(halfHourSlots.size() - 1)");
                    System.out.println("clear");
                    halfHourSlots.clear();
                    halfHourSlots = new ArrayList<>();
                }
            } catch (IndexOutOfBoundsException ie14) {
                ie14.printStackTrace();
                System.out.println("IndexOutOfBoundsException occurred: " + ie14.getMessage());
            }
            ConfirmAppointmentMap.put("dstSiteId", dstSiteId);
            List<LinkedHashMap<String, Object>> ItemsList1 = new ArrayList<>();


            for (int c5 = 0; c5 < ponum.size(); c5++) {
                LinkedHashMap<String, Object> ItemsObjct1 = new LinkedHashMap<>();

                //==================================================================================
                String poItemNumber = (String) ponum.get(c5).get("poItemNumber");
                int qty = (int) ponum.get(c5).get("qty");
                //    qty= qty-24;
                int caselot = (int) ponum.get(c5).get("caselot");
                int boxes = qty / caselot;
                double weight = (double) ponum.get(c5).get("weight");
                double volume = (double) ponum.get(c5).get("volume");

                weight = qty * weight / 1000;
                volume = qty * volume;

                ItemsObjct1.put("poItemNumber", poItemNumber);
                ItemsObjct1.put("qty", qty);
                ItemsObjct1.put("weight", weight);
                ItemsObjct1.put("volume", volume);
                ItemsObjct1.put("boxes", boxes);
                ItemsObjct1.put("isPalletized", isPalletizedBollean);
                ItemsList1.add(ItemsObjct1);

            }
            LinkedHashMap<String, Object> Poobject1 = new LinkedHashMap<>();
            Poobject1.put("poNumber", getslotsPonumber);
            Poobject1.put("items", ItemsList1);
            PoList1.add(Poobject1);

            ConfirmAppointmentMap.put("po", PoList1);

            CreateAppointmentResponse = RequestGenerator.getRequestWithheader(Headers, ConfirmAppointmentMap).log().all().when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CreateAppointment"));
            CreateAppointmentResponse.then().log().all();
            CommonUtilities.setResponseInstance(CreateAppointmentResponse);

            String asnNumberResponse = CreateAppointmentResponse.jsonPath().get("asnNumber").toString();
            String appointmentId = CreateAppointmentResponse.jsonPath().get("appointmentId").toString();
            asnNumberIdList.add(asnNumberResponse + "==>asnNumber " + appointmentId + "===>appointmentId " + j);

            //  System.out.println(CreateAppointmentResponse.prettyPrint());
            ExtentReportManager.logInfoDetails("Response is - ");
            ExtentReportManager.logJson(CreateAppointmentResponse.prettyPrint());
            ExtentReportManager.logInfoDetails("Response status code is " + CreateAppointmentResponse.getStatusCode());
            long responseTime = CreateAppointmentResponse.getTimeIn(TimeUnit.MILLISECONDS);
            ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");

            //  Thread.sleep(500);

            j++;
            k++;
//call the GRN method to create GRN for particular appontment
            CreateGrnStep createGrnStep = new CreateGrnStep();
            createGrnStep.afterAppontmentScheduledCreateGrnForParticularPoNumberForNumberOfIterations();

            if (j == PoNumbersList.size()) {
                break;
            }

            Thread.sleep(1000);
        }

        for (int q = 0; q < asnNumberIdList.size(); q++) {
            System.out.println(asnNumberIdList.get(q) + "============>asnNumber + ==> appointmentId" + q);
            ExtentReportManager.logInfoDetails("asnNumberIdList is " + asnNumberIdList.get(q) + "============>asnNumber + ==> appointmentId" + q);
            System.out.println(grnList.get(q) + "=================>grn number" + q);
            System.out.println(PoNumbersList.get(q) + "================>po number" + q);
            ExtentReportManager.logInfoDetails("grnNumberIdList is " + grnList.get(q) + "============>grnnumber" + q);
        }
        System.out.println(asnNumberIdList.size() + "==========>asnNumberIdList.size");
        ExtentReportManager.logInfoDetails(asnNumberIdList.size() + "==========>asnNumberIdList.size");
        System.out.println(grnList.size() + "==========>grnList.size");
        ExtentReportManager.logInfoDetails(grnList.size() + "==========>grnList.size");

    }


    //====================================================================================================================================================

//    //validae slots and assign the slots
//***************************************************************************************************************
//    *************************************************************************************************************


//    @And("using calender date whenever vendor created purchase order,Dc should give availability time,and confirm appointment and create a GRN and Validate")
//    public void usingCalenderDateWheneverVendorCreatedPurchaseOrderDcShouldGiveAvailabilityTimeAndConfirmAppointmentAndCreateAGRNAndValidate(DataTable dataTable) throws InterruptedException {
//
//
//        int j;
//        int k = 0;
//        List<String> calenderList = CommonUtilities.getcalenderResponseList();
//
//        for (int i = 0; i < PoNumbersList.size(); i++) {
//            System.out.println(PoNumbersList.get(i) + "=====GetSlotsConfirmApponment" + i);
//            ExtentReportManager.logInfoDetails("GetSlotsConfirmApponment is - ");
//            ExtentReportManager.logInfoDetails("poNumbersList " + PoNumbersList.get(i) + "=====GetSlotsConfirmApponment" + i);
//        }
//
//        System.out.println(PoNumbersList.size() + "====================>PoNumbersList.size()");
//
//        for (j = 0; j < PoNumbersList.size(); ) {
//            RequestGenerator requestGenerator = new RequestGenerator();
//            Response GetSlotsResponse;
//            Response CreateAppointmentResponse;
//            String ConfirmpoDueDate;
//
//
//            String getslotpoDueDate;
//
//            LinkedHashMap<String, Object> GetSlotmainObject = new LinkedHashMap<>();
////due date taking
//            List<LinkedHashMap<String, Object>> ResponseItemsList = CommonUtilities.getResponseItemsList();
//
//            LinkedHashMap<String, String> Headers = new LinkedHashMap<>();
//
//            //   String bearer = Bearertoken.get("Authorization");
//            Headers.put("Authorization", Auth);
//            Headers.put("requestid", GenricUtils.getRandomDeliveryNumber());
//
//
//            List<LinkedHashMap<String, Object>> PoList1 = new ArrayList<>();
//
//            LinkedHashMap<String, Object> poObject = new LinkedHashMap<>();
//            List<LinkedHashMap<String, Object>> PoList = new ArrayList<>();
//            // ponumbersbody= CommonUtilities.getPonumbersBody();
//            ponum = ponumbersbody.get(PoNumbersList.get(j));
//
//            List<LinkedHashMap<String, Object>> ItemsList = new ArrayList<>();
//            List<String> PoItemNumberList = new ArrayList<>();
//            LinkedHashMap<String, Object> ConfirmAppointmentMap = new LinkedHashMap<>();
//            getslotsPonumber = PoNumbersList.get(j);
//            Boolean isPalletizedBollean = null;
//            for (int i = 0; i < ponum.size(); i++) {
//                LinkedHashMap<String, Object> ItemsObject = new LinkedHashMap<>();
//
//                String poItemNumber = null;
//                double weight = 0;
//                double volume = 0;
//                Integer qty = 0;
//
//                String siteId = (String) ponum.get(i).get("siteId");
////================================================================================
//                poItemNumber = (String) ponum.get(i).get("poItemNumber");
//                PoItemNumberList.add(poItemNumber);
//
//                weight = (double) ponum.get(i).get("weight");
//
//                volume = (double) ponum.get(i).get("volume");
//
//                qty = (Integer) ponum.get(i).get("qty");
//
//                //    qty= qty-24;
//                weight = qty * weight / 1000;
//                volume = qty * volume;
//
//                Integer caselot = (Integer) ponum.get(i).get("caselot");
//                int totalBoxes = qty / caselot;
//
//
//                //============================================================================
//
//
//                ItemsObject.put("poItemNumber", poItemNumber);
//                ItemsObject.put("qty", qty);
//                ItemsObject.put("weight", weight);
//                ItemsObject.put("volume", volume);
//                ItemsObject.put("boxes", totalBoxes);
//                // "isPalletized": false
//                List<Map<String, String>> palletDT = dataTable.asMaps();
//                isPalletizedBollean = Boolean.valueOf(palletDT.get(j).get("isPalletized"));
//
//                ItemsObject.put("isPalletized", isPalletizedBollean);
//
//                ItemsList.add(ItemsObject);
//                if (ConfirmpoDueDate1 != null) {
//                    ConfirmpoDueDate1 = ConfirmpoDueDate1;
//                } else {
//                    //  getslotpoDueDate = (String) ResponseItemsList.get(i).get("poDueDate");
//                    getslotpoDueDate = calenderList.get(0);
//
//                    ConfirmpoDueDate1 = getslotpoDueDate;
//                }
//            }
//
//            poObject.put("poNumber", getslotsPonumber);
//            poObject.put("items", ItemsList);
//
//            PoList.add(poObject);
//
//            GetSlotmainObject.put("date", ConfirmpoDueDate1);
//            String id = CommonUtilities.getSiteId();
//            GetSlotmainObject.put("siteId", id);
//            GetSlotmainObject.put("po", PoList);
//
//
//            GetSlotsResponse = RequestGenerator.getRequestWithheader(Headers, GetSlotmainObject).log().all().when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "GetSlotsEndPoint"));
//            GetSlotsResponse.then().log().all();
//
//            CommonUtilities.setResponseInstance(GetSlotsResponse);
//            Thread.sleep(1000);
//            //==============================================================================================
//
//            //confirmappontment
//
//            int iterations = 0;
//            LocalTime currentSlotStart = null;
//            Integer unloadingRate = 0;
//            Response ConfirmGetslotResponse = CommonUtilities.getResponseInstance();
//            LinkedHashMap<String, Object> GetslotResponse1 = ConfirmGetslotResponse.as(new TypeRef<LinkedHashMap<String, Object>>() {
//            });
//
//            String workingHoursstartTime = ConfirmGetslotResponse.jsonPath().get("workingHours.startTime");//6.00
//            String workingHoursendTime = ConfirmGetslotResponse.jsonPath().get("workingHours.endTime");//18.00
//            if (unloadingRate != null) {
//                unloadingRate = ConfirmGetslotResponse.jsonPath().get("unloadingRate");
//            } else {
//                System.out.println("***********po number not found+ articles qty mismatch*************************");
//            }
//
//            List<Map<String, Object>> unAvailableSlotList = ConfirmGetslotResponse.jsonPath().get("unAvailableSlot");
//            LinkedHashMap<String, Object> VehicleMap = new LinkedHashMap<>();
//            LinkedHashMap<String, Object> driverDetailsMap = new LinkedHashMap<>();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//            LocalTime startTime = LocalTime.parse(workingHoursstartTime);
//            LocalTime endTime = LocalTime.parse(workingHoursendTime);
//            List<LinkedHashMap<String, String>> unAvailableSlotstartAndEndTimeMApList = new ArrayList<>();
//            List<Map<String, String>> halfHourSlots = new ArrayList<>();
//            try {
//                for (int C = 0; C < unAvailableSlotList.size(); C++) {
//                    LinkedHashMap<String, String> unAvailableSlotstartAndEndTimeMAp = new LinkedHashMap<>();
//                    String unAvailableSlotstartTime = (String) unAvailableSlotList.get(C).get("startTime");
//                    String unAvailableSlotendTime = (String) unAvailableSlotList.get(C).get("endTime");
//                    unAvailableSlotstartAndEndTimeMAp.put("startTime", unAvailableSlotstartTime);
//                    unAvailableSlotstartAndEndTimeMAp.put("endTime", unAvailableSlotendTime);
//                    unAvailableSlotstartAndEndTimeMApList.add(unAvailableSlotstartAndEndTimeMAp);
//                }
//                List<LinkedHashMap<String, String>> CompareendTimec4list = null;
//                LocalTime endTimec5Local = null;
//
//                for (int c4 = 0; c4 < unAvailableSlotList.size(); c4++) {
//                    LinkedHashMap<String, String> CompareendTimec4Map = new LinkedHashMap<>();
//                    CompareendTimec4list = new ArrayList<>();
//                    String ComparestartTimec4 = (String) unAvailableSlotList.get(c4).get("startTime");
//                    String CompareendTimec4 = (String) unAvailableSlotList.get(c4).get("endTime");
//                    CompareendTimec4Map.put("endTime", CompareendTimec4);
//                    CompareendTimec4list.add(CompareendTimec4Map);
//                }
//                for (int c5 = 0; c5 < CompareendTimec4list.size(); c5++) {
//                    String endTimec5 = CompareendTimec4list.get(c5).get("endTime");
//                    endTimec5Local = LocalTime.parse(endTimec5);
//                    currentSlotStart = endTimec5Local;
//                }
//                //=============================================================================================================================
//                if (unloadingRate <= 30) {
//                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
//                    System.out.println(iterations + "=========================>endTimec30Local");
//                } else if (unloadingRate > 30 && unloadingRate <= 60) {
//                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 60);
//                    System.out.println(iterations + "=========================>endTimec60Local");
//                } else if (unloadingRate > 60 && unloadingRate <= 90) {
//                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 90);
//                    System.out.println(iterations + "=========================>endTimec90Local");
//                } else if (unloadingRate > 90 && unloadingRate <= 120) {
//                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 120);
//                    System.out.println(iterations + "=========================>endTimec120Local");
//                } else if (unloadingRate > 120 && unloadingRate <= 150) {
//                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 150);
//                    System.out.println(iterations + "=========================>endTimec150Local");
//                } else if (unloadingRate > 150 && unloadingRate <= 180) {
//                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 180);
//                    System.out.println(iterations + "=========================>endTimec120Local");
//                } else if (unloadingRate > 180 && unloadingRate <= 210) {
//                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 210);
//                    System.out.println(iterations + "=========================>endTimec120Local");
//                } else if (unloadingRate > 210 && unloadingRate <= 240) {
//                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 240);
//                    System.out.println(iterations + "=========================>endTimec120Local");
//                } else if (unloadingRate > 240 && unloadingRate <= 270) {
//                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 270);
//                    System.out.println(iterations + "=========================>endTimec150Local");
//                } else if (unloadingRate > 270 && unloadingRate <= 300) {
//                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 300);
//                    System.out.println(iterations + "=========================>endTimec120Local");
//                } else if (unloadingRate > 300 && unloadingRate <= 330) {
//                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 330);
//                    System.out.println(iterations + "=========================>endTimec120Local");
//                } else if (unloadingRate > 330 && unloadingRate <= 360) {
//                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 360);
//                    System.out.println(iterations + "=========================>endTimec120Local");
//                } else if (unloadingRate > 360 && unloadingRate <= 390) {
//                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 390);
//                    System.out.println(iterations + "=========================>endTimec120Local");
//                } else if (unloadingRate > 390 && unloadingRate <= 420) {
//                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 420);
//                    System.out.println(iterations + "=========================>endTimec120Local");
//                } else {
//                    System.out.println("************Time slot not avaialble**********************************" + unloadingRate);
//                }
//
//                //=========================================================================================================
//
//                List<Integer> rateDurationList = new ArrayList<>();
//                rateDurationList.add(30);
//                rateDurationList.add(60);
//                rateDurationList.add(90);
//                rateDurationList.add(120);
//                rateDurationList.add(150);
//                rateDurationList.add(180);
//                rateDurationList.add(210);
//                rateDurationList.add(240);
//
//
//                for (int c1 = 0; c1 <= iterations - 2; c1++) {
//                    int duration = 0;
//                    for (int c2 = 0; c2 < rateDurationList.size(); c2++) {
//                        if (unloadingRate == rateDurationList.get(c2)) {
//                            duration = rateDurationList.get(c2);
//                            break;
//                        }
//                    }
//
//                    LocalTime currentSlotEnd = currentSlotStart.plusMinutes(duration);
//                    Map<String, String> slotMap = new HashMap<>();
//                    slotMap.put("startTime", currentSlotStart.format(formatter));
//                    slotMap.put("endTime", currentSlotEnd.format(formatter));
//                    halfHourSlots.add(slotMap);
//                    currentSlotStart = currentSlotEnd;
//
//                }
//
//                if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) &&
//                        halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {
//                    System.out.println(halfHourSlots.get(k).get("startTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + " and " +
//                            halfHourSlots.get(k).get("endTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"));
//
//                    for (cl = cl; cl < calenderList.size(); ) {
//                        ConfirmpoDueDate1 = calenderList.get(cl);
//                        // Reset currentSlotStart and k
//                        currentSlotStart = startTime;
//                        k = 0;
//                        cl++;
//                        break;
//                    }
//                }
//                for (int c2 = 0; c2 < unAvailableSlotstartAndEndTimeMApList.size(); c2++) {
//                    String unAvailablestartTime = unAvailableSlotstartAndEndTimeMApList.get(c2).get("startTime");
//                    String unAvailableendTime = unAvailableSlotstartAndEndTimeMApList.get(c2).get("endTime");
//                    LocalTime unAvailableStartTimeLocal = LocalTime.parse(unAvailablestartTime);
//                    LocalTime unAvailableendTimeLocal = LocalTime.parse(unAvailableendTime);
//                    for (int c3 = 0; c3 < halfHourSlots.size() - 1; c3++) {
//                        String halfHourSlotsstartTime = halfHourSlots.get(c3).get("startTime");
//                        String halfHourSlotsendTime = halfHourSlots.get(c3).get("endTime");
//                        LocalTime halfHourSlotsstartTimeLocal = LocalTime.parse(halfHourSlotsstartTime);
//                        LocalTime halfHourSlotsendTimeLocal = LocalTime.parse(halfHourSlotsendTime);
//                        if (halfHourSlotsstartTime.equals(unAvailablestartTime)) {
//                            System.out.println("================================================>");
//                            System.out.println("inside the if()");
//                            halfHourSlots.remove(halfHourSlots.get(c3));
//                            System.out.println("inside remove");
//                            break;
//                        }
//                    }
//                }
//                System.out.println("available times ===========================================> ");
//                System.out.println(halfHourSlots);
//            } catch (NullPointerException e) {
//                if (unloadingRate <= 30) {
//                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
//                    currentSlotStart = startTime;
//                    System.out.println(iterations + "=========================>startTime");
//                } else if (unloadingRate > 30 && unloadingRate <= 60) {
//                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 60);
//                    currentSlotStart = startTime;
//                    System.out.println(iterations + "=========================>startTime");
//                } else if (unloadingRate > 60 && unloadingRate <= 90) {
//                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 90);
//                    currentSlotStart = startTime;
//                    System.out.println(iterations + "=========================>startTime");
//                } else if (unloadingRate > 90 && unloadingRate <= 120) {
//                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 120);
//                    currentSlotStart = startTime;
//                    System.out.println(iterations + "=========================>startTime");
//                } else if (unloadingRate > 120 && unloadingRate <= 150) {
//                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 150);
//                    currentSlotStart = startTime;
//                    System.out.println(iterations + "=========================>startTime");
//                } else if (unloadingRate > 150 && unloadingRate <= 180) {
//                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 180);
//                    currentSlotStart = startTime;
//                    System.out.println(iterations + "=========================>startTime");
//                } else if (unloadingRate > 180 && unloadingRate <= 210) {
//                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 210);
//                    System.out.println(iterations + "=========================>endTimec120Local");
//                } else if (unloadingRate > 210 && unloadingRate <= 240) {
//                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 240);
//                    currentSlotStart = startTime;
//                    System.out.println(iterations + "=========================>startTime");
//                } else {
//                    System.out.println("************Time slot not avaialble**********************************");
//                }
//
//                List<Integer> rateDurationList = new ArrayList<>();
//                rateDurationList.add(30);
//                rateDurationList.add(60);
//                rateDurationList.add(90);
//                rateDurationList.add(120);
//                rateDurationList.add(150);
//                rateDurationList.add(180);
//                rateDurationList.add(210);
//                rateDurationList.add(240);
//                for (int c1 = 0; c1 <= iterations - 2; c1++) {
//                    int duration = 0;
//                    for (int c2 = 0; c2 < rateDurationList.size(); c2++) {
//                        if (unloadingRate == rateDurationList.get(c2)) {
//                            duration = rateDurationList.get(c2);
//                            break;
//                        }
//                    }
//                    LocalTime currentSlotEnd = currentSlotStart.plusMinutes(duration);
//                    Map<String, String> slotMap = new HashMap<>();
//                    slotMap.put("startTime", currentSlotStart.format(formatter));
//                    slotMap.put("endTime", currentSlotEnd.format(formatter));
//                    halfHourSlots.add(slotMap);
//                    currentSlotStart = currentSlotEnd;
//                }
//                try {
//
//                    if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) &&
//                            halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {
//                        System.out.println(halfHourSlots.get(k).get("startTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + " and " +
//                                halfHourSlots.get(k).get("endTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"));
//                        for (cl = cl; cl < calenderList.size(); ) {
//                            ConfirmpoDueDate1 = calenderList.get(cl);
//                            // Reset currentSlotStart and k
//                            currentSlotStart = startTime;
//                            k = 0;
//
//                            cl++;
//                            break;
//                        }
//                    }
//                } catch (IndexOutOfBoundsException ie12) {
//                    ie12.printStackTrace();
//                    System.out.println("IndexOutOfBoundsException occurred: " + ie12.getMessage());
//                }
//            }
//            List<Map<String, String>> vehicleData = dataTable.asMaps();
//            String truckType = vehicleData.get(j).get("truckType");
//            String truckNumber = vehicleData.get(j).get("truckNumber");
//            String driverName = vehicleData.get(j).get("driverName");
//            String driverNumber = vehicleData.get(j).get("driverNumber");
//            String dstSiteId = vehicleData.get(j).get("dstSiteId");
//            VehicleMap.put("truckType", truckType);
//            VehicleMap.put("truckNumber", truckNumber);
//            driverDetailsMap.put("driverName", driverName);
//            driverDetailsMap.put("driverNumber", driverNumber);
//            VehicleMap.put("driverDetails", driverDetailsMap);
//            ConfirmAppointmentMap.put("vehicle", VehicleMap);
//
//            ConfirmAppointmentMap.put("isPalletized", isPalletizedBollean);
//            ConfirmAppointmentMap.put("appnDate", ConfirmpoDueDate1);
////========================================================================================================================================================
//            //call the method and take return type
////            GetSlotsStep getSlotsStep = new GetSlotsStep();
////            LinkedHashMap<String, Object> ValidateObject = getSlotsStep.slotIsAvailbleForParticularTimeSlotOrNot(dataTable.asMaps());
//
//            slotIsAvailbleForParticularTimeSlotOrNot(dataTable.asMaps());
//
//            LinkedHashMap<String, Object> ValidateObject = slotIsAvailbleForParticularTimeSlotOrNot(dataTable.asMaps());
//
//
//            ValidateObject.put("appnStartTime", halfHourSlots.get(k).get("startTime"));
//            ValidateObject.put("appnEndTime", halfHourSlots.get(k).get("endTime"));
//
//            Response GetSlotsValidateResponse = RequestGenerator.getRequestWithheader(Headers, ValidateObject).log().all().when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "ValidateEndPoint"));
//            GetSlotsValidateResponse.then().log().all();
//            //     CommonUtilities.setResponseInstance(GetSlotsResponse);
//            String booleanValueResponse = GetSlotsValidateResponse.getBody().asString();
//            boolean falseValue = Boolean.parseBoolean(booleanValueResponse.trim());
//
//
//            System.out.println(booleanValueResponse + "===========================>booleanValueResponse");
//
//
//            if (falseValue == false) {
//                try {
//                    ConfirmAppointmentMap.put("appnStartTime", halfHourSlots.get(k++).get("startTime"));
//                    ConfirmAppointmentMap.put("appnEndTime", halfHourSlots.get(k).get("endTime"));
//
//
//                    if (halfHourSlots.get(k + 1).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) && halfHourSlots.get(k + 1).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {
//                        System.out.println(halfHourSlots.get(k).get("startTime") + "=halfHourSlots.get(k).get(\"startTime\")" + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + "halfHourSlots.get(halfHourSlots.size() - 1)");
//                        System.out.println("clear");
//                        halfHourSlots.clear();
//                        halfHourSlots = new ArrayList<>();
//                    }
//                } catch (IndexOutOfBoundsException ie14) {
//                    ie14.printStackTrace();
//                    System.out.println("IndexOutOfBoundsException occurred: " + ie14.getMessage());
//                }
//            } else {
//                try {
//                    ConfirmAppointmentMap.put("appnStartTime", halfHourSlots.get(k).get("startTime"));
//                    ConfirmAppointmentMap.put("appnEndTime", halfHourSlots.get(k).get("endTime"));
//                    if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) && halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {
//                        System.out.println(halfHourSlots.get(k).get("startTime") + "=halfHourSlots.get(k).get(\"startTime\")" + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + "halfHourSlots.get(halfHourSlots.size() - 1)");
//                        System.out.println("clear");
//                        halfHourSlots.clear();
//                        halfHourSlots = new ArrayList<>();
//                    }
//                } catch (IndexOutOfBoundsException ie14) {
//                    ie14.printStackTrace();
//                    System.out.println("IndexOutOfBoundsException occurred: " + ie14.getMessage());
//                }
//            }
//
//
//            //==========================================================================================================================================
//
//
//            ConfirmAppointmentMap.put("dstSiteId", dstSiteId);
//            List<LinkedHashMap<String, Object>> ItemsList1 = new ArrayList<>();
//
//
//            for (int c5 = 0; c5 < ponum.size(); c5++) {
//                LinkedHashMap<String, Object> ItemsObjct1 = new LinkedHashMap<>();
//
//                //==================================================================================
//                String poItemNumber = (String) ponum.get(c5).get("poItemNumber");
//                int qty = (int) ponum.get(c5).get("qty");
//                //    qty= qty-24;
//                int caselot = (int) ponum.get(c5).get("caselot");
//                int boxes = qty / caselot;
//                double weight = (double) ponum.get(c5).get("weight");
//                double volume = (double) ponum.get(c5).get("volume");
//
//                weight = qty * weight / 1000;
//                volume = qty * volume;
//
//                ItemsObjct1.put("poItemNumber", poItemNumber);
//                ItemsObjct1.put("qty", qty);
//                ItemsObjct1.put("weight", weight);
//                ItemsObjct1.put("volume", volume);
//                ItemsObjct1.put("boxes", boxes);
//                ItemsObjct1.put("isPalletized", isPalletizedBollean);
//                ItemsList1.add(ItemsObjct1);
//
//            }
//            LinkedHashMap<String, Object> Poobject1 = new LinkedHashMap<>();
//            Poobject1.put("poNumber", getslotsPonumber);
//            Poobject1.put("items", ItemsList1);
//            PoList1.add(Poobject1);
//
//            ConfirmAppointmentMap.put("po", PoList1);
//
//            CreateAppointmentResponse = RequestGenerator.getRequestWithheader(Headers, ConfirmAppointmentMap).log().all().when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CreateAppointment"));
//            CreateAppointmentResponse.then().log().all();
//            CommonUtilities.setResponseInstance(CreateAppointmentResponse);
//            try {
//                String asnNumberResponse = CreateAppointmentResponse.jsonPath().get("asnNumber").toString();
//                String appointmentId = CreateAppointmentResponse.jsonPath().get("appointmentId").toString();
//                asnNumberIdList.add(asnNumberResponse + "==>asnNumber " + appointmentId + "===>appointmentId " + j);
//            } catch (NullPointerException ne1) {
//
//                ConfirmAppointmentMap.put("appnStartTime", halfHourSlots.get(k++).get("startTime"));
//                ConfirmAppointmentMap.put("appnEndTime", halfHourSlots.get(k).get("endTime"));
//
//                ConfirmAppointmentMap.put("po", PoList1);
//
//                CreateAppointmentResponse = RequestGenerator.getRequestWithheader(Headers, ConfirmAppointmentMap).log().all().when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CreateAppointment"));
//                CreateAppointmentResponse.then().log().all();
//                CommonUtilities.setResponseInstance(CreateAppointmentResponse);
//
//                String asnNumberResponse = CreateAppointmentResponse.jsonPath().get("asnNumber").toString();
//                String appointmentId = CreateAppointmentResponse.jsonPath().get("appointmentId").toString();
//                asnNumberIdList.add(asnNumberResponse + "==>asnNumber " + appointmentId + "===>appointmentId " + j);
//
//            }
//            //  System.out.println(CreateAppointmentResponse.prettyPrint());
//            ExtentReportManager.logInfoDetails("Response is - ");
//            ExtentReportManager.logJson(CreateAppointmentResponse.prettyPrint());
//            ExtentReportManager.logInfoDetails("Response status code is " + CreateAppointmentResponse.getStatusCode());
//            long responseTime = CreateAppointmentResponse.getTimeIn(TimeUnit.MILLISECONDS);
//            ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");
//
//            //  Thread.sleep(500);
//
//            j++;
//            k++;
////call the GRN method to create GRN for particular appontment
//            CreateGrnStep createGrnStep = new CreateGrnStep();
//            createGrnStep.afterAppontmentScheduledCreateGrnForParticularPoNumberForNumberOfIterations();
//
//            if (j == PoNumbersList.size()) {
//                break;
//            }
//
//            Thread.sleep(1000);
//        }
//
//        for (int q = 0; q < asnNumberIdList.size(); q++) {
//            System.out.println(asnNumberIdList.get(q) + "============>asnNumber + ==> appointmentId" + q);
//            ExtentReportManager.logInfoDetails("asnNumberIdList is " + asnNumberIdList.get(q) + "============>asnNumber + ==> appointmentId" + q);
//            System.out.println(grnList.get(q) + "=================>grn number" + q);
//            System.out.println(PoNumbersList.get(q) + "================>po number" + q);
//            ExtentReportManager.logInfoDetails("grnNumberIdList is " + grnList.get(q) + "============>grnnumber" + q);
//        }
//        System.out.println(asnNumberIdList.size() + "==========>asnNumberIdList.size");
//        ExtentReportManager.logInfoDetails(asnNumberIdList.size() + "==========>asnNumberIdList.size");
//        System.out.println(grnList.size() + "==========>grnList.size");
//        ExtentReportManager.logInfoDetails(grnList.size() + "==========>grnList.size");
//
//
//    }

//    public static void main(String[] args) {
//        List<Map<String, String>> palletDT = new ArrayList<>();
//        slotIsAvailbleForParticularTimeSlotOrNot(palletDT);
//    }


    static LinkedHashMap<String, Object> GetSlotmainObject;

    public static LinkedHashMap<String, Object> slotIsAvailbleForParticularTimeSlotOrNot(List<Map<String, String>> palletDT) {

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
//                List<Map<String, String>> palletDT = dataTable.asMaps();
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
//*******************************************************************************************************************************


//sorted order
    //======================================
//    ******************************************************************************************************************************************************************************************************
/*
     private static int cl = 1;

    @And("using calender date whenever vendor created purchase order,Dc should give availability time,and confirm appointment and create a GRN")
    public void usingCalenderDateWheneverVendorCreatedPurchaseOrderDcShouldGiveAvailabilityTimeAndConfirmAppointmentAndCreateAGRN(DataTable dataTable) throws InterruptedException {

        int j;
        int k = 0;
        List<String> calenderList = CommonUtilities.getcalenderResponseList();

        for (int i = 0; i < PoNumbersList.size(); i++) {
            System.out.println(PoNumbersList.get(i) + "=====GetSlotsConfirmApponment" + i);
            ExtentReportManager.logInfoDetails("GetSlotsConfirmApponment is - ");
            ExtentReportManager.logInfoDetails("poNumbersList " + PoNumbersList.get(i) + "=====GetSlotsConfirmApponment" + i);
        }

        System.out.println(PoNumbersList.size() + "====================>PoNumbersList.size()");

        //=======================================================================================================================
        Collections.sort(GetpoItemNumberList);
        List<String> GetpoItemNumberList1 = new ArrayList<>(GetpoItemNumberList);


        int m2 = 0;
        for (String getposortOrder : GetpoItemNumberList1) {
            System.out.println(getposortOrder.toString() + "==========>sorted order" + m2);
            m2++;
        }

        //==================================================================================================================
        for (j = 0; j < PoNumbersList.size(); ) {
            RequestGenerator requestGenerator = new RequestGenerator();
            Response GetSlotsResponse;
            Response CreateAppointmentResponse;
            String ConfirmpoDueDate;


            String getslotpoDueDate;

            LinkedHashMap<String, Object> GetSlotmainObject = new LinkedHashMap<>();
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

            for (int i = 0; i < ponum.size(); i++) {
                LinkedHashMap<String, Object> ItemsObject = new LinkedHashMap<>();

                String poItemNumber = null;
                double weight = 0;
                double volume = 0;
                Integer qty = 0;

                String siteId = (String) ponum.get(i).get("siteId");
//================================================================================
//                poItemNumber = (String) ponum.get(i).get("poItemNumber");
//                PoItemNumberList.add(poItemNumber);
//
//                weight = (double) ponum.get(i).get("weight");
//
//                volume = (double) ponum.get(i).get("volume");
//
//                qty = (Integer) ponum.get(i).get("qty");
//================================================================================

                for (int m1 = 0; m1 < GetpoItemNumberList1.size(); m1++) {
                    //     System.out.println(GetpoItemNumberList1.get(m1)+"============>GetpoItemNumberList1.get(m1)");
                    //    System.out.println(ponum.get(i).get("poItemNumber")+"=================>ponum.get(i).get(\"poItemNumber\")");
                    if (GetpoItemNumberList1.get(m1).equals(ponum.get(i).get("poItemNumber"))) {
                        poItemNumber = (String) GetpoItemNumberList1.get(m1);
                        PoItemNumberList.add(poItemNumber);

                        weight = (double) ponum.get(i).get("weight");

                        volume = (double) ponum.get(i).get("volume");

                        qty = (Integer) ponum.get(i).get("qty");
                    }
                }

                //============================================================================================
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


            GetSlotsResponse = RequestGenerator.getRequestWithheader(Headers, GetSlotmainObject).log().all().when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "GetSlotsEndPoint"));
            GetSlotsResponse.then().log().all();
            //    System.out.println(GetSlotsResponse.prettyPrint());
            //GetSlotsResponse.getBody().as(HashMap.class)

            CommonUtilities.setResponseInstance(GetSlotsResponse);
            Thread.sleep(1000);
            //==============================================================================================

            //confirmappontment

            int iterations = 0;
            LocalTime currentSlotStart = null;
            Integer unloadingRate = 0;
            Response ConfirmGetslotResponse = CommonUtilities.getResponseInstance();
            LinkedHashMap<String, Object> GetslotResponse1 = ConfirmGetslotResponse.as(new TypeRef<LinkedHashMap<String, Object>>() {
            });

            String workingHoursstartTime = ConfirmGetslotResponse.jsonPath().get("workingHours.startTime");//6.00
            String workingHoursendTime = ConfirmGetslotResponse.jsonPath().get("workingHours.endTime");//18.00
            if (unloadingRate != null) {
                unloadingRate = ConfirmGetslotResponse.jsonPath().get("unloadingRate");
            } else {
                System.out.println("***********po number not found+ articles qty mismatch*************************");
            }

            List<Map<String, Object>> unAvailableSlotList = ConfirmGetslotResponse.jsonPath().get("unAvailableSlot");
            LinkedHashMap<String, Object> VehicleMap = new LinkedHashMap<>();
            LinkedHashMap<String, Object> driverDetailsMap = new LinkedHashMap<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime startTime = LocalTime.parse(workingHoursstartTime);
            LocalTime endTime = LocalTime.parse(workingHoursendTime);
            List<LinkedHashMap<String, String>> unAvailableSlotstartAndEndTimeMApList = new ArrayList<>();
            List<Map<String, String>> halfHourSlots = new ArrayList<>();
            try {
                for (int C = 0; C < unAvailableSlotList.size(); C++) {
                    LinkedHashMap<String, String> unAvailableSlotstartAndEndTimeMAp = new LinkedHashMap<>();
                    String unAvailableSlotstartTime = (String) unAvailableSlotList.get(C).get("startTime");
                    String unAvailableSlotendTime = (String) unAvailableSlotList.get(C).get("endTime");
                    unAvailableSlotstartAndEndTimeMAp.put("startTime", unAvailableSlotstartTime);
                    unAvailableSlotstartAndEndTimeMAp.put("endTime", unAvailableSlotendTime);
                    unAvailableSlotstartAndEndTimeMApList.add(unAvailableSlotstartAndEndTimeMAp);
                }
                List<LinkedHashMap<String, String>> CompareendTimec4list = null;
                LocalTime endTimec5Local = null;

                for (int c4 = 0; c4 < unAvailableSlotList.size(); c4++) {
                    LinkedHashMap<String, String> CompareendTimec4Map = new LinkedHashMap<>();
                    CompareendTimec4list = new ArrayList<>();
                    String ComparestartTimec4 = (String) unAvailableSlotList.get(c4).get("startTime");
                    String CompareendTimec4 = (String) unAvailableSlotList.get(c4).get("endTime");
                    CompareendTimec4Map.put("endTime", CompareendTimec4);
                    CompareendTimec4list.add(CompareendTimec4Map);
                }
                for (int c5 = 0; c5 < CompareendTimec4list.size(); c5++) {
                    String endTimec5 = CompareendTimec4list.get(c5).get("endTime");
                    endTimec5Local = LocalTime.parse(endTimec5);
                    currentSlotStart = endTimec5Local;
                }
                //=============================================================================================================================
                if (unloadingRate <= 30) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
                    System.out.println(iterations + "=========================>endTimec30Local");
                } else if (unloadingRate > 30 && unloadingRate <= 60) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 60);
                    System.out.println(iterations + "=========================>endTimec60Local");
                } else if (unloadingRate > 60 && unloadingRate <= 90) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 90);
                    System.out.println(iterations + "=========================>endTimec90Local");
                } else if (unloadingRate > 90 && unloadingRate <= 120) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 120);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else if (unloadingRate > 120 && unloadingRate <= 150) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 150);
                    System.out.println(iterations + "=========================>endTimec150Local");
                } else if (unloadingRate > 150 && unloadingRate <= 180) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 180);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else if (unloadingRate > 180 && unloadingRate <= 210) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 210);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else if (unloadingRate > 210 && unloadingRate <= 240) {
                    iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 240);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else {
                    System.out.println("************Time slot not avaialble**********************************");
                }

                //=========================================================================================================

                List<Integer> rateDurationList = new ArrayList<>();
                rateDurationList.add(30);
                rateDurationList.add(60);
                rateDurationList.add(90);
                rateDurationList.add(120);
                rateDurationList.add(150);
                rateDurationList.add(180);
                rateDurationList.add(210);
                rateDurationList.add(240);


                for (int c1 = 0; c1 <= iterations - 2; c1++) {
                    int duration = 0;
                    for (int c2 = 0; c2 < rateDurationList.size(); c2++) {
                        if (unloadingRate == rateDurationList.get(c2)) {
                            duration = rateDurationList.get(c2);
                            break;
                        }
                    }

                    LocalTime currentSlotEnd = currentSlotStart.plusMinutes(duration);
                    Map<String, String> slotMap = new HashMap<>();
                    slotMap.put("startTime", currentSlotStart.format(formatter));
                    slotMap.put("endTime", currentSlotEnd.format(formatter));
                    halfHourSlots.add(slotMap);
                    currentSlotStart = currentSlotEnd;

                }
//                Map<Integer, Integer> rateDurationMap = new HashMap<>();
//                rateDurationMap.put(0, 30);     // unloadingRate <= 30
//                rateDurationMap.put(31, 60);    // 31 < unloadingRate <= 60
//                rateDurationMap.put(61, 90);    // 61 < unloadingRate <= 90
//                rateDurationMap.put(91, 120);   // 91 < unloadingRate <= 120
//                rateDurationMap.put(121, 150);  // 121 < unloadingRate <= 150
//                rateDurationMap.put(151, 180);  // 151 < unloadingRate <= 180
//                rateDurationMap.put(181, 210);
//                rateDurationMap.put(211, 240);


//                for (int c1 = 0; c1 <= iterations - 2; c1++) {
//                    int duration = 0;
//                    for (Map.Entry<Integer, Integer> entry : rateDurationMap.entrySet()) {
//                        if (unloadingRate == entry.getValue()) {
//                            duration = entry.getValue();
//                            break;
//                        }
//                    }
//                    LocalTime currentSlotEnd = currentSlotStart.plusMinutes(duration);
//                    Map<String, String> slotMap = new HashMap<>();
//                    slotMap.put("startTime", currentSlotStart.format(formatter));
//                    slotMap.put("endTime", currentSlotEnd.format(formatter));
//                    halfHourSlots.add(slotMap);
//                    currentSlotStart = currentSlotEnd;
//                }


                //=============================================================================================================


                if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) &&
                        halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {
                    System.out.println(halfHourSlots.get(k).get("startTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + " and " +
                            halfHourSlots.get(k).get("endTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"));
                    // Update ConfirmpoDueDate1

                    //     String poDueDateIteration = ConfirmpoDueDate1;
//                        LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
//                        LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
//                        String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
//                        DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
//                        String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
                    for (cl = cl; cl < calenderList.size(); ) {
                        ConfirmpoDueDate1 = calenderList.get(cl);
                        // Reset currentSlotStart and k
                        currentSlotStart = startTime;
                        k = 0;
                        cl++;
                        break;
                    }


                }
                for (int c2 = 0; c2 < unAvailableSlotstartAndEndTimeMApList.size(); c2++) {
                    String unAvailablestartTime = unAvailableSlotstartAndEndTimeMApList.get(c2).get("startTime");
                    String unAvailableendTime = unAvailableSlotstartAndEndTimeMApList.get(c2).get("endTime");
                    LocalTime unAvailableStartTimeLocal = LocalTime.parse(unAvailablestartTime);
                    LocalTime unAvailableendTimeLocal = LocalTime.parse(unAvailableendTime);
                    for (int c3 = 0; c3 < halfHourSlots.size() - 1; c3++) {
                        String halfHourSlotsstartTime = halfHourSlots.get(c3).get("startTime");
                        String halfHourSlotsendTime = halfHourSlots.get(c3).get("endTime");
                        LocalTime halfHourSlotsstartTimeLocal = LocalTime.parse(halfHourSlotsstartTime);
                        LocalTime halfHourSlotsendTimeLocal = LocalTime.parse(halfHourSlotsendTime);
                        if (halfHourSlotsstartTime.equals(unAvailablestartTime)) {
                            System.out.println("================================================>");
                            System.out.println("inside the if()");
                            halfHourSlots.remove(halfHourSlots.get(c3));
                            System.out.println("inside remove");
                            break;
                        }
                    }
                }
                System.out.println("available times ===========================================> ");
                System.out.println(halfHourSlots);
            } catch (NullPointerException e) {
                if (unloadingRate <= 30) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else if (unloadingRate > 30 && unloadingRate <= 60) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 60);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else if (unloadingRate > 60 && unloadingRate <= 90) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 90);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else if (unloadingRate > 90 && unloadingRate <= 120) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 120);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else if (unloadingRate > 120 && unloadingRate <= 150) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 150);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else if (unloadingRate > 150 && unloadingRate <= 180) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 180);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else if (unloadingRate > 180 && unloadingRate <= 210) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 210);
                    System.out.println(iterations + "=========================>endTimec120Local");
                } else if (unloadingRate > 210 && unloadingRate <= 240) {
                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 240);
                    currentSlotStart = startTime;
                    System.out.println(iterations + "=========================>startTime");
                } else {
                    System.out.println("************Time slot not avaialble**********************************");
                }

                List<Integer> rateDurationList = new ArrayList<>();
                rateDurationList.add(30);
                rateDurationList.add(60);
                rateDurationList.add(90);
                rateDurationList.add(120);
                rateDurationList.add(150);
                rateDurationList.add(180);
                rateDurationList.add(210);
                rateDurationList.add(240);


                for (int c1 = 0; c1 <= iterations - 2; c1++) {
                    int duration = 0;
                    for (int c2 = 0; c2 < rateDurationList.size(); c2++) {
                        if (unloadingRate == rateDurationList.get(c2)) {
                            duration = rateDurationList.get(c2);
                            break;
                        }
                    }

                    LocalTime currentSlotEnd = currentSlotStart.plusMinutes(duration);
                    Map<String, String> slotMap = new HashMap<>();
                    slotMap.put("startTime", currentSlotStart.format(formatter));
                    slotMap.put("endTime", currentSlotEnd.format(formatter));
                    halfHourSlots.add(slotMap);
                    currentSlotStart = currentSlotEnd;

                }
                //  int cl = 1;
                try {

                    if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) &&
                            halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {
                        System.out.println(halfHourSlots.get(k).get("startTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + " and " +
                                halfHourSlots.get(k).get("endTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"));
//                            String poDueDateIteration = ConfirmpoDueDate1;
//                            LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
//                            LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
//                            String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
//                            DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
//                            String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
                        for (cl = cl; cl < calenderList.size(); ) {
                            ConfirmpoDueDate1 = calenderList.get(cl);
                            // Reset currentSlotStart and k
                            currentSlotStart = startTime;
                            k = 0;

                            cl++;
                            break;
                        }
                    }
                } catch (IndexOutOfBoundsException ie12) {
                    ie12.printStackTrace();
                    System.out.println("IndexOutOfBoundsException occurred: " + ie12.getMessage());
                }
            }
            List<Map<String, String>> vehicleData = dataTable.asMaps();
            String truckType = vehicleData.get(j).get("truckType");
            String truckNumber = vehicleData.get(j).get("truckNumber");
            String driverName = vehicleData.get(j).get("driverName");
            String driverNumber = vehicleData.get(j).get("driverNumber");
            String dstSiteId = vehicleData.get(j).get("dstSiteId");
            VehicleMap.put("truckType", truckType);
            VehicleMap.put("truckNumber", truckNumber);
            driverDetailsMap.put("driverName", driverName);
            driverDetailsMap.put("driverNumber", driverNumber);
            VehicleMap.put("driverDetails", driverDetailsMap);
            ConfirmAppointmentMap.put("vehicle", VehicleMap);
            ConfirmAppointmentMap.put("isPalletized", false);
            ConfirmAppointmentMap.put("appnDate", ConfirmpoDueDate1);
            try {
                ConfirmAppointmentMap.put("appnStartTime", halfHourSlots.get(k).get("startTime"));
                ConfirmAppointmentMap.put("appnEndTime", halfHourSlots.get(k).get("endTime"));
                if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) && halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {
                    System.out.println(halfHourSlots.get(k).get("startTime") + "=halfHourSlots.get(k).get(\"startTime\")" + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + "halfHourSlots.get(halfHourSlots.size() - 1)");
                    System.out.println("clear");
                    halfHourSlots.clear();
                    halfHourSlots = new ArrayList<>();
                }
            } catch (IndexOutOfBoundsException ie14) {
                ie14.printStackTrace();
                System.out.println("IndexOutOfBoundsException occurred: " + ie14.getMessage());
            }
            ConfirmAppointmentMap.put("dstSiteId", dstSiteId);
            List<LinkedHashMap<String, Object>> ItemsList1 = new ArrayList<>();
            for (int c5 = 0; c5 < ponum.size(); c5++) {
                LinkedHashMap<String, Object> ItemsObjct1 = new LinkedHashMap<>();

                //==================================================================================
//                String poItemNumber = (String) ponum.get(c5).get("poItemNumber");
//                int qty = (int) ponum.get(c5).get("qty");
//                //    qty= qty-24;
//                int caselot = (int) ponum.get(c5).get("caselot");
//                int boxes = qty / caselot;
//                double weight = (double) ponum.get(c5).get("weight");
//                double volume = (double) ponum.get(c5).get("volume");

                //===============================================


                String poItemNumber = null;
                double weight = 0;
                double volume = 0;
                Integer qty = null;
                int boxes = 0;
                for (int m1 = 0; m1 < GetpoItemNumberList1.size(); m1++) {
                    //     System.out.println(GetpoItemNumberList1.get(m1)+"============>GetpoItemNumberList1.get(m1)");
                    //    System.out.println(ponum.get(i).get("poItemNumber")+"=================>ponum.get(i).get(\"poItemNumber\")");
                    if (GetpoItemNumberList1.get(m1).equals(ponum.get(c5).get("poItemNumber"))) {
                        poItemNumber = (String) GetpoItemNumberList1.get(m1);
                        PoItemNumberList.add(poItemNumber);

                        weight = (double) ponum.get(c5).get("weight");

                        volume = (double) ponum.get(c5).get("volume");
                        int caselot = (int) ponum.get(c5).get("caselot");
                        qty = (Integer) ponum.get(c5).get("qty");
                        boxes = qty / caselot;


                    }
                }

                //=========================================================================
                weight = qty * weight / 1000;
                volume = qty * volume;

                ItemsObjct1.put("poItemNumber", poItemNumber);
                ItemsObjct1.put("qty", qty);
                ItemsObjct1.put("weight", weight);
                ItemsObjct1.put("volume", volume);
                ItemsObjct1.put("boxes", boxes);
                ItemsList1.add(ItemsObjct1);

            }
            LinkedHashMap<String, Object> Poobject1 = new LinkedHashMap<>();
            Poobject1.put("poNumber", getslotsPonumber);
            Poobject1.put("items", ItemsList1);
            PoList1.add(Poobject1);

            ConfirmAppointmentMap.put("po", PoList1);

            CreateAppointmentResponse = RequestGenerator.getRequestWithheader(Headers, ConfirmAppointmentMap).log().all().when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CreateAppointment"));
            CreateAppointmentResponse.then().log().all();

            String asnNumberResponse = CreateAppointmentResponse.jsonPath().get("asnNumber").toString();
            String appointmentId = CreateAppointmentResponse.jsonPath().get("appointmentId").toString();
            asnNumberIdList.add(asnNumberResponse + "==>asnNumber " + appointmentId + "===>appointmentId " + j);

            //  System.out.println(CreateAppointmentResponse.prettyPrint());
            ExtentReportManager.logInfoDetails("Response is - ");
            ExtentReportManager.logJson(CreateAppointmentResponse.prettyPrint());
            ExtentReportManager.logInfoDetails("Response status code is " + CreateAppointmentResponse.getStatusCode());
            long responseTime = CreateAppointmentResponse.getTimeIn(TimeUnit.MILLISECONDS);
            ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");

            //  Thread.sleep(500);

            j++;
            k++;
//call the GRN method to create GRN for particular appontment
            CreateGrnStep createGrnStep = new CreateGrnStep();
            createGrnStep.afterAppontmentScheduledCreateGrnForParticularPoNumberForNumberOfIterations();

            if (j == PoNumbersList.size()) {
                break;
            }

            Thread.sleep(1000);
        }

        for (int q = 0; q < asnNumberIdList.size(); q++) {
            System.out.println(asnNumberIdList.get(q) + "============>asnNumber + ==> appointmentId" + q);
            ExtentReportManager.logInfoDetails("asnNumberIdList is " + asnNumberIdList.get(q) + "============>asnNumber + ==> appointmentId" + q);
            System.out.println(grnList.get(q) + "=================>grn number" + q);
            ExtentReportManager.logInfoDetails("grnNumberIdList is " + grnList.get(q) + "============>grnnumber" + q);
        }
        System.out.println(asnNumberIdList.size() + "==========>asnNumberIdList.size");
        ExtentReportManager.logInfoDetails(asnNumberIdList.size() + "==========>asnNumberIdList.size");
        System.out.println(grnList.size() + "==========>grnList.size");
        ExtentReportManager.logInfoDetails(grnList.size() + "==========>grnList.size");

    }
//*******************************************************************************************************************************


 */
}


//=========================================================================================================



































/*
//=======================================================================

date time format
//==================
"   LocalDateTime currentDateTime = LocalDateTime.now();
//ymd  hms
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern(""yyyy-MM-dd HH:mm:ss"");


DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(""yyyy-MM-dd"");
        String formattedDate = currentDateTime.format(dateFormatter);

        // Format the time part (HH:mm:ss)
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(""HH:mm:ss"");
        String formattedTime = currentDateTime.format(timeFormatter);
//[print  tommorroe=w date
 LocalDateTime nextDayDateTime = currentDateTime.plusDays(1);"

 //===============================================================================================


   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
LocalTime currentSlotEnd = currentSlotStart.plusMinutes(30);
 String endTimeString = currentSlotEnd.format(formatter);
  LocalTime endTime = LocalTime.parse(endTimeString, formatter);
//================================

 public static long getRandomPONumber() {
        Random random = new Random();
        String num = "55" + (random.nextInt(1000000000));
        return Long.parseLong(num);
    }


 //===============================================================================
   for (int c1 = 0; c1 <= iterations - 2; c1++) {

                    if (unloadingRate <= 30) {
                        LocalTime currentSlotEnd = currentSlotStart.plusMinutes(30);
                        Map<String, String> slotMap = new HashMap<>();
                        slotMap.put("startTime", currentSlotStart.format(formatter));
                        slotMap.put("endTime", currentSlotEnd.format(formatter));
                        halfHourSlots.add(slotMap);
                        currentSlotStart = currentSlotStart.plusMinutes(30);
                    } else if (unloadingRate > 30 && unloadingRate <= 60) {
                        LocalTime currentSlotEnd = currentSlotStart.plusMinutes(60);
                        Map<String, String> slotMap = new HashMap<>();
                        slotMap.put("startTime", currentSlotStart.format(formatter));
                        slotMap.put("endTime", currentSlotEnd.format(formatter));
                        halfHourSlots.add(slotMap);
                        currentSlotStart = currentSlotStart.plusMinutes(60);
                    }
                    else if (unloadingRate > 60 && unloadingRate <= 90) {
                        LocalTime currentSlotEnd = currentSlotStart.plusMinutes(90);
                        Map<String, String> slotMap = new HashMap<>();
                        slotMap.put("startTime", currentSlotStart.format(formatter));
                        slotMap.put("endTime", currentSlotEnd.format(formatter));
                        halfHourSlots.add(slotMap);
                        currentSlotStart = currentSlotStart.plusMinutes(90);
                    }
                    else if (unloadingRate > 90 && unloadingRate <= 120) {
                        LocalTime currentSlotEnd = currentSlotStart.plusMinutes(120);
                        Map<String, String> slotMap = new HashMap<>();
                        slotMap.put("startTime", currentSlotStart.format(formatter));
                        slotMap.put("endTime", currentSlotEnd.format(formatter));
                        halfHourSlots.add(slotMap);
                        currentSlotStart = currentSlotStart.plusMinutes(120);
                    }
                    else if (unloadingRate > 120 && unloadingRate <= 150) {
                        LocalTime currentSlotEnd = currentSlotStart.plusMinutes(150);
                        Map<String, String> slotMap = new HashMap<>();
                        slotMap.put("startTime", currentSlotStart.format(formatter));
                        slotMap.put("endTime", currentSlotEnd.format(formatter));
                        halfHourSlots.add(slotMap);
                        currentSlotStart = currentSlotStart.plusMinutes(150);
                    }
                    else if (unloadingRate > 150 && unloadingRate <= 180) {
                        LocalTime currentSlotEnd = currentSlotStart.plusMinutes(180);
                        Map<String, String> slotMap = new HashMap<>();
                        slotMap.put("startTime", currentSlotStart.format(formatter));
                        slotMap.put("endTime", currentSlotEnd.format(formatter));
                        halfHourSlots.add(slotMap);
                        currentSlotStart = currentSlotStart.plusMinutes(180);
                    }
                }
   if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) && halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {

                        System.out.println(halfHourSlots.get(k).get("startTime") + "=halfHourSlots.get(k).get(\"startTime\")" + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + "halfHourSlots.get(halfHourSlots.size() - 1)");

                        if (unloadingRate <= 30) {
                            iterations = 0;

                            iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
                            String poDueDateIteration = ConfirmpoDueDate1;
                            LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
                            LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
                            String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
                            DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
                            String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
                            ConfirmpoDueDate1 = poDueDateIterationString24;
                            currentSlotStart = startTime;
                            k = 0;
                        }
                        if (unloadingRate > 30 && unloadingRate < 1000) {
                            iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 60);
                            String poDueDateIteration = ConfirmpoDueDate1;
                            LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
                            LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
                            String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
                            DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
                            String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
                            ConfirmpoDueDate1 = poDueDateIterationString24;
                            currentSlotStart = startTime;
                            k = 0;


                        }


                    }

 */



 