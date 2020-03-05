package uk.gov.hmcts.probate.service;

import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;

public interface BackOfficeService {

    CaseData sendNotification(ProbateCaseDetails probateCaseDetails);

    ResponseEntity<String> initiateGrantDelayedNotification(String date);
    
}
