package com.dmartLabs.stepdefinitions;


import com.dmartLabs.commonutils.ExtentReportManager;
import com.dmartLabs.commonutils.GenricUtils;
import com.dmartLabs.commonutils.RequestGenerator;
import com.dmartLabs.config.ConStants;
import com.dmartLabs.config.PropertyReader;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.bs.I;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.dmartLabs.stepdefinitions.AuthorizationStep.Auth;
import static com.dmartLabs.stepdefinitions.AuthorizationStep.Bearertoken;
import static com.dmartLabs.stepdefinitions.CreateGrnStep.grnList;
import static com.dmartLabs.stepdefinitions.CreateMultiplePurchaseOrderStep_HK.PoNumbersList;
//import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.Bearertoken;
//import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.ponumbersbody;


public class UpdatedGetSlotsConfirmApponmentStep {

    private static String ConfirmpoDueDate1;
    private static List<LinkedHashMap<String, Object>> ponum;
    private static String getslotsPonumber;
    private static LinkedHashMap<String, List<LinkedHashMap<String, Object>>> ponumbersbody = CommonUtilities.getPonumbersBody();
    private static int cl = 1;
    private static int j;
    private static int k;
    static List<String> calenderList = CommonUtilities.getcalenderResponseList();
    RequestGenerator requestGenerator = new RequestGenerator();
    private static Response GetSlotsResponse;
    private Response CreateAppointmentResponse;
    private static String ConfirmpoDueDate;
    private static String getslotpoDueDate;
    private static Boolean isPalletizedBollean = null;
    private static List<String> PoItemNumberList = new ArrayList<>();
    private static List<LinkedHashMap<String, Object>> ItemsList = new ArrayList<>();
    private static LinkedHashMap<String, Object> poObject = new LinkedHashMap<>();
    private static List<LinkedHashMap<String, Object>> PoList = new ArrayList<>();
    private static LinkedHashMap<String, Object> GetSlotmainObject = new LinkedHashMap<>();
    private static LinkedHashMap<String, String> Headers = new LinkedHashMap<>();
    private static List<Map<String, String>> halfHourSlots = new ArrayList<>();
    private static LocalTime currentSlotStart = null;
    private static List<LinkedHashMap<String, String>> unAvailableSlotstartAndEndTimeMApList = new ArrayList<>();
    private static Integer unloadingRate;
    private static List<Integer> unavialbilitygenerateTimeSlots;
    private static List<Integer> avialbilitygenerateTimeSlots;
    private static int start;
    private static int end;
    private static int step;
    private static LinkedHashMap<String, Object> ConfirmAppointmentMap = new LinkedHashMap<>();
    private static LinkedHashMap<String, Object> VehicleMap = new LinkedHashMap<>();
    private static List<LinkedHashMap<String, Object>> PoList1 = new ArrayList<>();
    private static LinkedHashMap<String, Object> driverDetailsMap = new LinkedHashMap<>();
    private static List<String> asnNumberIdList = new ArrayList<>();

    @And("Updated using calender date whenever vendor created purchase order,Dc should give availability time,and confirm appointment and create a GRN and Validate")


    public void updatedUsingCalenderDateWheneverVendorCreatedPurchaseOrderDcShouldGiveAvailabilityTimeAndConfirmAppointmentAndCreateAGRNAndValidate(DataTable dataTable) throws InterruptedException {

        List<Map<String, String>> palletDT = new ArrayList<>();
        for (j = 0; j < PoNumbersList.size(); ) {

            LinkedHashMap<String, Object> GetSlotmainObject = new LinkedHashMap<>();
            LinkedHashMap<String, String> Headers = new LinkedHashMap<>();

            Headers.put("Authorization", Auth);
            Headers.put("requestid", GenricUtils.getRandomDeliveryNumber());

            List<LinkedHashMap<String, Object>> PoList1 = new ArrayList<>();
            LinkedHashMap<String, Object> poObject = new LinkedHashMap<>();
            ponum = ponumbersbody.get(PoNumbersList.get(j));

            List<LinkedHashMap<String, Object>> ItemsList = new ArrayList<>();
            List<String> PoItemNumberList = new ArrayList<>();
            LinkedHashMap<String, Object> ConfirmAppointmentMap = new LinkedHashMap<>();
            getslotsPonumber = PoNumbersList.get(j);
            //call item object method
            poobjectMethod(j, palletDT);
            //send get slot request
            SendgetslotsRequest();


            //=============================================================
            //if unavailble timeslot prsent compare and generate time slot


//            int startTime = 30;
//            int endTime = 720;
//            int step = 30;
//            int unloadingRate = 150; // Example unloadingRate
//
//            List<Integer> timeSlots = generateTimeSlots(startTime, endTime, step);

            //   int iterations = calculateIterations(unloadingRate, timeSlots);

            //   System.out.println("Unloading Rate: " + unloadingRate);
            // System.out.println("Iterations: " + iterations);
//===================================================================================
            // generateTimeSlots(start,end,step);
            calculateIterations(unloadingRate, unavialbilitygenerateTimeSlots, start, end, step, k);
//            calculateIterations(unloadingRate, avialbilitygenerateTimeSlots, start, end, step, k);
            sendCreaateAppointmentRequest(CreateAppointmentResponse);


            j++;
            k++;
//call the GRN method to create GRN for particular appontment
            CreateGrnStep createGrnStep = new CreateGrnStep();
            createGrnStep.afterAppontmentScheduledCreateGrnForParticularPoNumberForNumberOfIterations();

            if (j == PoNumbersList.size()) {
                break;
            }

            Thread.sleep(1000);
            pritntAlltheData(CreateAppointmentResponse, j);

        }
    }

    //    List<Map<String, String>> palletDT = dataTable.asMaps();
    public static LinkedHashMap<String, Object> poobjectMethod(int j, List<Map<String, String>> palletDT) {

        LinkedHashMap<String, Object> ItemsObject = new LinkedHashMap<>();
        for (int i = 0; i < ponum.size(); i++) {
            String poItemNumber = null;
            double weight = 0;
            double volume = 0;
            Integer qty = 0;

            String siteId = (String) ponum.get(i).get("siteId");
            poItemNumber = (String) ponum.get(i).get("poItemNumber");
            PoItemNumberList.add(poItemNumber);
            weight = (double) ponum.get(i).get("weight");
            volume = (double) ponum.get(i).get("volume");
            qty = (Integer) ponum.get(i).get("qty");
            weight = qty * weight / 1000;
            volume = qty * volume;
            Integer caselot = (Integer) ponum.get(i).get("caselot");
            int totalBoxes = qty / caselot;

            ItemsObject.put("poItemNumber", poItemNumber);
            ItemsObject.put("qty", qty);
            ItemsObject.put("weight", weight);
            ItemsObject.put("volume", volume);
            ItemsObject.put("boxes", totalBoxes);
            // "isPalletized": false

            isPalletizedBollean = Boolean.valueOf(palletDT.get(j).get("isPalletized"));

            ItemsObject.put("isPalletized", isPalletizedBollean);

            ItemsList.add(ItemsObject);
            if (ConfirmpoDueDate1 != null) {
                ConfirmpoDueDate1 = ConfirmpoDueDate1;
            } else {
                getslotpoDueDate = calenderList.get(0);

                ConfirmpoDueDate1 = getslotpoDueDate;
            }
        }
        return ItemsObject;
    }

    public static Response SendgetslotsRequest() throws InterruptedException {

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
        return GetSlotsResponse;

    }

    public static List<Integer> unavialbilitygenerateTimeSlots(int start, int end, int step) {
        List<Integer> timeSlots = new ArrayList<>();

        for (int time = start; time <= end; time += step) {
            timeSlots.add(time);
        }

        return timeSlots;
    }

    public static List<Integer> avialbilitygenerateTimeSlots(int start, int end, int step) {
        List<Integer> timeSlots = new ArrayList<>();

        for (int time = start; time <= end; time += step) {
            timeSlots.add(time);
        }

        return timeSlots;
    }

    static String workingHoursstartTime;
    static String workingHoursendTime;

    public static void calculateIterations(Integer unloadingRate, List<Integer> unavialbilitygenerateTimeSlots, int k, int start, int end, int step) {

        int iterations = 0;
        LocalTime currentSlotStart = null;


        Response ConfirmGetslotResponse = CommonUtilities.getResponseInstance();
        LinkedHashMap<String, Object> GetslotResponse1 = ConfirmGetslotResponse.as(new TypeRef<LinkedHashMap<String, Object>>() {
        });

        workingHoursstartTime = ConfirmGetslotResponse.jsonPath().get("workingHours.startTime");//6.00
        workingHoursendTime = ConfirmGetslotResponse.jsonPath().get("workingHours.endTime");//18.00
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

            if (!unavialbilitygenerateTimeSlots.isEmpty()) {
                for (int i = 0; i < unavialbilitygenerateTimeSlots.size(); i++) {
                    int timeSlot = unavialbilitygenerateTimeSlots.get(i);

                    if (unloadingRate > timeSlot && (i + 1 < unavialbilitygenerateTimeSlots.size() && unloadingRate <= unavialbilitygenerateTimeSlots.get(i + 1))) {
                        iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / timeSlot);
                        System.out.println(iterations + "=========================>endTimec" + timeSlot + "Local");
                        break;
                    }
                }
            } else {
                for (int i = 0; i < avialbilitygenerateTimeSlots.size(); i++) {
                    int timeSlot = avialbilitygenerateTimeSlots.get(i);

                    if (unloadingRate > timeSlot && (i + 1 < avialbilitygenerateTimeSlots.size() && unloadingRate <= avialbilitygenerateTimeSlots.get(i + 1))) {
                        iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / timeSlot);
                        System.out.println(iterations + "=========================>endTimec" + timeSlot + "Local");
                        break;
                    }
                }
            }

            generateHalfanHourTimeSlots(iterations, k);
//           return  iterations;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            calculateIterations(unloadingRate, avialbilitygenerateTimeSlots, k, start, end, step);
        }


    }

    public static List<Map<String, String>> generateHalfanHourTimeSlots(int iterations, int k) {
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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            slotMap.put("startTime", currentSlotStart.format(formatter));
            slotMap.put("endTime", currentSlotEnd.format(formatter));
            halfHourSlots.add(slotMap);
            currentSlotStart = currentSlotEnd;

        }
        LocalTime startTime = LocalTime.parse(workingHoursstartTime);
        LocalTime endTime = LocalTime.parse(workingHoursendTime);
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
        return halfHourSlots;
    }

    public static LinkedHashMap<String, Object> createAppointmentData(List<Map<String, String>> vehicleData, int j, int k) {
        // List<Map<String, String>> vehicleData = dataTable.asMaps();
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
        return ConfirmAppointmentMap;

    }

    public static Response sendCreaateAppointmentRequest(Response CreateAppointmentResponse) {


        CreateAppointmentResponse = RequestGenerator.getRequestWithheader(Headers, ConfirmAppointmentMap).log().all().when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CreateAppointment"));
        CreateAppointmentResponse.then().log().all();
        CommonUtilities.setResponseInstance(CreateAppointmentResponse);

        return CreateAppointmentResponse;
    }

    public static void pritntAlltheData(Response CreateAppointmentResponse, int j) {
        String asnNumberResponse = CreateAppointmentResponse.jsonPath().get("asnNumber").toString();
        String appointmentId = CreateAppointmentResponse.jsonPath().get("appointmentId").toString();
        asnNumberIdList.add(asnNumberResponse + "==>asnNumber " + appointmentId + "===>appointmentId " + j);

        ExtentReportManager.logInfoDetails("Response is - ");
        ExtentReportManager.logJson(CreateAppointmentResponse.prettyPrint());
        ExtentReportManager.logInfoDetails("Response status code is " + CreateAppointmentResponse.getStatusCode());
        long responseTime = CreateAppointmentResponse.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");

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
}



