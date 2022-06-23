package nl.rijksoverheid.minienw.travelvalidation.airlinestub.api

import nl.rijksoverheid.minienw.travelvalidation.api.data.callback.ConfirmationTokenPayload
import nl.rijksoverheid.minienw.travelvalidation.api.data.token.InitiatingQrPayload
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RootController {

    @GetMapping("/")
    fun GetRoot() : ResponseEntity<String>
    {
        return ResponseEntity( "<html>Swagger is <a href=\"swagger-ui//index.html\">here</a><html>",HttpStatus.OK)
    }

//    @GetMapping("/fakeInitiatingQrPayload")
//    fun showInitiatingQrPayload() : ResponseEntity<InitiatingQrPayload>
//    {
//        return ResponseEntity(HttpStatus.I_AM_A_TEAPOT);
//    }
//
//    @GetMapping("/fakeConfirmationTokenPayload")
//    fun showConfirmationTokenPayload() : ResponseEntity<ConfirmationTokenPayload>
//    {
//        return ResponseEntity(HttpStatus.I_AM_A_TEAPOT);
//    }
}