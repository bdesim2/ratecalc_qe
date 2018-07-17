package com.ratecalc.lib.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

/**
 * This class will hold all generic and common functions. This includes generic REST operations from RESTAssured
 *
 * @Author Brian DeSimone
 * @Date 07/16/2018
 */
public class CommonLibrary extends BaseCucumberTest {

    // GLOBAL CLASS VARIABLES
    private static Logger logger = LogManager.getLogger(CommonLibrary.class);

    // ================================================================================================================
    // GENERIC FUNCTIONS
    // ================================================================================================================

    /**
     * Take an input string and change all vars if present to the memory value
     * This method is recursive
     * @param inputStr the string without the actual var
     * @return the string with the vars replaced
     */
    public String checkForVariables(String inputStr){
        String theDestination = inputStr;
        if (!inputStr.isEmpty()) {
            if (inputStr.contains("%")) {
                int start = inputStr.indexOf('{');
                int end = inputStr.indexOf('}');
                String temp = inputStr.substring(start + 1, end);
                if (memory.retrieveValue(temp) == null){
                    logger.error("We could not find a value stored in memory with the key: " + temp + ". Please check your feature to make sure it was stored properly.");
                    throw new NullPointerException("We could not find a value stored in memory with the key: " + temp + ". Please check your feature to make sure it was stored properly.");
                }
                String theVar = memory.retrieveValue(temp);
                theDestination = inputStr.substring(0, (start - 1)) + theVar + inputStr.substring(end + 1, inputStr.length());
                return checkForVariables(theDestination);
            }
            return theDestination;
        }
        return theDestination;
    }

    /**
     * This method logs the object that is created as a pojo using jackson object mapper.
     * @param rawObject the raw java object that is logged as json
     */
    public void logObject(Object rawObject){
        try {
            ObjectMapper mapper = new ObjectMapper();
            logger.debug("JSON-OBJECT: Logging object we created: " + mapper.writeValueAsString(rawObject));
        }
        catch (JsonProcessingException ex){
            logger.error("Could not parse object: " + ex);
        }
    }

    // ================================================================================================================
    // GENERIC GENERATION FUNCTIONS
    // ================================================================================================================

    /**
     * This method will generate a random name
     */
    public String generateRandomString(int stringLength){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder stringBuilder = new StringBuilder();
        Random rnd = new Random();
        while (stringBuilder.length() <= stringLength) { // length of the random string.
            int index = (int) (rnd.nextFloat() * chars.length());
            stringBuilder.append(chars.charAt(index));
        }
        return stringBuilder.toString();
    }

    /**
     * This method will generate a random integer based on the length provided
     */
    public int generateRandomInteger(int integerLength){
        return new Random().nextInt((integerLength * 100) + 99);
    }

    /**
     * This method will generate a random integer based on length provided but return it as a string
     */
    public String generateRandomIntegerAsString(int integerLength){
        String chars = "1234567890";
        StringBuilder stringBuilder = new StringBuilder();
        Random rnd = new Random();
        while (stringBuilder.length() <= integerLength - 1) { // length of the random string.
            int index = (int) (rnd.nextFloat() * chars.length());
            stringBuilder.append(chars.charAt(index));
        }
        return stringBuilder.toString();
    }

    /**
     * This method will generate a random email and return it as a string
     */
    public String generateRandomEmail(){
        return generateRandomString(12) + "@QEmail.com";
    }

    /**
     * This method will generate a random phone number and return it as a string
     */
    public String genRandomPhoneNumber(){
        int num1, num2, num3;
        String set1, set2, set3;
        Random rand = new Random();
        // set 1 (area code)
        num1 = rand.nextInt(7) + 1; // never 0
        num2 = rand.nextInt(8);
        num3 = rand.nextInt(8);
        set1 = Integer.toString(num1) + Integer.toString(num2) + Integer.toString(num3);
        // set 2 (max set = 742)
        set2 = Integer.toString(rand.nextInt(643) + 100);
        //set 3 (max set = 9999)
        set3 = Integer.toString(rand.nextInt(8999) + 1000);
        return set1 + set2 + set3;
    }
}