package com.dmartLabs.config;

public interface CommonConstants {

	/* **************** Environments **************** */
	String ENVIRONMENT = System.getProperty("env")!=null?System.getProperty("env"):"CANARY";

	//Add Enum on Environments
	/* **************** TestData file paths **************** */
	
	String BASE_URL = BaseURL.valueOf(ENVIRONMENT).getBaseURL();
	String BASE_URL1 = BaseURL1.valueOf(ENVIRONMENT).getBaseURL1();
    String BASE_URLid = BaseURLid.valueOf(ENVIRONMENT).getBaseURLid();


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

	enum BaseURL1 {
		LOCAL("https://petstore.swagger.io/v2"), STAGING("https://petstore.swagger.io/v2"), CANARY(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "BASE_URL1"));

		private String baseURl1;

		public String getBaseURL1() {
			return this.baseURl1;
		}

		private BaseURL1(String baseURl1) {
			this.baseURl1 = baseURl1;
		}
	}

    enum BaseURLid {
        LOCAL("https://petstore.swagger.io/v2"), STAGING("https://petstore.swagger.io/v2"), CANARY(PropertyReader.getProperty(ConStants.ENDPOINTS_PATHS_PROPERTIES_PATH, "BASE_URLid"));

        private String baseURlid;

        public String getBaseURLid() {
            return this.baseURlid;
        }

        private BaseURLid(String baseURl1) {
            this.baseURlid = baseURl1;
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