package uk.gov.hmcts.probate.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.probate.service.DataExtractService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
@RequiredArgsConstructor
@Component
public class DataExtractServiceImpl implements DataExtractService {

    private final DataExtractDateValidator dataExtractDateValidator;
    private final BackOfficeService backOfficeService;

    @Override
    public ResponseEntity initiateHmrcExtract(String fromDate, String toDate) {

        dataExtractDateValidator.validate(fromDate, toDate);
        log.info("Calling perform HMRC data extract...");
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> {
            log.info("Perform HMRC data extract started");
            backOfficeService.initiateHmrcExtract(fromDate, toDate);
        });
        log.info("Perform HMRC data extract finished");

        return ResponseEntity.accepted().body("Perform HMRC data extract finished");
    }

    @Override
    public ResponseEntity initiateIronMountainExtract(String date) {

        dataExtractDateValidator.validate(date);

        log.info("Calling perform Iron Mountain data extract from date...");
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> {
            log.info("Perform Iron Mountain data extract from date started");
            backOfficeService.initiateIronMountainExtract(date);
        });
        log.info("Perform Iron Mountain data extract from date finished");

        return ResponseEntity.accepted().body("Perform Iron Mountain data extract finished");
    }

    @Override
    public ResponseEntity initiateExelaExtract(String date) {

        dataExtractDateValidator.validate(date);
        log.info("Calling perform Exela data extract from date...");
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> {
            log.info("Perform Exela data extract from date started");
            backOfficeService.initiateExelaExtract(date);
        });
        log.info("Perform Exela data extract from date finished");

        return ResponseEntity.accepted().body("Perform Exela data extract finished");
    }

    @Override
    public ResponseEntity initiateExelaExtractDateRange(String fromDate, String toDate) {

        dataExtractDateValidator.validate(fromDate, toDate);
        log.info("Calling perform Exela data extract from date...");
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> {
            log.info("Perform Exela data extract from date started");
            backOfficeService.initiateExelaExtractDateRange(fromDate, toDate);
        });
        log.info("Perform Exela data extract from date finished");

        return ResponseEntity.accepted().body("Perform Exela data extract finished");
    }

    @Override
    public ResponseEntity initiateSmeeAndFordExtractDateRange(String fromDate, String toDate) {

        dataExtractDateValidator.validate(fromDate, toDate);
        log.info("Calling perform Smee And Ford data extract from date...");
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> {
            log.info("Perform Smee And Ford data extract from date started");
            backOfficeService.initiateSmeeAndFordExtract(fromDate, toDate);
        });
        log.info("Perform Smee And Ford data extract from date finished");

        return ResponseEntity.accepted().body("Perform Smee And Ford data extract finished");
    }

    @Override
    public ResponseEntity makeDormant(String fromDate, String toDate) {

        dataExtractDateValidator.validate(fromDate, toDate);
        log.info("Calling perform Make Dormant from date...");
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> {
            log.info("Perform Make Dormant from date started");
            backOfficeService.makeDormant(fromDate, toDate);
        });
        log.info("Perform Make Dormant from date finished");

        return ResponseEntity.accepted().body("Perform Make Dormant finished");
    }
}
