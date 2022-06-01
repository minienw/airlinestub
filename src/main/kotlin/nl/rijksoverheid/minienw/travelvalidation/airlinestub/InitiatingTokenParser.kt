package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import com.google.gson.Gson
import io.jsonwebtoken.*
import nl.rijksoverheid.minienw.travelvalidation.api.data.*
import nl.rijksoverheid.minienw.travelvalidation.api.data.token.InitiatingQrTokenPayload
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

@Component
class InitiatingTokenParser(
    private val timeProvider: IDateTimeProvider,
    private val verificationKeyProvider: SigningKeyResolverAdapter,
)
{
    fun parse(value: String): ResponseEntity<InitiatingQrTokenPayload>
    {
        val logger = LoggerFactory.getLogger(InitiatingTokenParser::class.java)

        val errors = ArrayList<String>()
        val stripped = if (value.startsWith("bearer", true)) value.substring(6).trim() else value

        try{
            var parsed = build().parse(stripped)
            val gson = Gson()

            val header = gson.fromJson(gson.toJson(parsed.header), JwtHeader::class.java)
            if (header.kid.isNullOrBlank())
                errors.add("Kid missing.")

            //TODO base64 check on kid.

            if (header.alg.isNullOrBlank())
                errors.add("Algorithm missing.")
//            else
//            {
//                if (!appSettings.validationAccessTokentSigAlgorithms.any { it.equals(header.alg, ignoreCase = true) })
//                    errors.add("Algorithm not supported.")
//            }

            val result = gson.fromJson(gson.toJson(parsed.body), InitiatingQrTokenPayload::class.java)

            //TODO may be worth doing by hand instead.
            val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
            val validator: Validator = factory.validator
            val validationErrors = validator.validate(result)
            for(i in validationErrors)
                errors.add(i.toString())

            if (result.whenIssued > timeProvider.snapshot().epochSecond)
                errors.add("Issued in the future.")

            //TODO audience == validate url outside...
            //TODO subject == url subject from url... outside...

            if (errors.isNotEmpty()) {
                var sb = StringBuilder("Token is invalid:\n")
                for(i in errors) sb.append(i)
                logger.info(sb.toString());
                return ResponseEntity(HttpStatus.UNAUTHORIZED)
            }

            return ResponseEntity(result,HttpStatus.OK)

        } catch(e: JwtException) {
            logger.info(e.toString());
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }

    private fun build() : JwtParser {
        val result = Jwts.parserBuilder()
        result.setAllowedClockSkewSeconds(120) //TODO setting?
        //result.setAllowedClockSkewSeconds(10000000) //TODO setting?
        result.setSigningKeyResolver(verificationKeyProvider)
        //TODO .setClock() //Wrap time provider Tests?
        return result.build()
    }
}