package uk.gov.hmcts.probate.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import uk.gov.hmcts.probate.TestUtils;
import uk.gov.hmcts.probate.client.business.BusinessServiceApi;
import uk.gov.hmcts.probate.client.business.BusinessServiceDocumentsApi;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.mapper.ExecutorApplyingToInvitationMapper;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BusinessServiceImplTest {

    private static final String SERVICE_AUTHORIZATION = "SERVICEAUTH1234567";
    private static final String AUTHORIZATION = "AUTH1234567";
    private final String phoneNumber = "phoneNumber";
    private final String leadExecutorName = "leadExecutorName";
    private final String formdataId = "12345";
    private final String sessionId = "sessionId";
    private final String invitationId = "54321";
    private final String emailaddress = "emailaddress";

    @Mock
    private BusinessServiceApi businessServiceApi;

    @Mock
    private BusinessServiceDocumentsApi businessServiceDocumentsApi;

    @Mock
    private SubmitServiceApi submitServiceApi;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private ExecutorApplyingToInvitationMapper mockExecutorApplyingToInvitationMapper;

    @Mock
    private ProbateCaseDetails mockProbateCaseDetails;

    @Mock
    GrantOfRepresentationData mockGrantOfRepresentationData;

    private BusinessServiceImpl businessService;

    private byte[] pdfExample;

    private ObjectMapper objectMapper;


    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();

        when(securityUtils.getAuthorisation()).thenReturn(AUTHORIZATION);
        when(securityUtils.getServiceAuthorisation()).thenReturn(SERVICE_AUTHORIZATION);

        when(submitServiceApi.getCase(AUTHORIZATION, SERVICE_AUTHORIZATION,
            formdataId, ProbateType.PA.getCaseType().name())).thenReturn(mockProbateCaseDetails);
        when(mockProbateCaseDetails.getCaseData()).thenReturn(mockGrantOfRepresentationData);

        pdfExample = new byte[10];
        businessService = new BusinessServiceImpl(businessServiceApi, businessServiceDocumentsApi,
            submitServiceApi, securityUtils, mockExecutorApplyingToInvitationMapper);
    }

    @Test
    public void generateCheckAnswersSummaryPdf() throws Exception {

        String checkAnswersSummaryJson = TestUtils.getJSONFromFile("businessDocuments/validCheckAnswersSummary.json");
        CheckAnswersSummary checkAnswersSummary = objectMapper.readValue(checkAnswersSummaryJson, CheckAnswersSummary.class);

        when(businessServiceApi.generateCheckAnswersSummaryPdf(AUTHORIZATION, SERVICE_AUTHORIZATION, checkAnswersSummary)).thenReturn(pdfExample);

        businessService.generateCheckAnswersSummaryPdf(checkAnswersSummary);
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
        verify(businessServiceApi, times(1)).generateCheckAnswersSummaryPdf(AUTHORIZATION, SERVICE_AUTHORIZATION, checkAnswersSummary);
    }


    @Test
    public void generateLegalDeclarationPdf() throws Exception {

        String legalDeclarationJson = TestUtils.getJSONFromFile("businessDocuments/validLegalDeclaration.json");
        LegalDeclaration legalDeclaration = objectMapper.readValue(legalDeclarationJson, LegalDeclaration.class);

        when(businessServiceApi.generateLegalDeclarationPDF(AUTHORIZATION, SERVICE_AUTHORIZATION, legalDeclaration)).thenReturn(pdfExample);

        businessService.generateLegalDeclarationPdf(legalDeclaration);
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
        verify(businessServiceApi, times(1)).generateLegalDeclarationPDF(AUTHORIZATION, SERVICE_AUTHORIZATION, legalDeclaration);
    }


    @Test
    public void generateBulkScanCoversheetPdf() throws Exception {

        String bulkScanCoversheetJson = TestUtils.getJSONFromFile("businessDocuments/validBulkScanCoverSheet.json");
        BulkScanCoverSheet bulkScanCoverSheet = objectMapper.readValue(bulkScanCoversheetJson, BulkScanCoverSheet.class);

        when(businessServiceApi.generateBulkScanCoverSheetPDF(AUTHORIZATION, SERVICE_AUTHORIZATION, bulkScanCoverSheet)).thenReturn(pdfExample);

        businessService.generateBulkScanCoverSheetPdf(bulkScanCoverSheet);
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
        verify(businessServiceApi, times(1)).generateBulkScanCoverSheetPDF(AUTHORIZATION, SERVICE_AUTHORIZATION, bulkScanCoverSheet);
    }


    @Test
    public void shouldSendInvitationAndUpdateProbateCaseDetails() {

        Invitation invitation = getInvitation(formdataId);

        when(businessServiceApi.invite(invitation, sessionId)).thenReturn(invitationId);

        String result = businessService.sendInvitation(invitation, sessionId);

        verify(businessServiceApi).invite(invitation, sessionId);
        verifyGetCaseCalls();
        verify(mockGrantOfRepresentationData).setInvitationDetailsForExecutorApplying(invitation.getEmail(), invitationId, invitation.getLeadExecutorName());
        verify(submitServiceApi).saveCase(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId, mockProbateCaseDetails);

        Assert.assertThat(result, Matchers.equalTo(invitationId));
    }

    @Test
    public void shouldResendInvitation() {
        Invitation invitation = getInvitation(formdataId);
        String result = businessService.resendInvitation(invitationId, invitation, sessionId);
        verify(businessServiceApi).invite(invitationId, invitation, sessionId);

    }

    @Test
    public void shouldDetermineIfAllInviteesHaveAgreedFromCase() {

        when(mockGrantOfRepresentationData.haveAllExecutorsAgreed()).thenReturn(Boolean.TRUE);
        Boolean result = businessService.haveAllIniviteesAgreed(formdataId);
        Assert.assertThat(result, Matchers.equalTo(Boolean.TRUE));
    }

    @Test
    public void shouldUpdateContactDetailsOnCase() {

        businessService.updateContactDetails(formdataId, getInvitation(formdataId));
        verifyGetCaseCalls();
        verify(mockGrantOfRepresentationData).updateInvitationContactDetailsForExecutorApplying(invitationId, emailaddress, phoneNumber);
        verify(submitServiceApi).saveCase(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId, mockProbateCaseDetails);

    }

    @Test
    public void shouldSetInviteAgreedOnCase() {

        Invitation invitation = getInvitation(formdataId);
        businessService.inviteAgreed(formdataId, invitation);
        verifyGetCaseCalls();
        verify(mockGrantOfRepresentationData).setInvitationAgreedFlagForExecutorApplying(invitationId, invitation.getAgreed());
        verify(submitServiceApi).saveCase(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId, mockProbateCaseDetails);

    }

    @Test
    public void shouldResetInviteAgreedOnCase() {

        businessService.resetAgreedFlags(formdataId);
        verifyGetCaseCalls();
        verify(mockGrantOfRepresentationData).resetExecutorsApplyingAgreedFlags();
        verify(submitServiceApi).saveCase(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId, mockProbateCaseDetails);

    }

    @Test
    public void shouldDeleteInviteOnCase() {

        Invitation invitation = getInvitation(formdataId);
        businessService.deleteInvite(formdataId, invitation);
        verifyGetCaseCalls();
        verify(mockGrantOfRepresentationData).deleteInvitation(invitationId);
        verify(submitServiceApi).saveCase(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId, mockProbateCaseDetails);

    }

    @Test
    public void shouldGetPinNumber() {
        businessService.getPinNumber(phoneNumber, sessionId);
        verify(businessServiceApi).pinNumber(phoneNumber, sessionId);
    }

    @Test
    public void shouldGetCaseByInvitationId() {

        when(submitServiceApi.getCaseByInvitationId(AUTHORIZATION, SERVICE_AUTHORIZATION,
            invitationId, CaseType.GRANT_OF_REPRESENTATION.name())).thenReturn(mockProbateCaseDetails);
        when(mockProbateCaseDetails.getCaseData()).thenReturn(mockGrantOfRepresentationData);
        ExecutorApplying mockExecutorApplying = Mockito.mock(ExecutorApplying.class);
        when(mockGrantOfRepresentationData.getExecutorApplyingByInviteId(invitationId))
            .thenReturn(mockExecutorApplying);
        when(mockExecutorApplyingToInvitationMapper.map(mockExecutorApplying)).thenReturn(new Invitation());

        Invitation invitation = getInvitation(formdataId);
        businessService.getInviteData(invitationId);
        verify(submitServiceApi).getCaseByInvitationId(AUTHORIZATION, SERVICE_AUTHORIZATION,
            invitationId, CaseType.GRANT_OF_REPRESENTATION.name());
        verify(mockProbateCaseDetails).getCaseData();
        verify(mockGrantOfRepresentationData).getExecutorApplyingByInviteId(invitationId);
        verify(mockExecutorApplyingToInvitationMapper).map(mockExecutorApplying);

    }

    private void verifyGetCaseCalls() {
        verify(submitServiceApi).getCase(AUTHORIZATION, SERVICE_AUTHORIZATION,
            formdataId, ProbateType.PA.getCaseType().name());
        verify(mockProbateCaseDetails).getCaseData();
    }

    private Invitation getInvitation(String formdataId) {

        return Invitation.builder()
            .inviteId(invitationId)
            .email(emailaddress)
            .phoneNumber(phoneNumber)
            .leadExecutorName(leadExecutorName)
            .formdataId(formdataId)
            .agreed(Boolean.TRUE)
            .build();
    }

    @Test
    public void shouldUploadSuccessfully() {
        MockMultipartFile file = new MockMultipartFile("file", "orig", MediaType.IMAGE_PNG_VALUE, "bar".getBytes());
        String authorizationToken = "AUTHTOKEN12345";
        String userId = "USERID12345";

        businessService.uploadDocument(authorizationToken, userId, Lists.newArrayList(file));

        verify(businessServiceDocumentsApi).uploadDocument(userId, authorizationToken, file);
    }

    @Test
    public void shouldDeleteSuccessfully() {
        String documentId = "DOC12345";
        String userId = "USERID12345";

        businessService.delete(userId, documentId);

        verify(businessServiceDocumentsApi).delete(userId, documentId);
    }
}
