package uk.gov.hmcts.probate.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaveatExpiryUpdater {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final SecurityUtils securityUtils;
    private final SubmitService submitService;

    public void expireCaveats(String expiryDate) {
        dateValidator(expiryDate);
        log.info("Caveat expire initiated for expiryDate: {}", expiryDate);

        securityUtils.setSecurityContextUserAsScheduler();

        List<ProbateCaseDetails> expired = submitService.expireCaveats(expiryDate);
        log.info("Number of caveats expired for date {}", expired.size());
    }

    private void dateValidator(String date) {
        try {
            LocalDate.parse(date, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            log.error("Error parsing date for caveat expiry, use the format of 'yyyy-MM-dd': " + date);
            throw new IllegalArgumentException(
                "Error parsing date for caveat expiry, use the format of 'yyyy-MM-dd': " + e.getMessage());
        }
    }
}
