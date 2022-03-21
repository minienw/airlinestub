package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import java.security.Security

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableWebMvc //For swagger
@EnableConfigurationProperties(ApplicationPropertiesFile::class)
class AirlineStubApplication

fun main(args: Array<String>) {
	Security.addProvider(BouncyCastleProvider())
	runApplication<AirlineStubApplication>(*args)
}


