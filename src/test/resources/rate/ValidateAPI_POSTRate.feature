#######################################################################################################################
# This Feature file will test the POST /rate endpoint in the ratecalc micro-service
#
# Author: Brian DeSimone
# Date: 07/16/2018
#######################################################################################################################
@Rate @Regression
Feature: Validate API POST /rate

  Background: Set the valid rates
    Given I set the following attributes:
      | startRate | 2019-07-15T09:00:00Z |
      | endRate   | 2019-07-15T21:00:00Z |

  @HealthCheck
  Scenario: Validate that a valid request returns the proper response code (smoke-test)
    Given I generate a rate request object
    When I send a RATECALC POST request to "/rate" with the generated body
    Then the response status should be "200"

  @HealthCheck
  Scenario: Validate that a valid request returns the documented schema, headers, data types, and data
    Given I generate a rate request object
    When I send a RATECALC POST request to "/rate" with the generated body
    Then the response status should be "200"
    And the response headers should be JSON
    And the JSON response should include the following:
      | timestamp     |
      | statusCode    |
      | statusMessage |
      | rate          |
    And the JSON response should have the following data types:
      | timestamp     | Long    |
      | statusCode    | Integer |
      | statusMessage | String  |
      | rate          | Integer |
    And the JSON response should be the following:
      | statusCode    | 200     |
      | statusMessage | Success |
      | rate          | 2000    |

  @Positive
  Scenario Outline: Validate that a valid request for each day and time in the config returns the proper rate
    Given I set the following attributes:
      | startRate | <startRate> |
      | endRate   | <endRate>   |
    And I generate a rate request object
    When I send a RATECALC POST request to "/rate" with the generated body
    Then the response status should be "200"
    And the JSON response should include "timestamp"
    And the JSON response should be the following:
      | statusCode    | 200     |
      | statusMessage | Success |
      | rate          | <rate>  |

    Examples: Days, times, and rates (Sunday - Saturday)
      | startRate            | endRate              | rate |
      | 2019-07-15T10:00:00Z | 2019-07-15T20:00:00Z | 2000 |
      | 2019-07-16T02:00:00Z | 2019-07-16T04:00:00Z | 1000 |
      | 2019-07-17T10:00:00Z | 2019-07-17T12:00:00Z | 1500 |
      | 2019-07-18T06:00:00Z | 2019-07-18T18:00:00Z | 1750 |
      | 2019-07-19T09:00:00Z | 2019-07-19T21:00:00Z | 1500 |
      | 2019-07-20T09:00:00Z | 2019-07-20T21:00:00Z | 2000 |
      | 2019-07-20T15:00:00Z | 2019-07-20T16:00:00Z | 2000 |

  @Negative
  Scenario: Validate that the proper error code and error message is displayed when invalid JSON in the request payload
    When I send a RATECALC POST request to "/rate" with JSON body:
    """
    {invalid:invalid}
    """
    Then the response status should be "400"

  @Negative
  Scenario Outline: Validate that the proper error code and error message is displayed when attribute: <attribute> is value: <value>
    Given I generate a rate request object
    And I modify the JSON at "<attribute>" to be "<value>"
    When I send a RATECALC POST request to "/rate" with the generated body
    Then the response status should be "400"
    And the JSON response should be the following:
      | statusCode    | 400                                                                               |
      | statusMessage | Bad Request                                                                       |
      | errorCode     | 1003                                                                              |
      | errorMessage  | One or more request attributes is missing, invalid, or not following constraints. |

    Examples: Missing or empty attributes and values
      | attribute | value |
#      | startRate | null  |    THIS TEST DOES NOT PASS ... BUG! NEEDS THE EXCEPTION HANDLER WRITTEN
#      | endRate   | null  |    THIS TEST DOES NOT PASS ... BUG! NEEDS THE EXCEPTION HANDLER WRITTEN
      | startRate |       |
      | endRate   |       |


  @Negative
  Scenario: Validate that the proper error code and error message is displayed when startRate is not in ISO format
    Given I set the following attributes:
      | startRate | 2018-07-16 |
    And I generate a rate request object
    When I send a RATECALC POST request to "/rate" with the generated body
    Then the response status should be "400"
    And the response headers should be JSON
    And the JSON response should include "timestamp"
    And the JSON response should be the following:
      | statusCode    | 400                                                                                                     |
      | statusMessage | Bad Request                                                                                             |
      | errorCode     | 1004                                                                                                    |
      | errorMessage  | startRate and endRate must be in ISO DateTime format and is a required field. Invalid value: 2018-07-16 |

  @Negative
  Scenario: Validate that the proper error code and error message is displayed when startRate is not in ISO format
    Given I set the following attributes:
      | endRate | 2020-07-16 |
    And I generate a rate request object
    When I send a RATECALC GET request to "/rate/%{startRate}/2020-07-16"
    Then the response status should be "400"
    And the response headers should be JSON
    And the JSON response should include "timestamp"
    And the JSON response should be the following:
      | statusCode    | 400                                                                                                     |
      | statusMessage | Bad Request                                                                                             |
      | errorCode     | 1004                                                                                                    |
      | errorMessage  | startRate and endRate must be in ISO DateTime format and is a required field. Invalid value: 2020-07-16 |

  @Negative
  Scenario: Validate that the proper error code and error message is displayed when startRate is after endRate
    Given I set the following attributes:
      | startRate | 2025-07-15T09:00:00Z |
      | endRate   | 2019-07-15T21:00:00Z |
    And I generate a rate request object
    When I send a RATECALC GET request to "/rate/%{startRate}/%{endRate}"
    Then the response status should be "400"
    And the response headers should be JSON
    And the JSON response should include "timestamp"
    And the JSON response should be the following:
      | statusCode    | 400                                          |
      | statusMessage | Bad Request                                  |
      | errorCode     | 1005                                         |
      | errorMessage  | The "endRate" is sooner than the "startRate" |

  @Negative
  Scenario: Validate that the proper error code and error message is displayed when no rate available for date/time range
    Given I set the following attributes:
      | startRate | 2019-07-15T01:00:00Z |
      | endRate   | 2019-07-15T23:00:00Z |
    And I generate a rate request object
    When I send a RATECALC GET request to "/rate/%{startRate}/%{endRate}"
    Then the response status should be "404"
    And the response headers should be JSON
    And the JSON response should include "timestamp"
    And the JSON response should be the following:
      | statusCode    | 404                                                        |
      | statusMessage | Not Found                                                  |
      | errorCode     | 1007                                                       |
      | errorMessage  | We could not find a rate for the given startRate - endRate |