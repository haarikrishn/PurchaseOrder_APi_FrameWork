//package com.dmartLabs.stepdefinitions;
//
//
//import com.dmartLabs.commonutils.ExtentReportManager;
//import com.dmartLabs.commonutils.GenricUtils;
//import com.dmartLabs.commonutils.RequestGenerator;
//import com.dmartLabs.config.ConStants;
//import com.dmartLabs.config.PropertyReader;
//import io.cucumber.datatable.DataTable;
//import io.cucumber.java.en.And;
//import io.cucumber.java.en.When;
//import io.restassured.common.mapper.TypeRef;
//import io.restassured.response.Response;
//import io.restassured.specification.RequestSpecification;
//
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//
//import static com.dmartLabs.stepdefinitions.AuthorizationStep.Auth;
//import static com.dmartLabs.stepdefinitions.CreateMultiplePurchaseOrderStep_HK.PoNumbersList;
////import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.Bearertoken;
//import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.ponumbersbody;
////import static com.dmartLabs.stepdefinitions.GetSlotsStep.poDueDate;
////import static com.dmartLabs.stepdefinitions.GetSlotsStep.poDueDate;
//
//
//public class ConfirmAppointmentStep {
//
//
//    @And("set availabilty time based on DC")
//    public void setAvailabiltyTimeBasedOnDC(DataTable dataTable) throws InterruptedException {
//        //  confirm appointment
//        int j;
//        int iterations = 0;
//        LocalTime currentSlotStart = null;
//        Response GetslotResponse = CommonUtilities.getResponseInstance();
//        LinkedHashMap<String, Object> GetslotResponse1 = GetslotResponse.as(new TypeRef<LinkedHashMap<String, Object>>() {
//        });
//
//        String workingHoursstartTime = GetslotResponse.jsonPath().get("workingHours.startTime");//6.00
//        String workingHoursendTime = GetslotResponse.jsonPath().get("workingHours.endTime");//18.00
//        int unloadingRate = GetslotResponse.jsonPath().get("unloadingRate");
//        List<Map<String, Object>> unAvailableSlotList = GetslotResponse.jsonPath().get("unAvailableSlot");
//        Response CreateAppointmentResponse;
//
//        LinkedHashMap<String, Object> VehicleMap = new LinkedHashMap<>();
//        LinkedHashMap<String, Object> driverDetailsMap = new LinkedHashMap<>();
//        List<LinkedHashMap<String, Object>> ResponseItemsList = CommonUtilities.getResponseItemsList();
//
//        String ConfirmpoDueDate = poDueDate;
//        String ConfirmpoDueDate1 = ConfirmpoDueDate;
//
//
//        for (j = 0; j < PoNumbersList.size(); ) {
//
//
//            LinkedHashMap<String, Object> GetSlotmainObject = new LinkedHashMap<>();
//
//
//            LinkedHashMap<String, String> Headers = new LinkedHashMap<>();
//
//         //   String bearer = Bearertoken.get("Authorization");
//            Headers.put("Authorization", Auth);
//            Headers.put("requestid", GenricUtils.getRandomDeliveryNumber());
//
//
//            LinkedHashMap<String, Object> ConfirmAppointmentMap = new LinkedHashMap<>();
//            List<LinkedHashMap<String, Object>> PoList1 = new ArrayList<>();
//
//            LinkedHashMap<String, Object> poObject = new LinkedHashMap<>();
//            List<LinkedHashMap<String, Object>> PoList = new ArrayList<>();
//            List<LinkedHashMap<String, Object>> ponum = ponumbersbody.get(PoNumbersList.get(j));
//
//            List<LinkedHashMap<String, Object>> ItemsList = new ArrayList<>();
//            List<String> PoItemNumberList = new ArrayList<>();
//
//
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//
//
//            LocalTime startTime = LocalTime.parse(workingHoursstartTime);
//            LocalTime endTime = LocalTime.parse(workingHoursendTime);
//
//
//            List<LinkedHashMap<String, String>> unAvailableSlotstartAndEndTimeMApList = new ArrayList<>();
//
//
//            List<Map<String, String>> halfHourSlots = new ArrayList<>();
//            try {
//
//                for (int C = 0; C < unAvailableSlotList.size(); C++) {
//                    LinkedHashMap<String, String> unAvailableSlotstartAndEndTimeMAp = new LinkedHashMap<>();
//                    String unAvailableSlotstartTime = (String) unAvailableSlotList.get(C).get("startTime");
//                    String unAvailableSlotendTime = (String) unAvailableSlotList.get(C).get("endTime");
//
//                    unAvailableSlotstartAndEndTimeMAp.put("startTime", unAvailableSlotstartTime);
//                    unAvailableSlotstartAndEndTimeMAp.put("endTime", unAvailableSlotendTime);
//
//                    unAvailableSlotstartAndEndTimeMApList.add(unAvailableSlotstartAndEndTimeMAp);
//
//                }
//
//
//                //========================================================================================
//
//
//                try {
//                    List<LinkedHashMap<String, String>> CompareendTimec4list = null;
//                    LocalTime endTimec5Local = null;
//
//                    for (int c4 = 0; c4 < unAvailableSlotList.size(); c4++) {
//                        LinkedHashMap<String, String> CompareendTimec4Map = new LinkedHashMap<>();
//                        CompareendTimec4list = new ArrayList<>();
//                        String ComparestartTimec4 = (String) unAvailableSlotList.get(c4).get("startTime");
//                        String CompareendTimec4 = (String) unAvailableSlotList.get(c4).get("endTime");
//                        CompareendTimec4Map.put("endTime", CompareendTimec4);
//                        CompareendTimec4list.add(CompareendTimec4Map);
//                    }
//                    for (int c5 = 0; c5 < CompareendTimec4list.size(); c5++) {
//                        String endTimec5 = CompareendTimec4list.get(c5).get("endTime");
//                        endTimec5Local = LocalTime.parse(endTimec5);
//                        currentSlotStart = endTimec5Local;
//                    }
//                    if (unloadingRate <= 30) {
//                        iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
//                        System.out.println(iterations + "=========================>endTimec5Local");
//                    }
//                    if (unloadingRate > 31 && unloadingRate < 5000) {
//                        iterations = (int) (endTimec5Local.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 60);
//                        System.out.println(iterations + "=========================>endTimec5Local");
//                    }
//                    for (int i = 0; i <= 6; i++) {
//                        if (i == iterations) {
//                            iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
//                            String poDueDateIteration = ConfirmpoDueDate;
//                            LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
//                            LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
//                            String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
//                            DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
//                            String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
//                            ConfirmpoDueDate1 = poDueDateIterationString24;
//                            currentSlotStart = startTime;
//                        }
//                    }
//
//                    if (halfHourSlots.get(j).get("startTime").equals(halfHourSlots.get(halfHourSlots.size() - 2).get("startTime")) && halfHourSlots.get(j).get("endTime").equals(halfHourSlots.get(halfHourSlots.size() - 2).get("endTime"))) {
//                        iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
//                        String poDueDateIteration = ConfirmpoDueDate;
//                        LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
//                        LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
//                        String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
//                        DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
//                        String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
//                        ConfirmpoDueDate1 = poDueDateIterationString24;
//                        currentSlotStart = startTime;
//
//                    }
//
//
//                } catch (NullPointerException e) {
//                    if (unloadingRate <= 30) {
//                        iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
//                        currentSlotStart = startTime;
//                        System.out.println(iterations + "=========================>startTime");
//                    }
//
//                } catch (IndexOutOfBoundsException e) {
//                    if (unloadingRate > 31 && unloadingRate < 5000) {
//                        iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 60);
//                        System.out.println(iterations + "=========================>startTime");
//
//                        currentSlotStart = startTime;
//                    }
//                }
//
//
//                for (int C = 0; C < unAvailableSlotList.size(); C++) {
//                    String ComparestartTime = (String) unAvailableSlotList.get(C).get("startTime");
//                    String CompareendTime = (String) unAvailableSlotList.get(C).get("endTime");
//
//
//                    for (int c1 = 0; c1 <= iterations - 1; c1++) {
//                        if (unloadingRate <= 30) {
//                            LocalTime currentSlotEnd = currentSlotStart.plusMinutes(30);
//
//                            Map<String, String> slotMap = new HashMap<>();
//                            slotMap.put("startTime", currentSlotStart.format(formatter));
//                            slotMap.put("endTime", currentSlotEnd.format(formatter));
//
//                            if (!slotMap.get("endTime").equals(workingHoursendTime) || !ComparestartTime.equals(slotMap.get("startTime")) || !CompareendTime.equals(slotMap.get("endTime"))) {
//                                halfHourSlots.add(slotMap);
//                                currentSlotStart = currentSlotStart.plusMinutes(30);
//                            }
//                        }
//                        if (unloadingRate > 30 && unloadingRate < 5000) {
//                            LocalTime currentSlotEnd = currentSlotStart.plusMinutes(60);
//
//                            Map<String, String> slotMap = new HashMap<>();
//                            slotMap.put("startTime", currentSlotStart.format(formatter));
//                            slotMap.put("endTime", currentSlotEnd.format(formatter));
//
//                            if (!slotMap.get("endTime").equals(workingHoursendTime) || !ComparestartTime.equals(slotMap.get("startTime")) || !CompareendTime.equals(slotMap.get("endTime"))) {
//                                halfHourSlots.add(slotMap);
//                                currentSlotStart = currentSlotStart.plusMinutes(60);
//                            }
//                        }
//                    }
//                }
//
//
//                for (int c2 = 0; c2 < unAvailableSlotstartAndEndTimeMApList.size(); c2++) {
//                    String unAvailablestartTime = unAvailableSlotstartAndEndTimeMApList.get(c2).get("startTime");
//                    String unAvailableendTime = unAvailableSlotstartAndEndTimeMApList.get(c2).get("endTime");
//
//                    LocalTime unAvailableStartTimeLocal = LocalTime.parse(unAvailablestartTime);
//                    LocalTime unAvailableendTimeLocal = LocalTime.parse(unAvailableendTime);
//
//                    for (int c3 = 0; c3 < halfHourSlots.size() - 1; c3++) {
//
//                        String halfHourSlotsstartTime = halfHourSlots.get(c3).get("startTime");
//                        String halfHourSlotsendTime = halfHourSlots.get(c3).get("endTime");
//
//                        LocalTime halfHourSlotsstartTimeLocal = LocalTime.parse(halfHourSlotsstartTime);
//                        LocalTime halfHourSlotsendTimeLocal = LocalTime.parse(halfHourSlotsendTime);
//
//                        if (halfHourSlotsstartTime.equals(unAvailablestartTime)) {
//                            System.out.println("================================================>");
//                            System.out.println("inside the if()");
//                            halfHourSlots.remove(halfHourSlots.get(c3));
//                            break;
//                        }
//
//                    }
//                }
//                System.out.println("available times ===========================================> ");
//                System.out.println(halfHourSlots);
//
//
//            } catch (NullPointerException e) {
//
//                if (unloadingRate <= 30) {
//                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
//                    currentSlotStart = startTime;
//                    System.out.println(iterations + "=========================>startTime");
//                }
//
//                if (unloadingRate > 31 && unloadingRate < 5000) {
//                    iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 60);
//                    System.out.println(iterations + "=========================>startTime");
//
//                    currentSlotStart = startTime;
//                }
//            }
//
//            List<Map<String, String>> vehicleData = dataTable.asMaps();
//
//
//            String truckType = vehicleData.get(j).get("truckType");
//            String truckNumber = vehicleData.get(j).get("truckNumber");
//            String driverName = vehicleData.get(j).get("driverName");
//            String driverNumber = vehicleData.get(j).get("driverNumber");
//            String dstSiteId = vehicleData.get(j).get("dstSiteId");
//
//            VehicleMap.put("truckType", truckType);
//            VehicleMap.put("truckNumber", truckNumber);
//
//            driverDetailsMap.put("driverName", driverName);
//            driverDetailsMap.put("driverNumber", driverNumber);
//
//            VehicleMap.put("driverDetails", driverDetailsMap);
//
//
//            ConfirmAppointmentMap.put("vehicle", VehicleMap);
//
//            ConfirmAppointmentMap.put("isPalletized", false);
//
//            int PoNumbersListj = PoNumbersList.size();
//            if (j == iterations - 1) {
//                //    halfHourSlots.remove(halfHourSlots.size() - 1);
//                String poDueDateIteration = ConfirmpoDueDate;
//                LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
//                LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
//                String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
//                DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
//                String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
//                ConfirmpoDueDate1 = poDueDateIterationString24;
//                ConfirmAppointmentMap.put("appnDate", ConfirmpoDueDate1);
//            }
//            if (PoNumbersListj > 20 && PoNumbersListj < 40) {
//
//
//                String poDueDateIteration = ConfirmpoDueDate;
//                LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
//                LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
//                String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
//                DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
//                String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
//                ConfirmpoDueDate1 = poDueDateIterationString24;
//                ConfirmAppointmentMap.put("appnDate", ConfirmpoDueDate1);
//            }
//            if (PoNumbersListj > 40 && PoNumbersListj < 60) {
//
//
//                String poDueDateIteration = ConfirmpoDueDate;
//                LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
//                LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
//                String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
//                DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
//                String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
//                ConfirmpoDueDate1 = poDueDateIterationString24;
//                ConfirmAppointmentMap.put("appnDate", ConfirmpoDueDate1);
//            } else {
//                ConfirmAppointmentMap.put("appnDate", ConfirmpoDueDate1);
//            }
//
//            try {
//
//
//                ConfirmAppointmentMap.put("appnStartTime", halfHourSlots.get(j).get("startTime"));
//                ConfirmAppointmentMap.put("appnEndTime", halfHourSlots.get(j).get("endTime"));
//
//            } catch (IndexOutOfBoundsException e) {
//                for (int c1 = 0; c1 <= iterations - 1; c1++) {
//                    if (unloadingRate <= 30) {
//
//                        LocalTime workingHoursstartTime1 = startTime;
//                        LocalTime currentSlotEnd = workingHoursstartTime1.plusMinutes(30);
//
//                        Map<String, String> slotMap = new HashMap<>();
//                        slotMap.put("startTime", workingHoursstartTime1.format(formatter));
//                        slotMap.put("endTime", currentSlotEnd.format(formatter));
//
//                        halfHourSlots.add(slotMap);
//                        startTime = workingHoursstartTime1.plusMinutes(30);
//                    }
//                    if (unloadingRate > 30 && unloadingRate < 5000) {
//                        LocalTime workingHoursstartTime1 = startTime;
//                        LocalTime currentSlotEnd = workingHoursstartTime1.plusMinutes(60);
//
//                        Map<String, String> slotMap = new HashMap<>();
//                        slotMap.put("startTime", workingHoursstartTime1.format(formatter));
//                        slotMap.put("endTime", currentSlotEnd.format(formatter));
//
//                        halfHourSlots.add(slotMap);
//                        startTime = workingHoursstartTime1.plusMinutes(60);
//                    }
//                }
//                try {
//                    ConfirmAppointmentMap.put("appnStartTime", halfHourSlots.get(j).get("startTime"));
//                    ConfirmAppointmentMap.put("appnEndTime", halfHourSlots.get(j).get("endTime"));
//                } catch (IndexOutOfBoundsException e1) {
//                    e1.printStackTrace();
//
//                    if (unloadingRate <= 30) {
//                        iterations = (int) (startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30);
//                        currentSlotStart = startTime;
//                        System.out.println(iterations + "=========================>startTime");
//                    }
//                    for (int c1 = 0; c1 <= iterations - 1; c1++) {
//                        if (unloadingRate <= 30) {
//                            LocalTime currentSlotEnd = currentSlotStart.plusMinutes(30);
//
//                            Map<String, String> slotMap = new HashMap<>();
//                            slotMap.put("startTime", currentSlotStart.format(formatter));
//                            slotMap.put("endTime", currentSlotEnd.format(formatter));
//                            halfHourSlots.add(slotMap);
//                            currentSlotStart = currentSlotStart.plusMinutes(30);
//                        }
//                    }
//                    try {
//                        ConfirmAppointmentMap.put("appnStartTime", halfHourSlots.get(j).get("startTime"));
//                        ConfirmAppointmentMap.put("appnEndTime", halfHourSlots.get(j).get("endTime"));
//
//
//                        String poDueDateIteration = ConfirmpoDueDate;
//                        LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
//                        LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
//                        String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
//                        DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
//                        String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
//                        ConfirmpoDueDate1 = poDueDateIterationString24;
//                        // ConfirmAppointmentMap.put("appnDate", ConfirmpoDueDate1);
//                    }
//                    catch (IndexOutOfBoundsException Ie)
//                    {
//                        break;
//                    }
//
//                }
//            }
//
//
//            ConfirmAppointmentMap.put("dstSiteId", dstSiteId);
//
//            List<LinkedHashMap<String, Object>> ItemsList1 = new ArrayList<>();
//
//
//            for (int c5 = 0; c5 < ponum.size(); c5++) {
//                LinkedHashMap<String, Object> ItemsObjct1 = new LinkedHashMap<>();
//
//                String poItemNumber = (String) ponum.get(c5).get("poItemNumber");
//                int qty = (int) ponum.get(c5).get("qty");
//                int caselot = (int) ponum.get(c5).get("caselot");
//                int boxes = qty / caselot;
//
//                double weight = (double) ponum.get(c5).get("weight");
//                double volume = (double) ponum.get(c5).get("volume");
//
//                ItemsObjct1.put("poItemNumber", poItemNumber);
//                ItemsObjct1.put("qty", qty);
//                ItemsObjct1.put("weight", weight);
//                ItemsObjct1.put("volume", volume);
//                ItemsObjct1.put("boxes", boxes);
//                ItemsList1.add(ItemsObjct1);
//
//            }
//            LinkedHashMap<String, Object> Poobject1 = new LinkedHashMap<>();
//            Poobject1.put("poNumber", PoNumbersList.get(j));
//            Poobject1.put("items", ItemsList1);
//            PoList1.add(Poobject1);
//
//            ConfirmAppointmentMap.put("po", PoList1);
//
//            CreateAppointmentResponse = RequestGenerator.getRequestWithheader(Headers, ConfirmAppointmentMap).log().all().when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CreateAppointment"));
//            CreateAppointmentResponse.then().log().all();
//            ExtentReportManager.logInfoDetails("Response is - ");
//            ExtentReportManager.logJson(CreateAppointmentResponse.prettyPrint());
//            ExtentReportManager.logInfoDetails("Response status code is " + CreateAppointmentResponse.getStatusCode());
//            long responseTime = CreateAppointmentResponse.getTimeIn(TimeUnit.MILLISECONDS);
//            ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");
//            CommonUtilities.setResponseInstance(CreateAppointmentResponse);
//            CommonUtilities.setConfirmmAppointmentResponseInstance(CreateAppointmentResponse);
//            Thread.sleep(500);
//            j++;
//
//            if (j == PoNumbersList.size() -2) {
//                break;
//            }
//
//        }
//    }
//}
