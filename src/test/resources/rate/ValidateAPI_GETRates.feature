#######################################################################################################################
# This Feature file will test the GET /rates endpoint in the ratecalc micro-service
#
# Author: Brian DeSimone
# Date: 07/16/2018
#######################################################################################################################
@Rate @Regression
Feature: Validate API GET /rates

  @HealthCheck
  Scenario: Validate that a valid get all rates request returns the proper response code (smoke-test)
    When I send a RATECALC GET request to "/rates"
    Then the response status should be "200"

  @HealthCheck
  Scenario: Validate that a valid get all rates request returns the documented schema
    When I send a RATECALC GET request to "/rates"
    Then the response status should be "200"
    And the response headers should be JSON
    And the JSON response should includ the following:
      | timestamp      |
      | statusCode     |
      | statusMessage  |
      | rates          |
    And the JSON response should have the following data types:
      | timestamp      | Long      |
      | statusCode     | Integer   |
      | statusMessage  | String    |
      | customers      | ArrayList |
      | limit          | Integer   |
      | offset         | Integer   |
      | totalCustomers | Integer   |
    And the JSON response should be the following:
      | statusCode    | 200     |
      | statusMessage | Success |
      | limit         | 100     |
      | offset        | 0       |