package nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.identity

import com.fasterxml.jackson.annotation.JsonProperty

class IdentityResponse
    (
    @JsonProperty("id")
        val id: String,

    /**
        TODO name should be plural
         */
        @JsonProperty("verificationMethod")
        val verificationMethods :  Array<VerificationMethod>,

    /** TODO name should be plural*/
        @JsonProperty("service")
        val services : Array<Service>
)