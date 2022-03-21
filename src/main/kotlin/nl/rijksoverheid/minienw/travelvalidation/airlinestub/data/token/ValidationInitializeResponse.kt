package nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.token

import com.fasterxml.jackson.annotation.JsonProperty
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.PublicKeyJwk

/**
    * 3.8.3
    * The validation initialisation response delivers a JSON with a unique subject ID, which identifies the
    * occurrence in the validation service.Next to the unique subject, a var key and an expiration date
    * for the occurrence are attached.The var key must be unique for each subject for maximum data
    * privacy.
    */
class ValidationInitializeResponse(
    /**
     * hexadecimal-encoded value
     * Traveller or whole trip?
     */
    @JsonProperty("sub")
    var subjectId: String,

    /**
     * Unix epoch time
     */
    @JsonProperty("exp")
    var whenExpires: Long,

    /**
     * E.g. https://validationprovider/callback/{subject}
     * TODO odd name
     * Moved to Identity end point... :(
     */
    //@JsonProperty("aud")
    //var ValidationUrl: Int,

    /**
     * "Optional var Key for Encryption of Validation nl.rijksoverheid.minienw.validationservice.airlinestub.data.identity.Service."
     * TODO should be using RSA not AES as an AES secret key should be exchange with DiffieHellman first.
     */
    @JsonProperty("encKey")
    var validationServiceEncryptionKey: PublicKeyJwk?,

    /**
     * "Optional var Key for Signing of Validation nl.rijksoverheid.minienw.validationservice.airlinestub.data.identity.Service."
     * See encKey...
     * TODO NB why bother as the wallet has already sent you a public key and GET/identity provides the one for the service
     */
    @JsonProperty("signKey")
    var signKey: PublicKeyJwk?,
)