package com.dmartLabs.config;

public interface CommonConstants {

	/* **************** Environments **************** */
	String ENVIRONMENT = System.getProperty("env")!=null?System.getProperty("env"):"CANARY";

	//Add Enum on Environments
	/* **************** TestData file paths **************** */
	
	String BASE_URL = BaseURL.valueOf(ENVIRONMENT).getBaseURL();
	String TEST_DATA_PATH = TestDataPath.valueOf(ENVIRONMENT).getTestDataPath();

	/**
	 * Enum BaseURL
	 * 
	 * @@author  
	 *
	 */
	enum BaseURL {
		LOCAL("https://petstore.swagger.io/v2"), STAGING("https://petstore.swagger.io/v2"), CANARY(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "BASE_URL"));

		private String baseURl;

		public String getBaseURL() {
			return this.baseURl;
		}

		private BaseURL(String baseURl) {
			this.baseURl = baseURl;
		}
	}

	/**
	 * Enum TestDataPath
	 * 
	 * @author 
	 *
	 */
	enum TestDataPath {
		LOCAL("src/test/resources/features/TestData/Canary/"), STAGING("src/test/resources/features/TestData/Canary/"),
		CANARY("src/test/resources/features/TestData/Canary/");

		private String testDataPath;

		public String getTestDataPath() {
			return this.testDataPath;
		}

		private TestDataPath(String testDataPath) {
			this.testDataPath = testDataPath;
		}
	}
}