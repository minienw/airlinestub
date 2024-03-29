package nl.rijksoverheid.minienw.travelvalidation.airlinestub.web

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.IApplicationSettings
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.ISessionRepository
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.services.SessionInfo
import nl.rijksoverheid.minienw.travelvalidation.api.data.identity.*
import nl.rijksoverheid.minienw.travelvalidation.api.data.token.*
import nl.rijksoverheid.minienw.travelvalidation.validationservice.services.CryptoKeyConverter
import org.bouncycastle.util.encoders.Base64

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*

import java.io.File
import java.time.Instant
import java.util.*

data class TripArgs
    (
    var fromCountry: String,
    var toCountry: String,
)

data class TripArgsViewModel
(
    var message: String?,
    var tokenJson: String?,
    var payloadJson: String?,
    var startingUrl: String?,
    var validationStatusUrl: String?
)

data class StatusViewModel(val subject:String, val status:String)

@Controller
class StartHereController(
    private val appSettings: IApplicationSettings,
    private val repo: ISessionRepository
)
{
    @GetMapping("/start")
    fun send(model: Model): String? {
        model.addAttribute("tripArgs", TripArgs("DE", "NL"))
        model.addAttribute("tripArgsViewModel", TripArgsViewModel("","","","",""))
        return "start/index"
    }

    @GetMapping("/status/{subject}")
    fun getStatus(@PathVariable("subject") subject: String, model: Model): String? {
        val session = repo.find(subject)
        model.addAttribute("statusViewModel", StatusViewModel(subject, getResultText(session)))
        return "status"
    }

    fun getResultText(sessionInfo: SessionInfo?) :String
    {
        if (sessionInfo == null) return "Not found."
        return sessionInfo.validationResult ?: "Pending"
    }

    @PostMapping("/start/update")
    fun messageSend(model: Model, @ModelAttribute("tripArgs") tripArgs:TripArgs): String?
    {
        val initiatingQrTokenPayload = createInitiatingQrTokenPayload(Instant.now()) //="white-space: pre"ingToken()
        val initiatingQrPayload = createExampleInitiatingToken(initiatingQrTokenPayload)
        val tokenJson = Gson().toJson(initiatingQrPayload)
        repo.save(SessionInfo(initiatingQrPayload.subject, tripArgs, null))
        val startingUrl = "${appSettings.walletProcessUrl}/${Base64.toBase64String(tokenJson.toByteArray(Charsets.UTF_8))}"
        val statusUrl = "/status/${initiatingQrPayload.subject}"

        val prettyJsonWriter = GsonBuilder()
            .setPrettyPrinting()
            .create()

        val prettyPayloadJson = prettyJsonWriter.toJson(initiatingQrPayload)
        val prettyTokenJson = prettyJsonWriter.toJson(initiatingQrTokenPayload)

        model["tripArgsViewModel"] = TripArgsViewModel("", prettyTokenJson, prettyPayloadJson, startingUrl, statusUrl)
        return "start/index"
    }

    private fun readIdentityFile(): IdentityResponse {
        val content = File(appSettings.configFileFolderPath, "identity.json").readText()
        val doc = Gson().fromJson(content, IdentityResponse::class.java)
        return doc
    }

    private fun createExampleInitiatingToken(initiatingQrTokenPayload: InitiatingQrTokenPayload): InitiatingQrPayload
    {
        val initiatingQrTokenPayloadJson = Gson().toJson(initiatingQrTokenPayload)
        val privateKeyPem = File(appSettings.configFileFolderPath, "accesstokensign-privatekey-1.pem").readText()
        val privateKey = CryptoKeyConverter.decodeAsn1DerPkcs8PemPrivateKey(privateKeyPem)

        val subjectJwt = Jwts.builder()
            .setHeaderParam("kid", "L6TcjnbZge4=") //TODO grab from identity
            .setPayload(initiatingQrTokenPayloadJson)
            .signWith(privateKey, SignatureAlgorithm.RS256)
            .compact()

        return InitiatingQrPayload(
        serviceIdentity = readIdentityFile().id,
            subject = initiatingQrTokenPayload.subject,
            consent = "By clicking “Upload” and selecting a QR code you will be sending you DCC containing personal data to the server that will validate it for your travel. Make sure you expect to do so. If you are not checking in for a trip abroad, close your browser screen.;By selecting OK you will be sending the validation result containg personal data to the transport company. Only do so if you are actually checking in.",
            privacyUrl = "privacy policy url...",
            token = subjectJwt,
            serviceProvider = "Kellair"
        )
    }

    private fun createInitiatingQrTokenPayload(snapshot: Instant) = InitiatingQrTokenPayload(
        issuer = "https://kellair.com",
        whenIssued = Instant.now().epochSecond,
        subject = UUID.randomUUID().toString().replace("-", "").uppercase(),
        whenExpires = snapshot.epochSecond + 100 * 24 * 3600
    )
}