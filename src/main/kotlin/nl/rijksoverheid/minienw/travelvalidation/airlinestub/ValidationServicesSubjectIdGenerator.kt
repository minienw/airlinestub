package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class ValidationServicesSubjectIdGenerator {
    fun next():String {
        return HexStringEncoding.encode(Random.nextBytes(16));
    }

    fun validate(value:String): List<String> {
        var result = ArrayList<String>()
        try {
            if (HexStringEncoding.decode(value).size != 16)
                result.add("Incorrect length.")
        }
        catch (ex: NumberFormatException)
        {
            result.add("Incorrect length.")
            return result;
        }
        return result
    }
}