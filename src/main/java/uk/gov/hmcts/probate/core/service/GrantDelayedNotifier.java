package uk.gov.hmcts.probate.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.probate.model.backoffice.GrantScheduleResponse;
import uk.gov.hmcts.probate.service.BackOfficeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
public class GrantDelayedNotifier {
    private static final Logger log = LoggerFactory.getLogger(GrantDelayedNotifier.class);

    private final BackOfficeService backOfficeService;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public GrantDelayedNotifier(BackOfficeService backOfficeService) {
        this.backOfficeService = backOfficeService;
    }

    public void initiateGrantDelayedNotification() {
        String yesterday = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
        log.info("Grant delayed notification initiated for date: {}", yesterday);

        GrantScheduleResponse response = backOfficeService.initiateGrantDelayedNotification(yesterday);
        String updates = response.scheduleResponseData().stream().collect(Collectors.joining(","));
        log.info("Grant delayed notification completed for date: {} "
            + "for {} cases, details: {}", yesterday, response.scheduleResponseData().size(), updates);
    }
}
