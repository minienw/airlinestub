package nl.rijksoverheid.minienw.travelvalidation.airlinestub.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.commands.Constants
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.token.ValidationType

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

/**
* 3.8.4.3 Validation Access Token Payload
* The Token Replay section regarding replay mitigation is in section 3.8.4.3 ->
* this must refer to <see cref="nl.rijksoverheid.minienw.validationservice.airlinestub.data.ValidationAccessTokenPayload.TokenIdentifier"/> and <see cref="nl.rijksoverheid.minienw.validationservice.airlinestub.data.ValidationAccessTokenPayload.WhenExpires"/>
 *
*/
class ValidationAccessTokenPayload {

    @JsonCreator
    constructor(
        jsonTokenIdentifier: String,
        serviceProvider: String,
        subject: String,
        subjectUri: String,
        whenExpires: Long,
        ValidationType: ValidationType,
        ValidationVersion: String,
        ValidationCondition: ValidationAccessTokenConditionPayload,
        whenIssued: Long
    )
    {
        this.id = jsonTokenIdentifier
        this.issuingServiceProvider = serviceProvider
        this.subject = subject
        this.validationUrl = subjectUri
        this.whenIssued = whenIssued
        this.whenExpires = whenExpires
        this.validationType = ValidationType
        this.validationVersion = ValidationVersion
        this.validationCondition = ValidationCondition
    }

    /**
     *
     */
    @NotNull
    //@Size(min = 32, max = 32)
    //@Pattern(regexp = Constants.GuidAsHexRegexPattern)
    @JsonProperty("jti")
    var id: String

    /**
     * https://serviceprovider
     * id (uri?) of identity document - whose?
     */
    //@NotBlank
    @JsonProperty("iss")
    var issuingServiceProvider: String

    /**
     * TODO of what?
     * Unix epoch time
     */
    @Min(Constants.LowestPossibleTime)
    @JsonProperty("iat")
    var whenIssued: Long

    /**
     * TODO "Value of the Initialisation"
     * e.g. ADEDDDDDDDDDDDDDDD
     */
    //@Pattern(regexp = Constants.GuidAsHexRegexPattern)
    @JsonProperty("sub")
    var subject: String

    /**
     * Value of the Initialisation (must match to service endpoint “ValidationService”)
     * e.g. https://validationprovider/callback/{subject}
     * Audience
     */
    //@NotBlank
    //TODO additional Uri.parse...
    @JsonProperty("aud")
    var validationUrl: String

    /**
     * Epoch time
     */
    @Min(Constants.LowestPossibleTime)
    //TODO also max
    @JsonProperty("exp")
    var whenExpires: Long

    /**
     * This is the type discriminator for the condition and governs the mandatory fields
     * NB Must be 2 for this type of validation
     * Depth of probing for the DCC during verification
     */
    //@NotBlank //TODO regex or code?
    @JsonProperty("t")
    var validationType: ValidationType

    /**
     * TODO what is this used for?
     * e.g. 1.0
     */
    //@NotBlank //TODO regex or code?
    @JsonProperty("v")
    var validationVersion: String

    /**
     * Validation Conditions
     * (optional, depending on Type = Full)
     */
    @NotNull
    @JsonProperty("vc")
    var validationCondition: ValidationAccessTokenConditionPayload
}