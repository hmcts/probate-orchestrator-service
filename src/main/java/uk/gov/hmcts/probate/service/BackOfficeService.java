package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.reform.probate.model.cases.CaseData;

public interface BackOfficeService {

    CaseData sendNotification(CaseData caseData);
}
