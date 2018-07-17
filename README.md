# Rate Calculator Automation Framework #

## Overview: ##
This project is an automation framework that will test the rate calculator developed in the ratecalc repo.

## About: ##
Author: Brian DeSimone  
Date: 07/16/2018  

## Technologies and Frameworks: ##
- TestNG
- RestAssured
- Gradle
- Cucumber

## Cucumber Test Tags: ##
- Healthcheck
- Regression
- Positive
- Negative

## Environments: ##
- Local
- DEV - Coming Soon!
- STAGE - Coming Soon!
- PROD - Comong Soon!

## Jenkins Automation: ##
- This would come into play with CI/CD integration

## Bug Tracking: ##
There is no bug tracking for this project yet.

#### APIs Tested: ####
- POST /rates
- GET /rate/{startRate}/{endRate}
- GET /rates

## Logging: ##
Everything is logged to the console. There is also a log file created in the {root}/logs/*.log file

## Running the Test Automation Project: ##
#### Local Running ####
This project can be run locally with gradle. Use the following command:
```bash
gradle clean build test -Dcucumber.options="--tags Regression"
```

