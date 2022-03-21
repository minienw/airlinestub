package nl.rijksoverheid.minienw.travelvalidation.airlinestub.data

//import com.fasterxml.jackson.core.JsonProcessingException
//import com.fasterxml.jackson.databind.JsonNode
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.databind.node.ArrayNode
//import eu.europa.ec.dgc.validation.config.DgcConfigProperties
//import eu.europa.ec.dgc.validation.exception.DccException
//import eu.europa.ec.dgc.validation.service.AccessTokenKeyProvider
//import lombok.RequiredArgsConstructor
//import lombok.extern.slf4j.Slf4j
//import nl.rijksoverheid.minienw.validationservice.airlinestub.services.IApplicationSettings
//import org.bouncycastle.util.encoders.Base64
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
//import org.springframework.scheduling.annotation.Scheduled
//import org.springframework.stereotype.Service
//import java.io.ByteArrayInputStream
//import java.io.IOException
//import java.net.URI
//import java.net.http.HttpClient
//import java.net.http.HttpRequest
//import java.net.http.HttpResponse
//import java.security.PublicKey
//import java.security.cert.CertificateException
//import java.security.cert.CertificateFactory
//import javax.annotation.PostConstruct
//

data class PublicKeyJwk(
    /**
     * PEM format, base64
     * */
    val x5c: Array<String>,
    val kid: String,
    val alg: String,
    val use: String
)

//package eu.europa.ec.dgc.validation.service.impl
//import eu.europa.ec.dgc.validation.config.DgcConfigProperties
//import eu.europa.ec.dgc.validation.exception.DccException
//import eu.europa.ec.dgc.validation.service.AccessTokenKeyProvider
//import javax.annotation.PostConstruct
//import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//@ConditionalOnProperty(prefix = "dgc", name = ["decoratorUrl"])
//class DynamicAccessTokenKeyProvider() {
//    private val publicKeys: MutableMap<String, PublicKey> = HashMap()
//    private val dgcConfigProperties: IApplicationSettings? = null
//
//    /**
//     * load keys from identity document of decorator.
//     * The keys will be refreshed every 24h (configuration default).
//     * It is necessary for each node.
//     * @throws IOException IOException
//     * @throws InterruptedException InterruptedException
//     */
//    @PostConstruct
//    @Scheduled(fixedDelayString = "\${dgc.accessKeysRefresh.timeInterval}")
//    @Throws(
//        IOException::class,
//        InterruptedException::class
//    )
//    fun loadKeys() {
//        val decoratorUrl: String = dgcConfigProperties.getDecoratorUrl() //
//        DynamicAccessTokenKeyProvider.log.info("accessing identity document from decorator url: {}", decoratorUrl)
//        val client = HttpClient.newHttpClient()
//        val request = HttpRequest.newBuilder()
//            .uri(URI.create(decoratorUrl))
//            .build()
//        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
//        if (response.statusCode() == 200) {
//            loadKeysFrom(response.body())
//        } else {
//            throw DccException(
//                "can not load identity document from " + decoratorUrl
//                        + " response code " + response.statusCode()
//            )
//        }
//    }
//
//    /**
//     * load keys from json string.
//     * @param identityJson json
//     * @throws JsonProcessingException exception
//     */
//    @Throws(JsonProcessingException::class)
//    fun loadKeysFrom(identityJson: String?) {
//        val objectMapper = ObjectMapper()
//        val json = objectMapper.readTree(identityJson)
//        val verificationMethods = json["verificationMethod"]
//        if (verificationMethods != null && verificationMethods.isArray) {
//            for (verificationMethod: JsonNode in verificationMethods) {
//                val idNode = verificationMethod["id"]
//                if (idNode != null && idNode.isTextual) {
//                    val id = idNode.asText()
//                    if (id.contains("AccessTokenSignKey")) {
//                        val publicKeyJwk = verificationMethod["publicKeyJwk"]
//                        if (publicKeyJwk != null && publicKeyJwk.isObject) {
//                            val kidNode = publicKeyJwk["kid"]
//                            val x5cNode: ArrayNode? = publicKeyJwk["x5c"] as ArrayNode
//                            if (((kidNode != null) && kidNode.isTextual
//                                        && (x5cNode != null) && x5cNode.isArray)
//                            ) {
//                                val kid = kidNode.asText()
//                                val x5c = x5cNode[0].asText()
//                                importKey(kid, x5c)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun importKey(kid: String, x5c: String) {
//        try {
//            val certificateFactory = CertificateFactory
//                .getInstance("X.509")
//            val byteArrayInputStream = ByteArrayInputStream(Base64.decode(x5c))
//            val certificate = certificateFactory
//                .generateCertificate(byteArrayInputStream)
//            publicKeys[kid] = certificate.publicKey
//            DynamicAccessTokenKeyProvider.log.info("access key for kid={} imported from identity json", kid)
//        } catch (e: CertificateException) {
//            DynamicAccessTokenKeyProvider.log.warn(
//                "can not import access public key from identity json for kid: $kid",
//                e
//            )
//        }
//    }
//
//    fun getPublicKey(kid: String): PublicKey {
//        val publicKey = publicKeys.get(kid) ?: throw DccException("can not find access key with kid: $kid")
//        return publicKey
//    }
//}