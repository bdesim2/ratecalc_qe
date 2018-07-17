#######################################################################################################################
# This Feature file will test the GET /metrics/responseTimes endpoint in the ratecalc micro-service
#
# Author: Brian DeSimone
# Date: 07/16/2018
#######################################################################################################################
@Metrics @Regression
Feature: Validate API GET /metrics/responseTimes

  @HealthCheck
  Scenario: Validate that a valid request returns the proper response code (smoke-test)
    When I send a RATECALC GET request to "/metrics/responseTimes"
    Then the response status should be "200"