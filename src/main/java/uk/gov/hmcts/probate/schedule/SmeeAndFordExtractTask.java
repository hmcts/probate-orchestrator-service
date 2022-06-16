package uk.gov.hmcts.probate.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.DataExtractDateValidator;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.probate.service.DataExtractService;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class SmeeAndFordExtractTask implements Runnable {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DataExtractDateValidator dataExtractDateValidator;
    private final BackOfficeService backOfficeService;

    @Override
    public void run() {
        log.info("Scheduled task SmeeAndFordExtractTask started to extract data for Smee and Ford");
        final String date = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
        log.info("Calling perform Smee and Ford data extract from date, to date {} {}", date, date);
        try {
            dataExtractDateValidator.validate(date, date);
            log.info("Perform Smee And Ford data extract from date started");
            backOfficeService.initiateSmeeAndFordExtract(date, date);
            log.info("Perform Smee And Ford data extract from date finished");
        } catch (ApiClientException e) {
            log.error(e.getMessage());
        }
    }

}
