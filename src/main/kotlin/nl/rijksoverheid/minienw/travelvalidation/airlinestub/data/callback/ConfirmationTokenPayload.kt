package nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.callback

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

/**
* 3.8.5.3
* <see cref="nl.rijksoverheid.minienw.travelvalidation.service.api.data.callback.ResultTokenPayload.Confirmation" />
*/
class ConfirmationTokenPayload
    (
    /**
    * Unique Identifier of the confirmation token
    * GUID
    * TODO <see href="https://docs.microsoft.com/en-us/dotnet/api/system.guid.tostring?view=net-6.0">What format? No example given</see>
    * Not <see cref="nl.rijksoverheid.minienw.validationservice.airlinestub.data.ValidationAccessTokenPayload.TokenIdentifier">!!!!!
    */

    @SerializedName("jti")
    @JsonProperty("jti")
    var id : String,

    /**
    * Value of the access token
    * e.g. ADEDDDDDDDDDDDDDDD
    * TODO this looks like hex rather than base64??
    * <see cref="nl.rijksoverheid.minienw.validationservice.airlinestub.data.ValidationAccessTokenPayload.TokenIdentifier">
    */
    @SerializedName("sub")
    @JsonProperty("sub")
    var subject : String,

    /**
    * TODO of what? This confirmation or is this copied from the validation request payload?
    * Unix epoch time
    */
    @SerializedName("iat")
    @JsonProperty("iat")
    var whenIssued: Long,

    /**
    * Epoch time
    * TODO of what? This confirmation or is this copied from the validation request payload?
    */
    @SerializedName("exp")
    @JsonProperty("exp")
    var whenExpires : Long,

    /**
    * e.g. Inter-Flight, Concert, Domestic, MassEvent > 1000, etc.
    * Category which  shall be reflected in the Validation by additional rules/logic. If null, Standard Business Rule Check will apply.
    * Default: “Standard”
    * For 2
    */
    @SerializedName("category")
    @JsonProperty("category")
    var category : Array<String>,

    /**
    * Final result of the evaluation.
    * OK = Passed
    * NOK = Fail
    * CHK = Cross Check(OPEN)
    */
    var result : String,

    //V2 Extra
    @SerializedName("poa")
    @JsonProperty("poa")
    var portOfArrival : String,

    @SerializedName("pod")
    @JsonProperty("pod")
    var portOfDeparture : String,

    /**
     * V2 Extra
     *  DCC must be valid from this date.
     * ISO8601 with time and offset e.g. 2021-01-29T12:00:00+01:00
     * For 0,1,2
     */
    @SerializedName("validfrom")
    @JsonProperty("validfrom")
    var whenValidStart : String,

    /**
     * V2 Extra
     * DCC must be valid minimum to this date.
     * ISO8601 with time and offset e.g. 2021-01-29T12:00:00+01:00
     * For 0,1,2
     */
    @SerializedName("validTo")
    @JsonProperty("validTo")
    var whenValidEnd : String,

    /**
     * V2 Extra
     * Date where the DCC must be validatable.
     * ISO8601 with time and offset e.g. 2021-01-29T12:00:00+01:00
     * For 1,2
     */
    @SerializedName("validationclock")
    @JsonProperty("validationclock")
    var validationClock : String,

    // V2 Extra
    @SerializedName("personalinfodccextract")
    @JsonProperty("personalinfodccextract")
    var dccExtract: DccExtract
)