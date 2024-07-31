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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.dmartLabs.stepdefinitions.AuthorizationStep.Auth;
import static com.dmartLabs.stepdefinitions.CreateGrnStep.ReturnGRNResponse;
import static com.dmartLabs.stepdefinitions.CreateGrnStep.grnList;
import static com.dmartLabs.stepdefinitions.CreateMultiplePurchaseOrderStep_HK.PoNumbersList;


public class validateGetSlots2ConfirmApponmentStep {

    private static List<LinkedHashMap<String, Object>> ResponseItemsList = CommonUtilities.getResponseItemsList();
    private static LinkedHashMap<String, List<LinkedHashMap<String, Object>>> ponumbersbody = CommonUtilities.getPonumbersBody();
    private static List<String> calenderList = CommonUtilities.getcalenderResponseList();

    public static List<LinkedHashMap<String, Object>> ponum1;
    public static List<String> getslotsPonumber1 = new ArrayList<>();
    public static String getslotsPonumber;
    public static String ConfirmpoDueDate2=null;
    private static int cl;
    private static List<String> asnNumberIdList = new ArrayList<>();
    private static LinkedHashMap<String, Object> GetSlotmainObject = new LinkedHashMap<>();
    private static List<Map<String, String>> halfHourSlots;
    private static HashMap<String, String> Headers = new HashMap<>();

    private static String getslotpoDueDate;
    private static List<Map<String, String>> palletDT = new ArrayList<>();
    private static LocalTime startTime;
    private static LocalTime endTime;
    private static LocalTime currentSlotStart = null;
    private static DateTimeFormatter hoursformatter;
    private static Integer unloadingRate = 0;
    private static int iterations = 0;
    private static LinkedHashMap<String, Object> GrnObject = new LinkedHashMap<>();
    private static int j = 0;
    private static int k = 0;
    //  private static List<LinkedHashMap<String, Object>> PoList1 = new ArrayList<>();
    private static LinkedHashMap<String, Object> poObject = new LinkedHashMap<>();
    private static List<LinkedHashMap<String, Object>> PoList = new ArrayList<>();
    private static List<String> PoItemNumberList = new ArrayList<>();
    private static Boolean isPalletizedBollean = null;
    private static List<LinkedHashMap<String, Object>> ItemsList;
    //    private static List<LinkedHashMap<String, Object>> ItemsList1 = new ArrayList<>();
    private static RequestGenerator requestGenerator = new RequestGenerator();
    private static Response GetSlotsResponse;
    private static Response CreateAppointmentResponse;

    private static LinkedHashMap<String, Object> ConfirmAppointmentMap = new LinkedHashMap<>();
    private static LinkedHashMap<String, Object> VehicleMap = new LinkedHashMap<>();
    private static LinkedHashMap<String, Object> driverDetailsMap = new LinkedHashMap<>();
    private static Response ConfirmGetslotResponse;
    private static LinkedHashMap<String, Object> GetslotResponse1;
    private static String workingHoursstartTime;
    private static String workingHoursendTime;
    private static List<Map<String, Object>> unAvailableSlotList;
    private static List<LinkedHashMap<String, String>> unAvailableSlotstartAndEndTimeMApList = new ArrayList<>();

    private static List<LinkedHashMap<String, String>> CompareendTimec4list = null;
    private static LocalTime endTimec5Local = null;
    private static String dstSiteId;
    private static LinkedHashMap<String, Object> Poobject1 = new LinkedHashMap<>();
    private static LinkedHashMap<String, Object> ItemsObject;
    private static int h1 = 0;

    @When("call all the methods to execute the script")
    public void callAllTheMethodsToExecuteTheScript(DataTable dataTable) throws InterruptedException {
        palletDT = dataTable.asMaps();

        printAllPoNumbers();
        //   ReturnGetSlotPoMainObject(ponum1, palletDT, j,GetSlotmainObject);
    }

    public static void printAllPoNumbers() {
        for (int i = 0; i < PoNumbersList.size(); i++) {
            System.out.println(PoNumbersList.get(i) + "=====GetSlotsConfirmApponment" + i);
            ExtentReportManager.logInfoDetails("GetSlotsConfirmApponment is - ");
            ExtentReportManager.logInfoDetails("poNumbersList " + PoNumbersList.get(i) + "=====GetSlotsConfirmApponment" + i);
        }

        System.out.println(PoNumbersList.size() + "====================>PoNumbersList.size()");
    }

    public static HashMap<String, String> ReturnHeaders() {

        Headers.put("Authorization", Auth);
        Headers.put("requestid", GenricUtils.getRandomDeliveryNumber());
        return Headers;
    }

    public static LocalTime addUnavailabilitySlot(LocalTime currentSlotStart) {
        unAvailableSlotList = ConfirmGetslotResponse.jsonPath().get("unAvailableSlot");

        hoursformatter = DateTimeFormatter.ofPattern("HH:mm");
        startTime = LocalTime.parse(workingHoursstartTime);
        endTime = LocalTime.parse(workingHoursendTime);

        halfHourSlots = new ArrayList<>();
        try {
            for (int C = 0; C < unAvailableSlotList.size(); C++) {
                LinkedHashMap<String, String> unAvailableSlotstartAndEndTimeMAp = new LinkedHashMap<>();
                String unAvailableSlotstartTime = (String) unAvailableSlotList.get(C).get("startTime");
                String unAvailableSlotendTime = (String) unAvailableSlotList.get(C).get("endTime");
                unAvailableSlotstartAndEndTimeMAp.put("startTime", unAvailableSlotstartTime);
                unAvailableSlotstartAndEndTimeMAp.put("endTime", unAvailableSlotendTime);
                unAvailableSlotstartAndEndTimeMApList.add(unAvailableSlotstartAndEndTimeMAp);
            }


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

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return currentSlotStart;
    }

    public static LinkedHashMap<String, Object> ReturnGetSlotPoMainObject(List<LinkedHashMap<String, Object>> ponum1, List<Map<String, String>> palletDT, int j, LinkedHashMap<String, Object> GetSlotmainObject, LinkedHashMap<String, Object> ItemsObject, List<LinkedHashMap<String, Object>> PoList) throws InterruptedException {

        for (j = j; j < PoNumbersList.size(); ) {
            GetSlotmainObject = new LinkedHashMap<>();


            ponum1 = new ArrayList<>();

            ponum1 = ponumbersbody.get(PoNumbersList.get(j));
            getslotsPonumber = PoNumbersList.get(j);
            PoList = new ArrayList<>();

            System.out.println(PoList.size() + "========>PoList");
            System.out.println(ponum1.size() + "========>ponum1");
            ItemsList = new ArrayList<>();
            for (int i = 0; i < ponum1.size(); i++) {
                ItemsObject = new LinkedHashMap<>();
                String poItemNumber = null;
                double weight = 0;
                double volume = 0;
                Integer qty = 0;
                String siteId = (String) ponum1.get(i).get("siteId");

                poItemNumber = (String) ponum1.get(i).get("poItemNumber");
                PoItemNumberList.add(poItemNumber);


                weight = (double) ponum1.get(i).get("weight");
                volume = (double) ponum1.get(i).get("volume");
                qty = (Integer) ponum1.get(i).get("qty");
                weight = qty * weight / 1000;
                volume = qty * volume;
                Integer caselot = (Integer) ponum1.get(i).get("caselot");
                int totalBoxes = qty / caselot;
                ItemsObject.put("poItemNumber", poItemNumber);
                ItemsObject.put("qty", qty);
                ItemsObject.put("weight", weight);
                ItemsObject.put("volume", volume);
                ItemsObject.put("boxes", totalBoxes);
                isPalletizedBollean = Boolean.valueOf(palletDT.get(j).get("isPalletized"));
                ItemsObject.put("isPalletized", isPalletizedBollean);

                ItemsList.add(ItemsObject);
                if (ConfirmpoDueDate2 != null) {
                    ConfirmpoDueDate2 = ConfirmpoDueDate2;
                } else {
                    getslotpoDueDate = calenderList.get(h1);
                    ConfirmpoDueDate2 = getslotpoDueDate;
                    h1++;
                }
            }

            poObject.put("poNumber", getslotsPonumber);
            poObject.put("items", ItemsList);
            PoList.add(poObject);
            GetSlotmainObject.put("date", ConfirmpoDueDate2);
            String id = CommonUtilities.getSiteId();
            GetSlotmainObject.put("siteId", id);
            GetSlotmainObject.put("po", PoList);
            //    sendGetslotRequest(GetSlotmainObject);
            j++;
            break;
        }

        return GetSlotmainObject;

    }

    public static Response sendGetslotRequest(LinkedHashMap<String, Object> GetSlotmainObject) throws
            InterruptedException {
        GetSlotsResponse = RequestGenerator.getRequestWithheader(validateGetSlots2ConfirmApponmentStep.ReturnHeaders(), GetSlotmainObject).log().all().when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "GetSlotsEndPoint"));
        GetSlotsResponse.then().log().all();
        CommonUtilities.setResponseInstance(GetSlotsResponse);
        Thread.sleep(1000);
        return GetSlotsResponse;
    }

    public static List<Map<String, String>> GenerateTryTimeSlotsStartTime(int k) {
        processiterations();
        //   processSartTime();
        List<Integer> rateDurationList = Arrays.asList(30, 60, 90, 120, 150, 180, 210, 240, 270, 300, 330, 360, 390, 420, 450, 480, 510, 540, 570, 600, 630, 660, 690, 720);
        halfHourSlots.clear();
        for (int c1 = 0; c1 <= iterations - 1; c1++) {
            int duration = 0;
            for (int c2 = 0; c2 < rateDurationList.size(); c2++) {
                Integer rate = rateDurationList.get(c2);
                if (unloadingRate.equals(rate)) {
                    duration = rateDurationList.get(c2);
                    break;
                }
            }
            LocalTime currentSlotEnd = currentSlotStart.plusMinutes(duration);
            Map<String, String> slotMap = new HashMap<>();
            slotMap.put("startTime", currentSlotStart.format(hoursformatter));
            slotMap.put("endTime", currentSlotEnd.format(hoursformatter));
            halfHourSlots.add(slotMap);
            currentSlotStart = currentSlotEnd;
        }
        try {

            if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) &&
                    halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {
                System.out.println(halfHourSlots.get(k).get("startTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + " and " +
                        halfHourSlots.get(k).get("endTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"));
                for (cl = cl; cl < calenderList.size(); ) {
                    //    ConfirmpoDueDate2 = calenderList.get(cl);
                    ConfirmpoDueDate2 = calenderList.get(h1);
                    // Reset currentSlotStart and k
                    currentSlotStart = startTime;
                    k = 0;
                    h1++;
                    cl++;
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //  halfHourSlots.clear();
        return halfHourSlots;
    }

    public static void RemoveUnavailabilitySlots() {
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
                    //  System.out.println("inside the if()");
                    halfHourSlots.remove(halfHourSlots.get(c3));
                    System.out.println("inside remove");
                    break;
                }
            }
        }
        System.out.println("available times ===========================================> ");
        System.out.println(halfHourSlots);
    }

    public static LinkedHashMap<String, Object> ReturnConfirmAppointmentmap(DataTable dataTable) {
        List<Map<String, String>> vehicleData = dataTable.asMaps();
        String truckType = vehicleData.get(j).get("truckType");
        String truckNumber = vehicleData.get(j).get("truckNumber");
        String driverName = vehicleData.get(j).get("driverName");
        String driverNumber = vehicleData.get(j).get("driverNumber");
        dstSiteId = vehicleData.get(j).get("dstSiteId");
        VehicleMap.put("truckType", truckType);
        VehicleMap.put("truckNumber", truckNumber);
        driverDetailsMap.put("driverName", driverName);
        driverDetailsMap.put("driverNumber", driverNumber);
        VehicleMap.put("driverDetails", driverDetailsMap);
        ConfirmAppointmentMap.put("vehicle", VehicleMap);

        ConfirmAppointmentMap.put("isPalletized", isPalletizedBollean);
        ConfirmAppointmentMap.put("appnDate", ConfirmpoDueDate2);
        return ConfirmAppointmentMap;

    }

    public static LinkedHashMap<String, Object> ReturnPoObject1(LinkedHashMap<String, Object> Poobject1) {

        ItemsList = new ArrayList<>();
        for (int c5 = 0; c5 < ponum1.size(); c5++) {
            LinkedHashMap<String, Object> ItemsObjct1 = new LinkedHashMap<>();

            //==================================================================================
            String poItemNumber = (String) ponum1.get(c5).get("poItemNumber");
            int qty = (int) ponum1.get(c5).get("qty");
            //    qty= qty-24;
            int caselot = (int) ponum1.get(c5).get("caselot");
            int boxes = qty / caselot;
            double weight = (double) ponum1.get(c5).get("weight");
            double volume = (double) ponum1.get(c5).get("volume");

            weight = qty * weight / 1000;
            volume = qty * volume;

            ItemsObjct1.put("poItemNumber", poItemNumber);
            ItemsObjct1.put("qty", qty);
            ItemsObjct1.put("weight", weight);
            ItemsObjct1.put("volume", volume);
            ItemsObjct1.put("boxes", boxes);
            ItemsObjct1.put("isPalletized", isPalletizedBollean);
            ItemsList.add(ItemsObjct1);

        }
        //  Poobject1 = new LinkedHashMap<>();
        Poobject1.put("poNumber", getslotsPonumber);
        Poobject1.put("items", ItemsList);
        return Poobject1;
    }

    public static Response SendConfirmAppointmentRequest(LinkedHashMap<String, Object> ConfirmAppointmentMap) {

        CreateAppointmentResponse = RequestGenerator.getRequestWithheader(validateGetSlots2ConfirmApponmentStep.ReturnHeaders(), ConfirmAppointmentMap).log().all().when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CreateAppointment"));
        CreateAppointmentResponse.then().log().all();
        CommonUtilities.setResponseInstance(CreateAppointmentResponse);
        return CreateAppointmentResponse;
    }


    public static void GetJsonFromConfirmAppointmnetResponse(Response CreateAppointmentResponse) {
        try {
            String asnNumberResponse = CreateAppointmentResponse.jsonPath().get("asnNumber").toString();
            String appointmentId = CreateAppointmentResponse.jsonPath().get("appointmentId").toString();
            asnNumberIdList.add(asnNumberResponse + "==>asnNumber " + appointmentId + "===>appointmentId " + j);
        } catch (NullPointerException ne1) {

            ConfirmAppointmentMap.put("appnStartTime", halfHourSlots.get(k++).get("startTime"));
            ConfirmAppointmentMap.put("appnEndTime", halfHourSlots.get(k).get("endTime"));

            ConfirmAppointmentMap.put("po", PoList);

            CreateAppointmentResponse = RequestGenerator.getRequestWithheader(validateGetSlots2ConfirmApponmentStep.ReturnHeaders(), ConfirmAppointmentMap).log().all().when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CreateAppointment"));
            CreateAppointmentResponse.then().log().all();
            CommonUtilities.setResponseInstance(CreateAppointmentResponse);

            String asnNumberResponse = CreateAppointmentResponse.jsonPath().get("asnNumber").toString();
            String appointmentId = CreateAppointmentResponse.jsonPath().get("appointmentId").toString();
            asnNumberIdList.add(asnNumberResponse + "==>asnNumber " + appointmentId + "===>appointmentId " + j);

        }
        ExtentReportManager.logInfoDetails("Response is - ");
        ExtentReportManager.logJson(CreateAppointmentResponse.prettyPrint());
        ExtentReportManager.logInfoDetails("Response status code is " + CreateAppointmentResponse.getStatusCode());
        long responseTime = CreateAppointmentResponse.getTimeIn(TimeUnit.MILLISECONDS);
        ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");
    }

    public static void printGrnNumbers() {


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


    @And("using calender date whenever vendor created purchase order,Dc should give availability time,and confirm appointment and create a GRN and Validate")
    public void usingCalenderDateWheneverVendorCreatedPurchaseOrderDcShouldGiveAvailabilityTimeAndConfirmAppointmentAndCreateAGRNAndValidate(DataTable dataTable) throws InterruptedException {
        palletDT = dataTable.asMaps();


        for (j = j; j < PoNumbersList.size(); ) {

            Poobject1 = new LinkedHashMap<>();
            PoList = new ArrayList<>();

            ponum1 = ponumbersbody.get(PoNumbersList.get(j));
            getslotsPonumber1.add(PoNumbersList.get(j));
            getslotsPonumber = PoNumbersList.get(j);
            GetSlotmainObject = ReturnGetSlotPoMainObject(ponum1, palletDT, j, GetSlotmainObject, ItemsObject, PoList);

            sendGetslotRequest(GetSlotmainObject);

            //confirmappontment

            ConfirmGetslotResponse = CommonUtilities.getResponseInstance();
            GetslotResponse1 = ConfirmGetslotResponse.as(new TypeRef<LinkedHashMap<String, Object>>() {
            });

            workingHoursstartTime = ConfirmGetslotResponse.jsonPath().get("workingHours.startTime");//6.00
            workingHoursendTime = ConfirmGetslotResponse.jsonPath().get("workingHours.endTime");//18.00
            if (unloadingRate != null) {
                unloadingRate = ConfirmGetslotResponse.jsonPath().get("unloadingRate");
            } else {
                System.out.println("***********po number not found+ articles qty mismatch*************************");
            }
            try {
                addUnavailabilitySlot(currentSlotStart);
                GenerateTryTimeSlotsStartTime(k);
                RemoveUnavailabilitySlots();
            } catch (NullPointerException e) {

                GenerateTimeSlotsStartTime(k);


            }

            ReturnConfirmAppointmentmap(dataTable);
//========================================================================================================================================================
            slotIsAvailbleForParticularTimeSlotOrNot(palletDT, j, halfHourSlots, GetSlotmainObject);

            String appnStartTime = (String) GetSlotmainObject.get("appnStartTime");
            String appnEndTime = (String) GetSlotmainObject.get("appnEndTime");

            ConfirmAppointmentMap.put("appnStartTime", appnStartTime);
            ConfirmAppointmentMap.put("appnEndTime", appnEndTime);

            ConfirmAppointmentMap.put("dstSiteId", dstSiteId);
//=========================================================================
            ReturnPoObject1(Poobject1);
            PoList.add(Poobject1);

            ConfirmAppointmentMap.put("po", PoList);
            //========================================================

            SendConfirmAppointmentRequest(ConfirmAppointmentMap);

            GetJsonFromConfirmAppointmnetResponse(CreateAppointmentResponse);

            ReturnGRNResponse(GrnObject, j);


            j++;
            k++;

            if (j == PoNumbersList.size()) {
                break;
            }
            Thread.sleep(1000);

            //   printGrnNumbers();
        }
        printGrnNumbers();
    }

    //=================================================================================================================================
//*********************************************************************************************************
    private static boolean falseValue;
    private static Response GetSlotsValidateResponse;
    private static String booleanValueResponse;

    public static HashMap<String, String> returnHeaders() {
        Headers.put("Authorization", Auth);
        Headers.put("requestid", GenricUtils.getRandomDeliveryNumber());
        return Headers;
    }

    public static LinkedHashMap<String, Object> ReturnPoMainObject(List<Map<String, String>> palletDT,
                                                                   int j, LinkedHashMap<String, Object> GetSlotmainObject) {
        // List<LinkedHashMap<String, Object>> PoList1 = new ArrayList<>();
        LinkedHashMap<String, Object> poObject = new LinkedHashMap<>();
        List<LinkedHashMap<String, Object>> PoList = new ArrayList<>();
        ponum1 = ponumbersbody.get(PoNumbersList.get(j));
        List<LinkedHashMap<String, Object>> ItemsList = new ArrayList<>();
        List<String> PoItemNumberList = new ArrayList<>();
        LinkedHashMap<String, Object> ConfirmAppointmentMap = new LinkedHashMap<>();
        getslotsPonumber = PoNumbersList.get(j);
        Boolean isPalletizedBollean = null;
        for (int i = 0; i < ponum1.size(); i++) {
            LinkedHashMap<String, Object> ItemsObject = new LinkedHashMap<>();
            String poItemNumber = null;
            double weight = 0;
            double volume = 0;
            Integer qty = 0;
            String siteId = (String) ponum1.get(i).get("siteId");
            poItemNumber = (String) ponum1.get(i).get("poItemNumber");
            PoItemNumberList.add(poItemNumber);
            weight = (double) ponum1.get(i).get("weight");
            volume = (double) ponum1.get(i).get("volume");
            qty = (Integer) ponum1.get(i).get("qty");
            weight = qty * weight / 1000;
            volume = qty * volume;
            Integer caselot = (Integer) ponum1.get(i).get("caselot");
            int totalBoxes = qty / caselot;
            ItemsObject.put("poItemNumber", poItemNumber);
            ItemsObject.put("qty", qty);
            ItemsObject.put("weight", weight);
            ItemsObject.put("volume", volume);
            ItemsObject.put("boxes", totalBoxes);
            isPalletizedBollean = Boolean.valueOf(palletDT.get(j).get("isPalletized"));

            ItemsObject.put("isPalletized", isPalletizedBollean);

            ItemsList.add(ItemsObject);
            if (ConfirmpoDueDate2 != null) {
                ConfirmpoDueDate2 = ConfirmpoDueDate2;
            } else {
                getslotpoDueDate = calenderList.get(h1);

                ConfirmpoDueDate2 = getslotpoDueDate;
                h1++;
            }
        }

        poObject.put("poNumber", getslotsPonumber);
        poObject.put("items", ItemsList);

        PoList.add(poObject);

        GetSlotmainObject.put("appnDate", ConfirmpoDueDate2);
        String id = CommonUtilities.getSiteId();
        GetSlotmainObject.put("dstSiteId", id);
        GetSlotmainObject.put("po", PoList);

        return GetSlotmainObject;
    }


    public static Boolean slotIsAvailbleForParticularTimeSlotOrNot(List<Map<String, String>> palletDT,
                                                                   int j, List<Map<String, String>> halfHourSlots, LinkedHashMap<String, Object> GetSlotmainObject) {

        int k = 0;
        //   List<String> calenderList = CommonUtilities.getcalenderResponseList();

        for (int i = 0; i < PoNumbersList.size(); i++) {
            System.out.println(PoNumbersList.get(i) + "=====GetSlotsConfirmApponment" + i);
            ExtentReportManager.logInfoDetails("GetSlotsConfirmApponment is - ");
            ExtentReportManager.logInfoDetails("poNumbersList " + PoNumbersList.get(i) + "=====GetSlotsConfirmApponment" + i);
        }

        System.out.println(PoNumbersList.size() + "====================>PoNumbersList.size()");
        for (k = k; k < halfHourSlots.size(); ) {

            returnHeaders();
            ReturnPoMainObject(palletDT, j, GetSlotmainObject);
            GetSlotmainObject.put("appnStartTime", halfHourSlots.get(k).get("startTime"));
            GetSlotmainObject.put("appnEndTime", halfHourSlots.get(k).get("endTime"));
            Response GetSlotsValidateResponse = RequestGenerator.getRequestWithheader(validateGetSlots2ConfirmApponmentStep.returnHeaders(), GetSlotmainObject).log().all().when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "ValidateEndPoint"));
            GetSlotsValidateResponse.then().log().all();
            //     CommonUtilities.setResponseInstance(GetSlotsResponse);
            String booleanValueResponse = GetSlotsValidateResponse.getBody().asString();
            falseValue = Boolean.parseBoolean(booleanValueResponse.trim());
            System.out.println(booleanValueResponse + "===========================>booleanValueResponse");

            if (falseValue == true) {
                break;
            }

            if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) &&
                    halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {
                System.out.println(halfHourSlots.get(k).get("startTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + " and " +
                        halfHourSlots.get(k).get("endTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"));

                GenerateTimeSlotsStartTime(k);
                k = 0;
            }
            k++;
        }
        return falseValue;
    }

    public static int findDurationForRate() {
        int[] durations = {30, 60, 90, 120, 150, 180, 210, 240, 270, 300, 330, 360, 390, 420, 450, 480, 510, 540, 570, 600, 630, 660, 690, 720};

        for (int duration : durations) {
            if (unloadingRate <= duration) {
                return duration;
            }
        }
        return -1; // Not available
    }

    public static int processiterations() {
        int duration = findDurationForRate();

        iterations = (int) ((startTime.until(endTime, ChronoUnit.MINUTES)) / duration);
        currentSlotStart = startTime;

        return iterations;
    }

    public static LocalTime processSartTime() {
        int duration = findDurationForRate();

        iterations = (int) ((startTime.until(endTime, ChronoUnit.MINUTES)) / duration);
        currentSlotStart = startTime;

        return currentSlotStart;

    }

    public static List<Map<String, String>> GenerateTimeSlotsStartTime(int k) {
        processiterations();
        processSartTime();
        List<Integer> rateDurationList = Arrays.asList(30, 60, 90, 120, 150, 180, 210, 240, 270, 300, 330, 360, 390, 420, 450, 480, 510, 540, 570, 600, 630, 660, 690, 720);
        halfHourSlots.clear();
        for (int c1 = 0; c1 <= iterations - 1; c1++) {
            int duration = 0;
            for (int c2 = 0; c2 < rateDurationList.size(); c2++) {
                if (unloadingRate.equals(rateDurationList.get(c2))) {
                    duration = rateDurationList.get(c2);
                    break;
                }
            }
            LocalTime currentSlotEnd = currentSlotStart.plusMinutes(duration);
            Map<String, String> slotMap = new HashMap<>();
            slotMap.put("startTime", currentSlotStart.format(hoursformatter));
            slotMap.put("endTime", currentSlotEnd.format(hoursformatter));
            halfHourSlots.add(slotMap);
            currentSlotStart = currentSlotEnd;
        }
        try {

            if (halfHourSlots.get(k).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("startTime")) &&
                    halfHourSlots.get(k).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"))) {
                System.out.println(halfHourSlots.get(k).get("startTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("startTime") + " and " +
                        halfHourSlots.get(k).get("endTime") + " = " + halfHourSlots.get(halfHourSlots.size() - 1).get("endTime"));
                for (cl = cl; cl < calenderList.size(); ) {
                    // ConfirmpoDueDate2 = calenderList.get(cl);

                    ConfirmpoDueDate2 = calenderList.get(h1);
                    // Reset currentSlotStart and k
                    currentSlotStart = startTime;
                    k = 0;

                    cl++;
                    h1++;
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //  halfHourSlots.clear();
        return halfHourSlots;
    }


}


//==========================================================================================================================================

























