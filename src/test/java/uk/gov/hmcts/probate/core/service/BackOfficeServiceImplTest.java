package uk.gov.hmcts.probate.core.service;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.mock.web.MockMultipartFile;
import uk.gov.hmcts.probate.client.backoffice.BackOfficeApi;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCallbackRequest;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCaveatData;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCaveatResponse;
import uk.gov.hmcts.probate.model.backoffice.GrantScheduleResponse;
import uk.gov.hmcts.reform.probate.model.ProbateDocument;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.cases.CaseInfo;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
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

    @Mock
    private ResponseEntity<String> responseEntity;

    @InjectMocks
    private BackOfficeServiceImpl backOfficeService;

    @BeforeEach
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
        when(backOfficeApi
            .raiseCaveat(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), any(BackOfficeCallbackRequest.class)))
            .thenReturn(backOfficeCaveatResponse);

        CaveatData caveatData = CaveatData.builder().build();
        backOfficeService.sendNotification(
            ProbateCaseDetails.builder().caseInfo(CaseInfo.builder().caseId("123132").build()).caseData(caveatData)
                .build());

        verify(backOfficeApi)
            .raiseCaveat(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), any(BackOfficeCallbackRequest.class));
    }

    @Test
    public void shouldSendNotificationWhenCaseTypeIsGrantOfrepresentation() {
        ProbateDocument probateDocument = ProbateDocument.builder().build();
        when(backOfficeApi
            .applicationReceived(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), any(BackOfficeCallbackRequest.class)))
            .thenReturn(probateDocument);

        GrantOfRepresentationData grantOfRepresentationData = GrantOfRepresentationData.builder().build();
        backOfficeService.sendNotification(
            ProbateCaseDetails.builder().caseInfo(CaseInfo.builder().caseId("123132").build())
                .caseData(grantOfRepresentationData).build());

        verify(backOfficeApi)
            .applicationReceived(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), any(BackOfficeCallbackRequest.class));
    }

    @Test
    public void shouldNotSendNotificationWhenCaseTypeIsGrantOfrepresentation() {
        when(backOfficeApi
            .applicationReceived(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), any(BackOfficeCallbackRequest.class)))
            .thenReturn(null);

        GrantOfRepresentationData grantOfRepresentationData = GrantOfRepresentationData.builder().build();
        CaseData caseData = backOfficeService.sendNotification(
            ProbateCaseDetails.builder().caseInfo(CaseInfo.builder().caseId("123132").build())
                .caseData(grantOfRepresentationData).build());

        assertThat(((GrantOfRepresentationData) caseData).getProbateNotificationsGenerated()).isNull();
        verify(backOfficeApi)
            .applicationReceived(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), any(BackOfficeCallbackRequest.class));
    }

    @Test
    public void shouldInitiateHmrcExtract() {
        when(backOfficeApi
            .initiateHmrcExtract(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), any(String.class), any(String.class)))
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
    public void shouldInitiateExelaExtractDateRange() {
        when(backOfficeApi
            .initiateExelaExtractDateRange(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), any(String.class),
                any(String.class)))
            .thenReturn(responseEntity);

        assertThat(backOfficeService.initiateExelaExtractDateRange("2020-02-27", "2020-02-29"))
            .isEqualTo(responseEntity);
    }

    @Test
    public void shouldInitiateSmeeAndFordExtractDateRange() {
        when(backOfficeApi
            .initiateSmeeAndFordExtractDateRange(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), any(String.class),
                any(String.class)))
            .thenReturn(responseEntity);

        assertThat(backOfficeService.initiateSmeeAndFordExtract("2020-02-27", "2020-02-29"))
            .isEqualTo(responseEntity);
    }

    @Test
    void shouldMakeDormant() {
        when(backOfficeApi
                .makeDormant(AUTHORIZATION, SERVICE_AUTHORIZATION, "2022-01-27"))
                .thenReturn(responseEntity);

        assertThat(backOfficeService.makeDormant("2022-01-27"))
                .isEqualTo(responseEntity);
    }

    @Test
    public void shouldInitiateGrantDelayedNotification() {
        String date = "someDate";
        GrantScheduleResponse responseBody =
            GrantScheduleResponse.builder().scheduleResponseData(Arrays.asList("case1", "case2")).build();
        Mockito.when(backOfficeApi.initiateGrantDelayedNotification("Bearer "
                + securityUtils.getAuthorisation(),
            "Bearer " + securityUtils.getServiceAuthorisation(),
            date)).thenReturn(responseBody);

        GrantScheduleResponse response = backOfficeService.initiateGrantDelayedNotification(date);

        assertEquals(response.getScheduleResponseData().size(), 2);
        verify(backOfficeApi)
            .initiateGrantDelayedNotification(eq("Bearer " + AUTHORIZATION), eq("Bearer "
                    + SERVICE_AUTHORIZATION),
                eq(date));
    }

    @Test
    public void shouldInitiateGrantAwaitingDocumentsNotification() {
        String date = "someDate";
        GrantScheduleResponse responseBody =
            GrantScheduleResponse.builder().scheduleResponseData(Arrays.asList("case1", "case2")).build();
        Mockito.when(backOfficeApi
            .initiateGrantAwaitingDocumentsNotification("Bearer " + securityUtils.getAuthorisation(),
                "Bearer " + securityUtils.getServiceAuthorisation(),
                date)).thenReturn(responseBody);

        GrantScheduleResponse response = backOfficeService.initiateGrantAwaitingDocumentsNotification(date);

        assertEquals(response.getScheduleResponseData().size(), 2);
        verify(backOfficeApi).initiateGrantAwaitingDocumentsNotification(eq("Bearer " + AUTHORIZATION),
            eq("Bearer " + SERVICE_AUTHORIZATION), eq(date));
    }

    @Test
    public void shouldUploadSuccessfully() {
        MockMultipartFile file = new MockMultipartFile("file", "orig",
            MediaType.IMAGE_PNG_VALUE, "bar".getBytes());
        String authorizationToken = "AUTHTOKEN12345";
        String serviceToken = "SERVICETOKEN67890";

        when(securityUtils.getServiceAuthorisation()).thenReturn(serviceToken);
        backOfficeService.uploadDocument(authorizationToken, Lists.newArrayList(file));

        verify(backOfficeApi).uploadDocument(authorizationToken, serviceToken, file);
    }

    @Test()
    public void shouldThrowExceptoinIfFilesAreEmpty() {
        String authorizationToken = "AUTHTOKEN12345";
        assertThrows(IllegalArgumentException.class, () -> {
            backOfficeService.uploadDocument(authorizationToken, Lists.newArrayList());
        });
    }

}
