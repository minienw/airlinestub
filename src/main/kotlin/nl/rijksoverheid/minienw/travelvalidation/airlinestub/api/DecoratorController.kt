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
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.io.File


@Controller
class DecoratorController(
    private val appSettings: IApplicationSettings,
    //val createInitiatingTokenV2Command: CreateInitiatingTokenV2Command,
    val postTokenV2Command: HttpPostTokenV2Command,
    val postConfirmationTokenV2Command: HttpPostConfirmationTokenV2Command
)
{
    @GetMapping("/starthere")
    fun getInitiatingQrCode() : ResponseEntity<Any>
    {        val initiatingQrTokenPayload = InitiatingQrTokenPayload(
        issuer = "http://kellair.com",
        iat = 10000000,
        sub="0123456789ABCDEF0123456789ABCDEF",
        exp= 20000000
    )
        val initiatingQrTokenPayloadJson = Gson().toJson(initiatingQrTokenPayload)
        var privateKeyPem = File(appSettings.configFileFolderPath,"accesstokensign-privatekey-1.pem").readText()
        var privateKey = CryptoKeyConverter.decodeAsn1DerPkcs8PemPrivateKey(privateKeyPem)
        var subjectJwt = Jwts.builder()
            .setHeaderParam("kid", "L6TcjnbZge4=") //TODO grab from identity
            .setPayload(initiatingQrTokenPayloadJson)
            .signWith(privateKey, SignatureAlgorithm.RS256)
            .compact()

        val initiatingQrPayload = InitiatingQrPayload(
            serviceIdentity = "http://localhost:8081/identity",
            subject =  "0123456789ABCDEF0123456789ABCDEF",
            consent = "informed consent text...",
            privacyUrl = "privacy policy url...",
            token = subjectJwt,
            serviceProvider = "Kellair"
        )

        var responseBody = Gson().toJson(initiatingQrPayload)

        return ResponseEntity.ok(responseBody)
        //return createInitiatingTokenV2Command.execute()
    }

    @PostMapping("/token")
    fun token(@RequestBody initiatingToken: String) : ResponseEntity<Any>
    {
        return postTokenV2Command.execute(initiatingToken)
    }

    @PostMapping("/callback")
    fun validationResultCallback(@RequestBody body: CallbackRequestBody) : ResponseEntity<Any>
    {
        return postConfirmationTokenV2Command.execute(body.confirmationToken)
    }
}