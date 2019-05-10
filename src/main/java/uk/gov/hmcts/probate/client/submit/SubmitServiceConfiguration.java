package uk.gov.hmcts.probate.client.submit;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

class SubmitServiceConfiguration {

    static final String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    static final String APPLICATION_ID = "applicationId";

    @Bean
    @Primary
    public Decoder feignDecoder(ObjectMapper objectMapper) {
        return new JacksonDecoder(objectMapper);
    }

    @Bean
    public SubmitServiceApiErrorDecoder submitServiceApiErrorDecoder() {
        return new SubmitServiceApiErrorDecoder();
    }

    @Bean
    public Logger.Level submitServiceApiLoggerLevel() {
        return Logger.Level.FULL;
    }
}
