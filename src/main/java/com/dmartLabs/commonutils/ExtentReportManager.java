package com.dmartLabs.commonutils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.restassured.http.Header;

public class ExtentReportManager {
	
	
	public static ExtentReports extentReports;
	public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();
	
	
	public static ExtentReports  createInstance(String filePath,String ReportName,String documentTille)
	{
		ExtentSparkReporter htmlReporter = new ExtentSparkReporter (filePath);
		htmlReporter.config().setDocumentTitle("PWP API Automation");
//		htmlReporter.config().setReportName("QuickFill - " + System.getProperty("env") +" Environment");
        htmlReporter.config().setReportName("PWP - CANARY Environment");
		htmlReporter.config().setTheme(Theme.STANDARD);
		
		if(extentReports == null) {
			extentReports = new ExtentReports();
			extentReports.attachReporter(htmlReporter);
		}
		
		
		return extentReports;
		
	}
	
	
	public static String getReportNameWithTimeStamp() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        String formattedTime = dateTimeFormatter.format(localDateTime);
        String reportName = "TestReport_" + formattedTime + ".html";
        return reportName;
    }

    public static void logPassDetails(String log) {
    	ExtentTestFactory.getExtentTest().pass(MarkupHelper.createLabel(log, ExtentColor.GREEN));
        
    }
    public static void logFailureDetails(String log) {
    	ExtentTestFactory.getExtentTest().fail(MarkupHelper.createLabel(log, ExtentColor.RED));
    }
    public static void logExceptionDetails(String log) {
    	ExtentTestFactory.getExtentTest().fail(log);
    }
    public static void logInfoDetails(String log) {
    	ExtentTestFactory.getExtentTest().info(log);
    }

    public static ExtentTest info(String details) {
        return info(details);
    }

    public static ExtentTest info(Integer details) {
        return info(details);
    }

    public static void logWarningDetails(String log) {
        ExtentTestFactory.getExtentTest().warning(MarkupHelper.createLabel(log, ExtentColor.YELLOW));
    }

    public static void logDeepWarningDetails(String log) {
        ExtentTestFactory.getExtentTest().warning(MarkupHelper.createLabel(log, ExtentColor.PINK));
    }
    public static void logFailedDetails(String log) {
        ExtentTestFactory.getExtentTest().warning(MarkupHelper.createLabel(log, ExtentColor.BLACK));
    }


    public static void logJson(String json) {
    	ExtentTestFactory.getExtentTest().info(MarkupHelper.createCodeBlock(json, CodeLanguage.JSON));
    }
    public static void logHeaders(List<Header> headersList) {

        String[][] arrayHeaders = headersList.stream().map(header -> new String[] {header.getName(), header.getValue()})
                        .toArray(String[][] :: new);
        ExtentTestFactory.getExtentTest().info(MarkupHelper.createTable(arrayHeaders));
    }

}
