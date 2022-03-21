package nl.rijksoverheid.minienw.travelvalidation.airlinestub.commands

import nl.rijksoverheid.minienw.travelvalidation.airlinestub.IApplicationSettings
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.IDateTimeProvider
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.ISessionRepository
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.ValidationServicesSubjectIdGenerator
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class HttpPostConfirmationTokenV2Command(
    private val repo: ISessionRepository,
    private val dtp : IDateTimeProvider,
    private val appSettings : IApplicationSettings,
    private val subjectIdGenerator: ValidationServicesSubjectIdGenerator
)
{
    fun execute(confirmationToken: String): ResponseEntity<Any>
    {
        //TODO Validate token
        //TODO Tie up a subjectId with an existing session
        return ResponseEntity.ok().build()
    }
}

