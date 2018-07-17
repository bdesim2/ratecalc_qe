package com.ratecalc.lib.rest;

import cucumber.api.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.collection.IsCollectionWithSize;
import com.ratecalc.lib.enums.DataType;
import com.ratecalc.lib.framework.BaseCucumberTest;
import com.ratecalc.lib.framework.CommonLibrary;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

/**
 * This class will be where all asserts for the test framework are held. These will be generic
 * re-usable methods that will assert based on input.
 *
 * @Author Brian DeSimone
 * @Date 07/16/2018
 */
public class RestAssertStepDefinitions extends BaseCucumberTest {

    // GLOBAL CLASS VARIABLES
    private static Logger logger = LogManager.getLogger(RestAssertStepDefinitions.class);
    private static CommonLibrary commonLibrary = new CommonLibrary();


    // ================================================================================================================
    // GENERIC COMMON ASSERTION STEP DEFINITIONS
    // ================================================================================================================

    @Then("^the response status should be \"(\\d+)\"$")
    public void AssertResponseStatus(int statusCode) throws Throwable {
        logger.debug("Asserting that the expected response status: " + statusCode + ", equals the service response code: " + memory.getLastResponse().statusCode());
        assertThat(
                "The expected response status code was: " + statusCode + ", but we found: " + memory.getLastResponse().statusCode(),
                memory.getLastResponse().statusCode(),
                is(statusCode)
        );
    }

    @Then("^the response headers should be (?:JSON|json)$")
    public void AssertJSONHeaders() {
        logger.debug("Asserting that the response Content-Type headers are application/json");
        assertThat(
                "The expected Content-Type header was application/json;charset=UTF-8, but we found: " + memory.getLastResponse().contentType(),
                memory.getLastResponse().contentType(),
                is("application/json;charset=UTF-8")
        );
    }

    @Then("^the response headers should be \"(.*)\"$")
    public void AssertResponseTypeHeader(String header) {
        logger.debug("Asserting that the response Content-Type header is: " + header);
        assertThat(
                "The expected Content-Type header was: " + header + ", but we found: " + memory.getLastResponse().contentType(),
                memory.getLastResponse().contentType(),
                is(header)
        );
    }

    @Then("^the \"(.*)\" header should be \"(.*)\"$")
    public void AssertCustomHeader(String name, String header) {
        logger.debug("Asserting that the response " + name + " header is: " + header);
        assertThat(
                "The expected " + name + " header was: " + header + ", but we found: " + memory.getLastResponse().contentType(),
                memory.getLastResponse().header(name),
                is(header)
        );
    }

    // TODO: Validate table headers

    @Then("^the response should( not|) be null$")
    public void AssertResponseNotNull(String negate) {
        if (negate.isEmpty()) {
            logger.debug("Asserting that the response is null.");
            assertThat(
                    "Expected then response to be null, but it was not.",
                    memory.getLastResponse().body().asString(),
                    nullValue()
            );
        } else if (!negate.isEmpty()) {
            logger.debug("Asserting that the response is not null.");
            assertThat(
                    "Expected the response to not be null, but it was",
                    memory.getLastResponse().body().asString(),
                    notNullValue()
            );
        }
    }

    @Then("^the response should( not|) have no content")
    public void AssertResponseNoContent(String negate) {
        if (negate.isEmpty()) {
            logger.debug("Asserting that the response has no content.");
            assertThat(
                    "Expected then response to have no content, but the response was: " + memory.getLastResponse().asString(),
                    memory.getLastResponse().body().asString(),
                    is("")
            );
        } else if (!negate.isEmpty()) {
            logger.debug("Asserting that the response does not have no content.");
            assertThat(
                    "Expected the response to have content, but it had no content.",
                    memory.getLastResponse().body().asString(),
                    not(is(""))
            );
        }
    }

    // ================================================================================================================
    // COUNTER MEMORY ASSERTION STEP DEFINITIONS
    // ================================================================================================================

    @Then("^the count at \"(.*)\" should (increase|decrease) by (\\d+)$")
    public void AssertCountIncreaseDecrease(String attribute, String type, int amount){
        logger.debug("Asserting that the count for " + attribute + " has " + type + " by " + amount);
        Long beforeCount = memory.retrieveCount(attribute);
        switch (type.toUpperCase()){
            case "INCREASE":
                assertThat(
                        "Expected the before count: " + beforeCount + " to increase by: " + amount + " for " + attribute + ", but the count was: " + memory.getLastResponse().jsonPath().get(attribute),
                        memory.getLastResponse().jsonPath().getLong(attribute),
                        is(beforeCount + amount)
                );
                break;
            case "DECREASE":
                assertThat(
                        "Expected the before count: " + beforeCount + " to decrease by: " + amount + " for " + attribute + ", but the count was: " + memory.getLastResponse().jsonPath().get(attribute),
                        memory.getLastResponse().jsonPath().getLong(attribute),
                        is(beforeCount - amount)
                );
                break;
            default:
                logger.error("Invalid type of operation: " + type + "Please check cucumber feature.");
                throw new IllegalArgumentException("Invalid type of operation: " + type + "Please check cucumber feature.");
        }
    }

    // ================================================================================================================
    // COMMON ASSERTION JSON STEP DEFINITIONS
    // ================================================================================================================

    // TODO: Fix counting of elements inside nested arrays/hashes. Also the read out of the error in actual size vs the element itself
    @Then("^the (?:JSON|json) response at \"(.*)\" should( not|) have \"(\\d+)\"")
    public void AssertCollectionSize(String path, String negate, int size) {
        if (negate.isEmpty()) {
            logger.debug("Asserting that the response collection at " + path + " has " + size + " items.");
            assertThat(
                    "Expected to find " + size + " items at " + path + ", but we found " + memory.getLastResponse().jsonPath().get(path),
                    memory.getLastResponse().jsonPath().get(path),
                    IsCollectionWithSize.hasSize(size)
            );
        } else if (!negate.isEmpty()) {
            logger.debug("Asserting that the response collection at " + path + " does not have " + size + " items.");
            assertThat(
                    "Expected to find " + size + " items at " + path + ", but we found " + memory.getLastResponse().jsonPath().get(path),
                    memory.getLastResponse().jsonPath().get(path),
                    not(IsCollectionWithSize.hasSize(size))
            );
        }
    }

    // TODO: Fix counting of elements inside nested arrays/hashes. Also the read out of the error in actual size vs the element itself
    @Then("^the (?:JSON|json) response should( not|) have the following number of entries:")
    public void AssertCollectionSizeTable(String negate, Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String path = entry.getKey();
            Integer size = entry.getValue();
            if (negate.isEmpty()) {
                logger.debug("Asserting that the response collection at " + path + " has " + size + " items.");
                assertThat(
                        "Expected to find " + size + " items at " + path + ", but we found " + memory.getLastResponse().jsonPath().get(path),
                        memory.getLastResponse().jsonPath().get(path),
                        IsCollectionWithSize.hasSize(size)
                );
            }
            if (!negate.isEmpty()) {
                logger.debug("Asserting that the response collection at " + path + " does not have " + size + " items.");
                assertThat(
                        "Expected to find " + size + " items at " + path + ", but we found " + memory.getLastResponse().jsonPath().get(path),
                        memory.getLastResponse().jsonPath().get(path),
                        not(IsCollectionWithSize.hasSize(size))
                );
            }
        }
    }

    @Then("^the (?:JSON|json) response at \"(.*)\" should( not|) be \"(.*)\"$")
    public void AssertJSONAttribute(String key, String negate, String value) {
        value = commonLibrary.checkForVariables(value);
        key = commonLibrary.checkForVariables(key);
        try {
            if (negate.isEmpty()) {
                logger.debug("Asserting that the JSON response at: " + key + " is: " + value);
                assertThat(
                        "Expected JSON response at: " + key + " to be: " + value + ", but found " + memory.getLastResponse().jsonPath().get(key),
                        memory.getLastResponse().jsonPath().get(key).toString(),
                        equalTo(value)
                );
            } else if (!negate.isEmpty()) {
                logger.debug("Asserting that the JSON response at " + key + " is not: " + value);
                assertThat(
                        "Expected JSON response at: " + key + " to not be: " + value + ", but found " + memory.getLastResponse().jsonPath().get(key),
                        memory.getLastResponse().jsonPath().get(key).toString(),
                        not(equalTo(value))
                );
            }
        } catch (NullPointerException e) {
            logger.error("We could not find the key in the JSON response at '" + key + "'. Please check your cucumber feature file." + e);
            throw new NullPointerException("We could not find the key in the JSON response at '" + key + "'. Please check your cucumber feature file.");
        }
    }

    @Then("^the (?:JSON|json) response should(| not) be the following:$")
    public void AssertJSONAttributeTable(String negate, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = commonLibrary.checkForVariables(entry.getKey());
            String value = commonLibrary.checkForVariables(entry.getValue());
            try {
                if (negate.isEmpty()) {
                    logger.debug("Asserting that the value for key: " + key + " is the expected value: " + value);
                    assertThat(
                            "The value at key: " + key + " should be value: " + value + ", but we found: " + memory.getLastResponse().jsonPath().get(key),
                            memory.getLastResponse().jsonPath().get(key).toString(),
                            equalTo(value)
                    );
                } else if (!negate.isEmpty()) {
                    logger.debug("Asserting that the value for key: " + key + " is not the value: " + value);
                    assertThat(
                            "The value at key: " + key + " should not be value: " + value + ", but we found: " + memory.getLastResponse().jsonPath().get(key),
                            memory.getLastResponse().jsonPath().get(key).toString(),
                            not(equalTo(value))
                    );
                }
            } catch (NullPointerException e) {
                logger.error("We could not find the key in the JSON response at '" + key + "'. Please check your cucumber feature file." + e);
                throw new NullPointerException("We could not find the key in the JSON response at '" + key + "'. Please check your cucumber feature file.");
            }
        }
    }

    @Then("^the (?:JSON|json) response at \"(.*)\" should(| not) be null$")
    public void AssertJSONAttributeNull(String key, String negate) {
        key = commonLibrary.checkForVariables(key);
    logger.debug("Asserting that the value at key: " + key + " is" + negate + " null");
        try {
            if (negate.isEmpty()) {
                assertThat(
                        "The value at key: " + key + " should be null but we found: " + memory.getLastResponse().jsonPath().get(key),
                        memory.getLastResponse().jsonPath().get(key),
                        nullValue()
                );
            } else if (!negate.isEmpty()) {
                assertThat(
                        "The value at key: " + key + " should not be null, but it was",
                        memory.getLastResponse().jsonPath().get(key),
                        notNullValue()
                );
            }
        } catch (NullPointerException e) {
            logger.error("We could not find the key in the JSON response at '" + key + "'. Please check your cucumber feature file." + e);
            throw new NullPointerException("We could not find the key in the JSON response at '" + key + "'. Please check your cucumber feature file.");
        }
    }

    //TODO: Add functionality so we can check JSON schema either from JSON file or from """ method in cukes


    // TODO: Fix 0th element in collection for has key
    @Then("^the (?:JSON|json) response should (|not )include \"(.*)\"$")
    public void SchemaValidation(String negate, String key) {
        String path = "";
        if (key.contains(".")) {
            int theSplit = key.lastIndexOf(".");
            path = key.substring(0, theSplit);
            key = key.substring(theSplit + 1, key.length());
        }
        if (negate.isEmpty()) {
            logger.debug("Asserting that key: " + key + " is present at " + path + " in the response.");
            assertThat(
                    "Asserting key: " + key + " is present, but was not found in the response",
                    memory.getLastResponse().jsonPath().get(path),
                    hasKey(key)
            );
        } else if (!negate.isEmpty()) {
            logger.debug("Asserting that key: " + key + " is not present at " + path + " in the response.");
            assertThat(
                    "Asserting key: " + key + " is not present, but was found in the response",
                    memory.getLastResponse().jsonPath().get(path),
                    not(hasKey(key))
            );
        }
    }

    // TODO: Fix 0th element in collection for has key
    @Then("^the (?:JSON|json) response should (|not )include the following:$")
    public void SchemaValidationTable(String negate, List<String> keys) {
        if (negate.isEmpty()) {
            logger.debug("Asserting that the keys provided in the cucumber scenario are present in the response.");
            for (String key : keys) {
                key = commonLibrary.checkForVariables(key);
                String path = "";
                if (key.contains(".")) {
                    int theSplit = key.lastIndexOf(".");
                    path = key.substring(0, theSplit);
                    key = key.substring(theSplit + 1, key.length());
                }
                logger.debug("Asserting key: " + key);
                if (key.endsWith("]")) {
                    assertThat("Asserting key: " + key + " is present at " + path + ", but it was not found in the response",
                            memory.getLastResponse().jsonPath().get(path).getClass().getName(),
                            is("java.util.HashMap"));
                } else {
                    assertThat(
                            "Asserting key: " + key + " is present at " + path + ", but it was not found in the response",
                            memory.getLastResponse().jsonPath().get(path),
                            hasKey(key)
                    );
                }
            }
        } else if (!negate.isEmpty()) {
            logger.debug("Asserting that the keys provided in the cucumber scenario are not present in the response.");
            for (String key : keys) {
                key = commonLibrary.checkForVariables(key);
                String path = "";
                if (key.contains(".")) {
                    int theSplit = key.lastIndexOf(".");
                    path = key.substring(0, theSplit);
                    key = key.substring(theSplit + 1, key.length());
                }
                logger.debug("Asserting key: " + key);
                assertThat(
                        "Asserting key: " + key + " is not present at " + path + ", but it was found in the response",
                        memory.getLastResponse().jsonPath().get(path),
                        not(hasKey(key))
                );
            }
        }
    }

    @Then("^the (?:JSON|json) response at \"(.*)\" should( not|) have data type \"(.*)\"$")
    public void DataTypeValidation(String key, String negate, String dataType) {
        try {
            if (negate.isEmpty()) {
                logger.debug("Asserting key: " + key + " is type: " + dataType);
                assertThat(
                        "Expected the data type of key: " + key + " to be " + dataType + ", but we found " + memory.getLastResponse().jsonPath().get(key).getClass().getName(),
                        memory.getLastResponse().jsonPath().get(key).getClass().getName(),
                        equalTo(DataType.getDataType(dataType).toString())
                );
            } else if (!negate.isEmpty()) {
                logger.debug("Asserting key: " + key + " is not type: " + dataType);
                assertThat(
                        "Expected the data type of key: " + key + " to not be " + dataType + ", but we found " + memory.getLastResponse().jsonPath().get(key).getClass().getName(),
                        memory.getLastResponse().jsonPath().get(key).getClass().getName(),
                        not(equalTo(DataType.getDataType(dataType).toString()))
                );
            }
        } catch (NullPointerException e) {
            logger.error("We could not find the key in the JSON response at '" + key + "'. Please check your cucumber feature file." + e);
            throw new NullPointerException("We could not find the key in the JSON response at '" + key + "'. Please check your cucumber feature file.");
        }
    }

    @Then("^the (?:JSON|json) response should(| not) have the following data types:$")
    public void DataTypeValidationTable(String negate, Map<String, String> map) {
        logger.debug("Asserting that the data type for each key does match the data type present in the scenario");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = commonLibrary.checkForVariables(entry.getKey());
            String value = commonLibrary.checkForVariables(entry.getValue());
            logger.debug("Asserting that the key: " + key + " has a data type of: " + value);
            try {
                if (negate.isEmpty()) {
                    assertThat(
                            "Expected the data type of key: " + key + " to be " + value + ", but we found " + memory.getLastResponse().jsonPath().get(key).getClass().getName(),
                            memory.getLastResponse().jsonPath().get(key).getClass().getName(),
                            equalTo(DataType.getDataType(value).toString())
                    );
                } else if (!negate.isEmpty()) {
                    assertThat(
                            "Expected the data type of key: " + key + " to not be " + value + ", but we found " + memory.getLastResponse().jsonPath().get(key).getClass().getName(),
                            memory.getLastResponse().jsonPath().get(key).getClass().getName(),
                            not(equalTo(DataType.getDataType(value).toString()))
                    );
                }
            } catch (NullPointerException e) {
                logger.error("We could not find the key in the JSON response at '" + key + "'. Please check your cucumber feature file." + e);
                throw new NullPointerException("We could not find the key in the JSON response at '" + key + "'. Please check your cucumber feature file.");
            }
        }
    }

}