package uk.gov.hmcts.probate.core.service;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.mapper.CaseSummaryMapper;
import uk.gov.hmcts.probate.core.service.mapper.FormMapper;
import uk.gov.hmcts.probate.core.service.mapper.PaymentDtoMapper;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.cases.CaseInfo;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.SubmitResult;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.forms.CaseSummary;
import uk.gov.hmcts.reform.probate.model.forms.CaseSummaryHolder;
import uk.gov.hmcts.reform.probate.model.forms.CcdCase;
import uk.gov.hmcts.reform.probate.model.forms.Form;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaForm;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubmitServiceImpl implements SubmitService {

    private final Map<ProbateType, FormMapper> mappers;

    private final PaymentDtoMapper paymentDtoMapper;

    private final SubmitServiceApi submitServiceApi;

    private final BackOfficeService backOfficeService;

    private final SecurityUtils securityUtils;

    private final Map<ProbateType, Function<Form, String>> formIdentifierFunctionMap;

    private final Set<CaseType> caseTypesForNotifications =
        Sets.newHashSet(CaseType.CAVEAT, CaseType.GRANT_OF_REPRESENTATION);

    private final CaseSubmissionUpdater caseSubmissionUpdater;

    private final CaseSummaryMapper caseSummaryMapper;

    @Override
    public Form getCase(String identifier, ProbateType probateType) {
        log.info("============================ Get case called for : {}", probateType.getName());

        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        ProbateCaseDetails probateCaseDetails = submitServiceApi.getCase(authorisation,
            serviceAuthorisation, identifier, probateType.getCaseType().name());
        FormMapper formMapper = getFormMapper(probateType, probateCaseDetails);
        Form form = formMapper.fromCaseData(probateCaseDetails.getCaseData());
        updateCcdCase(probateCaseDetails, form);
        form.getType().getCaseType().getName();
        return form;
    }

    @Override
    public CaseSummaryHolder getAllCases() {
        log.info("========================== Get all cases called");
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        List<ProbateCaseDetails> probateCaseDetails = submitServiceApi.getAllCases(authorisation,
            serviceAuthorisation, CaseType.GRANT_OF_REPRESENTATION.name());
        List<CaseSummary> caseSummaries = probateCaseDetails.stream().map(caseSummaryMapper::createCaseSummary)
            .sorted(Comparator.comparing(CaseSummary::getDateCreated)).collect(Collectors.toList());
        for (int i = 0; i < caseSummaries.size(); i++) {
            CaseSummary caseSummary = caseSummaries.get(i);
            log.info("getAllCases, looping caseSummary.cdCase.id {}/{}", i, caseSummary.getCcdCase().getId());
        }
        return new CaseSummaryHolder(caseSummaries);
    }


    @Override
    public List<ProbateCaseDetails> expireCaveats(String expiryDate) {
        log.info("find expired caveats called");
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        return submitServiceApi.expireCaveats(authorisation,
            serviceAuthorisation, expiryDate);
    }

    private FormMapper getFormMapper(ProbateType probateType, ProbateCaseDetails probateCaseDetails) {
        FormMapper formMapper = null;
        if (probateType.equals(ProbateType.CAVEAT)) {
            formMapper = mappers.get(probateType);
        } else if (((GrantOfRepresentationData) probateCaseDetails.getCaseData())
            .getGrantType().equals(GrantType.INTESTACY)) {
            formMapper = mappers.get(ProbateType.INTESTACY);
        } else {
            formMapper = mappers.get(ProbateType.PA);
        }
        return formMapper;
    }

    @Override
    public CaseSummaryHolder initiateCase(ProbateType probateType) {

        CaseSummaryHolder allCases = getAllCases();
        log.info("================================ Initiate case called");
        FormMapper formMapper = mappers.get(probateType);
        ProbateCaseDetails probateCaseDetails = submitServiceApi.initiateCase(
            securityUtils.getAuthorisation(),
            securityUtils.getServiceAuthorisation(),
            ProbateCaseDetails.builder().caseData(mapToCase(initiateForm(probateType), formMapper)).build()
        );
        log.info("Initiate case for new caseid: {}", probateCaseDetails.getCaseInfo().getCaseId());
        allCases.getApplications().add(caseSummaryMapper.createCaseSummary(probateCaseDetails));
        for (int i = 0; i < allCases.getApplications().size(); i++) {
            CaseSummary caseSummary = allCases.getApplications().get(i);
            log.info("initiateCase, looping caseSummary.cdCase.id {}/{}", i, caseSummary.getCcdCase().getId());
        }
        return allCases;
    }

    private Form initiateForm(ProbateType probateType) {

        if (ProbateType.INTESTACY.equals(probateType)) {
            return IntestacyForm.builder().build();
        } else if (ProbateType.PA.equals(probateType)) {
            return PaForm.builder().build();
        }
        return null;
    }


    @Override
    public Form saveCase(String identifier, Form form) {
        log.info("=========================  Save case called");
        assertIdentifier(identifier, form);
        FormMapper formMapper = mappers.get(form.getType());
        CaseInfo caseInfo = new CaseInfo();
        log.info(form.toString());
        if (form.getEventDescription() != null && !form.getEventDescription().equals("null")) {
            log.info(form.getEventDescription());
            caseInfo.setEventDescription(form.getEventDescription());
        } else {
            log.info("coming to not having event description");
        }
        ProbateCaseDetails probateCaseDetails = submitServiceApi.saveCase(
            securityUtils.getAuthorisation(),
            securityUtils.getServiceAuthorisation(),
            identifier,
            ProbateCaseDetails.builder().caseData(mapToCase(form, formMapper)).caseInfo(caseInfo).build()
        );
        return mapFromCase(formMapper, probateCaseDetails);
    }

    @Override
    public Form submit(String identifier, Form form) {
        log.info("========================= Submit called forn {}", form.toString());
        assertIdentifier(identifier, form);
        FormMapper formMapper = mappers.get(form.getType());
        log.debug("calling submit on submitserviceapi");
        SubmitResult submitResult = submitServiceApi.submit(
            securityUtils.getAuthorisation(),
            securityUtils.getServiceAuthorisation(),
            identifier,
            ProbateCaseDetails.builder().caseData(mapToCase(form, formMapper)).build()
        );
        return mapFromCase(formMapper, submitResult.getProbateCaseDetails());
    }

    @Override
    public Form update(String identifier, ProbateType probateType, PaymentDto paymentDto) {
        String authorisation = securityUtils.getAuthorisation();
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        log.info("Get existing case from submit service");
        ProbateCaseDetails existingCase = submitServiceApi.getCase(authorisation,
            serviceAuthorisation, identifier, probateType.getCaseType().name());

        log.info("Got existing case now set payment on this case");
        CasePayment casePayment = paymentDtoMapper.toCasePayment(paymentDto);
        CollectionMember<CasePayment> collectionMember = new CollectionMember<CasePayment>();
        collectionMember.setValue(casePayment);
        existingCase.getCaseData().setPayments(Arrays.asList(collectionMember));

        log.info("Send notification");
        sendNotification(existingCase);

        log.info("Update case for submission");
        updateCaseForSubmission(existingCase);

        log.debug("calling create case in submitServiceApi");
        ProbateCaseDetails probateCaseDetails = submitServiceApi.createCase(
            authorisation,
            serviceAuthorisation,
            identifier,
            existingCase
        );

        FormMapper formMapper = getFormMapper(probateType, probateCaseDetails);
        return mapFromCase(formMapper, probateCaseDetails);
    }

    private void assertIdentifier(String identifier, Form form) {
        String identifierInForm = Optional.of(formIdentifierFunctionMap.get(form.getType()))
            .orElseThrow(IllegalArgumentException::new)
            .apply(form);
        Assert.isTrue(identifierInForm != null && identifierInForm.equals(identifier),
            "Path variable identifier must match identifier in form");
    }

    private void updateCcdCase(ProbateCaseDetails probateCaseDetails, Form formResponse) {
        formResponse.setCcdCase(CcdCase.builder()
            .id(Long.valueOf(probateCaseDetails.getCaseInfo().getCaseId()))
            .state(probateCaseDetails.getCaseInfo().getState().getName())
            .build());
    }

    @Override
    public Form updatePayments(String identifier, Form form) {
        log.info("update Payments called");
        Assert.isTrue(!CollectionUtils.isEmpty(form.getPayments()) || form.getPayment() != null,
            "Cannot update case with no payments, there needs to be at least one payment");
        String authorisation = securityUtils.getAuthorisation();
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        log.info("Get existing case from submit service");
        ProbateCaseDetails existingCase = submitServiceApi.getCase(authorisation,
            serviceAuthorisation, identifier, form.getType().getCaseType().name());
        log.info("Got existing case now set payment on this case");
        FormMapper formMapper = mappers.get(form.getType());
        CaseData caseData = formMapper.toCaseData(form);

        existingCase.getCaseData().setPayments(caseData.getPayments());
        log.info("Send notification");
        sendNotification(existingCase);

        log.info("Update case for submission");
        updateCaseForSubmission(existingCase);
        //TODO: PRO-5580 - Uncomment once applicationSubmittedDate has been re-added to the spreadsheet

        log.debug("calling create case in submitServiceApi");
        ProbateCaseDetails probateCaseDetails = submitServiceApi.createCase(
            authorisation,
            serviceAuthorisation,
            identifier,
            existingCase
        );
        return mapFromCase(formMapper, probateCaseDetails);
    }

    public Optional<CaseData> sendNotification(ProbateCaseDetails probateCaseDetails) {
        CaseType caseType = CaseType.getCaseType(probateCaseDetails.getCaseData());
        log.info("Send notification with case type {}", caseType.getName());
        CasePayment casePayment = probateCaseDetails.getCaseData().getPayments().get(0).getValue();
        if (caseTypesForNotifications.contains(caseType)
            && (PaymentStatus.SUCCESS.equals(casePayment.getStatus())
            || PaymentStatus.NOT_REQUIRED.equals(casePayment.getStatus()))) {
            return Optional.ofNullable(backOfficeService.sendNotification(probateCaseDetails));
        }
        return Optional.empty();
    }

    public void updateCaseForSubmission(ProbateCaseDetails probateCaseDetails) {
        CasePayment casePayment = probateCaseDetails.getCaseData().getPayments().get(0).getValue();
        if (PaymentStatus.SUCCESS.equals(casePayment.getStatus())) {
            caseSubmissionUpdater.updateCaseForSubmission(probateCaseDetails.getCaseData());
        }
    }

    @Override
    public ProbateCaseDetails updateByCaseId(String caseId, ProbateCaseDetails caseDetails) {
        log.info("Call to submit service with id {}", caseId);
        ProbateCaseDetails probateCaseDetails = submitServiceApi.updateByCaseId(
            securityUtils.getAuthorisation(),
            securityUtils.getServiceAuthorisation(),
            caseId,
            caseDetails);
        return probateCaseDetails;
    }

    @Override
    public Form validate(String identifier, ProbateType probateType) {
        FormMapper formMapper = mappers.get(probateType);
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        ProbateCaseDetails probateCaseDetails = submitServiceApi.validate(authorisation,
            serviceAuthorisation, identifier, probateType.getCaseType().name());
        Form form = formMapper.fromCaseData(probateCaseDetails.getCaseData());
        updateCcdCase(probateCaseDetails, form);
        return form;
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
