package nl.rijksoverheid.minienw.travelvalidation.airlinestub.commands

import com.google.gson.Gson
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.*
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.api.*
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.*
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.token.ValidationInitializeRequestBody
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.token.ValidationType
import org.bouncycastle.util.encoders.Base64
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Instant
import java.util.*

@Service
class HttpPostTokenV2Command(
    private val dateTimeProvider: IDateTimeProvider,
    private val appSettings: IApplicationSettings,
    private val sessionRepository: ISessionRepository,
    private val bodyValidator: ValidationInitializeRequestBodyValidatorV2,
) {
    fun execute(initiatingQrCodePayload: String): ResponseEntity<Any>
    {
        val validationAccessTokenPayload = ValidationAccessTokenPayload(
            jsonTokenIdentifier = ValidationServicesSubjectIdGenerator().next(),
            whenExpires = Instant.now().epochSecond + 3600,
            whenIssued = 1645966339L,
            serviceProvider = "http://localhost:8081/identity", //Identity of the validation service or the validation service
            subject = "0123456789ABCDEF0123456789ABCDEF",
            subjectUri = "http://localhost:8080/validate/0123456789ABCDEF0123456789ABCDEF", //TODO from validation service identity
            ValidationCondition = ValidationAccessTokenConditionPayload(
                //DccHash = "sdaasdad",
                language = "en",
                familyNameTransliterated = "who",
                givenNameTransliterated = "knows",
                dateOfBirth = "1979-04-14",
                countryOfArrival = "NL",
                countryOfDeparture = "DE",
                portOfArrival = "AMS",
                portOfDeparture = "FRA",
                regionOfArrival = "",
                regionOfDeparture = "",
                dccTypes = arrayOf("v"),
                categories = arrayOf("standard"),
                validationClock = "2021-01-29T12:00:00+01:00",
                whenValidStart = "2021-01-29T12:00:00+01:00",
                whenValidEnd = "2021-01-29T12:00:00+01:00",
            ),
            ValidationVersion =  "1.0", //TODO probably 2.0?
            ValidationType = ValidationType.Full
        )

        val payloadJson = Gson().toJson(validationAccessTokenPayload)

        var privateKeyPem = File(appSettings.configFileFolderPath,"accesstokensign-privatekey-1.pem").readText()
        var privateKey = CryptoKeyConverter.decodeAsn1DerPkcs8PemPrivateKey(privateKeyPem)

        val jws : String = Jwts.builder()
            .setPayload(payloadJson)
            .setHeaderParam("kid", "L6TcjnbZge4=") //TODO grab from identity
            .signWith(privateKey, SignatureAlgorithm.RS256)
            .compact()

        val key = ByteArray(16)
        Random().nextBytes(key)
        val literal = Base64.toBase64String(key);

        val body = ValidationInitializeRequestBody(
            walletPublicKey = "",
            walletPublicKeyAlgorithm = "",
            nonce = literal
        )

        val bodyJson = Gson().toJson(body)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/initialize/0123456789ABCDEF0123456789ABCDEF")) //TODO from validation service identity
            .setHeader("authorization", "bearer $jws")

            .setHeader(Headers.Version, Headers.V2)
            .setHeader("accept", Headers.Json)
            .setHeader("content-type", Headers.Json)

            .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
            .build()

        val response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString())
        println(response)
        if (response.statusCode() != HttpStatus.OK.value())
            throw Exception()

        return ResponseEntity.ok().header("x-nonce", literal).body(jws)
    }
}


