package nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.callback

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

class DccExtract
(
/**
 * ICAO 9303 transliterated
 * For 1 and 2
 */
@SerializedName("fnt")
@JsonProperty("fnt")
var surnameTransliterated : String?,

/**
 * ICAO 9303 transliterated
 * For 1 and 2
 */
@SerializedName("gnt")
@JsonProperty("gnt")
var givenNameTransliterated : String?,

/**
 * Various formats:
 * 1979-04-14 or 1901-08 or 1950
 * For 1 and 2
 */
@SerializedName("dob")
@JsonProperty("dob")
var dateOfBirth : String?,
)