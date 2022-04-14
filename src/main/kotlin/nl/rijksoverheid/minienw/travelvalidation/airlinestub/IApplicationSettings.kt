package nl.rijksoverheid.minienw.travelvalidation.airlinestub

interface IApplicationSettings
{
    val walletProcessUrl: String
    val configFileFolderPath: String
    val validationServiceIdentityUri: String
    val redisHost: String
}