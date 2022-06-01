package nl.rijksoverheid.minienw.travelvalidation.airlinestub.api

import nl.rijksoverheid.minienw.travelvalidation.airlinestub.InitiatingTokenParser
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.commands.HttpPostConfirmationTokenV2Command
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.commands.HttpPostTokenV2Command
import nl.rijksoverheid.minienw.travelvalidation.api.data.callback.CallbackRequestBody
import nl.rijksoverheid.minienw.travelvalidation.api.data.token.TokenRequestBody
import org.springframework.http.HttpStatus

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@Controller
class DecoratorController(
    val postTokenV2Command: HttpPostTokenV2Command,
    val postConfirmationTokenV2Command: HttpPostConfirmationTokenV2Command,
    val initiatingTokenParser : InitiatingTokenParser, //TODO make once per request
)
{
    @PostMapping("/token")
    fun token(
        @RequestBody requestBody: TokenRequestBody,
        @RequestHeader("authorization") initiatingToken: String
    ) : ResponseEntity<Any>
    {
        var parsed = initiatingTokenParser.parse(initiatingToken)
        if(parsed.statusCode != HttpStatus.OK)
            return ResponseEntity(parsed.statusCode)

        return postTokenV2Command.execute(requestBody, initiatingToken.substring("bearer ".length))
    }

    @PostMapping("/callback")
    fun validationResultCallback(
        @RequestBody body: CallbackRequestBody,
        @RequestHeader("authorization") initiatingToken: String
    ) : ResponseEntity<Any>
    {
        var parsed = initiatingTokenParser.parse(initiatingToken)
        if(parsed.statusCode != HttpStatus.OK)
            return ResponseEntity(parsed.statusCode)

        return postConfirmationTokenV2Command.execute(body.confirmationToken)
    }
}