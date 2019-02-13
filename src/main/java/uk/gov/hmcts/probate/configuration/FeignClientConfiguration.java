package uk.gov.hmcts.probate.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class FeignClientConfiguration {
    @Bean
    @Primary
    Decoder feignDecoder(ObjectMapper objectMapper) {
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        return new JacksonDecoder(objectMapper);
    }
}
