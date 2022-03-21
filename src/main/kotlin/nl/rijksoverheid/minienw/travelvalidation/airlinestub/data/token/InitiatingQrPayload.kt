package nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.token

import com.fasterxml.jackson.annotation.JsonProperty
data class InitiatingQrTokenPayload
    (
    val issuer:String,
    val iat:Long,
    val sub:String,
    val exp: Long
)

/// <summary>
/// 3.8.1 Initialization QR Code Content
/// Called InitializationQrCodeContent in another doc/code
/// </summary>
data class InitiatingQrPayload
    (
    /// <summary>
    /// Type of the requested protocol
    /// </summary>
    var protocol:String = "DCCVALIDATION",

    /// <summary>
    /// SemVer version number
    /// </summary>
    val protocolVersion :String = "2.00",

    /// <summary>
    /// TODO URL to the service provider identity document - if the Airline knows the 'Give me a QR Code' end point, they know the Identity end point too.
    /// TODO When are the contents of the Identity Document used?
    /// </summary>
    val serviceIdentity :String,

    /// <summary>
    /// A separate privacy statement url with additional data processing information.
    /// TODO who writes and hosts this?
    /// </summary>
    val privacyUrl :String,

    /// <summary>
    /// This token is generated to access the Validation Decorator endpoints and contains the information about the booking.
    /// TODO JWT or JWK RFC7517 ?
    /// <see cref="SubjectValidationDecoratorAccessToken" />
    /// </summary>
    @JsonProperty("token")
    val token :String,

    /// <summary>
    /// Consent text which is shown to the user by the wallet app
    /// </summary>
    val consent :String,

    ///<summary>
    // e.g. Booking Nr.1234
    // Plain text
    ///</summary>
    val subject :String,

    /// <summary>
    /// e.g. Airline name
    /// </summary>
    val serviceProvider :String
)