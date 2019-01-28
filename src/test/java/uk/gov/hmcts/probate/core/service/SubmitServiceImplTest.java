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
import uk.gov.hmcts.probate.client.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.mapper.FormMapper;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.cases.CaseInfo;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.ProbatePaymentDetails;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.forms.Form;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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


    private Map<ProbateType, FormMapper> mappers;

    @Mock
    private SubmitServiceApi submitServiceApi;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private FormMapper formMapper;

    private SubmitServiceImpl submitService;

    private IntestacyForm form;

    private ProbateCaseDetails probateCaseDetails;

    private CaseData caseData;

    private CaseInfo caseInfo;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        IdentifierConfiguration identifierConfiguration = new IdentifierConfiguration();

        mappers = ImmutableMap.<ProbateType, FormMapper>builder()
            .put(ProbateType.INTESTACY, formMapper)
            .build();
        submitService = new SubmitServiceImpl(mappers, submitServiceApi, securityUtils,
            identifierConfiguration.formIdentifierFunctionMap());
        String formStr = TestUtils.getJSONFromFile("intestacyForm.json");
        form = objectMapper.readValue(formStr, IntestacyForm.class);
        CasePayment casePayment = new CasePayment();
        caseData = GrantOfRepresentationData.builder().payments(
            Lists.newArrayList(CollectionMember.<CasePayment>builder().value(casePayment).build())).build();
        caseInfo = CaseInfo.builder().state(STATE).caseId(CASE_ID).build();
        probateCaseDetails = ProbateCaseDetails.builder().caseData(caseData).caseInfo(caseInfo).build();
        when(securityUtils.getAuthorisation()).thenReturn(AUTHORIZATION);
        when(securityUtils.getServiceAuthorisation()).thenReturn(SERVICE_AUTHORIZATION);
        when(formMapper.toCaseData(form)).thenReturn(caseData);
        when(formMapper.fromCaseData(caseData)).thenReturn(form);
    }

    @Test
    public void shouldGetCase() {
        when(submitServiceApi.getCase(AUTHORIZATION, SERVICE_AUTHORIZATION,
            EMAIL_ADDRESS, CaseType.GRANT_OF_REPRESENTATION.getName())).thenReturn(probateCaseDetails);

        Form formResponse = submitService.getCase(EMAIL_ADDRESS, ProbateType.INTESTACY);

        assertThat(formResponse, is(form));
        verify(submitServiceApi, times(1)).getCase(AUTHORIZATION, SERVICE_AUTHORIZATION,
            EMAIL_ADDRESS, CaseType.GRANT_OF_REPRESENTATION.getName());
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
    }

    @Test
    public void shouldSaveDraft() {
        when(submitServiceApi.saveDraft(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(EMAIL_ADDRESS), any(ProbateCaseDetails.class))).thenReturn(probateCaseDetails);

        Form formResponse = submitService.saveDraft(EMAIL_ADDRESS, form);

        assertThat(formResponse, is(form));
        verify(submitServiceApi, times(1)).saveDraft(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(EMAIL_ADDRESS), any(ProbateCaseDetails.class));
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorOnSaveDraftIfEmailAddressDoesNotMatchForm() {
        form.getApplicant().setEmail("test@Test.com");

        submitService.saveDraft(EMAIL_ADDRESS, form);
    }

    @Test
    public void shouldSubmit() {
        when(submitServiceApi.submit(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(EMAIL_ADDRESS), any(ProbateCaseDetails.class))).thenReturn(probateCaseDetails);

        Form formResponse = submitService.submit(EMAIL_ADDRESS, form);

        assertThat(formResponse, is(form));
        verify(submitServiceApi, times(1)).submit(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(EMAIL_ADDRESS), any(ProbateCaseDetails.class));
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorOnSubmitIfEmailAddressDoesNotMatchForm() {
        form.getApplicant().setEmail("test@Test.com");

        submitService.submit(EMAIL_ADDRESS, form);
    }

    @Test
    public void shouldUpdatePayments() {
        when(submitServiceApi.updatePayments(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(EMAIL_ADDRESS), any(ProbatePaymentDetails.class))).thenReturn(probateCaseDetails);

        Form formResponse = submitService.updatePayments(EMAIL_ADDRESS, form);

        assertThat(formResponse, is(form));
        verify(submitServiceApi, times(1)).updatePayments(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION),
            eq(EMAIL_ADDRESS), any(ProbatePaymentDetails.class));
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorOnUpdatePaymentsIfNoPaymentsOnForm() {
        form.setPayments(null);

        submitService.updatePayments(EMAIL_ADDRESS, form);
    }
}