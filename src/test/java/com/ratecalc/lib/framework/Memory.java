package com.ratecalc.lib.framework;

import io.restassured.response.Response;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple memory class that will store last response from rest assured for use inside other cucumber steps
 *
 * @Author Brian DeSimone
 * @Date 07/16/2018
 */
public class Memory {

    private Response lastResponse;
    private String contentTypeHeader = MediaType.APPLICATION_JSON;      // DEFAULTS TO JSON
    private String acceptHeader = MediaType.APPLICATION_JSON;           // DEFAULTS TO JSON
    private Map<String, String> storage = new HashMap<>();
    private Object generatedObject;
    private Map<String, Long> counter = new HashMap<>();

    // FUNCTIONS OF MEMORY
    public void saveValue(String key, String value){
        storage.put(key, value);
    }

    public String retrieveValue(String key){
        return storage.get(key);
    }

    // FUNCTIONS OF COUNTER
    public void saveCounter(String key, Long count){
        counter.put(key, count);
    }

    public Long retrieveCount(String key){
        return counter.get(key);
    }

    // GETTERS AND SETTERS
    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    }

    public String getContentTypeHeader() {
        return contentTypeHeader;
    }

    public void setContentTypeHeader(String contentTypeHeader) {
        this.contentTypeHeader = contentTypeHeader;
    }

    public String getAcceptHeader() {
        return acceptHeader;
    }

    public void setAcceptHeader(String acceptHeader) {
        this.acceptHeader = acceptHeader;
    }

    public Map<String, String> getStorage() {
        return storage;
    }

    public void setStorage(Map<String, String> storage) {
        this.storage = storage;
    }

    public Object getGeneratedObject() {
        return generatedObject;
    }

    public void setGeneratedObject(Object generatedObject) {
        this.generatedObject = generatedObject;
    }
} // end class Memory
