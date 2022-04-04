package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("airline-stub")
class ApplicationPropertiesFile {
    lateinit var redisHost: String
    lateinit var configFileFolderPath: String
    lateinit var rootUri:String
    lateinit var validationServiceIdentityUri: String
    lateinit var validationServiceInitializeUri: String
    lateinit var validationServiceValidateUri: String


}