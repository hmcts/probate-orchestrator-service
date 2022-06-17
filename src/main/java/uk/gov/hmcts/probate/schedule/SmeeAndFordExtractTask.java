package uk.gov.hmcts.probate.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.service.DataExtractService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class SmeeAndFordExtractTask implements Runnable {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DataExtractService dataExtractService;

    @Override
    public void run() {
        log.info("Scheduled task started to extract data for Smee and Ford");
        String date = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
        log.info("Calling perform Smee and Ford data extract from date, to date {} {}", date, date);
        dataExtractService.initiateSmeeAndFordExtractDateRange(date, date);
    }

}
