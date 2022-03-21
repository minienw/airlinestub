package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import io.jsonwebtoken.SignatureAlgorithm
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.data.PublicKeyJwk
import org.bouncycastle.util.encoders.Base64
import org.bouncycastle.util.io.pem.PemObject
import org.bouncycastle.util.io.pem.PemWriter
import java.io.*
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*


class CryptoKeyConverter {
    companion object {

        fun decodeAsn1DerPkcs8Base64ToPrivateKey(algorithm: String, base64PrivateKey: String): PrivateKey =
            decodeAsn1DerPkcs8ToPrivateKey(algorithm, Base64.decode(base64PrivateKey))

        fun decodeAsn1DerPkcs8ToPrivateKey(algorithm: String, buffer: ByteArray): PrivateKey {
            val keySpec = PKCS8EncodedKeySpec(buffer)
            val keyFactory = KeyFactory.getInstance(algorithm)
            return keyFactory.generatePrivate(keySpec)
        }

        fun decodeAsn1DerPkcs1X509Base64ToPublicKey(algorithm: String, base64PublicKey: String): PublicKey =
            decodeAsn1DerPkcs1X509ToPublicKey(algorithm, Base64.decode(base64PublicKey))

//        fun decodeAsn1DerPkcs1X509ToPublicKey(buffer: ByteArray): PublicKey {
////            val input = ByteArrayInputStream(buffer)
////            val certificateFactory = CertificateFactory.getInstance("X.509", "BC")
////            val certificate = certificateFactory.generateCertificate(input)// as java.security.cert.X509Certificate
////            val keyFactory: KeyFactory = KeyFactory.getInstance((certificate as java.security.cert.X509Certificate).sigAlgName)
////            val keySpec = X509EncodedKeySpec(buffer)
////            return keyFactory.generatePublic(keySpec)
//
//            val input : Reader = ByteArrayInputStream(buffer).reader()
//            val pemParser = PEMParser(input);
//            var pemObj = pemParser.readObject()
//            val publicKeyInfo = SubjectPublicKeyInfo.getInstance(pemObj);
//            val converter = JcaPEMKeyConverter();
//            return converter.getPublicKey(publicKeyInfo);
//        }

        fun decodeAsn1DerPkcs1X509ToPublicKey(algorithm: String, buffer: ByteArray): PublicKey {
            val keyFactory: KeyFactory = KeyFactory.getInstance(algorithm)
            val keySpec = X509EncodedKeySpec(buffer)
            return keyFactory.generatePublic(keySpec)
        }

        /**
         * No certificate chain
         * */
        fun encodeJwkX5c(publicKey: PublicKey): String = encodeAsn1DerPkcs1X509Base64(publicKey)

        fun decodeJwkX5c(publicKey: PublicKeyJwk): PublicKey
        {
            if (publicKey.alg.isNullOrBlank())
                throw IllegalArgumentException("JWK alg value missing.")

            if (publicKey.x5c.size == 0)
                throw IllegalArgumentException("JWK x5c value missing.")

            var sigAlg: SignatureAlgorithm
            try {
                sigAlg = SignatureAlgorithm.valueOf(publicKey.alg)
            }
            catch (_:IllegalArgumentException)
            {
                throw IllegalArgumentException("JWK x5c value missing.")
            }

            return decodeAsn1DerPkcs1X509Base64ToPublicKey(sigAlg.familyName, publicKey.x5c[0])
        }

        /**
         * No certificate chain
         * */
        fun encodeRs256VerificationJwk(key: PublicKey): PublicKeyJwk {
            if (key.algorithm != "RSA")
                throw IllegalArgumentException("Key is not RSA.");

            var pemBytes = key.encoded
            return PublicKeyJwk(
                use = "sig",
                alg = "RS256", //TODO convert?
                kid = getKid(pemBytes), //TODO double encoded?
                x5c = arrayOf(encodeJwkX5c(key))
            )
        }

        fun encodeRsaEncryptionJwk(key: PublicKey): PublicKeyJwk {
            if (key.algorithm != "RSA")
                throw IllegalArgumentException("Key is not RSA.");

            var pemBytes = key.encoded
            return PublicKeyJwk(
                use = "enc",
                alg = "RSA",
                kid = getKid(pemBytes),
                x5c = arrayOf(encodeJwkX5c(key))
            )
        }

        /**
         * PKCS#8, type = PRIVATE KEY or PUBLIC KEY
         * Remove headers/footers/64 char line split
         */
        fun decodePem(type: String, pemValue: String): String {
            var header = "-----BEGIN $type-----"

            if (!pemValue.startsWith(header))
                throw IllegalArgumentException("Header missing.")

            val footerIndex = pemValue.indexOf("-----END $type-----")
            if (footerIndex < 0)
                throw IllegalArgumentException("Footer missing.")

            var result = pemValue.substring(header.length, footerIndex)
            result = result.replace("\r\n", "")
            return result
        }

//        fun parseWithException(derEncodedCert: ByteArray): X509Certificate? {
//            if (derEncodedCert == null || derEncodedCert.size == 0)
//            throw new
//
//            val cf = CertificateFactory.getInstance("X.509")
//                val cert = cf.generateCertificate(ByteArrayInputStream(derEncodedCert))
//
//                if (cert !is X509Certificate)
//                    throw CertificateException("Not a X.509 certificate: " + cert.type)
//
//                return cert as X509Certificate
//        }


        private fun convertEncodedKeyToPemWithHeaderAndFooter(header: String, encodedKey: ByteArray): String {
            return encodePem(header, encodedKey)
        }

        fun encodePem(header: String, buffer: ByteArray): String {
            var sw = StringWriter()
            PemWriter(sw).use { writer ->
                writer.writeObject(PemObject(header, buffer))
                writer.flush()
            }
            return sw.toString()
        }

        fun getKid(keyValue: ByteArray): String {
            val hash = MessageDigest.getInstance("SHA-256").digest(keyValue);
            val kidBytes = Arrays.copyOfRange(hash, 0, 8)
            return Base64.toBase64String(kidBytes);
        }

        /**
         * Halfway to PEM
         * */
        fun encodeAsn1DerPkcs8Base64(key: PrivateKey): String = Base64.toBase64String(key.encoded)
        fun encodeAsn1DerPkcs8Pem(key: PrivateKey): String = encodePem("PRIVATE KEY", key.encoded)
        fun decodeAsn1DerPkcs8PemPrivateKey(value: String): PrivateKey {
            var stripped = decodePem("PRIVATE KEY", value)
            return decodeAsn1DerPkcs8Base64ToPrivateKey("RSA", stripped)
        }

        fun encodeAsn1DerPkcs1X509Base64(key: PublicKey): String = Base64.toBase64String(key.encoded)
        fun encodeAsn1DerPkcs1X509Pem(key: PublicKey): String = encodePem("PUBLIC KEY", key.encoded)
        fun decodeAsn1DerPkcs1X509PemPublicKey(value: String): PublicKey {
            var stripped = decodePem("PUBLIC KEY", value)

            return decodeAsn1DerPkcs1X509Base64ToPublicKey("RSA", stripped)
        }

//        //For X509s in JWKs
//        private fun getPublicKey(x5c: String): PublicKey {
//            val certificateFactory = CertificateFactory.getInstance("X.509")
//            val byteArrayInputStream = ByteArrayInputStream(Base64.decode(x5c))
//            val certificate = certificateFactory.generateCertificate(byteArrayInputStream)
//            return certificate.publicKey
//        }


//        //For X509s in JWKs
//        private fun get(publicKey: PublicKey): PublicKey
//        {
//            val certificateFactory = CertificateFactory.getInstance("X.509")
//            val byteArrayOutputStream = ByteArrayOutputStream()
//            certificateFactory.a
//
//            return certificate.publicKey
//        }
    }
}

