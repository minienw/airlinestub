FROM openjdk:17-alpine
WORKDIR service-bin
COPY build/libs/*.jar /service-bin/
COPY build/resources/main /service-bin/
ENTRYPOINT ["java","-jar","airline-stub-0.0.1-SNAPSHOT.jar","--spring.profiles.active=default,dockerdemolocal"]
