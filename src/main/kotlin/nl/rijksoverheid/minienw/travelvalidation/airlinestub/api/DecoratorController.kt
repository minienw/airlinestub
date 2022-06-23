package nl.rijksoverheid.minienw.travelvalidation.airlinestub.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.responses.ApiResponse
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.InitiatingTokenParser
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.commands.HttpPostConfirmationTokenV2Command
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.commands.HttpPostTokenV2Command
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.swagger.Examples
import nl.rijksoverheid.minienw.travelvalidation.api.Headers
import nl.rijksoverheid.minienw.travelvalidation.api.data.callback.CallbackRequestBody
import nl.rijksoverheid.minienw.travelvalidation.api.data.identity.IdentityResponse
import nl.rijksoverheid.minienw.travelvalidation.api.data.token.TokenRequestBody
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@Api
@Controller
class DecoratorController(
    val postTokenV2Command: HttpPostTokenV2Command,
    val postConfirmationTokenV2Command: HttpPostConfirmationTokenV2Command,
    val initiatingTokenParser : InitiatingTokenParser, //TODO make once per request
)
{
    @PostMapping("/token")
    @ApiOperation(value = "Post the initiating token and receive the Validation Access Token and encryption parameters.")
    //@ApiResponse(code = 200, message = "Options for the endpoint", responseHeaders = {@ResponseHeader(name = "Allow", description = "Verbs allowed", response = String.class)})})
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200", description = "OK",
            headers = [
                Header(name = "x-sig", description = "A JWK formatted as a JWK -> JSON -> UTF-8 byte array -> base64.", schema = Schema(implementation = String::class, description = "A JWK formatted as JSON -> UTF-8 byte array -> base64.")),
                Header(name = "x-enc", description = "An RSA 4096 public key formatted as a JWK -> JSON -> UTF-8 byte array -> base64."),
                Header(name = "x-nonce", description = "Length 16 byte array formatted as base 64 string"),  ],
            content = [Content(
                mediaType = MediaType.TEXT_PLAIN_VALUE
                , schema = Schema(implementation = String::class, description = "Validation Access Token, the signed JWT with payload ValidationAccessTokenPayload.")
            )])
        , ApiResponse(responseCode = "400", description = "Invalid request body.", content = [Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = Schema(implementation = String::class))])
        , ApiResponse(responseCode = "401", description = "Authorization token signature invalid or payload invalid.")
        , ApiResponse(responseCode = "410", description = "Subject does not exist.", content = [Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = Schema(implementation = String::class))])
        , ApiResponse(responseCode = "500", description = "Server Error.", content = [Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = Schema(implementation = String::class))])
    ])
    fun token(
        @RequestBody requestBody: TokenRequestBody,
        @ApiParam(name=Headers.Authorization, required = true, example = "bearer notreallyanexample")
        @RequestHeader(Headers.Authorization) initiatingToken: String
    ) : ResponseEntity<Any>
    {
        var parsed = initiatingTokenParser.parse(initiatingToken)
        if(parsed.statusCode != HttpStatus.OK)
            return ResponseEntity(parsed.statusCode)

        return postTokenV2Command.execute(requestBody, initiatingToken.substring("bearer ".length))
    }

    @PostMapping("/callback")
    @ApiOperation(value = "Post the initiating token and receive the Validation Access token and encryption parameters.")
    @ApiResponses(value = [
        io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "OK",
            content = [Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = Schema(implementation = IdentityResponse::class, example = Examples.Identity)
            )]
        )
        , io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request body including invalid Response Token signature.", content = [Content(mediaType = MediaType.TEXT_PLAIN_VALUE)])
        , io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authorization token signature invalid or payload invalid.", content = [Content(mediaType = MediaType.TEXT_PLAIN_VALUE)])
        , io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "410", description = "Subject does not exist.")
        , io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Server Error.", content = [Content(mediaType = MediaType.TEXT_PLAIN_VALUE)])
    ])
    fun validationResultCallback(
        @RequestBody body: CallbackRequestBody,
        @ApiParam(name=Headers.Authorization, required = true, example = "bearer notreallyanexample")
        @RequestHeader(Headers.Authorization) initiatingToken: String
    ) : ResponseEntity<Any>
    {
        var parsed = initiatingTokenParser.parse(initiatingToken)
        if(parsed.statusCode != HttpStatus.OK)
            return ResponseEntity(parsed.statusCode)

        return postConfirmationTokenV2Command.execute(body.confirmationToken)
    }
}