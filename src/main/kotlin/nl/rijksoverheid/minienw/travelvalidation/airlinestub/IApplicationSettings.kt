package nl.rijksoverheid.minienw.travelvalidation.airlinestub

interface IApplicationSettings
{
    val walletProcessUrl: String
    val configFileFolderPath: String
    val validationServiceIdentityUri: String

    //TODO may use this later
    val redisHost: String
}