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
import uk.gov.hmcts.probate.model.backoffice.GrantScheduleResponse;
import uk.gov.hmcts.probate.service.BusinessService;
import uk.gov.hmcts.reform.probate.model.cases.CaseInfo;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.notification.ApplicationReceivedDetails;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
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
    private BusinessService businessService;

    @Mock
    private SecurityUtils securityUtils;

    private BackOfficeCaveatResponse backOfficeCaveatResponse;

    @Mock
    private ResponseEntity<String> responseEntity;
    
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
        backOfficeService.sendNotification(ProbateCaseDetails.builder().caseInfo(CaseInfo.builder().caseId("123132").build()).caseData(GrantOfRepresentationData.builder().build()).build());

        verify(businessService).notifyApplicationReceived(any(ApplicationReceivedDetails.class));
    }

    @Test
    public void shouldInitiateHmrcExtract() {
        when(backOfficeApi.initiateHmrcExtract(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), any(String.class), any(String.class)))
            .thenReturn(responseEntity);

        assertThat(backOfficeService.initiateHmrcExtract("2020-02-27", "2020-02-28")).isEqualTo(responseEntity);
    }

    @Test
    public void shouldInitiateIronMountainExtract() {
        when(backOfficeApi.initiateIronMountainExtract(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), any(String.class)))
            .thenReturn(responseEntity);

        assertThat(backOfficeService.initiateIronMountainExtract("2020-02-27")).isEqualTo(responseEntity);
    }

    @Test
    public void shouldInitiateExelaExtract() {
        when(backOfficeApi.initiateExelaExtract(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), any(String.class)))
            .thenReturn(responseEntity);

        assertThat(backOfficeService.initiateExelaExtract("2020-02-27")).isEqualTo(responseEntity);
    }

    @Test
    public void shouldInitiateGrantDelayedNotification() {
        String date = "someDate";
        GrantScheduleResponse responseBody = GrantScheduleResponse.builder().scheduleResponseData(Arrays.asList("case1", "case2")).build();
        Mockito.when(backOfficeApi.initiateGrantDelayedNotification(securityUtils.getAuthorisation(), securityUtils.getServiceAuthorisation(),
            date)).thenReturn(responseBody);

        GrantScheduleResponse response = backOfficeService.initiateGrantDelayedNotification(date);

        Assert.assertThat(response.getScheduleResponseData().size(), equalTo(2));
        verify(backOfficeApi).initiateGrantDelayedNotification(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), eq(date));
    }
    @Test
    public void shouldInitiateGrantAwaitingDocumentsNotification() {
        String date = "someDate";
        GrantScheduleResponse responseBody = GrantScheduleResponse.builder().scheduleResponseData(Arrays.asList("case1", "case2")).build();
        Mockito.when(backOfficeApi.initiateGrantAwaitingDocumentsNotification(securityUtils.getAuthorisation(), securityUtils.getServiceAuthorisation(),
            date)).thenReturn(responseBody);

        GrantScheduleResponse response = backOfficeService.initiateGrantAwaitingDocumentsNotification(date);

        Assert.assertThat(response.getScheduleResponseData().size(), equalTo(2));
        verify(backOfficeApi).initiateGrantAwaitingDocumentsNotification(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), eq(date));
    }
}
