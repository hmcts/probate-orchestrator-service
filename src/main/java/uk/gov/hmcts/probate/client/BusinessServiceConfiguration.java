package uk.gov.hmcts.probate.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.codec.Encoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;


public class BusinessServiceConfiguration {

    static final String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    @Bean
    Encoder feignEncoder(ObjectMapper objectMapper) {
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        return new JacksonEncoder(objectMapper);
    }
}
