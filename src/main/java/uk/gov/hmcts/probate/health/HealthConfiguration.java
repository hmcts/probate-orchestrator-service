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

    @Value("${auth.idam.client.baseUrl}")
    private String idamUrl;

    @Value("${core_case_data.api.url}")
    private String ccdUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ProbateHealthIndicator ccdHealthIndicator(RestTemplate restTemplate) {
        return new ProbateHealthIndicator(ccdUrl, restTemplate, HEALTH_ENDPOINT);
    }

    @Bean
    public ProbateHealthIndicator idamHealthIndicator(RestTemplate restTemplate) {
        return new ProbateHealthIndicator(idamUrl, restTemplate, HEALTH_ENDPOINT);
    }

    @Bean
    public ProbateHealthIndicator serviceAuthHealthIndicator(RestTemplate restTemplate) {
        return new ProbateHealthIndicator(serviceAuthUrl, restTemplate, HEALTH_ENDPOINT);
    }
}
