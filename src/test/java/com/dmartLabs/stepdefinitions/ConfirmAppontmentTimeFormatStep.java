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
//
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//
//import static com.dmartLabs.stepdefinitions.CreateMultiplePurchaseOrderStep_HK.PoNumbersList;
//import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.Bearertoken;
//import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.ponumbersbody;
//import static com.dmartLabs.stepdefinitions.GetSlotsStep.poDueDate;
//
//
//public class ConfirmAppontmentTimeFormatStep {
//
//
//    Response CreateAppointmentResponse;
//
//    LinkedHashMap<String, Object> VehicleMap = new LinkedHashMap<>();
//    LinkedHashMap<String, Object> driverDetailsMap = new LinkedHashMap<>();
//
//
//    public int l = 0;
//
//    @And("set availabilty time based on DC")
//    public void setAvailabiltyTimeBasedOnDC(DataTable dataTable) throws InterruptedException {
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//        Response GetslotResponse = CommonUtilities.getResponseInstance();
//        LinkedHashMap<String, Object> GetslotResponse1 = GetslotResponse.as(new TypeRef<LinkedHashMap<String, Object>>() {
//        });
//
//        String workingHoursstartTime = GetslotResponse.jsonPath().get("workingHours.startTime");//6.00
//        String workingHoursendTime = GetslotResponse.jsonPath().get("workingHours.endTime");//18.00
//
//        LocalTime startTime = LocalTime.parse(workingHoursstartTime);
//        LocalTime endTime = LocalTime.parse(workingHoursendTime);
//
//
//        int unloadingRate = GetslotResponse.jsonPath().get("unloadingRate");
//
//
//        LinkedHashMap<String, String> unAvailableSlotstartAndEndTimeMAp = new LinkedHashMap<>();
//        List<LinkedHashMap<String, String>> unAvailableSlotstartAndEndTimeMApList = new ArrayList<>();
//
//
//        List<Map<String, Object>> unAvailableSlotList = GetslotResponse.jsonPath().get("unAvailableSlot");
////unavailability slots
//        String ConfirmAppontmentstartTime = null;
//        String ConfirmAppontmentEndTime = null;
//        try {
//            for (int i = 0; i < unAvailableSlotList.size(); i++) {
//                String unAvailableSlotstartTime = (String) unAvailableSlotList.get(i).get("startTime");
//                String unAvailableSlotendTime = (String) unAvailableSlotList.get(i).get("endTime");
//
//                unAvailableSlotstartAndEndTimeMAp.put("startTime", unAvailableSlotstartTime);
//                unAvailableSlotstartAndEndTimeMAp.put("endTime", unAvailableSlotendTime);
//
//                unAvailableSlotstartAndEndTimeMApList.add(unAvailableSlotstartAndEndTimeMAp);
//
//            }
////====================================================================================================
//
//            //available slots
//      //      List<Map<String, String>> halfHourSlots = new ArrayList<>();
//     //       LocalTime currentSlotStart = startTime;
//
////            while (currentSlotStart.isBefore(endTime) || currentSlotStart.equals(endTime)) {
////                LocalTime currentSlotEnd = currentSlotStart.plusMinutes(30);
////
////                Map<String, String> slotMap = new HashMap<>();
////                slotMap.put("startTime", currentSlotStart.format(formatter));
////                slotMap.put("endTime", currentSlotEnd.format(formatter));
////                if (!slotMap.get("endTime").equals(workingHoursendTime)) {
////                    halfHourSlots.add(slotMap);
////                    currentSlotStart = currentSlotStart.plusMinutes(30);
////                }
////                else {
////                    break;
////                }
////            }
//
//            //========================================================================================
//
//            List<Map<String, String>> halfHourSlots = new ArrayList<>();
//            long iterations = startTime.until(endTime, java.time.temporal.ChronoUnit.MINUTES) / 30;
//
//            LocalTime currentSlotStart = startTime;
//
//            for (int i = 0; i <= iterations; i++) {
//                LocalTime currentSlotEnd = currentSlotStart.plusMinutes(30);
//
//                Map<String, String> slotMap = new HashMap<>();
//                slotMap.put("startTime", currentSlotStart.format(formatter));
//                slotMap.put("endTime", currentSlotEnd.format(formatter));
//
//                if (!slotMap.get("endTime").equals(workingHoursendTime)) {
//                    halfHourSlots.add(slotMap);
//                    currentSlotStart = currentSlotStart.plusMinutes(30);
//                } else {
//                    break;
//                }
//            }
//
//
//                for (int j = 0; j < unAvailableSlotstartAndEndTimeMApList.size(); j++) {
//                    String unAvailablestartTime = unAvailableSlotstartAndEndTimeMApList.get(j).get("startTime");
//                    String unAvailableendTime = unAvailableSlotstartAndEndTimeMApList.get(j).get("endTime");
//
//                    LocalTime unAvailableStartTimeLocal = LocalTime.parse(unAvailablestartTime);
//                    LocalTime unAvailableendTimeLocal = LocalTime.parse(unAvailableendTime);
//
//                    for (int k = 0; k < halfHourSlots.size(); k++) {
//                        String halfHourSlotsstartTime = halfHourSlots.get(k).get("startTime");
//                        String halfHourSlotsendTime = halfHourSlots.get(k).get("endTime");
//
//                        LocalTime halfHourSlotsstartTimeLocal = LocalTime.parse(halfHourSlotsstartTime);
//                        LocalTime halfHourSlotsendTimeLocal = LocalTime.parse(halfHourSlotsendTime);
//
//                        if (!unAvailableStartTimeLocal.equals(halfHourSlotsstartTimeLocal) && !unAvailableendTimeLocal.equals(halfHourSlotsendTimeLocal)) {
//
//                            String halfHourSlotsstartTimeString = halfHourSlotsstartTimeLocal.format(formatter);
//                            String halfHourSlotsendTimeString = halfHourSlotsendTimeLocal.format(formatter);
//
//
//                            ConfirmAppontmentstartTime = halfHourSlotsstartTimeString;
//                            ConfirmAppontmentEndTime = halfHourSlotsendTimeString;
//                        }
//
////                    if (halfHourSlotsstartTimeLocal.isBefore(unAvailableendTimeLocal) && halfHourSlotsendTimeLocal.isAfter(unAvailableStartTimeLocal)) {
////
////                        String halfHourSlotsstartTimeString = halfHourSlotsstartTimeLocal.format(formatter);
////                        String halfHourSlotsendTimeString = halfHourSlotsendTimeLocal.format(formatter);
////
////
////                        ConfirmAppontmentstartTime = halfHourSlotsstartTimeString;
////                        ConfirmAppontmentEndTime = halfHourSlotsendTimeString;
////                    }
//
//
////                        else {
////
////
////                            ConfirmAppontmentstartTime = halfHourSlotsstartTime;
////                            ConfirmAppontmentEndTime = halfHourSlotsendTime;
////                        }
//
//                    }
//
//                }
//            } catch(NullPointerException e){
//                String NullstartTime = startTime.format(formatter);
//                LocalTime NullEndTimeLocal = startTime.plusMinutes(30);
//                String NullEndTime = NullEndTimeLocal.format(formatter);
//                ConfirmAppontmentstartTime = NullstartTime;
//
//                ConfirmAppontmentEndTime = NullEndTime;
//            }
//
//            List<Map<String, String>> vehicleData = dataTable.asMaps();
//
//            for (int i = 0; i < vehicleData.size(); i++) {
//                List<LinkedHashMap<String, Object>> PoList1 = new ArrayList<>();
//                LinkedHashMap<String, Object> ConfirmAppointmentMap = new LinkedHashMap<>();
//                String truckType = vehicleData.get(i).get("truckType");
//                String truckNumber = vehicleData.get(i).get("truckNumber");
//                String driverName = vehicleData.get(i).get("driverName");
//                String driverNumber = vehicleData.get(i).get("driverNumber");
//                String dstSiteId = vehicleData.get(i).get("dstSiteId");
//
//                VehicleMap.put("truckType", truckType);
//                VehicleMap.put("truckNumber", truckNumber);
//
//                driverDetailsMap.put("driverName", driverName);
//                driverDetailsMap.put("driverNumber", driverNumber);
//
//                VehicleMap.put("driverDetails", driverDetailsMap);
//
//
//                ConfirmAppointmentMap.put("vehicle", VehicleMap);
//
//                ConfirmAppointmentMap.put("isPalletized", false);
//
//
//                ConfirmAppointmentMap.put("appnDate", poDueDate);
//
//
//                ConfirmAppointmentMap.put("appnStartTime", ConfirmAppontmentstartTime);
//                ConfirmAppointmentMap.put("appnEndTime", ConfirmAppontmentEndTime);
//
//                ConfirmAppointmentMap.put("dstSiteId", dstSiteId);
//
//                List<LinkedHashMap<String, Object>> ItemsList1 = new ArrayList<>();
//
//
//                for (l = l; l < PoNumbersList.size(); ) {
//                    List<LinkedHashMap<String, Object>> ponum = ponumbersbody.get(PoNumbersList.get(l));
//
//                    String poNumber1 = PoNumbersList.get(l);
//                    for (int k = 0; k < ponum.size(); k++) {
//                        LinkedHashMap<String, Object> ItemsObjct1 = new LinkedHashMap<>();
//
//                        String poItemNumber = (String) ponum.get(k).get("poItemNumber");
//                        int qty = (int) ponum.get(k).get("qty");
//                        int caselot = (int) ponum.get(k).get("caselot");
//                        int boxes = qty / caselot;
//
//                        double weight = (double) ponum.get(k).get("weight");
//                        double volume = (double) ponum.get(k).get("volume");
//
//                        ItemsObjct1.put("poItemNumber", poItemNumber);
//                        ItemsObjct1.put("qty", qty);
//                        ItemsObjct1.put("weight", weight);
//                        ItemsObjct1.put("volume", volume);
//                        ItemsObjct1.put("boxes", boxes);
//                        ItemsList1.add(ItemsObjct1);
//
//                    }
//                    LinkedHashMap<String, Object> Poobject1 = new LinkedHashMap<>();
//                    Poobject1.put("poNumber", poNumber1);
//                    Poobject1.put("items", ItemsList1);
//                    PoList1.add(Poobject1);
//
//
//                    ConfirmAppointmentMap.put("po", PoList1);
//
//                    CreateAppointmentResponse = RequestGenerator.getRequestWithheader(Bearertoken, ConfirmAppointmentMap).log().all().when().post(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "CreateAppointment"));
//                    CreateAppointmentResponse.then().log().all();
//                    ExtentReportManager.logInfoDetails("Response is - ");
//                    ExtentReportManager.logJson(CreateAppointmentResponse.prettyPrint());
//                    ExtentReportManager.logInfoDetails("Response status code is " + CreateAppointmentResponse.getStatusCode());
//                    long responseTime = CreateAppointmentResponse.getTimeIn(TimeUnit.MILLISECONDS);
//                    ExtentReportManager.logInfoDetails("Response Time is " + responseTime + " ms");
//                    CommonUtilities.setResponseInstance(CreateAppointmentResponse);
//
//                    l++;
//
//                    //======================================================
//                    //2 round
////                    GetSlotsStep getslot = new GetSlotsStep();
////                    Response response = getslot.wheneverVendorCreatedPurchaseOrderDcShouldGiveAvailabilityTime();
////                    GetslotResponse1 = response.as(new TypeRef<LinkedHashMap<String, Object>>() {
////                    });
////
////                    GetslotResponse = CommonUtilities.getResponseInstance();
////                    GetslotResponse1 = GetslotResponse.as(new TypeRef<LinkedHashMap<String, Object>>() {
////                    });
////
////
////                    workingHoursstartTime = GetslotResponse.jsonPath().get("workingHours.startTime");//6.00
////                    workingHoursendTime = GetslotResponse.jsonPath().get("workingHours.endTime");//18.00
////
////                    startTime = LocalTime.parse(workingHoursstartTime);
////                    endTime = LocalTime.parse(workingHoursendTime);
////
////
////                    unloadingRate = GetslotResponse.jsonPath().get("unloadingRate");
////
////
//////                LinkedHashMap<String, String> unAvailableSlotstartAndEndTimeMAp = new LinkedHashMap<>();
//////                List<LinkedHashMap<String, String>> unAvailableSlotstartAndEndTimeMApList = new ArrayList<>();
////
////
////                    unAvailableSlotList = GetslotResponse.jsonPath().get("unAvailableSlot");
//////unavailability slots
////                    ConfirmAppontmentstartTime = null;
////                    ConfirmAppontmentEndTime = null;
////
////
////                    try {
////                        for (int m = 0; m < unAvailableSlotList.size(); m++) {
////                            String unAvailableSlotstartTime = (String) unAvailableSlotList.get(m).get("startTime");
////                            String unAvailableSlotendTime = (String) unAvailableSlotList.get(m).get("endTime");
////
////                            unAvailableSlotstartAndEndTimeMAp.put("startTime", unAvailableSlotstartTime);
////                            unAvailableSlotstartAndEndTimeMAp.put("endTime", unAvailableSlotendTime);
////
////                            unAvailableSlotstartAndEndTimeMApList.add(unAvailableSlotstartAndEndTimeMAp);
////
////                        }
////
////
////                        //available slots
////                        List<Map<String, String>> halfHourSlots = new ArrayList<>();
////                        LocalTime currentSlotStart = startTime;
////
////                        while (currentSlotStart.isBefore(endTime) || currentSlotStart.equals(endTime)) {
////                            LocalTime currentSlotEnd = currentSlotStart.plusMinutes(30);
////
////                            Map<String, String> slotMap = new HashMap<>();
////                            slotMap.put("startTime", currentSlotStart.format(formatter));
////                            slotMap.put("endTime", currentSlotEnd.format(formatter));
////
////                            halfHourSlots.add(slotMap);
////                            currentSlotStart = currentSlotStart.plusMinutes(30);
////                        }
////
////
////                        for (int j = 0; j < unAvailableSlotstartAndEndTimeMApList.size(); j++) {
////                            String unAvailablestartTime = unAvailableSlotstartAndEndTimeMApList.get(j).get("startTime");
////                            String unAvailableendTime = unAvailableSlotstartAndEndTimeMApList.get(j).get("endTime");
////
////                            LocalTime unAvailableStartTimeLocal = LocalTime.parse(unAvailablestartTime);
////                            LocalTime unAvailableendTimeLocal = LocalTime.parse(unAvailableendTime);
////                            for (int k = 0; k < halfHourSlots.size(); k++) {
////                                String halfHourSlotsstartTime = halfHourSlots.get(k).get("startTime");
////                                String halfHourSlotsendTime = halfHourSlots.get(k).get("endTime");
////
////                                LocalTime halfHourSlotsstartTimeLocal = LocalTime.parse(halfHourSlotsstartTime);
////                                LocalTime halfHourSlotsendTimeLocal = LocalTime.parse(halfHourSlotsendTime);
////
////                                if (!unAvailableStartTimeLocal.equals(halfHourSlotsstartTimeLocal) && !unAvailableendTimeLocal.equals(halfHourSlotsendTimeLocal)) {
////
////                                    String halfHourSlotsstartTimeString = halfHourSlotsstartTimeLocal.format(formatter);
////                                    String halfHourSlotsendTimeString = halfHourSlotsendTimeLocal.format(formatter);
////
////
////                                    ConfirmAppontmentstartTime = halfHourSlotsstartTimeString;
////                                    ConfirmAppontmentEndTime = halfHourSlotsendTimeString;
////                                }
//////                            if (halfHourSlotsstartTimeLocal.isBefore(unAvailableendTimeLocal) && halfHourSlotsendTimeLocal.isAfter(unAvailableStartTimeLocal)) {
//////
//////                                String halfHourSlotsstartTimeString = halfHourSlotsstartTimeLocal.format(formatter);
//////                                String halfHourSlotsendTimeString = halfHourSlotsendTimeLocal.format(formatter);
//////
//////
//////                                ConfirmAppontmentstartTime = halfHourSlotsstartTimeString;
//////                                ConfirmAppontmentEndTime = halfHourSlotsendTimeString;
//////                            }
////
////                                else {
////
////
////                                    ConfirmAppontmentstartTime = halfHourSlotsstartTime;
////                                    ConfirmAppontmentEndTime = halfHourSlotsendTime;
////                                }
////
////                            }
////
////                        }
////                    } catch (NullPointerException e) {
////                        String NullstartTime = startTime.format(formatter);
////                        LocalTime NullEndTimeLocal = startTime.plusMinutes(30);
////                        String NullEndTime = NullEndTimeLocal.format(formatter);
////                        ConfirmAppontmentstartTime = NullstartTime;
////
////                        ConfirmAppontmentEndTime = NullEndTime;
////                    }
////
////
//                    break;
//
//
//                }
//            }
//        }
//    }