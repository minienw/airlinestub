package nl.rijksoverheid.minienw.travelvalidation.airlinestub.api

import nl.rijksoverheid.minienw.travelvalidation.airlinestub.commands.HttpPostConfirmationTokenV2Command
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.commands.HttpPostTokenV2Command
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.callback.CallbackRequestBody
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.token.TokenRequestBody

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@Controller
class DecoratorController(
    val postTokenV2Command: HttpPostTokenV2Command,
    val postConfirmationTokenV2Command: HttpPostConfirmationTokenV2Command
)
{
    @PostMapping("/token")
    fun token(
        @RequestBody requestBody: TokenRequestBody,
        @RequestHeader("authorization") initiatingToken: String
    ) : ResponseEntity<Any>
    {
        return postTokenV2Command.execute(requestBody, initiatingToken.substring("bearer ".length))
    }

    @PostMapping("/callback")
    fun validationResultCallback(@RequestBody body: CallbackRequestBody) : ResponseEntity<Any>
    {
        return postConfirmationTokenV2Command.execute(body.confirmationToken)
    }
}