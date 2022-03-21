package nl.rijksoverheid.minienw.travelvalidation.airlinestub.api

object Headers {
    const val Authorization = "authorization"
    const val Version = "X-Version"
    //const val EncryptValidationInitResponse = "X-Crypto-Enc"
    //const val SignValidationInitResponse = "X-Crypto-Sign"

    //const val V1 = "1.00"
    const val V2 = "2.00"
    const val MustBeVersion2 ="$Version=$V2"
    //const val MustBeVersion1 ="$Version=$V1"

    const val Json = "application/json"
    const val AcceptJson = "Accept=$Json"
}