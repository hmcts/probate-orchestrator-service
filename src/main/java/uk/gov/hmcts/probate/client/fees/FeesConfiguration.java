package uk.gov.hmcts.probate.client.fees;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

class FeesConfiguration {

    static final String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    static final String APPLICATION_ID = "applicationId";

    @Bean
    @Primary
    Decoder feignDecoder(ObjectMapper objectMapper) {
        return new JacksonDecoder(objectMapper);
    }
}
