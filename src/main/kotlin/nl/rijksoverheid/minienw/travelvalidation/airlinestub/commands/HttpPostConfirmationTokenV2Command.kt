package nl.rijksoverheid.minienw.travelvalidation.airlinestub.commands

import nl.rijksoverheid.minienw.travelvalidation.airlinestub.*
import nl.rijksoverheid.minienw.travelvalidation.api.data.callback.ConfirmationTokenPayload
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.ResultTokenParser
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class HttpPostConfirmationTokenV2Command(
    private val repo: ISessionRepository,
    private val resultTokenParser: ResultTokenParser
)
{
    fun execute(confirmationToken: String): ResponseEntity<Any>
    {
        val logger = LoggerFactory.getLogger(HttpRemoteValidationServiceSigningKeyProvider::class.java)

        var parseResult = resultTokenParser.parse(confirmationToken);

        if (parseResult.statusCode != HttpStatus.OK) {
            logger.info("Bad result token.");
            //Ignore bad token for demo
            return ResponseEntity.ok().build()
        }
        val payload = JwtPayloadParser().getPayload<ConfirmationTokenPayload>(confirmationToken)
        val session = repo.find(payload.subject) ?: return ResponseEntity("Subject not found", HttpStatus.NOT_FOUND)
        session.validationResult = payload.result
        repo.save(session)
        return ResponseEntity.ok().build()
    }
}
