package uk.gov.hmcts.probate;

import com.microsoft.applicationinsights.attach.ApplicationInsights;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import uk.gov.hmcts.probate.client.FeignErrorDecoder;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@EnableFeignClients
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class OrchestratorApplication {

    public static void main(final String[] args) {
        ApplicationInsights.attach();
        final var application = new SpringApplication(OrchestratorApplication.class);
        final var instance = application.run(args);

        if (System.getenv("TASK_NAME") != null) {
            instance.close();
        }
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public FeignErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }
}
