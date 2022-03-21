package nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.token

/**
* Bits would have made more sense...
*/
enum class ValidationType(val value: Int) {
    /**
    * Validates just the Content of the DCC(Schema, Values, CBOR Structure)
    */
    Structure(0),

    /**
    * Structure Validation + Signature Validation
    */
    Cryptographic(1),

    /**
    * Structure Validation + Cryptographic + Business Rule Check(condition structure necessary)
    */
    Full(2)
}