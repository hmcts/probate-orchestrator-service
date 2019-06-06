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
import uk.gov.hmcts.reform.probate.model.cases.CaseInfo;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CaseState;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.ProbatePaymentDetails;
import uk.gov.hmcts.reform.probate.model.cases.SubmitResult;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.forms.Form;
import uk.gov.hmcts.reform.probate.model.forms.caveat.CaveatForm;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
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
    private static final String STATE = "DRAFT";
    public static final String CAVEAT_IDENTIFIER = "Id";


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

    private SubmitServiceImpl submitService;

    private IntestacyForm intestacyForm;

    private CaveatForm caveatForm;

    private ProbateCaseDetails intestacyCaseDetails;
    private ProbateCaseDetails caveatCaseDetails;

    private GrantOfRepresentationData intestacyCaseData;
    private CaveatData caveatCaseData;

    private CaseInfo caseInfo;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        IdentifierConfiguration identifierConfiguration = new IdentifierConfiguration();

        mappers = ImmutableMap.<ProbateType, FormMapper>builder()
            .put(ProbateType.INTESTACY, intestacyMapper)
            .put(ProbateType.CAVEAT, caveatMapper)
            .build();
        submitService = new SubmitServiceImpl(mappers, submitServiceApi, backOfficeService, securityUtils,
            identifierConfiguration.formIdentifierFunctionMap());

        when(securityUtils.getAuthorisation()).thenReturn(AUTHORIZATION);
        when(securityUtils.getServiceAuthorisation()).thenReturn(SERVICE_AUTHORIZATION);
        caseInfo = CaseInfo.builder().state(STATE).caseId(CASE_ID).build();

        // Intestacy setup
        String intestacyFormStr = TestUtils.getJSONFromFile("intestacyForm.json");
        intestacyForm = objectMapper.readValue(intestacyFormStr, IntestacyForm.class);
        CasePayment intestacyCasePayment = new CasePayment();
        intestacyCaseData = GrantOfRepresentationData.builder().payments(
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
        when(submitServiceApi.saveDraft(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(identifier), any(ProbateCaseDetails.class))).thenReturn(caseDetails);

        Form formResponse = submitService.saveDraft(identifier, form);

        assertThat(formResponse, is(form));
        verify(submitServiceApi, times(1)).saveDraft(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
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
        ((IntestacyForm) intestacyForm).getApplicant().setEmail("test@Test.com");

        submitService.saveDraft(EMAIL_ADDRESS, intestacyForm);
    }

    @Test
    public void shouldSubmitIntestacyForm() {
        shouldSubmitForm(intestacyForm, intestacyCaseDetails, EMAIL_ADDRESS);
    }

    @Test
    public void shouldSubmitCaveatForm() {
        shouldSubmitForm(caveatForm, caveatCaseDetails, CAVEAT_IDENTIFIER);
    }

    private void shouldSubmitForm(Form form, ProbateCaseDetails caseDetails, String identifier) {
        when(submitServiceApi.submit(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(identifier), any(ProbateCaseDetails.class))).thenReturn(new SubmitResult(caseDetails, null));

        Form formResponse = submitService.submit(identifier, form);

        assertThat(formResponse, is(form));
        verify(submitServiceApi, times(1)).submit(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(identifier), any(ProbateCaseDetails.class));
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
    }


    @Test
    public void shouldUpdateCaveatForm() {
        shouldUpdateForm(caveatForm, caveatCaseDetails, CAVEAT_IDENTIFIER);
    }

    @Test
    public void shouldUpdateIntestacyForm() {
        shouldUpdateForm(intestacyForm, intestacyCaseDetails, EMAIL_ADDRESS);
    }

    private void shouldUpdateForm(Form form, ProbateCaseDetails caseDetails, String identifier) {
//        when(submitServiceApi.update(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
//            eq(identifier), any(ProbateCaseDetails.class))).thenReturn(new SubmitResult(caseDetails, null));
//
//        Form formResponse = submitService.update(identifier, form);
//
//        assertThat(formResponse, is(form));
//        verify(submitServiceApi, times(1)).update(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
//            eq(identifier), any(ProbateCaseDetails.class));
//        verify(securityUtils, times(1)).getAuthorisation();
//        verify(securityUtils, times(1)).getServiceAuthorisation();
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorOnSubmitIfEmailAddressDoesNotMatchForm() {
        //TODO Remove CAST
        ((IntestacyForm) intestacyForm).getApplicant().setEmail("test@Test.com");

        submitService.submit(EMAIL_ADDRESS, intestacyForm);
    }

    @Test
    public void shouldUpdateIntestacyPayments() {
        when(submitServiceApi.getCase(anyString(), anyString(),
            anyString(), anyString())).thenReturn(caveatCaseDetails);;
        shouldUpdatePayments(intestacyForm, intestacyCaseDetails);
        verify(backOfficeService, never()).sendNotification(intestacyCaseDetails);
    }

    @Test
    public void shouldUpdateCaveatPaymentsAndSendNotification() {
        when(submitServiceApi.getCase(anyString(), anyString(),
            anyString(), anyString())).thenReturn(caveatCaseDetails);

        caveatCaseDetails.getCaseInfo().setState(CaseState.CAVEAT_RAISED.getName());
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
            .state(CaseState.PA_APP_CREATED.getName())
            .build())
            .caseData(CaveatData.builder().build())
            .build();

        when(submitServiceApi.updatePaymentsByCaseId(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), eq(caseId), eq(ProbatePaymentDetails.builder()
            .payment(casePayment)
            .build()))).thenReturn(probateCaseDetails);

        ProbateCaseDetails probateCaseDetailsResult = submitService.updatePaymentsByCaseId(caseId, casePayment);
        assertThat(probateCaseDetails, equalTo(probateCaseDetailsResult));
        verify(submitServiceApi).updatePaymentsByCaseId(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), eq(caseId), eq(ProbatePaymentDetails.builder()
            .payment(casePayment)
            .build()));
        verify(backOfficeService, never()).sendNotification(caveatCaseDetails);
    }

    @Test
    public void shouldUpdatePaymentsAndSendNotification() {
        String caseId = "234324";
        CasePayment casePayment = CasePayment.builder().build();
        ProbateCaseDetails probateCaseDetails = ProbateCaseDetails.builder().caseInfo(CaseInfo.builder()
            .state(CaseState.CAVEAT_RAISED.getName())
            .build())
            .caseData(CaveatData.builder().build())
            .build();

        when(submitServiceApi.updatePaymentsByCaseId(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), eq(caseId), eq(ProbatePaymentDetails.builder()
            .payment(casePayment)
            .build()))).thenReturn(probateCaseDetails);

        ProbateCaseDetails probateCaseDetailsResult = submitService.updatePaymentsByCaseId(caseId, casePayment);
        assertThat(probateCaseDetails, equalTo(probateCaseDetailsResult));
        verify(submitServiceApi).updatePaymentsByCaseId(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), eq(caseId), eq(ProbatePaymentDetails.builder()
            .payment(casePayment)
            .build()));
    }
}
