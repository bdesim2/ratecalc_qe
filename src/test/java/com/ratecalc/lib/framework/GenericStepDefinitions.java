package com.ratecalc.lib.framework;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class will link all of the common functions to cucumber step defs.
 *
 * @Author Brian DeSimone
 * @Date 07/16/2018
 */
public class GenericStepDefinitions extends BaseCucumberTest {

    // GLOBAL CLASS VARIABLES
    private static Logger logger = LogManager.getLogger(GenericStepDefinitions.class);

    // ================================================================================================================
    // BEFORE AND AFTER HOOKS AT TEST LEVEL
    // ================================================================================================================

    @Before
    public void onTestBoot(Scenario scenario) {
        memory = new Memory();
        logger.debug("TESTING: " + scenario.getName());
    }


    @After
    public void onTestComplete(Scenario scenario) {
        logger.debug("TEST STATUS: " + scenario.getStatus());
        logger.debug("\n\n");
    }

    @Given("^I wait for (\\d+) seconds$")
    public void waitSeconds(int seconds) throws InterruptedException {
        logger.debug("Sleeping for " + seconds + " seconds.");
        Thread.sleep(seconds * 1000);
    }

}