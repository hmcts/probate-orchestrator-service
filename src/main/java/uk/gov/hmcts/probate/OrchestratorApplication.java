package uk.gov.hmcts.probate;

import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import uk.gov.hmcts.probate.client.FeignErrorDecoder;

@EnableFeignClients(basePackages = {"uk.gov.hmcts.reform.idam", "uk.gov.hmcts.probate.client"})
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class OrchestratorApplication {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    public static void main(String[] args) {
        SpringApplication.run(OrchestratorApplication.class, args);
    }


    @Bean
    public FeignErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }
}
