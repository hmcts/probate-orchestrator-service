package uk.gov.hmcts.probate.service;

import org.springframework.http.ResponseEntity;

public interface DataExtractService {

    ResponseEntity<String> initiateHmrcExtract(String fromDate, String toDate);

    ResponseEntity<String> initiateIronMountainExtract(String date);

    ResponseEntity<String> initiateExelaExtract(String date);
}
