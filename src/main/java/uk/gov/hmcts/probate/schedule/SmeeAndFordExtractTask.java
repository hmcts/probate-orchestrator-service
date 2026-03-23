package uk.gov.hmcts.probate.schedule;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.DataExtractDateValidator;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class SmeeAndFordExtractTask implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SmeeAndFordExtractTask.class);

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DataExtractDateValidator dataExtractDateValidator;
    private final BackOfficeService backOfficeService;

    public SmeeAndFordExtractTask(DataExtractDateValidator dataExtractDateValidator, BackOfficeService backOfficeService) {
        this.dataExtractDateValidator = dataExtractDateValidator;
        this.backOfficeService = backOfficeService;
    }

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
        } catch (FeignException e) {
            log.error("Error on calling BackOfficeAPI {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error on SmeeAndFordExtractTask Scheduler {}", e.getMessage());
        }
    }

}
