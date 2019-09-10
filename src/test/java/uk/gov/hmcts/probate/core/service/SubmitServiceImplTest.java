package uk.gov.hmcts.probate.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.probate.TestUtils;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.mapper.CaveatMapper;
import uk.gov.hmcts.probate.core.service.mapper.FormMapper;
import uk.gov.hmcts.probate.core.service.mapper.IntestacyMapper;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.cases.CaseInfo;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CaseState;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.SubmitResult;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.forms.Form;
import uk.gov.hmcts.reform.probate.model.forms.caveat.CaveatForm;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubmitServiceImplTest {

    private static final String EMAIL_ADDRESS = "jon.snow@thenorth.com";
    private static final String SERVICE_AUTHORIZATION = "SERVICEAUTH1234567";
    private static final String AUTHORIZATION = "AUTH1234567";
    private static final String CASE_ID = "1232234234";
    private static final CaseState STATE = CaseState.DRAFT;
    private static final String CAVEAT_IDENTIFIER = "Id";
    public static final String EXTERNAL_REFERENCE = "EXT2213214";


    private Map<ProbateType, FormMapper> mappers;

    @Mock
    private SubmitServiceApi submitServiceApi;

    @Mock
    private BackOfficeService backOfficeService;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private IntestacyMapper intestacyMapper;

    @Mock
    private CaveatMapper caveatMapper;

    @Mock
    private CaseSubmissionUpdater caseSubmissionUpdater;

    private SubmitServiceImpl submitService;

    private IntestacyForm intestacyForm;

    private CaveatForm caveatForm;

    private ProbateCaseDetails intestacyCaseDetails;
    private ProbateCaseDetails caveatCaseDetails;

    private GrantOfRepresentationData intestacyCaseData;
    private CaveatData caveatCaseData;

    private CaseInfo caseInfo;

    private ObjectMapper objectMapper = new ObjectMapper();

    private PaymentDto paymentDto;

    @Before
    public void setUp() throws Exception {
        IdentifierConfiguration identifierConfiguration = new IdentifierConfiguration();

        mappers = ImmutableMap.<ProbateType, FormMapper>builder()
            .put(ProbateType.INTESTACY, intestacyMapper)
            .put(ProbateType.CAVEAT, caveatMapper)
            .build();
        submitService = new SubmitServiceImpl(mappers, submitServiceApi, backOfficeService, securityUtils,
            identifierConfiguration.formIdentifierFunctionMap(), caseSubmissionUpdater);

        when(securityUtils.getAuthorisation()).thenReturn(AUTHORIZATION);
        when(securityUtils.getServiceAuthorisation()).thenReturn(SERVICE_AUTHORIZATION);
        caseInfo = CaseInfo.builder().state(STATE).caseId(CASE_ID).build();

        // Intestacy setup
        String intestacyFormStr = TestUtils.getJSONFromFile("intestacyForm.json");
        intestacyForm = objectMapper.readValue(intestacyFormStr, IntestacyForm.class);
        CasePayment intestacyCasePayment = new CasePayment();
        intestacyCaseData = GrantOfRepresentationData.builder().grantType(GrantType.INTESTACY).payments(
            Lists.newArrayList(CollectionMember.<CasePayment>builder().value(intestacyCasePayment).build())).build();

        intestacyCaseDetails = ProbateCaseDetails.builder().caseData(intestacyCaseData).caseInfo(caseInfo).build();

        when(intestacyMapper.toCaseData(intestacyForm)).thenReturn(intestacyCaseData);
        when(intestacyMapper.fromCaseData(intestacyCaseData)).thenReturn(intestacyForm);

        // Intestacy setup
        String caveatFormStr = TestUtils.getJSONFromFile("caveatForm.json");
        caveatForm = objectMapper.readValue(caveatFormStr, CaveatForm.class);
        CasePayment caveatCasePayment = new CasePayment();
        caveatCasePayment.setStatus(PaymentStatus.SUCCESS);
        caveatCaseData = CaveatData.builder().applicationId(CAVEAT_IDENTIFIER).payments(
            Lists.newArrayList(CollectionMember.<CasePayment>builder().value(caveatCasePayment).build())).build();

        caveatCaseDetails = ProbateCaseDetails.builder().caseData(caveatCaseData).caseInfo(caseInfo).build();

        when(caveatMapper.toCaseData(caveatForm)).thenReturn(caveatCaseData);
        when(caveatMapper.fromCaseData(caveatCaseData)).thenReturn(caveatForm);

        paymentDto = PaymentDto.builder()
            .externalReference(EXTERNAL_REFERENCE)
            .ccdCaseNumber("1232142")
            .status("Success")
            .amount(BigDecimal.valueOf(1000L))
            .reference("REF13423")
            .channel("Online")
            .currency("GBP")
            .dateCreated(Date.from(LocalDate.of(2019, 1, 1).atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()))
            .build();
    }

    @Test
    public void shouldGetIntestacyForm() {
        when(submitServiceApi.getCase(AUTHORIZATION, SERVICE_AUTHORIZATION,
            EMAIL_ADDRESS, CaseType.GRANT_OF_REPRESENTATION.name())).thenReturn(intestacyCaseDetails);

        Form formResponse = submitService.getCase(EMAIL_ADDRESS, ProbateType.INTESTACY);

        assertThat(formResponse, is(intestacyForm));
        verify(submitServiceApi, times(1)).getCase(AUTHORIZATION, SERVICE_AUTHORIZATION,
            EMAIL_ADDRESS, CaseType.GRANT_OF_REPRESENTATION.name());
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
    }

    @Test
    public void shouldGetCaveatForm() {
        when(submitServiceApi.getCase(AUTHORIZATION, SERVICE_AUTHORIZATION,
            CAVEAT_IDENTIFIER, CaseType.CAVEAT.name())).thenReturn(caveatCaseDetails);

        Form formResponse = submitService.getCase(CAVEAT_IDENTIFIER, ProbateType.CAVEAT);

        assertThat(formResponse, is(caveatForm));
        verify(submitServiceApi, times(1)).getCase(AUTHORIZATION, SERVICE_AUTHORIZATION,
            CAVEAT_IDENTIFIER, CaseType.CAVEAT.name());
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
    }

    @Test
    public void shouldSaveDraftIntestacyForm() {
        shouldSaveDraftForm(intestacyForm, intestacyCaseDetails, EMAIL_ADDRESS);
    }

    @Test
    public void shouldSaveDraftCaveatsForm() {
        shouldSaveDraftForm(caveatForm, caveatCaseDetails, CAVEAT_IDENTIFIER);
    }

    private void shouldSaveDraftForm(Form form, ProbateCaseDetails caseDetails, String identifier) {
        when(submitServiceApi.saveCase(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(identifier), any(ProbateCaseDetails.class))).thenReturn(caseDetails);

        Form formResponse = submitService.saveCase(identifier, form);

        assertThat(formResponse, is(form));
        verify(submitServiceApi, times(1)).saveCase(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(identifier), any(ProbateCaseDetails.class));
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
    }

    @Test
    public void shouldSaveDraftCaveatForm() {
        shouldSaveDraftForm(caveatForm, caveatCaseDetails, CAVEAT_IDENTIFIER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorOnSaveDraftIfEmailAddressDoesNotMatchForm() {
        ((IntestacyForm) intestacyForm).setApplicantEmail("test@Test.com");

        submitService.saveCase(EMAIL_ADDRESS, intestacyForm);
    }

    @Test
    public void shouldSubmitCaveatForm() {
        when(submitServiceApi.submit(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(CAVEAT_IDENTIFIER), any(ProbateCaseDetails.class))).thenReturn(new SubmitResult(caveatCaseDetails, null));

        Form formResponse = submitService.submit(CAVEAT_IDENTIFIER, caveatForm);

        assertThat(formResponse, is(caveatForm));
        verify(submitServiceApi, times(1)).submit(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(CAVEAT_IDENTIFIER), any(ProbateCaseDetails.class));
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
    }

    @Test
    public void shouldUpdateForm() {
        when(submitServiceApi.getCase(AUTHORIZATION, SERVICE_AUTHORIZATION,
            EMAIL_ADDRESS, CaseType.GRANT_OF_REPRESENTATION.name())).thenReturn(intestacyCaseDetails);
        when(submitServiceApi.createCase(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(EMAIL_ADDRESS), any(ProbateCaseDetails.class))).thenReturn(intestacyCaseDetails);

        Form formResponse = submitService.update(EMAIL_ADDRESS, ProbateType.INTESTACY, paymentDto);

        assertThat(formResponse, is(intestacyForm));
        verify(submitServiceApi, times(2)).getCase(AUTHORIZATION, SERVICE_AUTHORIZATION,
            EMAIL_ADDRESS, CaseType.GRANT_OF_REPRESENTATION.name());
        verify(submitServiceApi, times(1)).createCase(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(EMAIL_ADDRESS), any(ProbateCaseDetails.class));
        verify(securityUtils, times(2)).getAuthorisation();
        verify(securityUtils, times(2)).getServiceAuthorisation();
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorOnSubmitIfEmailAddressDoesNotMatchForm() {
        //TODO Remove CAST
        ((IntestacyForm) intestacyForm).setApplicantEmail("test@Test.com");

        submitService.submit(EMAIL_ADDRESS, intestacyForm);
    }

    @Test
    public void shouldUpdateIntestacyPayments() {
        when(submitServiceApi.getCase(anyString(), anyString(),
            anyString(), anyString())).thenReturn(caveatCaseDetails);

        shouldUpdatePayments(intestacyForm, intestacyCaseDetails);
        verify(backOfficeService, never()).sendNotification(intestacyCaseDetails);
    }

    @Test
    public void shouldUpdateCaveatPaymentsAndSendNotification() {
        when(submitServiceApi.getCase(anyString(), anyString(),
            anyString(), anyString())).thenReturn(caveatCaseDetails);

        caveatCaseDetails.getCaseInfo().setState(CaseState.CAVEAT_RAISED);
        shouldUpdatePayments(caveatForm, caveatCaseDetails);
        verify(backOfficeService, times(1)).sendNotification(caveatCaseDetails);
    }

    @Test
    public void shouldUpdateCaveatPaymentsAndNotSendNotification() {
        when(submitServiceApi.getCase(anyString(), anyString(),
            anyString(), anyString())).thenReturn(caveatCaseDetails);

        caveatCaseDetails.getCaseData().getPayments().get(0).getValue().setStatus(PaymentStatus.FAILED);
        shouldUpdatePayments(caveatForm, caveatCaseDetails);
        verify(backOfficeService, never()).sendNotification(caveatCaseDetails);
        verify(caseSubmissionUpdater, never()).updateCaseForSubmission(any(CaseData.class));
    }

    private void shouldUpdatePayments(Form form, ProbateCaseDetails caseDetails) {
        when(submitServiceApi.createCase(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(EMAIL_ADDRESS), any(ProbateCaseDetails.class))).thenReturn(caseDetails);

        Form formResponse = submitService.updatePayments(EMAIL_ADDRESS, form);

        assertThat(formResponse, is(form));
        verify(submitServiceApi, times(1)).createCase(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(EMAIL_ADDRESS), any(ProbateCaseDetails.class));
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorOnUpdatePaymentsIfNoPaymentsOnForm() {
        intestacyForm.setPayments(null);

        submitService.updatePayments(EMAIL_ADDRESS, intestacyForm);
    }

    @Test
    public void shouldUpdatePayments() {
        String caseId = "234324";
        CasePayment casePayment = CasePayment.builder().build();
        ProbateCaseDetails probateCaseDetails = ProbateCaseDetails.builder().caseInfo(CaseInfo.builder()
            .state(CaseState.PA_APP_CREATED)
            .build())
            .caseData(CaveatData.builder().build())
            .build();

        when(submitServiceApi.updateByCaseId(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), eq(caseId), eq(probateCaseDetails))).thenReturn(probateCaseDetails);

        ProbateCaseDetails probateCaseDetailsResult = submitService.updateByCaseId(caseId, probateCaseDetails);
        assertThat(probateCaseDetails, equalTo(probateCaseDetailsResult));
        verify(submitServiceApi).updateByCaseId(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), eq(caseId), eq(probateCaseDetails));
        verify(backOfficeService, never()).sendNotification(caveatCaseDetails);
        verify(caseSubmissionUpdater, never()).updateCaseForSubmission(any(CaseData.class));
    }

    @Test
    public void shouldUpdatePaymentsAndSendNotification() {
        String caseId = "234324";
        CasePayment casePayment = CasePayment.builder().build();
        ProbateCaseDetails probateCaseDetails = ProbateCaseDetails.builder().caseInfo(CaseInfo.builder()
            .state(CaseState.CAVEAT_RAISED)
            .build())
            .caseData(CaveatData.builder().build())
            .build();

        when(submitServiceApi.updateByCaseId(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), eq(caseId), eq(probateCaseDetails))).thenReturn(probateCaseDetails);

        ProbateCaseDetails probateCaseDetailsResult = submitService.updateByCaseId(caseId, probateCaseDetails);
        assertThat(probateCaseDetails, equalTo(probateCaseDetailsResult));
        verify(submitServiceApi).updateByCaseId(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), eq(caseId), eq(probateCaseDetails));
    }


    @Test
    public void shouldValidate() {
        when(submitServiceApi.validate(AUTHORIZATION, SERVICE_AUTHORIZATION,
            EMAIL_ADDRESS, CaseType.GRANT_OF_REPRESENTATION.name())).thenReturn(intestacyCaseDetails);

        submitService.validate(EMAIL_ADDRESS, ProbateType.INTESTACY);

        verify(submitServiceApi).validate(AUTHORIZATION, SERVICE_AUTHORIZATION,
            EMAIL_ADDRESS, CaseType.GRANT_OF_REPRESENTATION.name());
    }

}
