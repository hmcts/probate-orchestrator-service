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
public class GrantAwaitingDocumentsNotifier {
    private static final Logger log = LoggerFactory.getLogger(GrantAwaitingDocumentsNotifier.class);

    private final BackOfficeService backOfficeService;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public GrantAwaitingDocumentsNotifier(BackOfficeService backOfficeService) {
        this.backOfficeService = backOfficeService;
    }

    public void initiateGrantAwaitingDocumentsNotification() {
        String yesterday = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
        log.info("Grant awaiting documents notification initiated for date: {}", yesterday);

        GrantScheduleResponse response = backOfficeService.initiateGrantAwaitingDocumentsNotification(yesterday);
        String updates = response.scheduleResponseData().stream().collect(Collectors.joining(","));
        log.info("Grant awaiting documents notification completed for date: {} "
            + "for {} cases, details: {}", yesterday, response.scheduleResponseData().size(), updates);
    }
}
