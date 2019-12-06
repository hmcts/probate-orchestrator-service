package uk.gov.hmcts.probate.client.submit;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class SubmitServiceConfiguration {

    public static final String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    static final String APPLICATION_ID = "applicationId";
    static final String APPLICATION_EMAIL = "applicantEmail";
    static final String USER_ID = "userId";
    static final String INVITATION_ID = "invitationId";
    static final String CASE_ID = "caseId";

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
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
