package uk.gov.hmcts.probate.schedule;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.DataExtractDateValidator;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExelaExtractTask implements Runnable {

    private final DataExtractDateValidator dataExtractJobDateValidator;
    private final BackOfficeService backOfficeService;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Value("${adhocSchedulerJobDate}")
    public String adHocJobStartDate;
    @Value("${adhocSchedulerJobToDate}")
    public String adHocJobEndDate;

    @Override
    public void run() {
        log.info("Scheduled task ExelaExtractTask started to extract data for Exela");
        String fromDate = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
        String toDate = fromDate;
        if (StringUtils.isNotEmpty(adHocJobStartDate)) {
            fromDate = adHocJobStartDate;
            toDate = StringUtils.isNotEmpty(adHocJobEndDate) ? adHocJobEndDate : adHocJobStartDate;
            log.info("Running ExelaDataExtractTask with Adhoc dates {} {}", fromDate, toDate);
        }
        log.info("Calling perform Exela data extract from date, to date {} {}", fromDate, toDate);
        try {
            dataExtractJobDateValidator.validate(fromDate, toDate);
            log.info("Perform Exela data extract from date started");
            backOfficeService.initiateExelaExtractDateRange(fromDate, toDate);
            log.info("Perform Exela data extract from date finished");
        } catch (ApiClientException e) {
            log.error(e.getMessage());
        } catch (FeignException e) {
            log.error("Error on calling BackOfficeAPI {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error on ExelaExtractTask Scheduler {}", e.getMessage());
        }
    }

}
