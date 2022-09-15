package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import io.jsonwebtoken.SigningKeyResolverAdapter
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import springfox.documentation.oas.annotations.EnableOpenApi
import java.security.Security
import nl.rijksoverheid.minienw.travelvalidation.validationservice.services.IAirlineSigningKeyProvider


@SpringBootApplication
@ConfigurationPropertiesScan
@EnableWebMvc //For swagger
@EnableOpenApi
@EnableConfigurationProperties(ApplicationPropertiesFile::class)
class AirlineStubApplication

	fun main(args: Array<String>) {
		Security.addProvider(BouncyCastleProvider())
		//CorsConfiguration().applyPermitDefaultValues()
		runApplication<AirlineStubApplication>(*args)
	}


	@Bean
	fun signingKeyResolverAdapter(iAirlineSigningKeyProvider: IAirlineSigningKeyProvider): SigningKeyResolverAdapter {
		return AirlineSigningKeyResolverAdapter(iAirlineSigningKeyProvider)
	}


