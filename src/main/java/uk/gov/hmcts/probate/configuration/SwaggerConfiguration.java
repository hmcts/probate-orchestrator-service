package uk.gov.hmcts.probate.configuration;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket orchestratorApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("orchestratorApi")
                .globalOperationParameters(Arrays.asList(
                        new ParameterBuilder()
                                .name("Authorization")
                                .description("User authorization header")
                                .required(true)
                                .parameterType("header")
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .name("ServiceAuthorization")
                                .description("Service authorization header")
                                .required(true)
                                .parameterType("header")
                                .modelRef(new ModelRef("string"))
                                .build())
                )
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(packagesLike("uk.gov.hmcts.probate.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private static Predicate<RequestHandler> packagesLike(final String pkg) {
        return input -> input.declaringClass().getPackage().getName().equals(pkg);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring Boot Template API Documentation")
                .description("Spring Boot Template API Documentation")
                .build();
    }
}
