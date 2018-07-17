package com.ratecalc.lib.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ratecalc.lib.framework.BaseCucumberTest;
import com.ratecalc.lib.framework.CommonLibrary;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.ratecalc.lib.rest.ModifyJson.modifyJson;

/**
 * This class will hold all generic rest step definitions that interact with JSON based services
 *
 * @Author Brian DeSimone
 * @Date 07/16/2018
 */
public class GenericRestStepDefinitions extends BaseCucumberTest {

    // GLOBAL CLASS VARIABLES
    private static Logger logger = LogManager.getLogger(GenericRestStepDefinitions.class);
    private static CommonLibrary commonLibrary = new CommonLibrary();
    private static RestCommonLibrary restCommonLibrary = new RestCommonLibrary();

    // ================================================================================================================
    // GENERIC STEP DEFINITIONS FOR REST SERVICES
    // ================================================================================================================

    @Given("I send and accept (?:JSON|json)$")
    public void SetHeadersJSON() {
        memory.setAcceptHeader(MediaType.APPLICATION_JSON);
        memory.setContentTypeHeader(MediaType.APPLICATION_JSON);
        logger.debug("Content-Type header is set to: " + memory.getContentTypeHeader());
        logger.debug("Accept header is set to: " + memory.getAcceptHeader());
    }

    @Given("^I send \"(.*)\" and accept \"(.*)\"$")
    public void SetHeaders(String send, String accept) {
        memory.setAcceptHeader(accept);
        memory.setContentTypeHeader(send);
        logger.debug("Content-Type header is set to: " + memory.getContentTypeHeader());
        logger.debug("Accept header is set to: " + memory.getAcceptHeader());
    }

    @Given("^I send and accept custom headers:$")
    public void SetCustomHeaders(DataTable table) {
        for (Map<String, String> map : table.asMaps(String.class, String.class)) {
            String header = map.get("header");
            String value = map.get("value");
            if (header.toLowerCase().equals("accept")) {
                memory.setAcceptHeader(value);
            }
            else if (header.toLowerCase().equals("contenttype")) {
                memory.setContentTypeHeader(value);
            }
            else {
                logger.error("The framework does not know what type of header that is. Please check code.");
            }
        }
        logger.debug("Content-Type header is set to: " + memory.getContentTypeHeader());
        logger.debug("Accept header is set to: " + memory.getAcceptHeader());
    }

    // ================================================================================================================
    // MEMORY STEP DEFINITIONS
    // ================================================================================================================

    @Given("^I save the generated (?:JSON|json) at \"(.*)\" as \"(.*)\"$")
    public void SaveGeneratedJSON(String key, String storageName) throws IOException {
        key = commonLibrary.checkForVariables(key);
        ObjectMapper mapper = new ObjectMapper();
        String jsonAsString = mapper.writeValueAsString(memory.getGeneratedObject());
        JsonNode node = mapper.readTree(jsonAsString);
        try {
            memory.saveValue(storageName, node.get(key).textValue());
            logger.debug("Saving key: " + key + " value: " + node.get(key).textValue() + " as: " + storageName);
        } catch (NullPointerException e) {
            logger.error("We could not find the key: '" + key + "' in the generated body. Please check Cucumber Feature." + e);
            throw new NullPointerException("We could not find the key: '" + key + "' in the body. Please check Cucumber Feature.");
        }
    }

    @Given("^I save the (?:JSON|json) at \"(.*)\" as \"(.*)\"$")
    public void SaveJSON(String key, String storageName) {
        key = commonLibrary.checkForVariables(key);
        logger.debug("Saving key: " + key + " value: " + memory.getLastResponse().jsonPath().get(key).toString() + " as: " + storageName);
        memory.saveValue(storageName, memory.getLastResponse().jsonPath().get(key).toString());
    }

    @Given("^I save the String \"(.*)\" as \"(.*)\"$")
    public void SaveString(String value, String storageName) {
        logger.debug("Saving value: " + value + " as: " + storageName);
        memory.saveValue(storageName, value);
    }

    @Given("^I print the stored value at \"(.*)\"$")
    public void PrintValue(String key) {
        logger.debug("The value of the stored value at key: " + key + " = " + memory.retrieveValue(key));
    }

    // ================================================================================================================
    // GENERIC COUNTER MEMORY STEP DEFINITIONS
    // ================================================================================================================

    @Given("^I set a counter from the (?:JSON|json) response at \"(.*)\"$")
    public void SetCounterFromJson(String attribute){
        attribute = commonLibrary.checkForVariables(attribute);
        logger.debug("Saving the value: " + memory.getLastResponse().jsonPath().get(attribute).toString() + " as count for variable: " + attribute);
        memory.saveCounter(attribute, memory.getLastResponse().jsonPath().getLong(attribute));
    }

    // ================================================================================================================
    // GENERIC CUCUMBER STEP DEFINITIONS FOR REST GENERIC OBJECTS
    // ================================================================================================================

    @Given("^I generate a (?:JSON|json) body from the following:$")
    public void GenerateJSONBody(String body) {
        logger.debug("The JSON body generated is: " + body);
        memory.setGeneratedObject(body);
    }

    @Given("^I generate an object with key: \"(.*)\" value: \"(.*)\"$")
    public void GenerateGenericObject(String key, String value) {
        key = commonLibrary.checkForVariables(key);
        value = commonLibrary.checkForVariables(value);
        logger.debug("Generating a body with key: " + key + " and value: " + value);
        Map<String, Object> body = new HashMap<>();
        body.put(key, value);
        memory.setGeneratedObject(body);
    }

    @Given("^I generate an object with the following attributes:$")
    public void GenerateGenericObjectTable(Map<String, String> map) {
        logger.debug("Generating a body with the following attributes:");
        Map<String, Object> body = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            key = commonLibrary.checkForVariables(key);
            value = commonLibrary.checkForVariables(value);
            logger.debug("Adding attribute to body as: key:" + key + " value: " + value);
            body.put(key, value);
        }
        memory.setGeneratedObject(body);
    }

    @Given("^I print the generated object$")
    public void PrintGeneratedObject() {
        logger.debug("Printing the last generated object stored in memory");
        commonLibrary.logObject(memory.getGeneratedObject());
    }

    @Given("^I modify the (?:JSON|json) at \"(.*)\" to be \"(.*)\"$")
    public void ModifyJsonAttribute(String key, String value) throws IOException {
        String originalKey = key;
        value = commonLibrary.checkForVariables(value);
        logger.debug("Modifying then JSON attribute at: " + key + " to be: " + value);
        ObjectMapper mapper = new ObjectMapper();
        String jsonAsString = mapper.writeValueAsString(memory.getGeneratedObject());
        JsonNode node = mapper.readTree(jsonAsString);
        String path = "/" + key.replace(".", "/").replace("[", "/").replace("]/", "/").replace("]", "/");
        int nestedLevel = path.length() - path.replace("/", "").length();
        if (nestedLevel == 1) {
            path = "";
        }
        else {
            key = path.substring(path.lastIndexOf("/") + 1);
            path = path.substring((0), path.lastIndexOf("/"));
        }
        if (node.at(path).has(key)) {
            modifyJson(node, key, value);
            Object body = mapper.convertValue(node, Object.class);
            commonLibrary.logObject(body);
            memory.setGeneratedObject(body);
        }
        else {
            logger.error("We could not find the key: " + originalKey + " in the body. Please check Cucumber Feature.");
            throw new NullPointerException("We could not find the key: " + originalKey + " in the body. Please check Cucumber Feature.");
        }
    }

    @Given("^I modify the following attributes:$")
    public void ModifyJsonAttributeTable(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            value = commonLibrary.checkForVariables(value);
            logger.debug("Modifying then JSON attribute at: " + key + " to be: " + value);
            // TODO: Finish this
        }
    }

    // ================================================================================================================
    // GENERIC CUCUMBER STEP DEFINITIONS FOR REST ASSURED FUNCTIONS
    // ================================================================================================================

    @When("^I send a (GET|POST|PUT|DELETE|PATCH) request to \"(.*)\"$")
    public void SendBasicRest(String requestType, String destination) {
        destination = commonLibrary.checkForVariables(destination);
        Response response;
        switch(requestType.toLowerCase()) {
            case "get":
                response = restCommonLibrary.GET_REQUEST(
                        destination,
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "post":
                response = restCommonLibrary.POST_REQUEST(
                        destination,
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "put":
                response = restCommonLibrary.PUT_REQUEST(
                        destination,
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "delete":
                response = restCommonLibrary.DELETE_REQUEST(
                        destination,
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "patch":
                response = restCommonLibrary.PATCH_REQUEST(
                        destination,
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

    @When("^I send a (GET|POST|PUT|DELETE|PATCH) request to \"(.*)\" with (?:JSON|json) body:$")
    public void SendBasicRestBody(String requestType, String destination, String body) {
        Response response;
        destination = commonLibrary.checkForVariables(destination);
        switch(requestType.toLowerCase()) {
            case "get":
                response = restCommonLibrary.GET_REQUEST_BODY(
                        destination,
                        body,
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "post":
                response = restCommonLibrary.POST_REQUEST_BODY(
                        destination,
                        body,
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "put":
                response = restCommonLibrary.PUT_REQUEST_BODY(
                        destination,
                        body,
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "delete":
                response = restCommonLibrary.DELETE_REQUEST_BODY(
                        destination,
                        body,
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "patch":
                response = restCommonLibrary.PATCH_REQUEST_BODY(
                        destination,
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

    @When("^I send a (GET|POST|PUT|DELETE|PATCH) request to \"(.*)\" with the generated body$")
    public void SendRestGenBody(String requestType, String destination){
        Response response;
        destination = commonLibrary.checkForVariables(destination);
        switch(requestType.toLowerCase()) {
            case "get":
                response = restCommonLibrary.GET_REQUEST_BODY(
                        destination,
                        memory.getGeneratedObject(),
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "post":
                response = restCommonLibrary.POST_REQUEST_BODY(
                        destination,
                        memory.getGeneratedObject(),
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "put":
                response = restCommonLibrary.PUT_REQUEST_BODY(
                        destination,
                        memory.getGeneratedObject(),
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "delete":
                response = restCommonLibrary.DELETE_REQUEST_BODY(
                        destination,
                        memory.getGeneratedObject(),
                        memory.getContentTypeHeader(),
                        memory.getAcceptHeader()
                );
                break;
            case "patch":
                response = restCommonLibrary.PATCH_REQUEST_BODY(
                        destination,
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

}
