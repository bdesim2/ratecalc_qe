package com.ratecalc.lib.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ratecalc.lib.framework.BaseCucumberTest;

import static io.restassured.RestAssured.given;

/**
 * This class holds all REST common functions that are sent with rest assured
 *
 * @Author Brian DeSimone
 * @Date 07/16/2018
 */
public class RestCommonLibrary extends BaseCucumberTest {

    // GLOBAL CLASS VARIABLES
    private static Logger logger = LogManager.getLogger(RestCommonLibrary.class);

    // ================================================================================================================
    // REST ASSURED COMMON GENERIC FUNCTIONS
    // ================================================================================================================

    /**
     * REST ASSURED GET request method
     *
     * @param url destination of the request
     * @return Response object that has the REST response
     */
    public Response GET_REQUEST(String url, String contentHeader, String acceptHeader) {
        logger.debug("REST-ASSURED: Sending a GET request to " + url);
        Response getResponse = given()
                .contentType(contentHeader)
                .accept(acceptHeader)
                .log()
                .all()
                .when()
                .get(url)
                .then()
                .extract()
                .response();
        // log then response
        logger.debug("REST-ASSURED: The response from the request is: " + getResponse.asString());
        return getResponse;
    } // end GET_REQUEST



    /**
     * REST ASSURED GET request method with body
     *
     * @param body the body of the request
     * @param url destination of the request
     * @return Response object that has the REST response
     */
    public Response GET_REQUEST_BODY(String url, Object body, String contentHeader, String acceptHeader) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String bodyAsString = mapper.writeValueAsString(body);
            logger.debug("REST-ASSURED: Sending a GET request to " + url);
            logger.debug("REST-ASSURED: The GET request is sent with the body: " + bodyAsString);
        }
        catch (JsonProcessingException e) {
            logger.error("JSON PARSE ERROR: " + e);
        }
        Response getResponse = given()
                .body(body)
                .contentType(contentHeader)
                .accept(acceptHeader)
                .log()
                .all()
                .when()
                .get(url)
                .then()
                .extract()
                .response();
        // log then response
        logger.debug("REST-ASSURED: The response from the request is: " + getResponse.asString());
        return getResponse;
    } // end GET_REQUEST



    /**
     * REST ASSURED POST request method
     *
     * @param url destination of the request
     * @return Response object that has the REST response
     */
    public Response POST_REQUEST(String url, String contentHeader, String acceptHeader) {
        logger.debug("REST-ASSURED: Sending a POST request to " + url);
        Response postResponse = given()
                .contentType(contentHeader)
                .accept(acceptHeader)
                .log()
                .all()
                .when()
                .post(url)
                .then()
                .extract()
                .response();
        // log then response
        logger.debug("REST-ASSURED: The response from the request is: " + postResponse.asString());
        return postResponse;
    } // end POST_REQUEST



    /**
     * REST ASSURED POST request method with body
     *
     * @param url destination of the request
     * @param body the body we wish to post with the request
     * @return Response object that has the REST response
     */
    public Response POST_REQUEST_BODY(String url, Object body, String contentHeader, String acceptHeader) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String bodyAsString = mapper.writeValueAsString(body);
            logger.debug("REST-ASSURED: Sending a POST request to " + url);
            logger.debug("REST-ASSURED: The POST request is sent with the body: " + bodyAsString);
        }
        catch (JsonProcessingException e) {
            logger.error("JSON PARSE ERROR: " + e);
        }
        Response postResponse = given()
                .body(body)
                .contentType(contentHeader)
                .accept(acceptHeader)
                .log()
                .all()
                .when()
                .post(url)
                .then()
                .extract()
                .response();
        // log then response
        logger.debug("REST-ASSURED: The response from the request is: " + postResponse.asString());
        return postResponse;
    } // end POST_REQUEST



    /**
     * REST ASSURED PUT request method
     *
     * @param url destination of the request
     * @return Response object that has the REST response
     */
    public Response PUT_REQUEST(String url, String contentHeader, String acceptHeader) {
        logger.debug("REST-ASSURED: Sending a PUT request to " + url);
        Response putResponse = given()
                .contentType(contentHeader)
                .accept(acceptHeader)
                .log()
                .all()
                .when()
                .put(url)
                .then()
                .extract()
                .response();
        // log then response
        logger.debug("REST-ASSURED: The response from the request is: " + putResponse.asString());
        return putResponse;
    } // end PUT_REQUEST



    /**
     * REST ASSURED PUT request method
     *
     * @param body the body of the request
     * @param url destination of the request
     * @return Response object that has the REST response
     */
    public Response PUT_REQUEST_BODY(String url, Object body, String contentHeader, String acceptHeader) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String bodyAsString = mapper.writeValueAsString(body);
            logger.debug("REST-ASSURED: Sending a PUT request to " + url);
            logger.debug("REST-ASSURED: The PUT request is sent with the body: " + bodyAsString);
        }
        catch (JsonProcessingException e) {
            logger.error("JSON PARSE ERROR: " + e);
        }
        Response putResponse = given()
                .body(body)
                .contentType(contentHeader)
                .accept(acceptHeader)
                .log()
                .all()
                .when()
                .put(url)
                .then()
                .extract()
                .response();
        // log then response
        logger.debug("REST-ASSURED: The response from the request is: " + putResponse.asString());
        return putResponse;
    } // end PUT_REQUEST



    /**
     * REST ASSURED DELETE request method
     *
     * @param url destination of the request
     * @return Response object that has the REST response
     */
    public Response DELETE_REQUEST(String url, String contentHeader, String acceptHeader) {
        logger.debug("REST-ASSURED: Sending a DELETE request to " + url);
        Response deleteRequest = given()
                .contentType(contentHeader)
                .accept(acceptHeader)
                .log()
                .all()
                .when()
                .delete(url)
                .then()
                .extract()
                .response();
        // log then response
        logger.debug("REST-ASSURED: The response from the request is: " + deleteRequest.asString());
        return deleteRequest;
    } // end DELETE_REQUEST



    /**
     * REST ASSURED DELETE request method
     *
     * @param body the body of the request
     * @param url destination of the request
     * @return Response object that has the REST response
     */
    public Response DELETE_REQUEST_BODY(String url, Object body, String  contentHeader, String acceptHeader) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String bodyAsString = mapper.writeValueAsString(body);
            logger.debug("REST-ASSURED: Sending a DELETE request to " + url);
            logger.debug("REST-ASSURED: The DELETE request is sent with the body: " + bodyAsString);
        }
        catch (JsonProcessingException e) {
            logger.error("JSON PARSE ERROR: " + e);
        }
        Response deleteRequest = given()
                .body(body)
                .contentType(contentHeader)
                .accept(acceptHeader)
                .log()
                .all()
                .when()
                .delete(url)
                .then()
                .extract()
                .response();
        // log then response
        logger.debug("REST-ASSURED: The response from the request is: " + deleteRequest.asString());
        return deleteRequest;
    } // end DELETE_REQUEST



    /**
     * REST ASSURED PATCH request method
     *
     * @param url destination of the request
     * @return Response object that has the REST response
     */
    public Response PATCH_REQUEST(String url, String contentHeader, String acceptHeader) {
        logger.debug("REST-ASSURED: Sending a PATCH request to " + url);
        Response patchRequest = given()
                .contentType(contentHeader)
                .accept(acceptHeader)
                .log()
                .all()
                .when()
                .patch(url)
                .then()
                .extract()
                .response();
        // log then response
        logger.debug("REST-ASSURED: The response from the request is: " + patchRequest.asString());
        return patchRequest;
    } // end PATCH_REQUEST



    /**
     * REST ASSURED PATCH request method
     *
     * @param body the body of the request
     * @param url destination of the request
     * @return Response object that has the REST response
     */
    public Response PATCH_REQUEST_BODY(String url, Object body, String  contentHeader, String acceptHeader) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String bodyAsString = mapper.writeValueAsString(body);
            logger.debug("REST-ASSURED: Sending a PATCH request to " + url);
            logger.debug("REST-ASSURED: The PATCH request is sent with the body: " + bodyAsString);
        }
        catch (JsonProcessingException e) {
            logger.error("JSON PARSE ERROR: " + e);
        }
        Response patchRequest = given()
                .body(body)
                .contentType(contentHeader)
                .accept(acceptHeader)
                .log()
                .all()
                .when()
                .patch(url)
                .then()
                .extract()
                .response();
        // log then response
        logger.debug("REST-ASSURED: The response from the request is: " + patchRequest.asString());
        return patchRequest;
    } // end PATCH_REQUEST

}
