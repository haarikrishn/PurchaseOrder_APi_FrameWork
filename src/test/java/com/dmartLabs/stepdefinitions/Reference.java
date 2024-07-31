package com.dmartLabs.stepdefinitions;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


import java.util.ArrayList;
import java.util.List;

class AvailabilitySlot {

//
//    public static void main(String[] args) throws MalformedURLException, InterruptedException {
//        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
////        desiredCapabilities.setCapability("app", "C:\\Users\\FD329\\Downloads\\Dlabs_poc.exe");
//        desiredCapabilities.setCapability("app", "C:\\Users\\FD329\\Downloads\\onepos.exe");
//        //desiredCapabilities.setCapability("app", "C:\\Windows\\System32\\calc.exe");
//        desiredCapabilities.setCapability("ms:waitForAppLaunch", "8");
//        desiredCapabilities.setCapability("ms:experimental-webdriver", false);
//        desiredCapabilities.setCapability("platformName", "Windows");
//        desiredCapabilities.setCapability("deviceName", "Windows11");
//
//        //  driver = new WindowsDriver(new URL("http://127.0.0.1:4723"), desiredCapabilities);
//        WindowsDriver<WindowsElement> windowsDriver = new WindowsDriver<>(new URL("http://127.0.0.1:4723"), desiredCapabilities);
//        TimeUnit.SECONDS.sleep(5);
//        WindowsElement userName = windowsDriver.findElementByXPath("//[@AutomationId='login-employee-id']");
//        Assert.assertTrue(userName.isDisplayed());
//        userName.sendKeys("123");
//        WindowsElement password = windowsDriver.findElementByXPath("//[@AutomationId='login-employee-password']");
//        Assert.assertTrue(password.isDisplayed());
//        password.sendKeys("usfd");
//        WindowsElement enterBtn = windowsDriver.findElementByName("ENTER");
//        Assert.assertTrue(enterBtn.isDisplayed());
//        enterBtn.click();
//        System.out.println(driver);
//    }
//




//
//    public static void main(String[] args) {
//        List<String> timeSlots = generate30MinuteSlots("00:00", "12:00");
//        for (String slot : timeSlots) {
//            System.out.println(slot);
//        }
//    }
//    public static List<String> generate30MinuteSlots(String startTime, String endTime) {
//        List<String> slots = new ArrayList<>();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//
//        LocalTime start = LocalTime.parse(startTime, formatter);
//        LocalTime end = LocalTime.parse(endTime, formatter);
//
//        while (!start.isAfter(end)) {
//            slots.add(start.format(formatter) + " - " + start.plusMinutes(30).format(formatter));
//            start = start.plusMinutes(30);
//        }
//
//        return slots;
//    }
}

//    public static void main(String[] args) {
//
//        List<String>orderList=new ArrayList<>();
//        orderList.add("55601467221_1");
//        orderList.add("55601467221_2");
//        orderList.add("55601467221_3");
//        orderList.add("55601467221_4");
//        orderList.add("55601467221_5");
//        orderList.add("55601467221_6");
//        orderList.add("55601467221_7");
//        orderList.add("55601467221_8");
//        orderList.add("55601467221_9");
//        orderList.add("55601467221_10");
//        orderList.add("55601467221_12");
//        orderList.add("55601467221_12");
//        orderList.add("55601467221_13");
//        orderList.add("55601467221_14");
//        orderList.add("55601467221_15");
//        orderList.add("55601467221_16");
//        orderList.add("55601467221_17");
//        orderList.add("55601467221_18");
//        orderList.add("55601467221_19");
//        orderList.add("55601467221_20");
//
//        Collections.sort(orderList);
//        for(String order:orderList)
//        System.out.println(orderList+"-=============>order");
//    }
//}
/*

//========================================================================================

55601467221_1
55601467221_2
55601467221_3
55601467221_4
55601467221_5
55601467221_6
55601467221_7
55601467221_8
55601467221_9
55601467221_10
55601467221_11
55601467221_12
55601467221_13
55601467221_14
55601467221_15
55601467221_16
55601467221_17
55601467221_18
55601467221_19
55601467221_20
//=========================================================
List<String> items = new ArrayList<>();

        // Add items to the list
        items.add("apple");
        items.add("orange");
        items.add("banana");
        items.add("pear");

        // Sort items lexicographically
        Collections.sort(items);







//=================================================================================================


        String originalDateString = "2024-07-07T00:00:00Z";

        ZonedDateTime zonedDateTime = ZonedDateTime.parse(originalDateString);
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String formattedDate = localDateTime.format(formatter);
        System.out.println(formattedDate); // Output: "grnDate": "2024-07-07T00:00:00Z",20240707
    }







//=================================================================================
  String originalDateString = "2024-07-17T00:00:00Z";
        Instant instant = Instant.parse(originalDateString);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneOffset.UTC);
        ZonedDateTime convertedDateTime = zonedDateTime.minusHours(5).minusMinutes(30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedDateTime = formatter.format(convertedDateTime);
        // Print the formatted date/time
        System.out.println(formattedDateTime);

 */


////    String startTime;
////    String endTime;
////
////    public AvailabilitySlot(String startTime, String endTime) {
////        this.startTime = startTime;
////        this.endTime = endTime;
////    }
////}
//
//public class AvailabilityScheduler {
//    public static void main(String[] args) {
//        List<AvailabilitySlot> unAvailableSlots = new ArrayList<>();
//        unAvailableSlots.add(new AvailabilitySlot("07:00", "07:30"));
//        unAvailableSlots.add(new AvailabilitySlot("07:30", "08:00"));
//        unAvailableSlots.add(new AvailabilitySlot("14:30", "15:30"));
//        unAvailableSlots.add(new AvailabilitySlot("14:00", "15:30"));
//        unAvailableSlots.add(new AvailabilitySlot("14:30", "16:00"));
//        unAvailableSlots.add(new AvailabilitySlot("17:30", "18:30"));
//
//        String workingStartTime = "07:00";
//        String workingEndTime = "20:00";
//        int unloadingRate = 30; // in minutes
//
//        List<AvailabilitySlot> availableSlots = new ArrayList<>();
//
//        // Initialize the first available slot
//        String currentStartTime = workingStartTime;
//        String currentEndTime = addMinutes(currentStartTime, unloadingRate);
//
//        while (currentEndTime.compareTo(workingEndTime) <= 0) {
//            boolean isUnavailable = false;
//            for (AvailabilitySlot unAvailableSlot : unAvailableSlots) {
//                if (isOverlap(currentStartTime, currentEndTime, unAvailableSlot.startTime, unAvailableSlot.endTime)) {
//                    isUnavailable = true;
//                    break;
//                }
//            }
//
//            if (!isUnavailable) {
//                availableSlots.add(new AvailabilitySlot(currentStartTime, currentEndTime));
//            }
//
//            // Move to the next slot
//            currentStartTime = currentEndTime;
//            currentEndTime = addMinutes(currentStartTime, unloadingRate);
//        }
//
//        // Print available slots
//        for (AvailabilitySlot slot : availableSlots) {
//            System.out.println("Available Slot: " + slot.startTime + " - " + slot.endTime);
//        }
//    }
//
//    private static String addMinutes(String time, int minutes) {
//        // Implement logic to add minutes to a given time (e.g., "07:00" + 30 minutes)
//        // You can use SimpleDateFormat or other libraries for time manipulation.
//        // For simplicity, I'm omitting the actual implementation here.
//        return time; // Placeholder
//    }
//
//    private static boolean isOverlap(String start1, String end1, String start2, String end2) {
//        // Implement logic to check if two time intervals overlap
//        // You can compare the time strings directly or convert them to Date/Time objects.
//        // For simplicity, I'm omitting the actual implementation here.
//        return false; // Placeholder
//    }
//}
//
//
////public class Reference {
//
////    public static void main(String[] args) {
////        String sitid = String.valueOf(1012);
////        getGrnNumber(sitid);
////        System.out.println( getGrnNumber(sitid));
////    }
////
////    public static String getGrnNumber(String sitid) {
////        Random random = new Random();
////        int randomNumber = random.nextInt(100000); // Generate random number between 0 and 99999
////
////        // Construct the GRN number string
////        String grnNumber = sitid + "/" + sitid + "-01/" + randomNumber;
////
////        return grnNumber;
////    }
//    /*
//     LocalDate today = LocalDate.now();
//
//        // Get tomorrow's date by adding one day to today
//        LocalDate tomorrow = today.plusDays(1);
//
//
// LocalDate today = LocalDate.now();
//
//        // Get yesterday's date by subtracting one day from today
//        LocalDate yesterday = today.minusDays(1);
////=================================================================
//    public static  String getDueDate(String dateFormat){
//        LocalDateTime todayDate = LocalDateTime.now();
//        LocalDateTime ThreeDaysBefore = todayDate.plusDays(3);
//        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
//        String formatDate = inputFormat.format(ThreeDaysBefore);
//        return formatDate;
//    }
//
////============================================
// public static void main(String[] args) {
//        String dueDate = getDueDate("yyyyMMdd");
//        System.out.println("Due date: " + dueDate);
//    }
//
//    public static String getDueDate(String dateFormat) {
//        LocalDateTime todayDate = LocalDateTime.now();
//        LocalDateTime threeDaysAfter = todayDate.plusDays(3);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
//        return threeDaysAfter.format(formatter);
//    }
//    //==================================
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//// Assuming you have ObjectMapper initialized somewhere in your code
//ObjectMapper objectMapper = new ObjectMapper();
//
//// Convert POItems to JSON string
//String json = objectMapper.writeValueAsString(POItems);
//
//// Now use JsonPath to parse the JSON string
//JsonPath js = new JsonPath(json);
//System.out.print(js.prettyPrint());
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//     */
//
////import java.time.LocalTime;
////import java.util.ArrayList;
////import java.util.List;
//
////    public class AvailabilitySlotSetter {
////
////        public static void main(String[] args) {
////            // Example configuration (replace with your actual configuration loading mechanism)
////            TimeSlot.AvailabilityConfiguration configuration = loadAvailabilityConfiguration();
////
////            // Extract working hours and unavailable slots from configuration
////            LocalTime startTime = configuration.getWorkingHours().getStartTime();
////            LocalTime endTime = configuration.getWorkingHours().getEndTime();
////            List<TimeSlot> unAvailableSlots = configuration.getUnAvailableSlots();
////
////            // Generate half-hour availability slots
////            List<TimeSlot> halfHourAvailabilitySlots = setHalfHourAvailabilitySlots(startTime, endTime, unAvailableSlots);
////
////            // Print the half-hour availability slots
////            for (TimeSlot slot : halfHourAvailabilitySlots) {
////                System.out.println(slot);
////            }
////        }
////
////
////        public static List<TimeSlot> setHalfHourAvailabilitySlots(LocalTime startTime, LocalTime endTime,
////                                                                  List<TimeSlot> unAvailableSlots) {
////            List<TimeSlot> halfHourAvailabilitySlots = new ArrayList<>();
////            LocalTime currentSlotStart = startTime;
////
////            while (currentSlotStart.isBefore(endTime.minusMinutes(29)) || currentSlotStart.equals(endTime.minusMinutes(29))) {
////                LocalTime currentSlotEnd = currentSlotStart.plusMinutes(30);
////                TimeSlot slot = new TimeSlot(currentSlotStart, currentSlotEnd);
////
////                boolean slotAvailable = true;
////                for (TimeSlot unavailableSlot : unAvailableSlots) {
////                    if (slot.overlaps(unavailableSlot)) {
////                        slotAvailable = false;
////                        break;
////                    }
////                }
////
////                if (slotAvailable) {
////                    halfHourAvailabilitySlots.add(slot);
////                }
////
////                currentSlotStart = currentSlotStart.plusMinutes(30);
////            }
////
////            return halfHourAvailabilitySlots;
////        }
////
////        /**
////         * Represents a time slot with start and end times.
////         */
////        static class TimeSlot {
////            private LocalTime startTime;
////            private LocalTime endTime;
////
////            public TimeSlot(LocalTime startTime, LocalTime endTime) {
////                this.startTime = startTime;
////                this.endTime = endTime;
////            }
////
////            public LocalTime getStartTime() {
////                return startTime;
////            }
////
////            public LocalTime getEndTime() {
////                return endTime;
////            }
////
////
////            public boolean overlaps(TimeSlot other) {
////                return this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime);
////            }
////
////
////        static class AvailabilityConfiguration {
////            private WorkingHours workingHours;
////            private List<TimeSlot> unAvailableSlots;
////
////            public AvailabilityConfiguration(WorkingHours workingHours, List<TimeSlot> unAvailableSlots) {
////                this.workingHours = workingHours;
////                this.unAvailableSlots = unAvailableSlots;
////            }
////
////            public WorkingHours getWorkingHours() {
////                return workingHours;
////            }
////
////            public List<TimeSlot> getUnAvailableSlots() {
////                return unAvailableSlots;
////            }
////        }
////
////        /**
////         * Represents the working hours with start and end times.
////         */
////        static class WorkingHours {
////            private LocalTime startTime;
////            private LocalTime endTime;
////
////            public WorkingHours(LocalTime startTime, LocalTime endTime) {
////                this.startTime = startTime;
////                this.endTime = endTime;
////            }
////
////            public LocalTime getStartTime() {
////                return startTime;
////            }
////
////            public LocalTime getEndTime() {
////                return endTime;
////            }
////        }
////
////
////        public static AvailabilityConfiguration loadAvailabilityConfiguration() {
////            // Simulated example configuration (replace with actual loading logic)
////            LocalTime startTime = LocalTime.of(7, 0);   // Start time: 07:00 AM
////            LocalTime endTime = LocalTime.of(20, 0);    // End time: 20:00 PM
////
////            List<TimeSlot> unAvailableSlots = new ArrayList<>();
////            unAvailableSlots.add(new TimeSlot(LocalTime.of(7, 0), LocalTime.of(7, 30)));
////            unAvailableSlots.add(new TimeSlot(LocalTime.of(7, 30), LocalTime.of(8, 0)));
////            unAvailableSlots.add(new TimeSlot(LocalTime.of(14, 30), LocalTime.of(15, 30)));
////
////            return new AvailabilityConfiguration(new WorkingHours(startTime, endTime), unAvailableSlots);
////        }
////    }
//
//
//
//    //==================================================================================================
///*
//
//        public class AvailabilitySlotHandler {
//
//            @Test
//            public void handleAvailabilitySlots() {
//                // Assuming you have a method to get the response instance
//                Response getSlotResponse = CommonUtilities.getResponseInstance();
//
//                // Extract data from the JSON response
//                String startTimeStr = getSlotResponse.jsonPath().get("workingHours.startTime");
//                String endTimeStr = getSlotResponse.jsonPath().get("workingHours.endTime");
//                int unloadingRate = getSlotResponse.jsonPath().get("unloadingRate");
//                List<Map<String, String>> unAvailableSlotList = getSlotResponse.jsonPath().getList("unAvailableSlot");
//
//                // Convert start and end times to LocalTime
//                LocalTime startTime = LocalTime.parse(startTimeStr);
//                LocalTime endTime = LocalTime.parse(endTimeStr);
//
//                // Generate half-hour availability slots
//                List<TimeSlot> halfHourAvailabilitySlots = generateHalfHourAvailabilitySlots(startTime, endTime, unAvailableSlotList);
//
//                // Print or process the generated availability slots
//                for (TimeSlot slot : halfHourAvailabilitySlots) {
//                    System.out.println(slot);
//                }
//
//            public List<TimeSlot> generateHalfHourAvailabilitySlots(LocalTime startTime, LocalTime endTime, List<Map<String, String>> unAvailableSlots) {
//                List<TimeSlot> halfHourAvailabilitySlots = new ArrayList<>();
//                LocalTime currentSlotStart = startTime;
//
//                while (currentSlotStart.isBefore(endTime.minusMinutes(29)) || currentSlotStart.equals(endTime.minusMinutes(29))) {
//                    LocalTime currentSlotEnd = currentSlotStart.plusMinutes(30);
//                    TimeSlot slot = new TimeSlot(currentSlotStart, currentSlotEnd);
//
//                    boolean slotAvailable = true;
//                    for (Map<String, String> unavailableSlot : unAvailableSlots) {
//                        LocalTime unavailabilityStart = LocalTime.parse(unavailableSlot.get("startTime"));
//                        LocalTime unavailabilityEnd = LocalTime.parse(unavailableSlot.get("endTime"));
//
//                        if (slot.overlaps(unavailabilityStart, unavailabilityEnd)) {
//                            slotAvailable = false;
//                            break;
//                        }
//                    }
//
//                    if (slotAvailable) {
//                        halfHourAvailabilitySlots.add(slot);
//                    }
//
//                    currentSlotStart = currentSlotStart.plusMinutes(30);
//                }
//                    ConfirmAppointmentMap.put("appnStartTime", startTimeMAp1);
////                ConfirmAppointmentMap.put("appnEndTime", endTimeMap1);
//                return halfHourAvailabilitySlots;
//            }
//
//
//                public boolean overlaps(LocalTime otherStart, LocalTime otherEnd) {
//                    return this.startTime.isBefore(otherEnd) && otherStart.isBefore(this.endTime);
//                }
//
//
//        }
//
//    }
//
//
//
//
//
//
////
////            @Override
////            public String toString() {
////                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
////                return "TimeSlot{" +
////                        "startTime=" + startTime.format(formatter) +
////                        ", endTime=" + endTime.format(formatter) +
////                        '}';
////            }
////        }
//
////// Common utilities class with method to get RestAssured response instance
////static class CommonUtilities {
////    public static Response getResponseInstance() {
////        // Simulated response for demonstration
////        return given()
////                .contentType("application/json")
////                .get("/get/availability")
////                .then()
////                .extract().response();
////    }
////}
//


//
//int minutes = 40;
//
//// Round up to the nearest 60 minutes
//        int roundedMinutes = (int) Math.ceil(minutes / 60.0) * 60;
//
//        System.out.println("Original minutes: " + minutes);
//        System.out.println("Rounded minutes: " + roundedMinutes);


// */
//
//
//
//
