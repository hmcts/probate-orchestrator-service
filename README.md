# Probate Orchestrator

[![codecov](https://codecov.io/gh/hmcts/probate-orchestrator-service/branch/develop/graph/badge.svg)](https://codecov.io/gh/hmcts/probate-orchestrator-service)

The Probate Orchestrator is an [API Gateway](https://microservices.io/patterns/apigateway.html) that mediates API calls between [Probate frontend](https://github.com/hmcts/probate-frontend) and APIs such as the [back office](https://github.com/hmcts/probate-back-office), [submit service](https://github.com/hmcts/probate-submit-service/) and [business service](https://github.com/hmcts/probate-business-service/).

It is written Java and uses the Sprint Boot framework.

## Getting Started
### Prerequisites
- Java 11
- Gradle
- Docker

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

## Docker Compose Environment

Not currently available.

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

