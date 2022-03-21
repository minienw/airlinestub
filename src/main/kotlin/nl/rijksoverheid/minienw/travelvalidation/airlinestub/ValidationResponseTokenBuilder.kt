//package nl.rijksoverheid.minienw.validationservice.airlinestub.services
//
//import com.google.gson.Gson
//import io.jsonwebtoken.Jwts
//import nl.rijksoverheid.minienw.validationservice.airlinestub.services.IApplicationSettings
//import nl.rijksoverheid.minienw.validationservice.airlinestub.data.callback.ConfirmationTokenPayload
//import org.springframework.stereotype.Component
//
//@Component
//class ValidationResponseTokenBuilder(
//    private val appSettings: IApplicationSettings
//)
//{
//    fun build(resultPayload: ConfirmationTokenPayload): String = buildAndSign(resultPayload)
//
//    private fun buildAndSign(resultPayload: Any): String {
//        val payloadJson = Gson().toJson(resultPayload) //NB have to add to  @SerializedName("insert output name here") to the properties
//        return Jwts.builder()
//            .setPayload(payloadJson)
//            .setHeaderParam("kid", "SsXyRIVSy4Y=") //TODO from settings which should be a jwk
//            .setHeaderParam("alg", "RS256") //TODO from settings which should be a jwk
//            .signWith(CryptoKeyConverter.decodeAsn1DerPkcs8Base64ToPrivateKey("RSA", appSettings.validationResultJwsSigningKey))//TODO from settings which should be a jwk
//            .compact()
//    }
//}
//
////@Component
////class ConfirmationTokenBuilder(
////    private val config: IApplicationSettings
////)
////{
////    fun build(resultPayload: ConfirmationTokenPayload, whenIssued: Date, whenExpires: Date): String
////    {
////
////        if (whenIssued > whenExpires)
////            throw IllegalArgumentException()
////
////
////        return result
////    }
////}