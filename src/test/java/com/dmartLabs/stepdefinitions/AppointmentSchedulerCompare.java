package com.dmartLabs.stepdefinitions;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentSchedulerCompare {


        public static void main(String[] args) {
            // Example usage
            LocalTime startTime = LocalTime.of(7, 0); // Start time
            LocalTime endTime = LocalTime.of(20, 0); // End time
            List<Map<String, String>> unAvailableSlots = new ArrayList<>(); // Assuming you have unavailability slots

            // Populate the unavailability slots from JSON or other sources
            unAvailableSlots.add(createSlot("07:00", "07:30"));
            unAvailableSlots.add(createSlot("07:30", "08:00"));
            unAvailableSlots.add(createSlot("14:30", "15:30"));
            unAvailableSlots.add(createSlot("14:00", "15:30"));
            unAvailableSlots.add(createSlot("14:30", "16:00"));
            unAvailableSlots.add(createSlot("17:30", "18:30"));

            List<Map<String, String>> availabilitySlots = generateHalfHourAvailabilitySlots(startTime, endTime, unAvailableSlots);

            // Print the generated availability slots
            for (Map<String, String> slot : availabilitySlots) {
                System.out.println("Start Time: " + slot.get("startTime") + ", End Time: " + slot.get("endTime"));
            }
        }

        public static Map<String, String> createSlot(String startTime, String endTime) {
            Map<String, String> slot = new HashMap<>();
            slot.put("startTime", startTime);
            slot.put("endTime", endTime);
            return slot;
        }

        public static List<Map<String, String>> generateHalfHourAvailabilitySlots(LocalTime startTime, LocalTime endTime, List<Map<String, String>> unAvailableSlots) {
            List<Map<String, String>> halfHourAvailabilitySlots = new ArrayList<>();
            LocalTime currentSlotStart = startTime;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            while (currentSlotStart.isBefore(endTime.minusMinutes(29)) || currentSlotStart.equals(endTime.minusMinutes(29))) {
                LocalTime currentSlotEnd = currentSlotStart.plusMinutes(30);

                boolean slotAvailable = true;

                // Check if the current slot overlaps with any unavailability slot
                for (Map<String, String> unavailabilitySlot : unAvailableSlots) {
                    LocalTime unavailabilityStart = LocalTime.parse(unavailabilitySlot.get("startTime"));
                    LocalTime unavailabilityEnd = LocalTime.parse(unavailabilitySlot.get("endTime"));

                    if (currentSlotStart.isBefore(unavailabilityEnd) && currentSlotEnd.isAfter(unavailabilityStart)) {
                        slotAvailable = false;
                        break; // No need to check further if overlap found
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

