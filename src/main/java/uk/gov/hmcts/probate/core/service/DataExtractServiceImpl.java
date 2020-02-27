package uk.gov.hmcts.probate.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.probate.service.DataExtractService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.http.HttpStatus.ACCEPTED;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataExtractServiceImpl implements DataExtractService {
    private final DataExtractDateValidator dataExtractDateValidator;
    private final BackOfficeService backOfficeService;

    @Override
    public ResponseEntity<String> initiateHmrcExtract(String fromDate, String toDate) {

        dataExtractDateValidator.validate(fromDate, toDate);
        log.info("Calling perform HMRC data extract...");
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> {
            backOfficeService.initiateHmrcExtract(fromDate, toDate);
        });
        log.info("Perform HMRC data extract called");

        return new ResponseEntity("Perform HMRC data extract finished", ACCEPTED);
    }

    @Override
    public ResponseEntity<String> initiateIronMountainExtract(String date) {
        
        dataExtractDateValidator.validate(date);

        log.info("Calling perform Iron Mountain data extract from date...");
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> {
            backOfficeService.initiateIronMountainExtract(date);
        });
        log.info("Perform Iron Mountain data extract from date finished");

        return new ResponseEntity("Perform Iron Mountain data extract finished", ACCEPTED);
    }

    @Override
    public ResponseEntity<String> initiateExelaExtract(String date) {
        
        dataExtractDateValidator.validate(date);
        log.info("Calling perform Exela data extract from date...");
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> {
            backOfficeService.initiateExelaExtract(date);
        });
        log.info("Perform Exela data extract from date finished");

        return new ResponseEntity("Perform Exela data extract finished", ACCEPTED);
    }
}
