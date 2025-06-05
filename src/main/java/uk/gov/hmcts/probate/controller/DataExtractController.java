package uk.gov.hmcts.probate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.probate.service.DataExtractService;
import uk.gov.hmcts.probate.service.FeatureToggleService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequestMapping("/data-extract")
@RestController
@Tag(name = "Initiate data extract for HMRC, IronMountain and Excela")
public class DataExtractController {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DataExtractService dataExtractService;
    private final FeatureToggleService featureToggleService;

    public DataExtractController(
            final DataExtractService dataExtractService,
            final FeatureToggleService featureToggleService) {
        this.dataExtractService = dataExtractService;
        this.featureToggleService = featureToggleService;
    }

    @Operation(summary = "Initiate HMRC data extract", description = "Will find cases for yesterdays date")
    @PostMapping(path = "/hmrc")
    public ResponseEntity initiateHmrcExtract() {

        return initiateHmrcExtractForDate(DATE_FORMAT.format(LocalDate.now().minusDays(1L)));
    }

    @Operation(summary = "Initiate HMRC data extract for date", description = "Date MUST be in format 'yyyy-MM-dd'")
    @PostMapping(path = "/hmrc/{date}")
    public ResponseEntity initiateHmrcExtractForDate(@PathVariable("date") String date) {

        return initiateHmrcExtractFromToDate(date, date);
    }

    @Operation(summary = "Initiate HMRC data extract within 2 dates",
            description = "Dates MUST be in format 'yyyy-MM-dd'")
    @PostMapping(path = "/hmrc/{fromDate}/{toDate}")
    public ResponseEntity initiateHmrcExtractFromToDate(@PathVariable("fromDate") String fromDate,
                                                        @PathVariable("toDate") String toDate) {

        log.info("Calling perform HMRC data extract from/to date...");
        return dataExtractService.initiateHmrcExtract(fromDate, toDate);
    }

    @Scheduled(cron = "${cron.ironMountain.schedule}")
    @Operation(summary = "Initiate IronMountain data extract", description = "Will find cases for yesterdays date")
    @PostMapping(path = "/iron-mountain")
    public ResponseEntity initiateIronMountainExtract() {
        if (featureToggleService.isIronMountainInBackOffice()) {
            log.info("IronMountain extract task is feature toggled to run through back office");
            return ResponseEntity.internalServerError().body("Iron Mountain extract task is run through back office");
        }

        return initiateIronMountainExtract(DATE_FORMAT.format(LocalDate.now().minusDays(1L)));
    }

    @Operation(summary = "Initiate IronMountain data extract with date",
            description = "Date MUST be in format 'yyyy-MM-dd'")
    @PostMapping(path = "/iron-mountain/{date}")
    public ResponseEntity initiateIronMountainExtract(@Parameter(name = "Date to find cases against", required = true)
                                                      @PathVariable("date") String date) {
        log.info("Calling perform Iron Mountain data extract from date...");
        return dataExtractService.initiateIronMountainExtract(date);
    }

    @Scheduled(cron = "${cron.exela.schedule}")
    @Operation(summary = "Initiate Excela data extract", description = "Will find cases for yesterdays date")
    @PostMapping(path = "/exela")
    public ResponseEntity initiateExelaExtract() {
        if (featureToggleService.isExelaInBackOffice()) {
            log.info("Exela extract task is feature toggled to run through back office");
            return ResponseEntity.internalServerError().body("Exela extract task is run through back office");
        }
        log.info("Extract initiated for Excela");
        return initiateExelaExtract(DATE_FORMAT.format(LocalDate.now().minusDays(1L)));
    }

    @Operation(summary = "Initiate Excela data extract", description = " Date MUST be in format 'yyyy-MM-dd'")
    @PostMapping(path = "/exela/{date}")
    public ResponseEntity initiateExelaExtract(@Parameter(name = "Date to find cases against", required = true)
                                               @PathVariable("date") String date) {

        log.info("Calling perform Excela data extract from date...");
        return initiateExelaExtractDateRange(date, date);
    }

    @Operation(summary = "Initiate Excela data extract", description = " Date MUST be in format 'yyyy-MM-dd'")
    @PostMapping(path = "/exela/{fromDate}/{toDate}")
    public ResponseEntity initiateExelaExtractDateRange(
            @Parameter(name = "Date range to find cases against", required = true)
            @PathVariable("fromDate") String fromDate,
            @PathVariable("toDate") String toDate) {

        log.info("Calling perform Excela data extract from date...");
        return dataExtractService.initiateExelaExtractDateRange(fromDate, toDate);
    }

    @Operation(summary = "Initiate Smee and Ford data extract", description = " Date MUST be in format 'yyyy-MM-dd'")
    @PostMapping(path = "/smee-and-ford/{fromDate}/{toDate}")
    public ResponseEntity initiateSmeeAndFordExtractDateRange(
            @Parameter(name = "Date range to find cases against", required = true)
            @PathVariable("fromDate") String fromDate,
            @PathVariable("toDate") String toDate) {

        log.info("Calling perform Smee And Ford data extract from date, to date {} {}", fromDate, toDate);
        return dataExtractService.initiateSmeeAndFordExtractDateRange(fromDate, toDate);
    }

}
