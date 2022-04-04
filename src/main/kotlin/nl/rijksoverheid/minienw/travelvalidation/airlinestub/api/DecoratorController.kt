package nl.rijksoverheid.minienw.travelvalidation.airlinestub.api

import nl.rijksoverheid.minienw.travelvalidation.airlinestub.CryptoKeyConverter
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.commands.HttpPostConfirmationTokenV2Command
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.commands.HttpPostTokenV2Command
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.IApplicationSettings
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.token.InitiatingQrPayload
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.token.InitiatingQrTokenPayload

import com.google.gson.Gson
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.callback.CallbackRequestBody
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.token.TokenRequestBody
import org.bouncycastle.util.encoders.Base64
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import java.io.File
import java.util.*


@Controller
class DecoratorController(
    private val appSettings: IApplicationSettings,
    //val createInitiatingTokenV2Command: CreateInitiatingTokenV2Command,
    val postTokenV2Command: HttpPostTokenV2Command,
    val postConfirmationTokenV2Command: HttpPostConfirmationTokenV2Command
)
{
    @GetMapping("/starthere2")
    fun getInitiatingQrCodePage() : ResponseEntity<String>
    {
        var tokenJson = CreateExampleInitiatingToken()
        var urlToken = Base64.toBase64String(tokenJson.toByteArray(Charsets.UTF_8))

        var html = "<html><a href=\"${appSettings.rootServiceUrl}/${urlToken}\">Link</a></html>"

        return ResponseEntity.ok(html)
        //return createInitiatingTokenV2Command.execute()
    }

    @GetMapping("/starthere")
    fun getInitiatingQrCode() : ResponseEntity<Any>
    {        val initiatingQrTokenPayload = InitiatingQrTokenPayload(
        issuer = "http://kellair.com",
        iat = 10000000,
        sub= "0123456789ABCDEF0123456789ABCDEF",
        exp= 20000000
    )
        var responseBody = CreateExampleInitiatingToken()
        return ResponseEntity.ok(responseBody)
        //return createInitiatingTokenV2Command.execute()
    }

    private fun CreateExampleInitiatingToken(): String
    {
        val initiatingQrTokenPayload = InitiatingQrTokenPayload(
            issuer = "http://kellair.com",
            iat = 10000000,
            sub= UUID.randomUUID().toString().replace("-","").uppercase(),
            exp= 20000000
        )

        val initiatingQrTokenPayloadJson = Gson().toJson(initiatingQrTokenPayload)
        var privateKeyPem = File(appSettings.configFileFolderPath, "accesstokensign-privatekey-1.pem").readText()
        var privateKey = CryptoKeyConverter.decodeAsn1DerPkcs8PemPrivateKey(privateKeyPem)

        var subjectJwt = Jwts.builder()
            .setHeaderParam("kid", "L6TcjnbZge4=") //TODO grab from identity
            .setPayload(initiatingQrTokenPayloadJson)
            .signWith(privateKey, SignatureAlgorithm.RS256)
            .compact()

        val initiatingQrPayload = InitiatingQrPayload(
            serviceIdentity = "${appSettings.rootServiceUrl}/identity",
            subject = initiatingQrTokenPayload.sub,
            consent = "By clicking “Upload” and selecting a QR code you will be sending you DCC containing personal data to the server that will validate it for your travel. Make sure you expect to do so. If you are not checking in for a trip abroad, close your browser screen.;By selecting OK you will be sending the validation result containg personal data to the transport company. Only do so if you are actually checking in.",
            privacyUrl = "privacy policy url...",
            token = subjectJwt,
            serviceProvider = "Kellair"
        )

        return Gson().toJson(initiatingQrPayload)
    }

    @PostMapping("/token")
    fun token(
        @RequestBody requestBody: TokenRequestBody,
        @RequestHeader("authorization") initiatingToken: String
    ) : ResponseEntity<Any>
    {
        return postTokenV2Command.execute(requestBody, initiatingToken)
    }

    @PostMapping("/callback")
    fun validationResultCallback(@RequestBody body: CallbackRequestBody) : ResponseEntity<Any>
    {
        return postConfirmationTokenV2Command.execute(body.confirmationToken)
    }
}