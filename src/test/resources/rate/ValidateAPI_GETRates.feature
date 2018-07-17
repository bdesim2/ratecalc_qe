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