package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.PublicKeyJwk
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File


//TODO obtain keys by GET/identity from each airline endpoint.
// If the sig fails to verify, refresh the keys of the airline the key came from

@Component
class FileAirlineSigningKeyProvider : IAirlineSigningKeyProvider {
    private val items: AirlineKeys

    constructor(appSettings: IApplicationSettings)
    {
        items = AirlineKeys(arrayOf(AirlineKey("asdasd", PublicKeyJwk(arrayOf(""),"","",""))))

        //TODO get the keys from airline

//        val configContents = File(appSettings.configFileFolderPath, "airlinePublicKeys.json").readText(Charsets.UTF_8)
//        items = Gson().fromJson(configContents, AirlineKeys::class.java)
    }

    override fun get(keyId: String?, algorithm: String?): PublicKeyJwk? {
        return items.keys.find { it.key.kid == keyId && it.key.alg == algorithm }?.key
    }
}

