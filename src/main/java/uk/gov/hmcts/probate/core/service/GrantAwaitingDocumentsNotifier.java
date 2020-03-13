package uk.gov.hmcts.probate.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.probate.model.backoffice.GrantDelayedResponse;
import uk.gov.hmcts.probate.service.BackOfficeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GrantAwaitingDocumentsNotifier {

    private final BackOfficeService backOfficeService;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void initiateGrantAwaitingDocumentsNotification() {
        String yesterday = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
        log.info("Grant awaiting documents notification initiated for date: {}", yesterday);

        GrantDelayedResponse response = backOfficeService.initiateGrantAwaitingDocumentsNotification(yesterday);
        String updates = response.getDelayResponseData().stream().collect(Collectors.joining(","));
        log.info("Grant awaiting documents notification completed for date: {} " +
            "for {} cases, details: {}", yesterday, response.getDelayResponseData().size(), updates);
    }
}
