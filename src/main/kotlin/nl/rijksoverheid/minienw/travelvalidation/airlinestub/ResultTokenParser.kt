package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import com.google.gson.Gson
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SigningKeyResolverAdapter
import nl.rijksoverheid.minienw.travelvalidation.api.data.JwtHeader
import nl.rijksoverheid.minienw.travelvalidation.api.data.validate.ResultTokenPayload
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

@Service
class ResultTokenParser(
//    private val appSettings: IApplicationSettings,
//    private val timeProvider: IDateTimeProvider,
    private val validationSigningKeyResolverAdapter: SigningKeyResolverAdapter
)
{
    fun parse(value: String): ResponseEntity<ResultTokenPayload>
    {
        val logger = LoggerFactory.getLogger(ResultTokenParser::class.java)

        val errors = ArrayList<String>()
        val stripped = if (value.startsWith("bearer", true)) value.substring(6).trim() else value

        try{
            val parsed = build().parse(stripped)
            val gson = Gson()

            val header = gson.fromJson(gson.toJson(parsed.header), JwtHeader::class.java)
            if (header.kid.isNullOrBlank())
                errors.add("Kid missing.")

            //TODO base64 check on kid.

            if (header.alg.isNullOrBlank())
                errors.add("Algorithm missing.")
//            else
//            {
//                if (!appSettings.validationAccessTokenSignatureAlgorithms.any { it.equals(header.alg, ignoreCase = true) })
//                    errors.add("Algorithm not supported.")
//            }

            val toJson = gson.toJson(parsed.body)
            val result = gson.fromJson(toJson, ResultTokenPayload::class.java)

            //TODO may be worth doing by hand instead.
            val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
            val validator: Validator = factory.validator
            val validationErrors = validator.validate(result)
            for(i in validationErrors)
                errors.add(i.toString())

//            if (result.iat > timeProvider.snapshot().epochSecond)
//                errors.add("Issued in the future.")

            //TODO audience == validate url outside...
            //TODO subject == url subject from url... outside...

            if (errors.isNotEmpty()) {
                val sb = StringBuilder("Token is invalid:\n")
                for(i in errors) sb.append(i)
                logger.info(sb.toString())
                return ResponseEntity(HttpStatus.UNAUTHORIZED)
            }

            return ResponseEntity(result, HttpStatus.OK)

        } catch(e: JwtException) {
            logger.info(e.toString())
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }

    private fun build() : JwtParser {
        val result = Jwts.parserBuilder()
        //result.setAllowedClockSkewSeconds(120) //TODO setting?
        result.setAllowedClockSkewSeconds(10000000) //TODO setting?
        result.setSigningKeyResolver(validationSigningKeyResolverAdapter)
        //TODO .setClock() //Wrap time provider Tests?
        return result.build()
    }
}