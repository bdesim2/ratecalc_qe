package com.ratecalc.lib.framework;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This is a config loader class that will read in config files for the tests and allocate them correctly
 * You will be able to get and set configs from this manager
 *
 * @Author Brian DeSimone
 * @Date 07/16/2018
 */
public class ConfigManager {

    // GLOBAL CLASS VARIABLES
    private static Logger logger = LogManager.getLogger(ConfigManager.class);
    private Properties prop;

    // CONFIG URLS
    private String RATE_CALC_URI;

    // CONFIG VARAIBLES
    private String testEnvironment;

    /**
     * Constructor for the ConfigManger. Loads the properties file and logs its location. Also does a generic config setup
     */
    public ConfigManager() {
        try {
            logger.info("We have created a Config Manager. Beginning to read properties!");
            prop = new Properties();
            InputStream inputStream = new FileInputStream(System.getProperty("user.dir") + "//src//config//test.properties");
            prop.load(inputStream);
            logger.info("Setting test configs from " + System.getProperty("user.dir") + "/src/config/test.properties");
            setConfigs();
            validateConfigs();
            setupTestEnvironment();
        }
        catch(IOException e) {
            logger.error("Could not find the properties file.\n" + e);
        }
    } // end constructor



    /**
     * sets all of the configs in the file to the proper global variables so we can use them later
     */
    private void setConfigs(){
        testEnvironment = prop.getProperty("testEnvironment");
        logger.info("Configs from properties file are set.");
    }



    /**
     * Validate configuration values in the properties file are not null or empty.
     */
    private void validateConfigs(){
        logger.info("We have found the following configs: ");
        for (String key : prop.stringPropertyNames()){
            String value = prop.getProperty(key);
            logger.info(key + ": " + value);
            if (key == null || key.isEmpty()){
                logger.error("Missing property from the test.properties file at: " + key);
                Assert.fail("See error logs and fix the missing test property.");
            }
        }
    }



    /**
     * Method to set up the test environment. This comes from system property first and defaults to the
     * test.properties file second as set in the setConfigs method in this class.
     */
    private void setupTestEnvironment(){
        // Set the test env
        if (getSystemProperty("test.environment") == null || getSystemProperty("test.environment").isEmpty()) {
            setSystemProperty("test.environment", getTestEnvironment());
            logger.info("There was no system testEnvironment property set. Setting from test.properties.");
            logger.info("Test environment: " + getTestEnvironment());
        }
        else {
            // Override the test environment in the config file with the system parameter
            setTestEnvironment(getSystemProperty("test.environment"));
            logger.info("The test environment is set: " + getTestEnvironment());
        }
    }

    public String getRATE_CALC_URI() {
        return RATE_CALC_URI;
    }

    public void setRATE_CALC_URI(String RATE_CALC_URI) {
        this.RATE_CALC_URI = RATE_CALC_URI;
    }

    public String getTestEnvironment() {
        return testEnvironment;
    }

    public void setTestEnvironment(String testEnvironment) {
        this.testEnvironment = testEnvironment;
    }

    public String getSystemProperty(String key) {
        return System.getProperty(key);
    }

    public void setSystemProperty(String key, String value){
        System.setProperty(key, value);
    }

}
