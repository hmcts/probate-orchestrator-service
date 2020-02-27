package uk.gov.hmcts.probate.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.probate.service.DataExtractService;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataExtractServiceImpl implements DataExtractService {
    private final DataExtractDateValidator dataExtractDateValidator;
    private final BackOfficeService backOfficeService;

    @Override
    public ResponseEntity<String> initiateHmrcExtract(String fromDate, String toDate) {

        dataExtractDateValidator.dateValidator(fromDate, toDate);
        return backOfficeService.initiateHmrcExtract(fromDate, toDate);
    }

    @Override
    public ResponseEntity<String> initiateIronMountainExtract(String date) {
        
        dataExtractDateValidator.dateValidator(date);
        return backOfficeService.initiateIronMountainExtract(date);
    }

    @Override
    public ResponseEntity<String> initiateExelaExtract(String date) {
        
        dataExtractDateValidator.dateValidator(date);
        return backOfficeService.initiateExelaExtract(date);
    }
}
