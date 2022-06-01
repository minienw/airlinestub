package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import nl.rijksoverheid.minienw.travelvalidation.api.data.initialize.ValidationInitializeRequestBody
import org.bouncycastle.util.encoders.Base64
import org.bouncycastle.util.encoders.DecoderException
import org.springframework.stereotype.Component

@Component
class ValidationInitializeRequestBodyValidatorV2 {
    val result = ArrayList<String>()

    fun validate(body: ValidationInitializeRequestBody): List<String> {
        validateNonce(body.nonce)

//        if (!body.walletPublicKey.isNullOrEmpty())
//            result.add("Wallet signing is not supported.")
//
//        if (!body.walletPublicKeyAlgorithm.isNullOrEmpty())
//            result.add("Wallet signing is not supported.")

        return result;
    }

    private fun validateNonce(nonce:String?){
        try {
            if (nonce.isNullOrEmpty()) {
                result.add("Nonce empty.")
                return;
            }

            if (Base64.decode(nonce).size != 16)
                result.add("Nonce is not correct length (16 bytes).")

        } catch (e: DecoderException) {
            result.add("IV is not valid base64.")
        }
    }

}
