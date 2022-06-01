package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import com.google.gson.Gson
import nl.rijksoverheid.minienw.travelvalidation.api.data.PublicKeyJwk
import nl.rijksoverheid.minienw.travelvalidation.api.data.identity.IdentityResponse
import nl.rijksoverheid.minienw.travelvalidation.validationservice.services.IAirlineSigningKeyProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File

//@Qualifier("remote")
@Component
class LocalFileAirlineSigningKeyProvider(private val appSettings: IApplicationSettings)
    : IAirlineSigningKeyProvider {

    private var items: ArrayList<PublicKeyJwk> = ArrayList<PublicKeyJwk>()
    private val logger: Logger = LoggerFactory.getLogger("LocalFileAirlineSigningKeyProvider")

    override fun refresh()
    {
        val result = ArrayList<PublicKeyJwk>()
        val responseBody = File(appSettings.configFileFolderPath, "identity.json").readText(Charsets.UTF_8)
        try {
            val doc = Gson().fromJson(responseBody, IdentityResponse::class.java)
            for (vm in doc.verificationMethod) {
                if (vm.publicKeyJwk?.use.equals("sig", ignoreCase = true)) {
                    logger.info("Found verification key '${vm.publicKeyJwk?.kid}'.")
                    result.add(vm.publicKeyJwk!!)
                }
            }
        } catch (ex: Exception) {
            logger.warn("Failed to parse identity from identity file.")
        }

        synchronized(this)
        {
            items = result
        }
    }

    override fun get(keyId: String?, algorithm: String?): PublicKeyJwk? {
        return items.find { it.kid == keyId && it.alg == algorithm }
    }
}