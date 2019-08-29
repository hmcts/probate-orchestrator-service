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

    @Value("${business.service.api.url}")
    private String businessServiceUrl;

    @Value("${submit.service.api.url}")
    private String submitServiceUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ProbateHealthIndicator idamHealthIndicator(RestTemplate restTemplate) {
        return new ProbateHealthIndicator(idamUrl, restTemplate, HEALTH_ENDPOINT);
    }

    @Bean
    public ProbateHealthIndicator businessServiceHealthIndicator(RestTemplate restTemplate) {
        return new ProbateHealthIndicator(businessServiceUrl, restTemplate, HEALTH_ENDPOINT);
    }

    @Bean
    public ProbateHealthIndicator submitServiceHealthIndicator(RestTemplate restTemplate) {
        return new ProbateHealthIndicator(submitServiceUrl, restTemplate, HEALTH_ENDPOINT);
    }
}
