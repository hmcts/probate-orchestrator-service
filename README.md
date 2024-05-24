# Probate Orchestrator


[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Probate-Orchestrator-Service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Probate-Orchestrator-Service) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Probate-Orchestrator-Service&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=Probate-Orchestrator-Service) [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Probate-Orchestrator-Service&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=Probate-Orchestrator-Service) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Probate-Orchestrator-Service&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Probate-Orchestrator-Service)

The Probate Orchestrator is an [API Gateway](https://microservices.io/patterns/apigateway.html) that mediates API calls between [Probate frontend](https://github.com/hmcts/probate-frontend) and APIs such as the [back office](https://github.com/hmcts/probate-back-office), [submit service](https://github.com/hmcts/probate-submit-service/) and [business service](https://github.com/hmcts/probate-business-service/).

It is written Java and uses the Sprint Boot framework.

## Overview

<p align="center">
<a href="https://github.com/hmcts/probate-frontend">probate-frontend</a> • <a href="https://github.com/hmcts/probate-caveats-frontend">probate-caveats-frontend</a> • <a href="https://github.com/hmcts/probate-back-office">probate-back-office</a> • <b><a href="https://github.com/hmcts/probate-orchestrator-service">probate-orchestrator-service</a></b> • <a href="https://github.com/hmcts/probate-business-service">probate-business-service</a> • <a href="https://github.com/hmcts/probate-submit-service">probate-submit-service</a> • <a href="https://github.com/hmcts/probate-persistence-service">probate-persistence-service</a>
</p>

<br>

<p align="center">
  <img src="https://raw.githubusercontent.com/hmcts/reform-api-docs/master/docs/c4/probate/images/structurizr-probate-overview.png" width="800"/>
</p>

<details>
<summary>Citizen view</summary>
<img src="https://raw.githubusercontent.com/hmcts/reform-api-docs/master/docs/c4/probate/images/structurizr-probate-citizen.png" width="700">
</details>
<details>
<summary>Caseworker view</summary>
<img src="https://raw.githubusercontent.com/hmcts/reform-api-docs/master/docs/c4/probate/images/structurizr-probate-caseworker.png" width="700">
</details>

## Getting Started
### Prerequisites
- Java 21
- Gradle

### Running the application
Run the service by executing the following command:  
```
$ ./gradlew bootRun
```

### API documentation

API documentation is provided with Swagger:
 - `http://localhost:4400/swagger-ui.html` - UI to interact with the API resources

## Developing

### Local development environment

```
# rebuild every time you make changes
./gradlew assemble

# first time only
npx @hmcts/probate-dev-env --create

# start the dev env
npx @hmcts/probate-dev-env
```

### Running against AAT:

You may run a local instance of orchestrator that points towards AAT BE services to use in conjunction with a local FE 
instance (probate-frontend or probate-caveats). This allows one to debug the orchestrator by triggering events through local FE.

Steps:
1. Ensure VPN is on.
2. Run `./gradlew generateAatEnvFile`
3. Run `POINT_TO_AAT=true ./gradlew run`
4. After ~10 seconds, orchestrator will be running.

### Unit tests

To run all unit tests please execute the following command:

```bash
$ ./gradlew test
```

### Coding style tests

To run all checks (including unit tests) please execute the following command:

```bash
$ ./gradlew check
```

### Crons

You can manually run a cron task from the cli:

```
TASK_NAME=[task] java -jar probate-orchestrator-service.jar run

# E.g.
TASK_NAME=SmeeAndFordExtractTask java -jar probate-orchestrator-service.jar

# or
TASK_NAME=SmeeAndFordExtractTask ./gradlew bootRun
```

To configure a new cron in AAT please checkout the [cnp-flux-config](https://github.com/hmcts/cnp-flux-config/) repository and run:

```
./bin/add-cron.sh SmeeAndFordExtractTask ~/cnp-flux-config "0/10 * * * *"
```

Then create a PR in the cnp-flux-config repository.

Note that the cron will only run in the aat-00 cluster as we don't have a way to run the job once over multiple clusters. Let's hope that cluster doesn't go down.


### IDE Settings

#### Project Lombok Plugin
When building the project in your IDE (eclipse or IntelliJ), Lombok plugin will be required to compile. 

For IntelliJ IDEA, please add the Lombok IntelliJ plugin:
* Go to `File > Settings > Plugins`
* Click on `Browse repositories...`
* Search for `Lombok Plugin`
* Click on `Install plugin`
* Restart IntelliJ IDEA

Plugin setup for other IDE's are available on [https://projectlombok.org/setup/overview]

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.

