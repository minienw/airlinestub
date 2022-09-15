package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import nl.rijksoverheid.minienw.travelvalidation.api.data.PublicKeyJwk

/*From identity documents of listed validation services*/
interface IValidationServiceSigningKeyProvider {
    fun get(keyId: String?, algorithm: String?): PublicKeyJwk?
    fun refresh()
}