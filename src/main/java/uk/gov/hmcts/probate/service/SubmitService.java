package uk.gov.hmcts.probate.service;

import java.util.Optional;

import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.forms.Form;

public interface SubmitService {

    Form getCase(String identifier, ProbateType probateType);

    Form saveDraft(String identifier, Form form);

    Form submit(String identifier, Form form);

    Form update(String identifier, Form form);

    Form updatePayments(String identifier, Form form);

    Optional<CaseData> sendNotification(ProbateCaseDetails probateCaseDetails);

    ProbateCaseDetails updateByCaseId(String caseId, ProbateCaseDetails probateCaseDetails);

}
