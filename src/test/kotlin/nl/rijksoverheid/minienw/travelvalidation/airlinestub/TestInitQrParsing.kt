package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.token.InitiatingQrTokenPayload
import org.junit.jupiter.api.Test

class TestInitQrParsing {

    //TODO this will eventually time out cos IAT/EXP
    @Test
    fun parse()
    {
        val token = "eyJraWQiOiJMNlRjam5iWmdlND0iLCJhbGciOiJSUzI1NiJ9.eyJpc3N1ZXIiOiJodHRwOi8va2VsbGFpci5jb20iLCJpYXQiOjE2NDk5NDU5OTYsInN1YiI6IjY0RTk3QUE3MUMwNTQ0OERBNjA5NTEzRDlFREU0RjQ2IiwiZXhwIjoxNjU4NTg1OTk2fQ.IhBpHQ60pJ9diYhMnZXhiW00x5G3IubeeDlo8vzayToDevZ7CeaIT06Je3lrsC4j0qwcYL9592KNLsfpUcfaj2UObvDcD1xXvWKyrm_MpYeRFNlzZRs3TK61C-paDy88S7KA9L6l_XUrt1Co0BdsgxFc683v1pR_Hx-lMJ8JPCBj5T3NgzDdgOnfvSgVPEmkQHjwTTQC9IUK7wExi9tCIaO-AGoXQS3wMOnNDTPrwLjatvdXQndS1UH8CBXdCreXXGchbW8sRyfp5k_EJjGxjqQYnynEjQtO0aeNelzbEZ7UPgunJq8KWgCl4xza8NaV_a1jZXymOjQhGZutI3mGLd8dSBNKHpu95hSt6bFhQK5iZ3OTnlfsYPNmFFqaAxlSSEUI7GQuvyWhqRSemWAi7KbHZ-BKWq4AWkYC02VeaTQV2urvYeJEYoUj0NW-z9VQLRhsgAPp_KT2nJaqvl-eWFRFBZHAJb7fGcyZIXutthLWGBe5rikky42fKeuht1XuFrsk-feH6vXxyRis6Rs8uZTghHwwnxPr4lk_6WsMKrRYLgoUb6XVxpa4FzizZKNk11YefPpyPvZjPL2HwtDbUmAma6SgVMjT8gPGig2JgxBdNQK-l3L9GnpK6OgJwhnc8G55paLgbt-3n40oCW_8Di14z4MMGMBttkWcGux8vaQ"
        assert(JwtPayloadParser().getPayload<InitiatingQrTokenPayload>(token).sub.length == 32)
    }
}