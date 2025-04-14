package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.forms.CaseSummaryHolder;
import uk.gov.hmcts.reform.probate.model.forms.Form;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubmitService {

    CaseSummaryHolder initiateCase(ProbateType probateType);

    Form getCase(String identifier, ProbateType probateType);

    List<ProbateCaseDetails> expireCaveats(String expiryDate);

    CaseSummaryHolder getAllCases();

    Form saveCase(String identifier, LocalDateTime lastModefidDateTime, Form form);

    Form submit(String identifier, Form form);

    Form update(String identifier, ProbateType probateType, PaymentDto paymentDto);

    Form updatePayments(String identifier, Form form);

    Optional<CaseData> sendNotification(ProbateCaseDetails probateCaseDetails);

    ProbateCaseDetails updateByCaseId(String caseId, ProbateCaseDetails probateCaseDetails);

    Form validate(String identifier, ProbateType probateType);

}
