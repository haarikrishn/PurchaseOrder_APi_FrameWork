package com.dmartLabs.config;

import com.dmartLabs.commonutils.ExtentReportManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Properties;
import java.util.logging.Logger;



/**
 * Gets the values from properties files
 */
public class PropertyReader implements CommonConstants {
	private static String sourceClass = PropertyReader.class.getName();
	private static String sourceMethod;
	static Logger LOGGER = Logger.getLogger(sourceClass);

	/**
	 * Gets the values from properties files
	 *
	 * @param path
	 * @param key
	 * @return propertyValue
	 */
	public static String getProperty(String path, String key) {
		sourceMethod = "getProperty";
		LOGGER.entering(sourceClass, sourceMethod);

		LOGGER.info("key " + key);
		String propertyValue = "";
		try {
			Properties p = new Properties();
			p.load(new FileInputStream(path));
			propertyValue = p.getProperty(key);

		} catch (Exception e) {
			e.printStackTrace();
		}
		LOGGER.info("propertyValue " + propertyValue);
		if(propertyValue.startsWith("/")){
			ExtentReportManager.logInfoDetails("Endpoint is : "+propertyValue);
		}

		LOGGER.exiting(sourceClass, sourceMethod);
		return propertyValue;

	}

	/**
	 * getPropertyValue - Gets propertyValue for the given propertyPath and
	 * propertyKey
	 *
	 * @param propertyKey
	 * @return propertyValue
	 * @author Ooviya
	 */
	public static String getPropertyValue(String propertyPath, String propertyKey) {
		sourceMethod = "getPropertyValue";
		LOGGER.entering(sourceClass, sourceMethod);

		LOGGER.info("propertyKey " + propertyKey);

		String propertyValue = PropertyReader.getProperty(propertyPath, propertyKey + "_" + ENVIRONMENT);
		LOGGER.info("propertyValue " + propertyValue);

		LOGGER.exiting(sourceClass, sourceMethod);

		return propertyValue;
	}

	public static FileReader fileReader(String path, String filePath) {

		try {
			return new FileReader(path + filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static File fileReaders(String path, String filePath) {

		try {
			return new File(path + filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}