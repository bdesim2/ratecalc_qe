package com.ratecalc.lib.applicationStepDefinitions;

import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ratecalc.lib.framework.BaseCucumberTest;
import com.ratecalc.lib.framework.CommonLibrary;
import com.ratecalc.lib.rest.RestCommonLibrary;

/**
 * This class will hold specific generic step definitions that are tailored to the specific app in test
 *
 * @Author Brian DeSimone
 * @Date 07/16/2018
 */
public class GenericApplicationStepDefinitions extends BaseCucumberTest {

    // GLOBAL CLASS VARIABLES
    private static Logger logger = LogManager.getLogger(GenericApplicationStepDefinitions.class);
    private static CommonLibrary commonLibrary = new CommonLibrary();
    private static RestCommonLibrary restCommonLibrary = new RestCommonLibrary();

    // ================================================================================================================
    // GENERIC CUCUMBER STEP DEFINITIONS FOR REST ASSURED FUNCTIONS
    // ================================================================================================================

    @When("^I send (?:a|an) (RATECALC) (GET|POST|PUT|DELETE|PATCH) request to \"(.*)\"$")
    public void SendBasicRest(String application, String requestType, String destination) {
        application = getServiceURI(application);
        destination = commonLibrary.checkForVariables(destination);
        Response response;
        switch(requestType.toLowerCase()) {
            case "get":
                response = restCommonLibrary.GET_REQUEST(
                        application + destination,
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "post":
                response = restCommonLibrary.POST_REQUEST(
                        application + destination,
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "put":
                response = restCommonLibrary.PUT_REQUEST(
                        application + destination,
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "delete":
                response = restCommonLibrary.DELETE_REQUEST(
                        application + destination,
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "patch":
                response = restCommonLibrary.PATCH_REQUEST(
                        application + destination,
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            default:
                // we should never get here!
                logger.error("Invalid type of REST request: " + requestType + "Please check cucumber feature.");
                throw new IllegalArgumentException("Invalid type of REST request: " + requestType);
        } // end switch of request type
        memory.setLastResponse(response);
    }

    @When("^I send (?:a|an) (RATECALC) (GET|POST|PUT|DELETE|PATCH) request to \"(.*)\" with (?:JSON|json) body:$")
    public void SendBasicRestBody(String application, String requestType, String destination, String body) {
        application = getServiceURI(application);
        Response response;
        destination = commonLibrary.checkForVariables(destination);
        switch(requestType.toLowerCase()) {
            case "get":
                response = restCommonLibrary.GET_REQUEST_BODY(
                        application + destination,
                        body,
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "post":
                response = restCommonLibrary.POST_REQUEST_BODY(
                        application + destination,
                        body,
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "put":
                response = restCommonLibrary.PUT_REQUEST_BODY(
                        application + destination,
                        body,
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "delete":
                response = restCommonLibrary.DELETE_REQUEST_BODY(
                        application + destination,
                        body,
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "patch":
                response = restCommonLibrary.PATCH_REQUEST_BODY(
                        application + destination,
                        body,
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            default:
                // we should never get here!
                logger.error("Invalid type of REST request: " + requestType + "Please check cucumber feature.");
                throw new IllegalArgumentException("Invalid type of REST request: " + requestType);
        } // end switch of request type
        memory.setLastResponse(response);
    }

    @When("^I send (?:a|an) (RATECALC) (GET|POST|PUT|DELETE|PATCH) request to \"(.*)\" with the generated body$")
    public void SendRestGenBody(String application, String requestType, String destination){
        application = getServiceURI(application);
        Response response;
        destination = commonLibrary.checkForVariables(destination);
        switch(requestType.toLowerCase()) {
            case "get":
                response = restCommonLibrary.GET_REQUEST_BODY(
                        application + destination,
                        memory.getGeneratedObject(),
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "post":
                response = restCommonLibrary.POST_REQUEST_BODY(
                        application + destination,
                        memory.getGeneratedObject(),
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "put":
                response = restCommonLibrary.PUT_REQUEST_BODY(
                        application + destination,
                        memory.getGeneratedObject(),
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "delete":
                response = restCommonLibrary.DELETE_REQUEST_BODY(
                        application + destination,
                        memory.getGeneratedObject(),
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "patch":
                response = restCommonLibrary.PATCH_REQUEST_BODY(
                        application + destination,
                        memory.getGeneratedObject(),
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            default:
                // we should never get here!
                logger.error("Invalid type of REST request: " + requestType + "Please check cucumber feature.");
                throw new IllegalArgumentException("Invalid type of REST request: " + requestType);
        } // end switch of request type
        memory.setLastResponse(response);
    }

    /**
     * This method will get the service URI for specific services that are in test so we can call them in cucumber
     * in a nice english format that is clean for people to read and understand
     */
    private String getServiceURI(String application){
        switch (application.toUpperCase()){
            case "RATECALC":
                return configManager.getRATE_CALC_URI();
            default:
                // we should never get here!
                logger.error("Invalid type of service to test: The framework does not know what " + application + " is. Please check cucumber feature file.");
                throw new IllegalArgumentException("Invalid type of service to test: The framework does not know what " + application + " is. Please check cucumber feature file.");
        }
    }

}
