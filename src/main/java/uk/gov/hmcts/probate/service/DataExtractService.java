package uk.gov.hmcts.probate.service;

import org.springframework.http.ResponseEntity;

public interface DataExtractService {

    ResponseEntity initiateHmrcExtract(String fromDate, String toDate);

    ResponseEntity initiateIronMountainExtract(String date);

    ResponseEntity initiateExelaExtract(String date);

    ResponseEntity initiateExelaExtractDateRange(String fromDate, String toDate);

    ResponseEntity initiateSmeeAndFordExtractDateRange(String fromDate, String toDate);

    ResponseEntity makeDormant(String date);

    ResponseEntity reactivateDormant(String date);
}
