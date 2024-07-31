package com.dmartLabs.stepdefinitions;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentSchedulerCompare3 {


    public static void main(String[] args) {
        // Example usage
        LocalTime startTime = LocalTime.of(6, 0); // Start time
        LocalTime endTime = LocalTime.of(18, 0); // End time

        List<Map<String, String>> unAvailableSlots = new ArrayList<>(); // Assuming you have unavailability slots

        // Populate the unavailability slots from JSON or other sources
        unAvailableSlots.add(createSlot("07:00", "07:30"));
        unAvailableSlots.add(createSlot("07:30", "08:00"));
        unAvailableSlots.add(createSlot("14:30", "15:30"));
        unAvailableSlots.add(createSlot("14:00", "15:30"));
        unAvailableSlots.add(createSlot("14:30", "16:00"));
        unAvailableSlots.add(createSlot("17:30", "18:30"));

        List<Map<String, String>> availabilitySlots = generateHalfHourSlots(startTime, endTime);

        // Print the generated availability slots
        for (Map<String, String> slot : availabilitySlots) {
            System.out.println("Start Time: " + slot.get("startTime") + ", End Time: " + slot.get("endTime") +
                    ", Available: " + isSlotAvailable(slot, unAvailableSlots));
        }
    }

    public static List<Map<String, String>> generateHalfHourSlots(LocalTime startTime, LocalTime endTime) {
        List<Map<String, String>> halfHourSlots = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime currentSlotStart = startTime;

        while (currentSlotStart.isBefore(endTime) || currentSlotStart.equals(endTime)) {
            LocalTime currentSlotEnd = currentSlotStart.plusMinutes(30);

            // Create a new HashMap for each slot
            Map<String, String> slotMap = new HashMap<>();
            slotMap.put("startTime", currentSlotStart.format(formatter));
            slotMap.put("endTime", currentSlotEnd.format(formatter));

            halfHourSlots.add(slotMap);
            currentSlotStart = currentSlotStart.plusMinutes(30);
        }

        return halfHourSlots;
    }

    public static boolean isSlotAvailable(Map<String, String> slot, List<Map<String, String>> unAvailableSlots) {
        LocalTime slotStart = LocalTime.parse(slot.get("startTime"));
        LocalTime slotEnd = LocalTime.parse(slot.get("endTime"));

        for (Map<String, String> unavailabilitySlot : unAvailableSlots) {
            LocalTime unavailabilityStart = LocalTime.parse(unavailabilitySlot.get("startTime"));
            LocalTime unavailabilityEnd = LocalTime.parse(unavailabilitySlot.get("endTime"));

            if (slotStart.isBefore(unavailabilityEnd) && slotEnd.isAfter(unavailabilityStart)) {
                return false; // Slot overlaps with unavailability slot
            }
        }

        return true; // Slot is available
    }

    // Utility method to create a map for unavailability slot
    public static Map<String, String> createSlot(String startTime, String endTime) {
        Map<String, String> slotMap = new HashMap<>();
        slotMap.put("startTime", startTime);
        slotMap.put("endTime", endTime);
        return slotMap;
    }
}
//    Start Time: 06:00, End Time: 06:30, Available: true
//        Start Time: 06:30, End Time: 07:00, Available: true
//        Start Time: 07:00, End Time: 07:30, Available: false
//        Start Time: 07:30, End Time: 08:00, Available: false
//        Start Time: 08:00, End Time: 08:30, Available: true
//        Start Time: 08:30, End Time: 09:00, Available: true
//        Start Time: 09:00, End Time: 09:30, Available: true
//        Start Time: 09:30, End Time: 10:00, Available: true
//        Start Time: 10:00, End Time: 10:30, Available: true
//        Start Time: 10:30, End Time: 11:00, Available: true
//        Start Time: 11:00, End Time: 11:30, Available: true
//        Start Time: 11:30, End Time: 12:00, Available: true
//        Start Time: 12:00, End Time: 12:30, Available: true
//        Start Time: 12:30, End Time: 13:00, Available: true
//        Start Time: 13:00, End Time: 13:30, Available: true
//        Start Time: 13:30, End Time: 14:00, Available: true
//        Start Time: 14:00, End Time: 14:30, Available: false
//        Start Time: 14:30, End Time: 15:00, Available: false
//        Start Time: 15:00, End Time: 15:30, Available: false
//        Start Time: 15:30, End Time: 16:00, Available: false
//        Start Time: 16:00, End Time: 16:30, Available: true
//        Start Time: 16:30, End Time: 17:00, Available: true
//        Start Time: 17:00, End Time: 17:30, Available: true
//        Start Time: 17:30, End Time: 18:00, Available: false
//        Start Time: 18:00, End Time: 18:30, Available: false


