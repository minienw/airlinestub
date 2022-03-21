package nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.identity

import com.fasterxml.jackson.annotation.JsonProperty

/**
 3.8.2.2
*/
class VerificationMethod
    (
    /**
     All ID values must be prefixed by {serviceproviderurl} and a fragment value, e.g., https://servicepovider/verificationmethod#AccessTokenSigning-1
    
     AccessTokenSignKey-X | Validation Decorator | Public key of the key pair of the service provider to sign the access token
     AccessTokenServiceKeyX | Validation Decorator | Public key of the access token service URL
     ValidationServiceKey-X | Validation Decorator |Public key of the used certificate for the validation service URL
     ValidationServiceEncKeyX | Validation nl.rijksoverheid.minienw.validationservice.airlinestub.data.identity.Service | Public key for encrypting the content send to the validation service
     ValidationServiceSignKey-X | Validation nl.rijksoverheid.minienw.validationservice.airlinestub.data.identity.Service | Public key of the key pair of the validation provider to sign the result token
     ValidationServiceEncSchemeKey-{EncryptionScheme} | Validation nl.rijksoverheid.minienw.validationservice.airlinestub.data.identity.Service | Verification Method definition of available encryption schemes. Contains no public key. The Encryption Scheme is later used in the Validation Request.
     ServiceProviderKey-X | Validation Decorator | Public key of the used certificate of the service provider URL
     CancellationServiceKey-X | Validation Decorator | Public key of the used certificate of the cancellation URL
     StatusServiceKey-X | Validation Decorator | Public key of the used certificate of thestatus URL
    */

    val id: String,
    val type: String = "JsonWebKey2020", //TODO simply this?

    /**
    */
    @JsonProperty("controller")
    val serviceUri: String,

    /**
     Mandatory only for asymmetric encryption/signing, otherwise optional
    */
    val publicKeyJwk: String, //JsonWebKeyRfc7517

    /**
     Optional Verification IDs Array which can be used for referencing other Keys.
     TODO this is a reference to <see cref="Id"/> ????
    */
    @JsonProperty("verificationMethods")
    val verificationMethodIds: Array<String>
)