package uk.gov.hmcts.probate.core.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.probate.client.backoffice.BackOfficeApi;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCallbackRequest;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCaveatData;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCaveatResponse;
import uk.gov.hmcts.probate.model.backoffice.GrantDelayedResponse;
import uk.gov.hmcts.reform.probate.model.cases.CaseInfo;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
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

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenCaseTypeIsGrantOfRepresentation() {
        backOfficeService.sendNotification(ProbateCaseDetails.builder().caseData(GrantOfRepresentationData.builder().build()).build());
    }

    @Test
    public void shouldInitiateGrantDelayedNotification() {
        String date = "someDate";
        GrantDelayedResponse responseBody = GrantDelayedResponse.builder().delayResponseData(Arrays.asList("case1", "case2")).build();
        Mockito.when(backOfficeApi.initiateGrantDelayedNotification(securityUtils.getAuthorisation(), securityUtils.getServiceAuthorisation(),
            date)).thenReturn(responseBody);

        GrantDelayedResponse response = backOfficeService.initiateGrantDelayedNotification(date);

        Assert.assertThat(response.getDelayResponseData().size(), equalTo(2));
        verify(backOfficeApi).initiateGrantDelayedNotification(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), eq(date));
    }
}
