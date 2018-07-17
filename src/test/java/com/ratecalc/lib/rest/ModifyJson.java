package com.ratecalc.lib.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ratecalc.lib.framework.CommonLibrary;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifyJson {

    // GLOBAL CLASS VARIABLES
    public static CommonLibrary commonLibrary = new CommonLibrary();

    public static void modifyJson(JsonNode parent, String fieldName, String newValue) {
        if (parent.has(fieldName)) {
            if (newValue.equalsIgnoreCase("null")){
                ((ObjectNode)parent).remove(fieldName);
            }
            else {
                if (newValue.contains("random")) {
                    Pattern pattern = Pattern.compile(" ");
                    Matcher matcher = pattern.matcher(newValue);
                    int randomChars;
                    if (matcher.find()) {
                        randomChars = new Integer(newValue.substring(matcher.end()));
                        newValue = commonLibrary.generateRandomString(randomChars);
                    }
                }
                ((ObjectNode) parent).put(fieldName, newValue);
            }
        }
        // TODO: WE ONLY DO THE BELOW UNTIL WE CHANGE A VALUE! This needs to be edited.
        // Now, recursively invoke this method on all properties
        for (JsonNode child : parent) {
            modifyJson(child, fieldName, newValue);
        }
    }
}