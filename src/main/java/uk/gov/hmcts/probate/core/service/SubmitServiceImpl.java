package uk.gov.hmcts.probate.core.service;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.probate.client.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.mapper.FormMapper;
import uk.gov.hmcts.probate.core.service.mapper.PaymentMapper;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.ProbatePaymentDetails;
import uk.gov.hmcts.reform.probate.model.forms.CcdCase;
import uk.gov.hmcts.reform.probate.model.forms.Form;
import uk.gov.hmcts.reform.probate.model.forms.Payment;

import java.util.Map;

@Component
@Slf4j
public class SubmitServiceImpl implements SubmitService {

    private final Map<ProbateType, FormMapper> mappers;

    private final SubmitServiceApi submitServiceApi;

    private final SecurityUtils securityUtils;

    @Autowired
    public SubmitServiceImpl(Map<ProbateType, FormMapper> mappers, SubmitServiceApi submitServiceApi,
                             SecurityUtils securityUtils) {
        this.mappers = mappers;
        this.submitServiceApi = submitServiceApi;
        this.securityUtils = securityUtils;
    }

    @Override
    public Form getCase(String applicantEmail, ProbateType probateType) {
        FormMapper formMapper = mappers.get(probateType);
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        ProbateCaseDetails probateCaseDetails = submitServiceApi.getCase(authorisation,
            serviceAuthorisation, applicantEmail, probateType.getCaseType().getName());
        return formMapper.fromCaseData(probateCaseDetails.getCaseData());
    }

    @Override
    public Form saveDraft(String applicantEmail, Form form) {
        assertEmail(applicantEmail, form);
        FormMapper formMapper = mappers.get(form.getType());
        ProbateCaseDetails probateCaseDetails = submitServiceApi.saveDraft(
            securityUtils.getAuthorisation(),
            securityUtils.getServiceAuthorisation(),
            applicantEmail,
            ProbateCaseDetails.builder().caseData(mapToCase(form, formMapper)).build()
        );
        return mapFromCase(formMapper, probateCaseDetails);
    }

    @Override
    public Form submit(String applicantEmail, Form form) {
        assertEmail(applicantEmail, form);
        FormMapper formMapper = mappers.get(form.getType());
        ProbateCaseDetails probateCaseDetails = submitServiceApi.submit(
            securityUtils.getAuthorisation(),
            securityUtils.getServiceAuthorisation(),
            applicantEmail,
            ProbateCaseDetails.builder().caseData(mapToCase(form, formMapper)).build()
        );
        return mapFromCase(formMapper, probateCaseDetails);
    }

    private void assertEmail(String applicantEmail, Form form) {
        Assert.isTrue(form.getApplicant() != null && form.getApplicant().getEmail() != null
                && form.getApplicant().getEmail().equals(applicantEmail),
            "Path variable email address must match applicant email address in form");
    }

    private void updateCcdCase(ProbateCaseDetails probateCaseDetails, Form formResponse) {
        formResponse.setCcdCase(CcdCase.builder()
            .id(Long.valueOf(probateCaseDetails.getCaseInfo().getCaseId()))
            .state(probateCaseDetails.getCaseInfo().getState())
            .build());
    }

    @Override
    public Form updatePayments(String applicantEmail, Form form) {
        Assert.isTrue(!CollectionUtils.isEmpty(form.getPayments()),
            "Cannot update case with no payments, there needs to be at least one payment");
        FormMapper formMapper = mappers.get(form.getType());
        CaseData caseData = formMapper.toCaseData(form);
        CasePayment casePayment = caseData.getPayments().get(0).getValue();
        ProbateCaseDetails probateCaseDetails = submitServiceApi.updatePayments(
            securityUtils.getAuthorisation(),
            securityUtils.getServiceAuthorisation(),
            applicantEmail,
            ProbatePaymentDetails.builder().caseType(form.getType().getCaseType())
                .payment(casePayment)
                .build());
        return mapFromCase(formMapper, probateCaseDetails);
    }

    private CaseData mapToCase(Form form, FormMapper formMapper) {
        return formMapper.toCaseData(form);
    }

    private Form mapFromCase(FormMapper formMapper, ProbateCaseDetails probateCaseDetails) {
        Form formResponse = formMapper.fromCaseData(probateCaseDetails.getCaseData());
        updateCcdCase(probateCaseDetails, formResponse);
        return formResponse;
    }
}
