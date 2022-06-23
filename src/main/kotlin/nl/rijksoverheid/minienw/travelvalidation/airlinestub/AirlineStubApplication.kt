package nl.rijksoverheid.minienw.travelvalidation.airlinestub

import com.fasterxml.classmate.TypeResolver
import io.jsonwebtoken.SigningKeyResolverAdapter
import nl.rijksoverheid.minienw.travelvalidation.airlinestub.api.IdentityController
import nl.rijksoverheid.minienw.travelvalidation.api.data.callback.ConfirmationTokenPayload
import nl.rijksoverheid.minienw.travelvalidation.api.data.token.InitiatingQrPayload
import nl.rijksoverheid.minienw.travelvalidation.validationservice.services.IAirlineSigningKeyProvider
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import springfox.documentation.builders.*
import springfox.documentation.builders.PathSelectors.regex
import springfox.documentation.oas.annotations.EnableOpenApi
import springfox.documentation.service.Tag
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.security.Security



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


