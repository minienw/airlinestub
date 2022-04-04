package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import java.io.File

interface IApplicationSettings
{
    val configFileFolderPath: String
    val validationServiceIdentityUri: String

    val rootServiceUrl: String

    val validationAccessTokenAlgorithm : String
    val validationAccessTokenPublicKey : String
    val validationAccessTokenPrivateKey : String

    val validationAccessTokenLifetimeSeconds: Long
    val validationAccessTokenSigningKey: String

    val sessionMaxDurationSeconds: Long
    val redisHost: String
    val demoModeOn: Boolean
}