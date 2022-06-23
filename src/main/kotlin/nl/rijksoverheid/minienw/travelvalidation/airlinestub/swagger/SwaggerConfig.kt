package nl.rijksoverheid.minienw.travelvalidation.airlinestub.swagger

import com.fasterxml.classmate.TypeResolver
import io.swagger.annotations.Api
import nl.rijksoverheid.minienw.travelvalidation.api.data.callback.ConfirmationTokenPayload
import nl.rijksoverheid.minienw.travelvalidation.api.data.identity.IdentityResponse
import nl.rijksoverheid.minienw.travelvalidation.api.data.token.InitiatingQrPayload
import nl.rijksoverheid.minienw.travelvalidation.api.data.validate.DccExtract
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import springfox.documentation.builders.HttpAuthenticationBuilder
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.oas.annotations.EnableOpenApi
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import java.util.*

@Configuration
@EnableOpenApi
class SwaggerConfig {
    @Bean
    fun docket(): Docket {
        return Docket(DocumentationType.OAS_30)
        .select()
		.apis(RequestHandlerSelectors.withClassAnnotation(Api::class.java))
    	.build()
//      .pathMapping("/")
//		.directModelSubstitute(LocalDate::class.java, String::class.java)
//		.genericModelSubstitutes(ResponseEntity::class.java)
//		.alternateTypeRules(
//			newRule(
//				typeResolver.resolve(
//					DeferredResult::class.java,
//					typeResolver.resolve(ResponseEntity::class.java, WildcardType::class.java)
//				),
//				typeResolver.resolve(WildcardType::class.java)
//			)
//		)
      .useDefaultResponseMessages(false)
//		.globalResponses(HttpMethod.GET,
//			singletonList(ResponseBuilder()
//				.code("500")
//				.description("500 message")
//				.representation(MediaType.TEXT_XML)
//				.apply { r ->
//					r.model { m ->
//						m.referenceModel { ref ->
//							ref.key { k ->
//								k.qualifiedModelName { q ->
//									q.namespace("some:namespace")
//										.name("ERROR")
//								}
//							}
//						}
//					}
//				}
//				.build()))
		.securitySchemes(listOf(securitySchemeJwt())) //Uncomment this line and go boom.
//		.securityContexts(listOf(securityContext()))
//		.enableUrlTemplating(true)
//		.globalRequestParameters(
//			singletonList(
//				RequestParameterBuilder()
//					.name("someGlobalParameter")
//					.description("Description of someGlobalParameter")
//					.`in`(ParameterType.QUERY)
//					.required(true)
//					.query(Consumer { q: SimpleParameterSpecificationBuilder ->
//						q.model { m: ModelSpecificationBuilder ->
//							m.scalarModel(
//								ScalarType.STRING
//							)
//						}
//					})
//					.build()
//			)
//		)
//            .tags(Tag("Airline Stub", "Stuff..."))
            .additionalModels(
                typeResolver!!.resolve(InitiatingQrPayload::class.java),
                typeResolver!!.resolve(ConfirmationTokenPayload::class.java),
                typeResolver!!.resolve(DccExtract::class.java),
                        typeResolver!!.resolve(IdentityResponse::class.java)
            )
            .apiInfo(apiInfo())
    }

    private fun securitySchemeJwt() : SecurityScheme {
        val argle = HttpAuthenticationBuilder()
            .name("InitiatingTokenJwt")
            .scheme("bearer")
            .bearerFormat("JWT")
            .description("Value is signed JWT of InitiatingQrToken")
            .build()

        return argle
    }

    private fun apiInfo(): ApiInfo? {
        return ApiInfo(
            "Online Verification for Travellers - Airline Stub",
            "Demonstrator of the airline component of Online Verification for Travellers at https://github.com/minienw/onlineverification_overview.",
            "0.0.1-SNAPSHOT",
            "-",
            Contact("Steve Kellaway", "https://www.linkedin.com/in/steve-kellaway-019ba7142/", "steve.kellaway@mefitihe.com"),
            "EUPL-1.2", "https://spdx.org/licenses/EUPL-1.2.html", Collections.emptyList()
        )
    }

    @Autowired
    private var typeResolver: TypeResolver? = null

//    @Bean
//    fun corsFilter(): CorsFilter? {
//        val source = UrlBasedCorsConfigurationSource()
//
//        // Allow anyone and anything access. Probably ok for Swagger spec
//        val config = CorsConfiguration()
//        config.allowCredentials = true
//        config.addAllowedOrigin("*")
//        config.addAllowedHeader("*")
//        config.addAllowedMethod("*")
//        source.registerCorsConfiguration("/v3/api-docs", config)
//        return CorsFilter(source)
//    }
}

