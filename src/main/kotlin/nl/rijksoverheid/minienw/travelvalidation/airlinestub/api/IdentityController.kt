package nl.rijksoverheid.minienw.travelvalidation.airlinestub.api

import io.swagger.annotations.*
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.IApplicationSettings
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.swagger.Examples
import nl.rijksoverheid.minienw.travelvalidation.api.data.identity.IdentityResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File

@Api
@RestController
class IdentityController(
    val appSettings : IApplicationSettings
)
{

    @GetMapping("/identity")
    @ApiOperation(value = "Get the configuration information about the hosted services.", authorizations = arrayOf<Authorization>())
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "OK"
            , content = [Content(mediaType=MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = IdentityResponse::class
                    , example = Examples.Identity
                    ) )]
            )
        , ApiResponse(responseCode = "500", description = "Server Error")
    ])
    fun WholeDoc(): ResponseEntity<String>
    {
        var f = File(appSettings.configFileFolderPath, "identity.json")
        var content = f.readText()
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(content)
    }
}
