# Airline Stub

A development stub for an airline website and supporting services to demonstrate and test online DCC verification.

NOT A PRODUCTION SYSTEM.

# Intro
This service is Springboot REST API application in Kotlin using IntelliJ-IDEA IDE and Gradle.
#Build and deploy

## Pre-requisites

* Git
* Java 17
* Gradle
* Docker
* Docker Compose
* Access to the common messages repo - https://github.com/minienw/onlineverification_messages.
* Overall instructions on https://github.com/minienw/onlineverification_code/blob/main/Configuration%20for%20Travel%20Validation%20Services.docx

## Build Common Messages
Requires:
*Clone https://github.com/minienw/onlineverification_messages to an adjacent folder.

Build using IntelliJ IDE.

## Building project
Build using IntelliJ IDE or the gradle command line.

# Docker
After building and testing code changes, build the new image using:

    docker build -f latest_noconfig.Dockerfile -t airline_stub:latest_noconfig .

Then deploy to the repository of your choice e.g.

    docker tag airline_stub:latest_noconfig stevekellaway/airline_stub:latest_noconfig
    docker push stevekellaway/airline_stub:latest_noconfig

For deploying to a local Docker server, see the 'deploy' folder.

To create an image suitable for deploying in a kubernetes cluster, see the 'deploy-azk' folder, then deploy to Azure using the instructions from the folder 'azurekubernetes'.


