package nl.rijksoverheid.minienw.travelvalidation.airlinestub.commands

import com.google.gson.Gson
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.*
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.api.Headers
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.PublicKeyJwk
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.ValidationAccessTokenConditionPayload
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.ValidationAccessTokenPayload
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.identity.IdentityResponse
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.token.InitiatingQrTokenPayload
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.token.TokenRequestBody
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.token.ValidationInitializeRequestBody
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.token.ValidationType
import org.bouncycastle.util.encoders.Base64
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientResponseException
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Instant
import java.util.*


@Service
class HttpPostTokenV2Command(
    private val appSettings: IApplicationSettings,
    private val repo : ISessionRepository
) {
    fun execute(requestBody: TokenRequestBody, initiatingToken: String): ResponseEntity<Any>
    {
        val logger = LoggerFactory.getLogger(HttpPostTokenV2Command::class.java)

        //Parse JWT
        val initiatingTokenPayloadObject = JwtPayloadParser().getPayload<InitiatingQrTokenPayload>(initiatingToken)
        val session = repo.find(initiatingTokenPayloadObject.sub) ?: return ResponseEntity("Subject does not exist.", HttpStatus.NOT_FOUND)

        val localIdentityDocId = readIdentityFile().id

        val validationIdentityUrl = appSettings.validationServiceIdentityUri
        val validationIdentity = getIdentity(validationIdentityUrl)
        val snapshot = Instant.now()
        val validationAccessTokenPayload = ValidationAccessTokenPayload(
            jsonTokenIdentifier = ValidationServicesSubjectIdGenerator().next(),
            whenExpires = snapshot.epochSecond + 3600,
            whenIssued = snapshot.epochSecond,
            serviceProvider = localIdentityDocId,
            subject = initiatingTokenPayloadObject.sub,
            validationUrl = "${findValidateUri(validationIdentity)}/${initiatingTokenPayloadObject.sub}", //TODO set from the initiatingQrCodePayload subject?
            ValidationCondition = ValidationAccessTokenConditionPayload(
                //DccHash = "sdaasdad",
                language = "en",
                familyNameTransliterated = "who",
                givenNameTransliterated = "knows",
                dateOfBirth = "1979-04-14",
                countryOfArrival = session.tripArgs.toCountry,
                countryOfDeparture = session.tripArgs.fromCountry,
                portOfArrival = "AMS", //TODO
                portOfDeparture = "FRA", //TODO
                regionOfArrival = "", //TODO
                regionOfDeparture = "", //TODO
                dccTypes = arrayOf("v","t","r"), //TODO...
                categories = arrayOf("standard"),
                validationClock = "2021-01-29T12:00:00+01:00", //TODO
                whenValidStart = "2021-01-29T12:00:00+01:00", //TODO
                whenValidEnd = "2021-01-29T12:00:00+01:00", //TODO
            ),
            ValidationVersion =  "2.0", //TODO parameter sent to verifier?
            ValidationType = ValidationType.Full
        )

        val payloadJson = Gson().toJson(validationAccessTokenPayload)
        val privateKeyPem = File(appSettings.configFileFolderPath,"accesstokensign-privatekey-1.pem").readText()
        val privateKey = CryptoKeyConverter.decodeAsn1DerPkcs8PemPrivateKey(privateKeyPem)

        val jws : String = Jwts.builder()
            .setPayload(payloadJson)
            .setHeaderParam("kid", "L6TcjnbZge4=") //TODO grab from identity
            .signWith(privateKey, SignatureAlgorithm.RS256)
            .compact()

        val nonce = ByteArray(16)
        Random().nextBytes(nonce)
        val nonceBase64 = Base64.toBase64String(nonce)

        val body = ValidationInitializeRequestBody(
            walletPublicKey = requestBody.pubKey,
            walletPublicKeyAlgorithm = requestBody.alg,
            nonce = nonceBase64
        )

        val initUri = findInitializeUri(validationIdentity)

        val bodyJson = Gson().toJson(body)
        val request = HttpRequest.newBuilder()
            .uri(URI.create("${initUri}/${initiatingTokenPayloadObject.sub}"))
            .setHeader("authorization", "bearer $jws")
            .setHeader(Headers.Version, Headers.V2)
            .setHeader("accept", Headers.Json)
            .setHeader("content-type", Headers.Json)
            .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
            .build()

        val response: HttpResponse<String>
        try{
            response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString())
        }
        catch(ex:Exception)
        {
            logger.error("POST to ${initUri} failed with $ex")
            throw ex
        }

        if (response.statusCode() != HttpStatus.OK.value())
        {
            logger.error("POST to ${initUri} failed with $response")
            throw Error("POST to ${initUri} failed with $response")
        }

        val validationEncryptionJwk = findEncryptionKey(validationIdentity)
        val validationEncryptionJwkBase64 = Base64.toBase64String(Gson().toJson(validationEncryptionJwk).toByteArray(Charsets.UTF_8))
        val validationVerificationJwk = findVerificationKey(validationIdentity)
        val validationVerificationJwkBase64 = Base64.toBase64String(Gson().toJson(validationVerificationJwk).toByteArray(Charsets.UTF_8))

        return ResponseEntity.ok()
            .header("x-nonce", nonceBase64)
            .header("x-enc", validationEncryptionJwkBase64)
            .header("x-sig", validationVerificationJwkBase64)
            .body(jws)
    }

    private fun readIdentityFile(): IdentityResponse {
        val content = File(appSettings.configFileFolderPath, "identity.json").readText()
        val doc = Gson().fromJson(content, IdentityResponse::class.java)
        return doc
    }

    fun getIdentity(url: String): IdentityResponse
    {
        try
        {
            val response = RestTemplateBuilder().build().getForEntity(URI(url), IdentityResponse::class.java)
            println(response.statusCode)
            println(response.body)
            //var result = ObjectMapper().readValue(response.body!!, IdentityResponse::class.java)
            //var result = Gson().fromJson(response.body!!, IdentityResponse::class.java)
            return response.body!!
        }
        catch(e: RestClientResponseException)
        {
            println(e)
            throw e
        }
    }

    private fun findEncryptionKey(validationIdentity: IdentityResponse): PublicKeyJwk {
        return validationIdentity.verificationMethod.find{it.publicKeyJwk.use.equals("enc")}?.publicKeyJwk ?: throw Exception()
    }

    private fun findVerificationKey(validationIdentity: IdentityResponse): PublicKeyJwk {
        return validationIdentity.verificationMethod.find{it.publicKeyJwk.use.equals("sig")}?.publicKeyJwk ?: throw Exception()
    }

    private fun findInitializeUri(validationIdentity: IdentityResponse): String {
        return validationIdentity.services.find{it.type.equals("InitializeService")}?.serviceEndpoint ?: throw Exception()
    }

    private fun findValidateUri(validationIdentity: IdentityResponse): String {
        return validationIdentity.services.find{it.type.equals("ValidationService")}?.serviceEndpoint ?: throw Exception()
    }
}


