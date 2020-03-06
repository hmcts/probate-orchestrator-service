package uk.gov.hmcts.probate.service;

import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.probate.model.backoffice.GrantDelayedResponse;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;

public interface BackOfficeService {

    CaseData sendNotification(ProbateCaseDetails probateCaseDetails);

    ResponseEntity<GrantDelayedResponse> initiateGrantDelayedNotification(String date);
    
}
