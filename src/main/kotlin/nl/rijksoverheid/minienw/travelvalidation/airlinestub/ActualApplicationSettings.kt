package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import org.springframework.stereotype.Component

@Component
class ActualApplicationSettings(
    private val file: ApplicationPropertiesFile
): IApplicationSettings
{
    override val configFileFolderPath: String
        get() = file.configFileFolderPath
    override val validationServiceIdentityUri: String
        get() = TODO("Not yet implemented")
    override val validationAccessTokenAlgorithm: String
        get() = TODO("Not yet implemented")
    override val validationAccessTokenPublicKey: String
        get() = TODO("Not yet implemented")
    override val validationAccessTokenPrivateKey: String
        get() = TODO("Not yet implemented")
    override val validationAccessTokenLifetimeSeconds: Long
        get() = TODO("Not yet implemented")
    override val validationAccessTokenSigningKey: String
        get() = TODO("Not yet implemented")
    override val sessionMaxDurationSeconds: Long
        get() = TODO("Not yet implemented")
    override val redisHost: String
        get() = TODO("Not yet implemented")
    override val demoModeOn: Boolean
        get() = TODO("Not yet implemented")
}
