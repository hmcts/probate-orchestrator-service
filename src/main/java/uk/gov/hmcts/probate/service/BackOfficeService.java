package uk.gov.hmcts.probate.service;

import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.probate.model.backoffice.GrantScheduleResponse;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;

public interface BackOfficeService {

    CaseData sendNotification(ProbateCaseDetails probateCaseDetails);

    ResponseEntity<String> initiateHmrcExtract(String fromDate, String toDate);

    ResponseEntity<String> initiateIronMountainExtract(String date);

    ResponseEntity<String>  initiateExelaExtract(String date);

    GrantScheduleResponse initiateGrantDelayedNotification(String date);

    GrantScheduleResponse initiateGrantAwaitingDocumentsNotification(String date);

    ResponseEntity<String> initiateExelaExtractDateRange(String fromDate, String toDate);

    ResponseEntity<String> initiateSmeeAndFordExtract(String fromDate, String toDate);
}
