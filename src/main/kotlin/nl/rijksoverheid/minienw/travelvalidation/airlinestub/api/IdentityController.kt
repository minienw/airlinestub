package nl.rijksoverheid.minienw.travelvalidation.airlinestub.api

import nl.rijksoverheid.minienw.travelvalidation.airlinestub.IApplicationSettings
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController
class IdentityController(
    val appSettings : IApplicationSettings
)
{
    @GetMapping("/identity")
    @ResponseStatus(HttpStatus.OK)
    fun WholeDoc(): ResponseEntity<String>
    {
        var f = File(appSettings.configFileFolderPath, "identity.json")
        var content = f.readText()
        return ResponseEntity(content, HttpStatus.OK)
    }
}
