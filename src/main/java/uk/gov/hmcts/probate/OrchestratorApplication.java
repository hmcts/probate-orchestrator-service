package uk.gov.hmcts.probate;

import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import uk.gov.hmcts.reform.authorisation.healthcheck.ServiceAuthHealthIndicator;

@EnableSwagger2
@EnableFeignClients
//@EnableAsync
@SpringBootApplication(exclude = {ServiceAuthHealthIndicator.class})
public class OrchestratorApplication {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    public static void main(String[] args) {
        SpringApplication.run(OrchestratorApplication.class, args);
    }
}
                                                                              