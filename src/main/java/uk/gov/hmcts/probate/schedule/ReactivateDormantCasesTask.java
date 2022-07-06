package uk.gov.hmcts.probate.schedule;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.DataExtractDateValidator;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReactivateDormantCasesTask implements Runnable {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DataExtractDateValidator dataExtractDateValidator;
    private final BackOfficeService backOfficeService;

    @Override
    public void run() {
        log.info("Scheduled task ReactivateDormantCasesTask started to reactivate dormant cases");
        final String date = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
        log.info("Calling perform reactivate dormant from date, to date {} {}", date, date);
        try {
            dataExtractDateValidator.validate(date, date);
            log.info("Perform reactivate dormant from date started");
            backOfficeService.reactivateDormant(date, date);
            log.info("Perform reactivate dormant from date finished");
        } catch (ApiClientException e) {
            log.error(e.getMessage());
        } catch (FeignException e) {
            log.error("Error on calling BackOfficeAPI {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error on ReactivateDormantCasesTask Scheduler {}", e.getMessage());
        }
    }

}
