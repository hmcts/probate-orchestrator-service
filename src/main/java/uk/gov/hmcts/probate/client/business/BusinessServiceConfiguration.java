package uk.gov.hmcts.probate.client.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.codec.Encoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class BusinessServiceConfiguration {

    static final String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    private final ObjectMapper objectMapper;

    public BusinessServiceConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    @Primary
    Encoder feignEncoder() {
        return new JacksonEncoder(objectMapper);
    }

    @Bean
    public Logger.Level businessServiceApiLoggerLevel() {
        return Logger.Level.FULL;
    }

}
