package com.dmartLabs.commonutils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestResult;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Date;

public class RestListener {
	public static ExtentTestFactory extentTestFactory;
	private static ExtentReports extent;
	public static String filePath = System.getProperty("user.dir") + "/Reports/ExtentReport"+new Date().getTime() +".html";

	public RestListener() {
		extentTestFactory = new ExtentTestFactory();
	}

	/**
	 * Load Extent report file
	 */
	public static void loadExtentFile() {
		if(extent == null) {
			extent = new ExtentReports();
			extent.attachReporter(getHtmlReporter());
		}
	}

	/**
	 *
	 * @return ExtentHtmlReporter Instance
	 */
	private static ExtentSparkReporter  getHtmlReporter() {
		new File(filePath);
		ExtentSparkReporter htmlReporter = new ExtentSparkReporter (filePath);
		htmlReporter.config().setDocumentTitle("D_Link API Automation");
		htmlReporter.config().setReportName("D_Link Automation - " + System.getProperty("env") +" Environment");
		htmlReporter.config().setTheme(Theme.STANDARD);
		return htmlReporter;
	}

	public static ExtentTest configExtenTest(String className) {
		
		
		ExtentTest parent = extent.createTest(className);
		
		return parent;
		//ExtentTestFactory.setExtentTest(parent);
	}

	public void childMethods(Method name, String className, String testDataName) {
		// Extent Report Configuration
		ExtentTest child = ExtentTestFactoryParent.getExtentTest().createNode(testDataName);
		ExtentTestFactory.setExtentTest(child);
	}

	/**
	 * Flush the report
	 */
	public void flushReport() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		extent.flush();
	}

	/**
	 * Result Status Capture
	 * 
	 * @param result
	 */
	public void testResultCapture(ITestResult result) {
		/**
		 * Success Block
		 */
		if (result.getStatus() == ITestResult.SUCCESS) {
			ExtentTestFactory.getExtentTest().log(Status.PASS, result.getMethod().getMethodName() + " Passed");
		}
		/**
		 * Failure Block
		 */
		if (result.getStatus() == ITestResult.FAILURE) {
			ExtentTestFactory.getExtentTest().fail(result.getThrowable().getMessage());
		}
	}
}
