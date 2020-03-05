package uk.gov.hmcts.probate.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.probate.service.BackOfficeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class GrantDelayedNotifier {

    private final SecurityUtils securityUtils;
    private final BackOfficeService backOfficeService;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void initiateGrantDelayedNotification() {
        String yesterday = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
        log.info("Grant delayed notification initiated for date: {}", yesterday);

        securityUtils.setSecurityContextUserAsCaseworker();

        ResponseEntity<String> response = backOfficeService.initiateGrantDelayedNotification(yesterday);
        log.info("Grant delayed notification completed for date: {} with repsonseCode: {}", yesterday, response.getStatusCode().value());
    }
}
