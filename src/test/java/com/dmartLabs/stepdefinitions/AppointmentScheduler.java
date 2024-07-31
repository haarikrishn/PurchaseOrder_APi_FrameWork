package com.dmartLabs.stepdefinitions;


import com.dmartLabs.commonutils.RequestGenerator;
import com.dmartLabs.config.ConStants;
import com.dmartLabs.config.PropertyReader;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.dmartLabs.stepdefinitions.CreateMultiplePurchaseOrderStep_HK.PoNumbersList;
//import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.Bearertoken;
import static com.dmartLabs.stepdefinitions.GetPOSStep_HK.ponumbersbody;

public class AppointmentScheduler {

    public static void main(String[] args) {
        // Example usage
        LocalTime startTime = LocalTime.of(7, 0); // Start time
        LocalTime endTime = LocalTime.of(20, 0); // End time
        List<Map<String, String>> unAvailableSlots = new ArrayList<>(); // Assuming you have unavailability slots

        List<Map<String, String>> availabilitySlots = generateHalfHourAvailabilitySlots(startTime, endTime, unAvailableSlots);

        // Print the generated availability slots
        for (Map<String, String> slot : availabilitySlots) {
            System.out.println("Start Time: " + slot.get("startTime") + ", End Time: " + slot.get("endTime"));
        }
    }


    public static List<Map<String, String>> generateHalfHourAvailabilitySlots(LocalTime startTime, LocalTime endTime,
                                                                              List<Map<String, String>> unAvailableSlots) {
        List<Map<String, String>> halfHourAvailabilitySlots = new ArrayList<>();
        LocalTime currentSlotStart = startTime;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        while (currentSlotStart.isBefore(endTime.minusMinutes(29)) || currentSlotStart.equals(endTime.minusMinutes(29))) {
            LocalTime currentSlotEnd = currentSlotStart.plusMinutes(30);

            boolean slotAvailable = true;
            for (Map<String, String> unavailabilitySlot : unAvailableSlots) {
                LocalTime unavailabilityStart = LocalTime.parse(unavailabilitySlot.get("startTime"));
                LocalTime unavailabilityEnd = LocalTime.parse(unavailabilitySlot.get("endTime"));

                if (currentSlotStart.isBefore(unavailabilityEnd) && currentSlotEnd.isAfter(unavailabilityStart)) {
                    slotAvailable = false;
                    break;
                }
            }

            if (slotAvailable) {
                // Create a map for the availability slot using HashMap
                Map<String, String> slotMap = new HashMap<>();
                slotMap.put("startTime", currentSlotStart.format(formatter));
                slotMap.put("endTime", currentSlotEnd.format(formatter));

                halfHourAvailabilitySlots.add(slotMap);
            }

            currentSlotStart = currentSlotStart.plusMinutes(30);
        }

        return halfHourAvailabilitySlots;
    }
}

//    Start Time: 07:00, End Time: 07:30
//        Start Time: 07:30, End Time: 08:00

//===============================================================================================





















//==============================================================================================


/*
      //  public static void main(String[] args) {
            @And("set availabilty time based on DC")
    public void setAvailabiltyTimeBasedOnDC(DataTable dataTable) throws InterruptedException {

            // Assuming you have a method to fetch the response
            Response getSlotResponse = CommonUtilities.getResponseInstance();

            // Extract data from the JSON response
            String startTimeStr = getSlotResponse.jsonPath().get("workingHours.startTime");
            String endTimeStr = getSlotResponse.jsonPath().get("workingHours.endTime");
            int unloadingRate = getSlotResponse.jsonPath().get("unloadingRate");
            List<Map<String, String>> unAvailableSlotList = getSlotResponse.jsonPath().getList("unAvailableSlot");

            // Convert start and end times to LocalTime
            LocalTime startTime = LocalTime.parse(startTimeStr);
            LocalTime endTime = LocalTime.parse(endTimeStr);

            // Generate half-hour availability slots
            List<Map<String, String>> availabilitySlots = generateHalfHourAvailabilitySlots(startTime, endTime, unAvailableSlotList);

            // Assuming you have other data to integrate
            List<Map<String, Object>> vehicleData = dataTable.asMaps();

            // Loop through vehicle data and integrate with appointment scheduling
            for (Map<String, Object> vehicleMap : vehicleData) {
                // Assuming you have other mappings and logic here
                String truckType = (String) vehicleMap.get("truckType");
                String truckNumber = (String) vehicleMap.get("truckNumber");
                String driverName = (String) vehicleMap.get("driverName");
                String driverNumber = (String) vehicleMap.get("driverNumber");
                String dstSiteId = (String) vehicleMap.get("dstSiteId");

                // Assuming poDueDate is defined elsewhere
                String poDueDate = "2024-06-25";

                // Example appointment start and end times (modify as needed based on availabilitySlots)
                String startTimeMap = availabilitySlots.get(0).get("startTime"); // Example, adjust as per your logic
                String endTimeMap = availabilitySlots.get(0).get("endTime");     // Example, adjust as per your logic

                // Assuming you have other mappings and logic for purchase orders
                List<Map<String, Object>> poList = new ArrayList<>();

                // Assuming you have a loop for purchase orders
                for (int j = 0; j < PoNumbersList.size(); j++) {
                    // Example purchase order details
                    String poNumber = PoNumbersList.get(j);
                    List<Map<String, Object>> itemsList = new ArrayList<>();
                    // Assuming you have logic to populate itemsList
                    // Example items map
                    Map<String, Object> itemsMap = new LinkedHashMap<>();
                    itemsMap.put("poItemNumber", "1234");
                    itemsMap.put("qty", 10);
                    itemsMap.put("weight", 25.5);
                    itemsMap.put("volume", 12.3);
                    itemsMap.put("boxes", 2);

                    itemsList.add(itemsMap);

                    // Example purchase order object
                    Map<String, Object> poObject = new LinkedHashMap<>();
                    poObject.put("poNumber", poNumber);
                    poObject.put("items", itemsList);

                    poList.add(poObject);
                }

                // Example confirmation appointment map
                Map<String, Object> confirmAppointmentMap = new LinkedHashMap<>();
                Map<String, Object> vehicleDetailsMap = new LinkedHashMap<>();
                vehicleDetailsMap.put("truckType", truckType);
                vehicleDetailsMap.put("truckNumber", truckNumber);
                vehicleDetailsMap.put("driverName", driverName);
                vehicleDetailsMap.put("driverNumber", driverNumber);

                confirmAppointmentMap.put("vehicle", vehicleDetailsMap);
                confirmAppointmentMap.put("isPalletized", false);
                confirmAppointmentMap.put("appnDate", poDueDate);
                confirmAppointmentMap.put("appnStartTime", startTimeMap);
                confirmAppointmentMap.put("appnEndTime", endTimeMap);
                confirmAppointmentMap.put("dstSiteId", dstSiteId);
                confirmAppointmentMap.put("po", poList);

                // Assuming you have further processing or storage logic here
                // For example, adding confirmAppointmentMap to a list or processing further
            }
        }


        public static List<Map<String, String>> generateHalfHourAvailabilitySlots(LocalTime startTime, LocalTime endTime, List<Map<String, String>> unAvailableSlots) {
            List<Map<String, String>> halfHourAvailabilitySlots = new ArrayList<>();
            LocalTime currentSlotStart = startTime;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            while (currentSlotStart.isBefore(endTime.minusMinutes(29)) || currentSlotStart.equals(endTime.minusMinutes(29))) {
                LocalTime currentSlotEnd = currentSlotStart.plusMinutes(30);

                boolean slotAvailable = true;
                for (Map<String, String> unavailabilitySlot : unAvailableSlots) {
                    LocalTime unavailabilityStart = LocalTime.parse(unavailabilitySlot.get("startTime"));
                    LocalTime unavailabilityEnd = LocalTime.parse(unavailabilitySlot.get("endTime"));

                    if (currentSlotStart.isBefore(unavailabilityEnd) && currentSlotEnd.isAfter(unavailabilityStart)) {
                        slotAvailable = false;
                        break;
                    }
                }

                if (slotAvailable) {
                    halfHourAvailabilitySlots.add(HashMap<String, String>("startTime", currentSlotStart.format(formatter),
                            "endTime", currentSlotEnd.format(formatter)));
                }

                currentSlotStart = currentSlotStart.plusMinutes(30);
            }

            return halfHourAvailabilitySlots;
        }
    }

}

*/
