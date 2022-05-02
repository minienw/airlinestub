FROM openjdk:17-alpine
WORKDIR service-bin
COPY build/libs/*.jar .
