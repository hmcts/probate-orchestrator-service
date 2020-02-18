package uk.gov.hmcts.probate.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.CaveatExpiryUpdater;
import uk.gov.hmcts.probate.core.service.ScheduleValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@ConditionalOnProperty("expireCaveatJob")
@Slf4j
public class ExpireCaveatJob implements ExitCodeGenerator  {

    private final CaveatExpiryUpdater caveatExpiryUpdater;

    private final ScheduleValidator scheduleValidator;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Value("${expireCaveatJob}")
    private String expireCaveatJob;

    private int exitCode =1;

    @Autowired
    public ExpireCaveatJob(CaveatExpiryUpdater caveatExpiryUpdater, ScheduleValidator scheduleValidator) {
        this.caveatExpiryUpdater = caveatExpiryUpdater;
        this.scheduleValidator = scheduleValidator;
       expireCaveats();
    }

    public void expireCaveats() {
        try {
            String expireForDate = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
            log.info("Calling perform expire caveats for date {} ...", expireForDate);
            caveatExpiryUpdater.expireCaveats(expireCaveatJob);
            log.info("Perform expire caveats called for date {}", expireCaveatJob);
            this.exitCode = 0;

        } catch (Exception e) {
            log.info("*** Job Failed " + this.getClass().getSimpleName() + "***** ");
           this.exitCode = 1;
        }
    }

    @Override
    public int getExitCode() {
        return this.exitCode;
    }
}
