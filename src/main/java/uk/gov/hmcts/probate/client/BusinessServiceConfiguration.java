package uk.gov.hmcts.probate.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class BusinessServiceConfiguration {

    static final String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    @Bean
    @Primary
    Decoder feignDecoder(ObjectMapper objectMapper) {
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        return new JacksonDecoder(objectMapper);
    }
}
