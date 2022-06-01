package nl.rijksoverheid.minienw.travelvalidation.validationservice.services

import nl.rijksoverheid.minienw.travelvalidation.api.data.PublicKeyJwk

interface IAirlineSigningKeyProvider {
    fun get(keyId: String?, algorithm: String?): PublicKeyJwk?
    fun refresh()
}