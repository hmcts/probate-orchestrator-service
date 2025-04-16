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
public class GrantAwaitingDocsExtractTask implements Runnable {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DataExtractDateValidator dataExtractDateValidator;
    private final BackOfficeService backOfficeService;


    @Value("${adhocSchedulerJobDate}")
    public String adHocJobFromDate;

    public void run() {
        log.info("Scheduled task GrantAwaitingDocumentation started to"
                + " extract data for Grant Awaiting Documentation Job");
        String fromDate = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
        if (StringUtils.isNotEmpty(adHocJobFromDate)) {
            fromDate = adHocJobFromDate;
            log.info("Running GrantAwaitingDocumentationExtractTask with Adhoc dates {}", fromDate);
        }
        log.info("Calling perform grant awaiting documentation data extract from date {}", fromDate);
        try {
            dataExtractDateValidator.validate(fromDate);
            log.info("Perform grant awaiting documentation data extract from date started");
            backOfficeService.initiateGrantAwaitingDocumentsNotification(fromDate);
            log.info("Perform grant awaiting documentation data extract from date finished");
        } catch (ApiClientException e) {
            log.error("ApiClientException from grant awaiting docs", e);
        } catch (FeignException e) {
            log.error("FeignException from grant awaiting docs", e);
        } catch (Exception e) {
            log.error("Exception from grant awaiting docs", e);
        }
    }
}
