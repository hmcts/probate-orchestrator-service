package uk.gov.hmcts.probate.client;

import feign.Logger;
import org.springframework.context.annotation.Bean;


public class BusinessServiceConfiguration {

    static final String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}
