FROM ghcr.io/minienw/airline_stub:latest_noconfig
WORKDIR /service-bin
COPY application.properties .
COPY identity.json .
COPY accesstokensign-privatekey-1.pem .
ENTRYPOINT ["java","-jar","airline-stub-0.0.1-SNAPSHOT.jar","--spring.profiles.active=default"]
