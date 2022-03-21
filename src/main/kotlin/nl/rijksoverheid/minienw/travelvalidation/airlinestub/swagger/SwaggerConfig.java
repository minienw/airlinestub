package nl.rijksoverheid.minienw.travelvalidation.airlinestub.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket scrumAllyApi() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .paths(regex("/api.*"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("NL DCC Validation for Airline Checkin")
                .description("See title.")
                //TODO .license("EU...")
                //TODO .licenseUrl("https://opensource.org/licenses/EU...")
                .build();
    }
}
