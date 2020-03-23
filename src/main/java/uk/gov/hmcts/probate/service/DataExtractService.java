package uk.gov.hmcts.probate.service;

import org.springframework.http.ResponseEntity;

public interface DataExtractService {

    ResponseEntity initiateHmrcExtract(String fromDate, String toDate);

    ResponseEntity initiateIronMountainExtract(String date);

    ResponseEntity initiateExelaExtract(String date);
}
