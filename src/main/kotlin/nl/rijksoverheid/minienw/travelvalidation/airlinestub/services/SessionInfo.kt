package nl.rijksoverheid.minienw.travelvalidation.airlinestub.services

import nl.rijksoverheid.minienw.travelvalidation.airlinestub.web.TripArgs

data class SessionInfo(
    var subjectId: String, //Initiating QR id
    var tripArgs: TripArgs, //Info to put in the /token response
    var validationResult: String?
)
