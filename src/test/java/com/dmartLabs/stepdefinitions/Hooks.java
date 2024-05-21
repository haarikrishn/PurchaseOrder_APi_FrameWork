package com.dmartLabs.stepdefinitions;

import java.io.IOException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.dmartLabs.commonutils.ExtentReportManager;
import com.dmartLabs.commonutils.ExtentTestFactory;
import com.dmartLabs.commonutils.RestListener;
import com.dmartLabs.config.CommonConstants;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;

public class Hooks extends RestListener implements CommonConstants {
	
	public static ExtentTestFactory extentTestFactory;
	
	public static ExtentReports extentReports;
	public static ExtentTest extentTest;

	public Hooks() {
		extentTestFactory = new ExtentTestFactory();
	}

	@Before
	public void loadReportFile(Scenario scenario) {

		String fileName = ExtentReportManager.getReportNameWithTimeStamp();
		String fullReportPath = System.getProperty("user.dir") + "\\reports\\" + fileName;
		extentReports = ExtentReportManager.createInstance(fullReportPath, "QickFill API Automation Report",
				"Test ExecutionReport");
		configExtenTest(scenario);
	}
	
	public static void configExtenTest(Scenario scenario) {
		 extentTest = extentReports.createTest(scenario.getName().toString());
		   ExtentTestFactory.setExtentTest(extentTest);
	}
	
	@BeforeStep(order = 1)
	public void onBeforeStep() throws IOException {
		

		extentTest = ExtentTestFactory.getExtentTest();

	}
	

	@AfterStep(order = 1)
	public void addScreenshot() throws IOException {

	}

	@After
	public void onFinish(Scenario scenario) {
		if (extentReports != null)
			extentReports.flush();
	}
}