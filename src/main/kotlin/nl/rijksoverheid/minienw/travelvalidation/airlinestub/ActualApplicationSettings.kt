package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import org.springframework.stereotype.Component

@Component
class ActualApplicationSettings(
    private val file: ApplicationPropertiesFile
): IApplicationSettings
{
    override val walletProcessUrl: String
        get() = file.walletProcessUrl
    override val configFileFolderPath: String
        get() = file.configFileFolderPath
    override val validationServiceIdentityUri: String
        get() = file.validationServiceIdentityUri
    override val redisHost: String
        get() = TODO("Not yet implemented")
}
