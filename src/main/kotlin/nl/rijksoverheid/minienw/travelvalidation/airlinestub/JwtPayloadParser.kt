package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import com.google.gson.Gson
import io.jsonwebtoken.Jwts

class JwtPayloadParser {
    //Splits the payload from the signature and parses. No sig check performed.
    inline fun <reified T: Any> getPayload(jwt: String): T {
        val splitToken = jwt.split(".")
        val unsignedToken = splitToken[0] + "." + splitToken[1] + "."
        val body = Jwts.parserBuilder().build().parse(unsignedToken).body
        val gson = Gson()
        return gson.fromJson(gson.toJson(body), T::class.java)
    }
}
