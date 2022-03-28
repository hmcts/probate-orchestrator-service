package uk.gov.hmcts.probate.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI orchestratorApi() {
        return new OpenAPI()
                .components(new Components()
                        .addHeaders("Authorization",
                                new Header()
                                .description("User authorization header")
                                .required(true)
                                .schema(new StringSchema()))
                        .addHeaders("ServiceAuthorization",
                                new Header()
                                .description("Service authorization header")
                                .required(true)
                                .schema(new StringSchema())))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Probate Orchestrator API Documentation")
                .description("Probate Orchestrator API Documentation");
    }
}
