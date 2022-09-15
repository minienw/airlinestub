package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwsHeader
import io.jsonwebtoken.SigningKeyResolverAdapter
import nl.rijksoverheid.minienw.travelvalidation.validationservice.services.CryptoKeyConverter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.security.PublicKey

@Service
class ValidationSigningKeyResolverAdapter(private val signingKeyProvider: IValidationServiceSigningKeyProvider) :
    SigningKeyResolverAdapter() {

    override fun resolveSigningKey(jwsHeader: JwsHeader<*>, claims: Claims?): PublicKey?
    {
        signingKeyProvider.refresh()

        val logger = LoggerFactory.getLogger(ValidationSigningKeyResolverAdapter::class.java)

        val found = signingKeyProvider.get(jwsHeader.keyId, jwsHeader.algorithm)

        if (found == null) {
            logger.info("Could not find public signing key ${jwsHeader.keyId} for ${jwsHeader.algorithm}.")
            return null
        }

        logger.info("Found public signing key ${jwsHeader.keyId} for ${jwsHeader.algorithm}.")
        return CryptoKeyConverter.decodeSigningJwkX5c(found)
    }
}