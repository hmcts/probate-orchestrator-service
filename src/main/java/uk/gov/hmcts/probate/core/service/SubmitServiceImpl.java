package uk.gov.hmcts.probate.core.service;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.client.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.mapper.FormMapper;
import uk.gov.hmcts.probate.core.service.mapper.PaymentMapper;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.forms.Form;

import java.util.Map;

@Component
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
        String serviceAuthorisation = securityUtils.generateServiceToken();
        String authorisation = securityUtils.getUserToken();
        ProbateCaseDetails probateCaseDetails
            = submitServiceApi.getCase(authorisation, serviceAuthorisation, applicantEmail,
            probateType.getCaseType().getName());
        return formMapper.fromCaseData(probateCaseDetails.getCaseData());
    }

    @Override
    public Form saveDraft(String applicantEmail, Form form) {
        FormMapper formMapper = mappers.get(form.getType());
        String serviceAuthorisation = securityUtils.generateServiceToken();
        String authorisation = securityUtils.getUserToken();
        CaseData caseData = formMapper.toCaseData(form);
        ProbateCaseDetails probateCaseDetails = submitServiceApi.saveDraft(authorisation, serviceAuthorisation,
            applicantEmail, ProbateCaseDetails.builder().caseData(caseData).build());
        return formMapper.fromCaseData(probateCaseDetails.getCaseData());
    }

    @Override
    public Form submit(String applicantEmail, Form form) {
        FormMapper formMapper = mappers.get(form.getType());
        String serviceAuthorisation = securityUtils.generateServiceToken();
        String authorisation = securityUtils.getUserToken();
        CaseData caseData = formMapper.toCaseData(form);
        ProbateCaseDetails probateCaseDetails = submitServiceApi.submit(authorisation, serviceAuthorisation,
            applicantEmail, ProbateCaseDetails.builder().caseData(caseData).build());
        return formMapper.fromCaseData(probateCaseDetails.getCaseData());
    }

    @Override
    public Form updatePayments(String applicantEmail, Form form) {
        PaymentMapper paymentMapper = Mappers.getMapper(PaymentMapper.class);
        String serviceAuthorisation = securityUtils.generateServiceToken();
        String authorisation = securityUtils.getUserToken();
//        CaseData caseData = paymentMapper.toCasePayment(form.getPayments())
//        ProbateCaseDetails probateCaseDetails = submitServiceApi.submit(authorisation, serviceAuthorisation,
//            applicantEmail, ProbateCaseDetails.builder().caseData(caseData).build());
        return form;
    }
}
