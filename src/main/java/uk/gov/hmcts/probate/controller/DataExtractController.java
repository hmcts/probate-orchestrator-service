package uk.gov.hmcts.probate.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.probate.service.DataExtractService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/data-extract")
@RestController
@Api(tags = "Initiate data extract for HMRC, IronMountain and Excela")
public class DataExtractController {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DataExtractService dataExtractService;

    @Scheduled(cron = "${cron.hmrc.schedule}")
    @ApiOperation(value = "Initiate HMRC data extract", notes = "Will find cases for yesterdays date")
    @PostMapping(path = "/hmrc")
    public ResponseEntity initiateHmrcExtract() {

        return initiateHmrcExtractForDate(DATE_FORMAT.format(LocalDate.now().minusDays(1L)));
    }

    @ApiOperation(value = "Initiate HMRC data extract for date", notes = "Date MUST be in format 'yyyy-MM-dd'")
    @PostMapping(path = "/hmrc/{date}")
    public ResponseEntity initiateHmrcExtractForDate(@PathVariable("date") String date) {

        return initiateHmrcExtractFromToDate(date, date);
    }

    @ApiOperation(value = "Initiate HMRC data extract within 2 dates", notes = "Dates MUST be in format 'yyyy-MM-dd'")
    @PostMapping(path = "/hmrc/{fromDate}/{toDate}")
    public ResponseEntity initiateHmrcExtractFromToDate(@PathVariable("fromDate") String fromDate,
                                                        @PathVariable("toDate") String toDate) {

        log.info("Calling perform HMRC data extract from/to date...");
        return dataExtractService.initiateHmrcExtract(fromDate, toDate);
    }

    @Scheduled(cron = "${cron.ironMountain.schedule}")
    @ApiOperation(value = "Initiate IronMountain data extract", notes = "Will find cases for yesterdays date")
    @PostMapping(path = "/iron-mountain")
    public ResponseEntity initiateIronMountainExtract() {

        return initiateIronMountainExtract(DATE_FORMAT.format(LocalDate.now().minusDays(1L)));
    }

    @ApiOperation(value = "Initiate IronMountain data extract with date", notes = "Date MUST be in format 'yyyy-MM-dd'")
    @PostMapping(path = "/iron-mountain/{date}")
    public ResponseEntity initiateIronMountainExtract(@ApiParam(value = "Date to find cases against", required = true)
                                                      @PathVariable("date") String date) {
        log.info("Calling perform Iron Mountain data extract from date...");
        return dataExtractService.initiateIronMountainExtract(date);
    }

    @Scheduled(cron = "${cron.exela.schedule}")
    @ApiOperation(value = "Initiate Excela data extract", notes = "Will find cases for yesterdays date")
    @PostMapping(path = "/exela")
    public ResponseEntity initiateExelaExtract() {
        log.info("Extract initiated for Excela");
        return initiateExelaExtract(DATE_FORMAT.format(LocalDate.now().minusDays(1L)));
    }

    @ApiOperation(value = "Initiate Excela data extract", notes = " Date MUST be in format 'yyyy-MM-dd'")
    @PostMapping(path = "/exela/{date}")
    public ResponseEntity initiateExelaExtract(@ApiParam(value = "Date to find cases against", required = true)
                                                @PathVariable("date") String date) {

        log.info("Calling perform Excela data extract from date...");
        return initiateExelaExtractDateRange(date, date);
    }

    @ApiOperation(value = "Initiate Excela data extract", notes = " Date MUST be in format 'yyyy-MM-dd'")
    @PostMapping(path = "/exela/{fromDate}/{toDate}")
    public ResponseEntity initiateExelaExtractDateRange(@ApiParam(value = "Date range to find cases against", required = true)
                                                   @PathVariable("fromDate") String fromDate,
                                                   @PathVariable("toDate") String toDate) {

        log.info("Calling perform Excela data extract from date...");
        return dataExtractService.initiateExelaExtractDateRange(fromDate, toDate);
    }

}
