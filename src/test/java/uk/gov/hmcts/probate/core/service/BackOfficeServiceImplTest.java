package uk.gov.hmcts.probate.core.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.probate.client.backoffice.BackOfficeApi;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCallbackRequest;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCaveatData;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCaveatResponse;
import uk.gov.hmcts.reform.probate.model.cases.CaseInfo;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BackOfficeServiceImplTest {

    private static final String CASE_ID = "42343543";
    private static final String SERVICE_AUTHORIZATION = "SERVICEAUTH1234567";
    private static final String AUTHORIZATION = "AUTH1234567";
    private static final String CAVEAT_EXPIRY_DATE = "2019-12-31";
    private static final String CAVEAT_SUBMITTED_DATE = "2019-12-01";

    @Mock
    private BackOfficeApi backOfficeApi;

    @Mock
    private SecurityUtils securityUtils;

    private BackOfficeCaveatResponse backOfficeCaveatResponse;

    @InjectMocks
    private BackOfficeServiceImpl backOfficeService;

    @Before
    public void setUp() {

        backOfficeCaveatResponse = BackOfficeCaveatResponse.builder().caseData(BackOfficeCaveatData.builder()
                .applicationSubmittedDate(CAVEAT_SUBMITTED_DATE)
                .expiryDate(CAVEAT_EXPIRY_DATE)
                .build())
            .build();
        Mockito.when(securityUtils.getAuthorisation()).thenReturn(AUTHORIZATION);
        Mockito.when(securityUtils.getServiceAuthorisation()).thenReturn(SERVICE_AUTHORIZATION);
    }

    @Test
    public void shouldSendNotificationWhenCaseTypeIsCaveat() {
        when(backOfficeApi.raiseCaveat(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), any(BackOfficeCallbackRequest.class)))
            .thenReturn(backOfficeCaveatResponse);

        CaveatData caveatData = CaveatData.builder().build();
        backOfficeService.sendNotification(ProbateCaseDetails.builder().caseInfo(CaseInfo.builder().caseId("123132").build()).caseData(caveatData).build());

        verify(backOfficeApi).raiseCaveat(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), any(BackOfficeCallbackRequest.class));
    }

    @Test
    public void shouldSendNotificationsWhenCaseTypeisGrantOfRepresentation() {

        when(backOfficeApi.applicationReceived(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), any(BackOfficeCallbackRequest.class)))
                .thenReturn("Success");

        backOfficeService.sendNotification(ProbateCaseDetails.builder().caseInfo(CaseInfo.builder().caseId("123132").build()).caseData(GrantOfRepresentationData.builder().build()).build());

        verify(backOfficeApi).applicationReceived(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), any(BackOfficeCallbackRequest.class));
    }
}
