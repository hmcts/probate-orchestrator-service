package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;

public interface BackOfficeService {

    void sendNotification(ProbateCaseDetails probateCaseDetails);
}
