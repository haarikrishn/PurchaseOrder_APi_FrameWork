package com.dmartLabs.stepdefinitions;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentSchedulerCompaeTrue {


    public static void main(String[] args) {
        LocalTime startTime = LocalTime.of(6, 0); // Start time
        LocalTime endTime = LocalTime.of(18, 0); // End time

        List<Map<String, String>> unAvailableSlots = new ArrayList<>();
        unAvailableSlots.add(createSlot("07:00", "07:30"));
        unAvailableSlots.add(createSlot("07:30", "08:00"));
        unAvailableSlots.add(createSlot("14:30", "15:30"));
        unAvailableSlots.add(createSlot("14:00", "15:30"));
        unAvailableSlots.add(createSlot("14:30", "16:00"));
        unAvailableSlots.add(createSlot("17:30", "18:30"));

        List<Map<String, String>> availabilitySlots = generateHalfHourSlots(startTime, endTime);

        // HashMap to store available slots
        Map<String, String> availableSlotsMap = new HashMap<>();

        // Collect available slots into the HashMap
        for (Map<String, String> slot : availabilitySlots) {
            if (isSlotAvailable(slot, unAvailableSlots)) {
                String start = slot.get("startTime");
                String end = slot.get("endTime");
                availableSlotsMap.put(start, end);
            }
        }

        // Print the available slots
        availableSlotsMap.forEach((start, end) -> System.out.println("Start Time: " + start + ", End Time: " + end));
    }

    public static List<Map<String, String>> generateHalfHourSlots(LocalTime startTime, LocalTime endTime) {
        List<Map<String, String>> halfHourSlots = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime currentSlotStart = startTime;

        while (currentSlotStart.isBefore(endTime) || currentSlotStart.equals(endTime)) {
            LocalTime currentSlotEnd = currentSlotStart.plusMinutes(30);

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

    public static Map<String, String> createSlot(String startTime, String endTime) {
        Map<String, String> slotMap = new HashMap<>();
        slotMap.put("startTime", startTime);
        slotMap.put("endTime", endTime);
        return slotMap;
    }
}
//================================================

/*
  LocalTime localTime = LocalTime.of(10, 30); // Example LocalTime object

        // Define a formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Format the LocalTime to String
        String timeAsString = localTime.format(formatter);


     String timeAsString = "14:45"; // Example time as String

        // Define a formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Parse the String to LocalTime
        LocalTime localTime = LocalTime.parse(timeAsString, formatter);


 */











//====================================================