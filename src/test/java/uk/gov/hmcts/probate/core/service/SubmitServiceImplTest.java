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
import uk.gov.hmcts.probate.core.service.mapper.CaseSummaryMapper;
import uk.gov.hmcts.probate.core.service.mapper.CaveatMapper;
import uk.gov.hmcts.probate.core.service.mapper.FormMapper;
import uk.gov.hmcts.probate.core.service.mapper.IntestacyMapper;
import uk.gov.hmcts.probate.core.service.mapper.PaymentDtoMapper;
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
import uk.gov.hmcts.reform.probate.model.forms.CaseSummary;
import uk.gov.hmcts.reform.probate.model.forms.CaseSummaryHolder;
import uk.gov.hmcts.reform.probate.model.forms.CcdCase;
import uk.gov.hmcts.reform.probate.model.forms.Form;
import uk.gov.hmcts.reform.probate.model.forms.caveat.CaveatForm;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubmitServiceImplTest {

    public static final String EXTERNAL_REFERENCE = "EXT2213214";
    private static final String EMAIL_ADDRESS = "jon.snow@thenorth.com";
    private static final String SERVICE_AUTHORIZATION = "SERVICEAUTH1234567";
    private static final String AUTHORIZATION = "AUTH1234567";
    private static final String CASE_ID = "1232234234";
    private static final CaseState STATE = CaseState.DRAFT;
    private static final String CAVEAT_IDENTIFIER = "Id";
    private static final String CAVEAT_EXPIRY_DATE = "2020-12-31";
    private Map<ProbateType, FormMapper> mappers;

    @Mock
    private SubmitServiceApi submitServiceApi;

    @Mock
    private BackOfficeService backOfficeService;

    @Mock
    private PaymentDtoMapper paymentDtoMapper;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private IntestacyMapper intestacyMapper;

    @Mock
    private CaveatMapper caveatMapper;

    @Mock
    private CaseSubmissionUpdater caseSubmissionUpdater;

    @Mock
    private CaseSummaryMapper caseSummaryMapper;

    private SubmitServiceImpl submitService;

    private IntestacyForm intestacyForm;

    private CaveatForm caveatForm;

    private ProbateCaseDetails intestacyCaseDetails;
    private ProbateCaseDetails caveatCaseDetails;
    private List<ProbateCaseDetails> expiredCaveatDetails;

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
        submitService =
            new SubmitServiceImpl(mappers, paymentDtoMapper, submitServiceApi, backOfficeService, securityUtils,
                identifierConfiguration.formIdentifierFunctionMap(), caseSubmissionUpdater, caseSummaryMapper);

        when(securityUtils.getAuthorisation()).thenReturn(AUTHORIZATION);
        when(securityUtils.getServiceAuthorisation()).thenReturn(SERVICE_AUTHORIZATION);
        caseInfo = CaseInfo.builder().state(STATE).caseId(CASE_ID).build();

        // Intestacy setup
        String intestacyFormStr = TestUtils.getJsonFromFile("intestacyFormTest.json");
        intestacyForm = objectMapper.readValue(intestacyFormStr, IntestacyForm.class);
        CasePayment intestacyCasePayment = new CasePayment();
        intestacyCaseData = GrantOfRepresentationData.builder().grantType(GrantType.INTESTACY).payments(
            Lists.newArrayList(CollectionMember.<CasePayment>builder().value(intestacyCasePayment).build())).build();

        intestacyCaseDetails = ProbateCaseDetails.builder().caseData(intestacyCaseData).caseInfo(caseInfo).build();

        when(intestacyMapper.toCaseData(intestacyForm)).thenReturn(intestacyCaseData);
        when(intestacyMapper.fromCaseData(intestacyCaseData)).thenReturn(intestacyForm);

        // Caveat setup
        String caveatFormStr = TestUtils.getJsonFromFile("caveatForm.json");
        caveatForm = objectMapper.readValue(caveatFormStr, CaveatForm.class);
        CasePayment caveatCasePayment = new CasePayment();
        caveatCasePayment.setStatus(PaymentStatus.SUCCESS);
        caveatCaseData = CaveatData.builder().applicationId(CAVEAT_IDENTIFIER).payments(
            Lists.newArrayList(CollectionMember.<CasePayment>builder().value(caveatCasePayment).build())).build();

        caveatCaseDetails = ProbateCaseDetails.builder().caseData(caveatCaseData).caseInfo(caseInfo).build();
        expiredCaveatDetails =
            Arrays.asList(ProbateCaseDetails.builder().build(), ProbateCaseDetails.builder().build());

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
    public void shouldGetAllCasesForm() {
        when(
            submitServiceApi.getAllCases(AUTHORIZATION, SERVICE_AUTHORIZATION, CaseType.GRANT_OF_REPRESENTATION.name()))
            .thenReturn(Arrays.asList(intestacyCaseDetails));
        CaseSummary caseSummary = mock(CaseSummary.class);
        CcdCase ccdCase = mock(CcdCase.class);
        when(caseSummaryMapper.createCaseSummary(intestacyCaseDetails)).thenReturn(caseSummary);
        when(caseSummary.getCcdCase()).thenReturn(ccdCase);

        CaseSummaryHolder caseSummaryHolderResponse = submitService.getAllCases();

        verify(submitServiceApi, times(1))
            .getAllCases(AUTHORIZATION, SERVICE_AUTHORIZATION, CaseType.GRANT_OF_REPRESENTATION.name());
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
        verify(caseSummaryMapper).createCaseSummary(intestacyCaseDetails);
    }


    @Test
    public void shouldInitiateCase() {
        when(submitServiceApi.initiateCase(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), any(ProbateCaseDetails.class)))
            .thenReturn(intestacyCaseDetails);
        CaseSummary caseSummary = mock(CaseSummary.class);
        CcdCase ccdCase = mock(CcdCase.class);
        when(caseSummaryMapper.createCaseSummary(any(ProbateCaseDetails.class))).thenReturn(caseSummary);
        when(caseSummary.getCcdCase()).thenReturn(ccdCase);

        CaseSummaryHolder caseSummaryHolder = submitService.initiateCase(ProbateType.INTESTACY);
        verify(submitServiceApi, times(1))
            .initiateCase(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), any(ProbateCaseDetails.class));
        verify(securityUtils, times(2)).getAuthorisation();
        verify(securityUtils, times(2)).getServiceAuthorisation();
        verify(caseSummaryMapper).createCaseSummary(any(ProbateCaseDetails.class));
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
        shouldSaveDraftForm(intestacyForm, intestacyCaseDetails, "1535574519543818");
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
        ((CaveatForm) caveatForm).setApplicationId("test@Test.com");

        submitService.saveCase(EMAIL_ADDRESS, caveatForm);
    }

    @Test
    public void shouldSubmitCaveatForm() {
        when(submitServiceApi.submit(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(CAVEAT_IDENTIFIER), any(ProbateCaseDetails.class)))
            .thenReturn(new SubmitResult(caveatCaseDetails, null));

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

        CasePayment casePayment = CasePayment.builder().build();
        when(paymentDtoMapper.toCasePayment(paymentDto)).thenReturn(casePayment);

        Form formResponse = submitService.update(EMAIL_ADDRESS, ProbateType.INTESTACY, paymentDto);

        assertThat(formResponse, is(intestacyForm));
        verify(submitServiceApi, times(1)).getCase(AUTHORIZATION, SERVICE_AUTHORIZATION,
            EMAIL_ADDRESS, CaseType.GRANT_OF_REPRESENTATION.name());
        verify(submitServiceApi, times(1)).createCase(eq(AUTHORIZATION),
            eq(SERVICE_AUTHORIZATION),
            eq(EMAIL_ADDRESS), any(ProbateCaseDetails.class));
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
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


    @Test
    public void shouldUpdateIntestacyPaymentsAndSendNotification() {
        when(submitServiceApi.getCase(anyString(), anyString(),
            anyString(), anyString())).thenReturn(intestacyCaseDetails);

        intestacyCaseDetails.getCaseData().getPayments().get(0).getValue().setStatus(PaymentStatus.SUCCESS);

        intestacyCaseDetails.getCaseInfo().setState(CaseState.PA_APP_CREATED);
        shouldUpdatePayments(intestacyForm, intestacyCaseDetails);
        verify(backOfficeService, times(1)).sendNotification(intestacyCaseDetails);
    }

    @Test
    public void shouldUpdateIntestacyPaymentsAndNotSendNotification() {
        when(submitServiceApi.getCase(anyString(), anyString(),
            anyString(), anyString())).thenReturn(intestacyCaseDetails);

        intestacyCaseDetails.getCaseData().getPayments().get(0).getValue().setStatus(PaymentStatus.FAILED);

        intestacyCaseDetails.getCaseInfo().setState(CaseState.PA_APP_CREATED);
        shouldUpdatePayments(intestacyForm, intestacyCaseDetails);
        verify(backOfficeService, never()).sendNotification(caveatCaseDetails);
    }

    private void shouldUpdatePayments(Form form, ProbateCaseDetails caseDetails) {
        when(submitServiceApi.createCase(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(EMAIL_ADDRESS), any(ProbateCaseDetails.class))).thenReturn(caseDetails);

        Form formResponse = submitService.updatePayments(EMAIL_ADDRESS, form);

        assertThat(formResponse, is(form));
        verify(submitServiceApi, times(1)).createCase(eq(AUTHORIZATION),
            eq(SERVICE_AUTHORIZATION),
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
    public void shouldUpdatePaymentAndSendNotificationGop() {
        String caseId = "234324";
        CasePayment casePayment = CasePayment.builder().status(PaymentStatus.SUCCESS).build();
        ProbateCaseDetails probateCaseDetails = ProbateCaseDetails.builder().caseInfo(CaseInfo.builder()
            .state(CaseState.PA_APP_CREATED)
            .build())
            .caseData(CaveatData.builder().build())
            .build();

        when(submitServiceApi
            .updateByCaseId(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), eq(caseId), eq(probateCaseDetails)))
            .thenReturn(probateCaseDetails);

        ProbateCaseDetails probateCaseDetailsResult = submitService.updateByCaseId(caseId, probateCaseDetails);
        assertThat(probateCaseDetails, equalTo(probateCaseDetailsResult));
        verify(submitServiceApi)
            .updateByCaseId(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), eq(caseId), eq(probateCaseDetails));
        verify(backOfficeService, never()).sendNotification(caveatCaseDetails);
        verify(caseSubmissionUpdater, never()).updateCaseForSubmission(any(CaseData.class));
    }

    @Test
    public void shouldUpdatePaymentAndSendNotificationGopForPaymentNotRequired() {
        String caseId = "234324";
        CasePayment casePayment = CasePayment.builder().status(PaymentStatus.NOT_REQUIRED).build();
        CollectionMember<CasePayment> collectionMember = new CollectionMember<>(null, casePayment);
        ArrayList<CollectionMember<CasePayment>> payments = new ArrayList();
        payments.add(collectionMember);
        ProbateCaseDetails probateCaseDetails = ProbateCaseDetails.builder().caseInfo(CaseInfo.builder()
            .state(CaseState.PA_APP_CREATED)
            .build())
            .caseData(CaveatData.builder().payments(payments).build())

            .build();
        Optional<CaseData> caseData = submitService.sendNotification(probateCaseDetails);

        verify(backOfficeService).sendNotification(probateCaseDetails);
    }

    @Test
    public void shouldUpdatePaymentAndSendNotificationGopForPaymentSucccess() {
        String caseId = "234324";
        CasePayment casePayment = CasePayment.builder().status(PaymentStatus.SUCCESS).build();
        CollectionMember<CasePayment> collectionMember = new CollectionMember<>(null, casePayment);
        ArrayList<CollectionMember<CasePayment>> payments = new ArrayList();
        payments.add(collectionMember);
        ProbateCaseDetails probateCaseDetails = ProbateCaseDetails.builder().caseInfo(CaseInfo.builder()
            .state(CaseState.PA_APP_CREATED)
            .build())
            .caseData(CaveatData.builder().payments(payments).build())

            .build();
        Optional<CaseData> caseData = submitService.sendNotification(probateCaseDetails);

        verify(backOfficeService).sendNotification(probateCaseDetails);
    }

    @Test
    public void shouldNotUpdatePaymentAndSendNotificationGopForPaymentFailed() {
        String caseId = "234324";
        CasePayment casePayment = CasePayment.builder().status(PaymentStatus.FAILED).build();
        CollectionMember<CasePayment> collectionMember = new CollectionMember<>(null, casePayment);
        ArrayList<CollectionMember<CasePayment>> payments = new ArrayList();
        payments.add(collectionMember);
        ProbateCaseDetails probateCaseDetails = ProbateCaseDetails.builder().caseInfo(CaseInfo.builder()
            .state(CaseState.PA_APP_CREATED)
            .build())
            .caseData(CaveatData.builder().payments(payments).build())

            .build();
        Optional<CaseData> caseData = submitService.sendNotification(probateCaseDetails);

        verify(backOfficeService, never()).sendNotification(probateCaseDetails);
    }

    @Test
    public void shouldUpdatePaymentsCaveats() {
        String caseId = "234324";
        CasePayment casePayment = CasePayment.builder().build();
        ProbateCaseDetails probateCaseDetails = ProbateCaseDetails.builder().caseInfo(CaseInfo.builder()
            .state(CaseState.CAVEAT_RAISED)
            .build())
            .caseData(CaveatData.builder().build())
            .build();

        when(submitServiceApi
            .updateByCaseId(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), eq(caseId), eq(probateCaseDetails)))
            .thenReturn(probateCaseDetails);

        ProbateCaseDetails probateCaseDetailsResult = submitService.updateByCaseId(caseId, probateCaseDetails);
        assertThat(probateCaseDetails, equalTo(probateCaseDetailsResult));
        verify(submitServiceApi)
            .updateByCaseId(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), eq(caseId), eq(probateCaseDetails));
    }


    @Test
    public void shouldValidate() {
        when(submitServiceApi.validate(AUTHORIZATION, SERVICE_AUTHORIZATION,
            EMAIL_ADDRESS, CaseType.GRANT_OF_REPRESENTATION.name())).thenReturn(intestacyCaseDetails);

        submitService.validate(EMAIL_ADDRESS, ProbateType.INTESTACY);

        verify(submitServiceApi).validate(AUTHORIZATION, SERVICE_AUTHORIZATION,
            EMAIL_ADDRESS, CaseType.GRANT_OF_REPRESENTATION.name());
    }

    @Test
    public void shouldExpireCaveats() {
        when(submitServiceApi.expireCaveats(AUTHORIZATION, SERVICE_AUTHORIZATION,
            CAVEAT_EXPIRY_DATE)).thenReturn(expiredCaveatDetails);

        submitService.expireCaveats(CAVEAT_EXPIRY_DATE);

        verify(submitServiceApi).expireCaveats(AUTHORIZATION, SERVICE_AUTHORIZATION,
            CAVEAT_EXPIRY_DATE);
    }
}
