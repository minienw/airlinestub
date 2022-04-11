package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("airline-stub")
class ApplicationPropertiesFile() {
    lateinit var walletProcessUrl: String
    //lateinit var redisHost: String
    lateinit var configFileFolderPath: String
    lateinit var validationServiceIdentityUri: String
}