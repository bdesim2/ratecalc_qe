package com.ratecalc.lib.framework;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.PickleEventWrapper;
import cucumber.api.testng.TestNGCucumberRunner;
import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;

/**
 * Class for all test framework utilities. Also contains all before and after setup steps.
 *
 * This class will act as a test runner for cucumber from within testNG.
 * Dynamic data allocation from each feature file is done here.
 *
 * @Author Brian DeSimone
 * @Date 07/16/2018
 */
@CucumberOptions(
        plugin = {"pretty"},
        format = {"html:test-reports/cucumber-features.html", "json:test-reports/cucumber.json"},
        features = "src/test/resources",
        glue = {"ratecalc.lib.framework", "ratecalc.lib.rest", "ratecalc.lib.applicationStepDefinitions"}
)
public class BaseCucumberTest {

    // GLOBAL CLASS VARIABLES
    private static TestNGCucumberRunner testNGCucumberRunner;
    private static Logger logger = LogManager.getLogger(BaseCucumberTest.class);
    public static ConfigManager configManager;
    public static Memory memory;



    /**
     * Method to initialize all configs from properties files and logging
     */
    private void Initialize() {
        // Setup the Config Manager
        configManager = new ConfigManager();

        // Set the BASE URIs
        configManager.setRATE_CALC_URI("http://localhost:8080");

    } // end Initialize



    // ================================================================================================================
    // TESTNG ANNOTATIONS & CUCUMBER SETUP
    // ================================================================================================================

    /**
     * Before entire test suite we need to setup everything we will need.
     */
    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {
        logger.info("Cucumber Test Framework for QE-RATECALC initialized!");
        logger.info("Logging initialized: All logs are located at " + System.getProperty("user.dir") + "/src/logs/ratecalc-qe-tests.log");
        Initialize();
        logger.info("Done with BeforeSuite setup! TESTS BEGINNING!\n\n");
    } // End TestSetup

    /**
     * Before class setup to initialize the cucumber runner.
     */
    @BeforeClass(alwaysRun = true)
    public void setupClass() {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    /**
     * After class to tear down the runner for cucumber.
     */
    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        testNGCucumberRunner.finish();
    }

    /**
     * After the entire test suite clean up rest assured
     */
    @AfterSuite(alwaysRun = true)
    public void cleanUp() {
        RestAssured.reset();
        logger.info("\n\n");
        logger.info("Rest Assured framework has been reset because all tests have been executed.");
        logger.info("TESTING COMPLETE: SHUTTING DOWN FRAMEWORK!!");
    } // end cleanUp

    /**
     * The main test that will kick off each scenario as a TestNG test case.
     */
    @Test(description = "Runs Cucumber Scenarios!", dataProvider = "scenarios")
    public void scenario(PickleEventWrapper pickleWrapper, CucumberFeatureWrapper featureWrapper) throws Throwable {
        testNGCucumberRunner.runScenario(pickleWrapper.getPickleEvent());
    }

    /**
     * The data provider that goes and gets all the scenarios from the feature files and injects them into each TestNG test.
     */
    @DataProvider
    public Object[][] scenarios() {
        return testNGCucumberRunner.provideScenarios();
    }

} // end class BaseTestCase
