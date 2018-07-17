package com.ratecalc.lib.applicationStepDefinitions;

import com.ratecalc.lib.framework.BaseCucumberTest;
import com.ratecalc.pojos.RateModel;
import cucumber.api.java.en.Given;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class that holds all cucumber step definitions specific to the rate calculator API
 *
 * @Author Brian DeSimone
 * @Date 07/16/2018
 */
public class RateCalcStepDefinitions extends BaseCucumberTest {

    // GLOBAL CLASS VARIABLES
    private static final Logger logger = LogManager.getLogger(RateCalcStepDefinitions.class);

    @Given("^I generate a rate request object$")
    public void generateRateRequestObject(){
        logger.debug("Generating a rate request object");
        String startRate = memory.retrieveValue("startRate");
        String endRate = memory.retrieveValue("endRate");
        RateModel rateModel = new RateModel(startRate, endRate);
        memory.setGeneratedObject(rateModel);
    }

}
