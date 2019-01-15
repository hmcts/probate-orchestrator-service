package uk.gov.hmcts.probate.health;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Configuration
public class HealthConfiguration {

    private static final String HEALTH_ENDPOINT = "/health";

    @Value("${service.auth.provider.base.url}")
    private String serviceAuthUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ProbateHealthIndicator serviceAuthHealthIndicator(RestTemplate restTemplate) {
        return new ProbateHealthIndicator(serviceAuthUrl, restTemplate, HEALTH_ENDPOINT);
    }
}