# Overview
Deploy and configure the services in the following order:

1. Wallet
2. Validation Service
3. Airline service

The 2 services are Spring Boot applications and use the standard configuration file mechanism (application.properties) and profiles for configuring different environments.

Configuration for the services is present in both the configuration files and their identity documents which themselves are JSON files.
The two services require at least one suitable key pair (RSA4096 or ECDSA) each for signing and verifying tokens. The public verification keys must be added to the service’s identity document. 

The intended endpoints of each service must also be added to the corresponding entry in the service's identity document or configuration file. Care should be taken when configuring the endpoint values – the URL must be reachable by the ultimate user of the value (which is often NOT the location of the service which has the value in its configuration). Point of View (POV) is given for each configured URL.

Logging is log4j.

In general, start with the basic profile or nearest file to your profile/environment and change the values as per the following sections.
Note the encoding (the one that starts with ASN1…) specified for public and private keys are the most commonly used formats and are usually obtained simply by using the default encoding of the key from Bouncy Castle, .NET or Java.
Several working examples of configuration files and identity files are present in the repositories.

## Configuration Items
Apologies in advance for the inconsistent approaches that follow. This is not ‘production’ quality code.

### Key Pair for Validation Access Token Signature
NB. Please make yourself familiar with the recent (at time of writing) ‘psychic paper signature’ vulnerability before using ES256 in production. See https://cve.mitre.org/cgi-bin/cvename.cgi?name=2022-21449.

For ES256 signatures, generate an ECDSA key pair. For RS256 and PS256, generate an RSA4096  key pair. Generate the associated configuration values.

For the private key, save this as a file called ‘accesstokensign-privatekey-1.pem’ in Asn1/Der/Pkcs8/X509/PEM format.

For the public key, which is configured in JWK format as:

x5c: base 64 string of byte array of the private key in Asn1/Der/Pkcs1/X509 format

kid: base64 string of first 8 bytes of the SHA256 of private key in Asn1/Der/Pkcs1/X509

Configuration file entry:

airline-stub.configFileFolderPath : the folder containing the private  key pem file – see below.

Identity file elements:

verificationMethod with id ending ‘#AccessTokenSign-1’ or other digit:  public key

### Folder identity.json and accesstokensign-privatekey-1.pem files
Configuration file entry:
validation-service.configFileFolderPath = build\\resources\\main\\dev

### Wallet Process URI
The Initiating QR Token will be processed by a wallet recommended by the Airline. To derive the full URI (= this prefix + base64 of the Initiating QR Token), use the URI where the wallet is hosted as noted earlier.

Configuration file entry:

airline-stub.walletProcessUrl = <your wallet URI>/process

### Validation Service URI
The airline also recommends which validation service to use. 
The Validation Access Token will specify which validation service is used by including the identity URI of the Validation Service as noted earlier.

Configuration file entry:

airline-stub.validationServiceIdentityUri = http://192.168.178.12:8080/identity

### Host name of the redis service
Note. If hosted in docker, this will be the name of the Redis service in the docker compose yml file.
This service uses Redis cache to store validation attempts and whether they have been notified of a result token.

Configuration file entry:
airline-stub.redisHost = redis
