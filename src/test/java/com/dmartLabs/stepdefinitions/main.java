//package com.dmartLabs.stepdefinitions;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//import static com.dmartLabs.stepdefinitions.GetSlotsStep.poDueDate;
//
//public class main {
//
//
//    public static void main(String[] args) {
//
//        poDueDate="2024-06-30T00:00:00Z";
//
//        String poDueDateIteration = poDueDate;
//        LocalDateTime poDueDateTime = LocalDateTime.parse(poDueDateIteration, DateTimeFormatter.ISO_DATE_TIME);
//        LocalDateTime poDueDateIterationLocal24 = poDueDateTime.plusHours(24);
//        String formatPattern1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
//        DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern(formatPattern1);
//        String poDueDateIterationString24 = poDueDateIterationLocal24.format(formatter24);
//        poDueDate = poDueDateIterationString24;
//        System.out.println(poDueDate);
//    }
//}
