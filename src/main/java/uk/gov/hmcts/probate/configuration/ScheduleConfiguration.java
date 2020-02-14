package uk.gov.hmcts.probate.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("cron.key")
public class ScheduleConfiguration {

    private String caveatExpiry;
}
