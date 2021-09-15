package uk.gov.hmcts.probate.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
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
import uk.gov.hmcts.reform.probate.model.cases.CaseInfo;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;

import java.util.Arrays;
import java.util.List;

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
    private final String firstName = "Rob";
    private final String lastName = "Beckett";
    private final String executorName = "Sally Matthews";
    private static final String EVENT_DESCRIPTION = "event update case data";

    @Mock
    GrantOfRepresentationData mockGrantOfRepresentationData;
    @Mock
    CaseInfo mockCaseInfo;
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
        when(mockProbateCaseDetails.getCaseInfo()).thenReturn(mockCaseInfo);
        when(mockCaseInfo.getCaseId()).thenReturn("123456789101112");

        pdfExample = new byte[10];
        businessService = new BusinessServiceImpl(businessServiceApi, businessServiceDocumentsApi,
            submitServiceApi, securityUtils, mockExecutorApplyingToInvitationMapper);
    }

    @Test
    public void generateCheckAnswersSummaryPdf() throws Exception {

        String checkAnswersSummaryJson = TestUtils
            .getJsonFromFile("businessDocuments/validCheckAnswersSummary.json");
        CheckAnswersSummary checkAnswersSummary =
            objectMapper.readValue(checkAnswersSummaryJson, CheckAnswersSummary.class);

        when(businessServiceApi
            .generateCheckAnswersSummaryPdf(AUTHORIZATION, SERVICE_AUTHORIZATION, checkAnswersSummary))
            .thenReturn(pdfExample);

        businessService.generateCheckAnswersSummaryPdf(checkAnswersSummary);
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
        verify(businessServiceApi, times(1))
            .generateCheckAnswersSummaryPdf(AUTHORIZATION, SERVICE_AUTHORIZATION, checkAnswersSummary);
    }


    @Test
    public void generateLegalDeclarationPdf() throws Exception {

        String legalDeclarationJson = TestUtils
            .getJsonFromFile("businessDocuments/validLegalDeclaration.json");
        LegalDeclaration legalDeclaration = objectMapper.readValue(legalDeclarationJson, LegalDeclaration.class);

        when(businessServiceApi.generateLegalDeclarationPDF(AUTHORIZATION, SERVICE_AUTHORIZATION, legalDeclaration))
            .thenReturn(pdfExample);

        businessService.generateLegalDeclarationPdf(legalDeclaration);
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
        verify(businessServiceApi, times(1))
            .generateLegalDeclarationPDF(AUTHORIZATION, SERVICE_AUTHORIZATION, legalDeclaration);
    }


    @Test
    public void generateBulkScanCoversheetPdf() throws Exception {

        String bulkScanCoversheetJson = TestUtils
            .getJsonFromFile("businessDocuments/validBulkScanCoverSheet.json");
        BulkScanCoverSheet bulkScanCoverSheet =
            objectMapper.readValue(bulkScanCoversheetJson, BulkScanCoverSheet.class);

        when(businessServiceApi.generateBulkScanCoverSheetPDF(AUTHORIZATION, SERVICE_AUTHORIZATION, bulkScanCoverSheet))
            .thenReturn(pdfExample);

        businessService.generateBulkScanCoverSheetPdf(bulkScanCoverSheet);
        verify(securityUtils, times(1)).getAuthorisation();
        verify(securityUtils, times(1)).getServiceAuthorisation();
        verify(businessServiceApi, times(1))
            .generateBulkScanCoverSheetPDF(AUTHORIZATION, SERVICE_AUTHORIZATION, bulkScanCoverSheet);
    }


    @Test
    public void shouldSendInvitationAndUpdateProbateCaseDetails() {

        Invitation invitation = getInvitation(formdataId);

        when(businessServiceApi.invite(invitation, sessionId)).thenReturn(invitationId);

        String result = businessService.sendInvitation(invitation, sessionId);

        verify(businessServiceApi).invite(invitation, sessionId);
        verifyGetCaseCalls();
        Assert.assertThat(result, Matchers.equalTo(invitationId));
        verify(mockGrantOfRepresentationData).setInvitationDetailsForExecutorApplying(invitation.getEmail(),
            invitationId, invitation.getLeadExecutorName(), executorName);
        verify(submitServiceApi).saveCase(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId, "event update case data",
            mockProbateCaseDetails);

    }

    @Test
    public void shouldSendInvitationsAndUpdateProbateCaseDetails() {

        Invitation newInvitation = getInvitation(formdataId);
        newInvitation.setInviteId(null);
        Invitation resendInvitation =
            Invitation.builder().inviteId("inviteId").lastName("RsLastName").firstName("RsFirstName")
                .leadExecutorName("RsLeadExecName").email("rsEmailAddress").formdataId(formdataId).build();

        when(businessServiceApi.invite(newInvitation, sessionId)).thenReturn(invitationId);
        when(businessServiceApi.invite(resendInvitation.getInviteId(), resendInvitation, sessionId))
            .thenReturn(resendInvitation.getInviteId());

        List<Invitation> results = businessService
            .sendInvitations(Lists.newArrayList(newInvitation, resendInvitation), sessionId, Boolean.FALSE);

        verify(businessServiceApi).invite(newInvitation, sessionId);
        verify(businessServiceApi).invite(resendInvitation.getInviteId(), resendInvitation, sessionId);
        verifyGetCaseCalls();
        verify(mockGrantOfRepresentationData)
            .setInvitationDetailsForExecutorApplying(newInvitation.getEmail(), invitationId,
                newInvitation.getLeadExecutorName(), executorName);
        verify(submitServiceApi).saveCase(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId,
            EVENT_DESCRIPTION, mockProbateCaseDetails);

        Assert.assertThat(newInvitation.getInviteId(), Matchers.equalTo(invitationId));

    }

    @Test
    public void shouldSendBilingualInvitationsAndUpdateProbateCaseDetails() {

        Invitation newInvitation = getInvitation(formdataId);
        newInvitation.setInviteId(null);
        Invitation resendInvitation =
            Invitation.builder().inviteId("inviteId").lastName("RsLastName").firstName("RsFirstName")
                .leadExecutorName("RsLeadExecName").email("rsEmailAddress").formdataId(formdataId).build();

        when(businessServiceApi.inviteBilingual(newInvitation, sessionId)).thenReturn(invitationId);
        when(businessServiceApi.inviteBilingual(resendInvitation.getInviteId(), resendInvitation, sessionId))
            .thenReturn(resendInvitation.getInviteId());

        List<Invitation> results = businessService
            .sendInvitations(Lists.newArrayList(newInvitation, resendInvitation), sessionId, Boolean.TRUE);

        verify(businessServiceApi).inviteBilingual(newInvitation, sessionId);
        verify(businessServiceApi).inviteBilingual(resendInvitation.getInviteId(), resendInvitation, sessionId);
        verifyGetCaseCalls();
        verify(mockGrantOfRepresentationData)
            .setInvitationDetailsForExecutorApplying(newInvitation.getEmail(), invitationId,
                newInvitation.getLeadExecutorName(), executorName);
        verify(submitServiceApi).saveCase(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId,
            EVENT_DESCRIPTION, mockProbateCaseDetails);

        Assert.assertThat(newInvitation.getInviteId(), Matchers.equalTo(invitationId));

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
        verify(mockGrantOfRepresentationData)
            .updateInvitationContactDetailsForExecutorApplying(invitationId, emailaddress, phoneNumber);
        verify(submitServiceApi).saveCase(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId,
            EVENT_DESCRIPTION, mockProbateCaseDetails);

    }

    @Test
    public void shouldSetInviteAgreedOnCase() {

        Invitation invitation = getInvitation(formdataId);
        businessService.inviteAgreed(formdataId, invitation);
        verifyGetCaseCalls();
        verify(mockGrantOfRepresentationData)
            .setInvitationAgreedFlagForExecutorApplying(invitationId, invitation.getAgreed());
        verify(submitServiceApi)
            .updateCaseAsCaseWorker(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId, mockProbateCaseDetails);

    }

    @Test
    public void shouldResetInviteAgreedOnCase() {

        businessService.resetAgreedFlags(formdataId);
        verifyGetCaseCalls();
        verify(mockGrantOfRepresentationData).resetExecutorsApplyingAgreedFlags();
        verify(submitServiceApi).saveCase(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId,
            EVENT_DESCRIPTION, mockProbateCaseDetails);

    }

    @Test
    public void shouldDeleteInviteOnCase() {

        Invitation invitation = getInvitation(formdataId);
        businessService.deleteInvite(formdataId, invitation);
        verifyGetCaseCalls();
        verify(mockGrantOfRepresentationData).deleteInvitation(invitationId);
        verify(submitServiceApi).saveCase(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId,
            EVENT_DESCRIPTION, mockProbateCaseDetails);
    }

    @Test
    public void shouldGetPinNumber() {

        businessService.getPinNumber(phoneNumber, sessionId, Boolean.FALSE);
        verify(businessServiceApi).pinNumber(phoneNumber, sessionId);

        businessService.getPinNumber(phoneNumber, sessionId, Boolean.TRUE);
        verify(businessServiceApi).pinNumberBilingual(phoneNumber, sessionId);
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
        businessService.getInviteData(invitation.getInviteId());
        verify(submitServiceApi).getCaseByInvitationId(AUTHORIZATION, SERVICE_AUTHORIZATION,
            invitationId, CaseType.GRANT_OF_REPRESENTATION.name());
        verify(mockProbateCaseDetails).getCaseData();
        verify(mockGrantOfRepresentationData).getExecutorApplyingByInviteId(invitationId);
        verify(mockExecutorApplyingToInvitationMapper).map(mockExecutorApplying);

    }

    @Test
    public void shouldGetAllInviteData() {
        CollectionMember<ExecutorApplying> mockExecutorApplying = CollectionMember
            .<ExecutorApplying>builder()
            .value(ExecutorApplying.builder()
                .applyingExecutorApplicant(Boolean.TRUE)
                .build())
            .build();
        CollectionMember<ExecutorApplying> mockExecutorApplying2 = CollectionMember
            .<ExecutorApplying>builder()
            .value(ExecutorApplying.builder()
                .applyingExecutorApplicant(Boolean.FALSE)
                .applyingExecutorInvitationId(invitationId)
                .applyingExecutorAgreed(Boolean.TRUE)
                .build())
            .build();
        List<CollectionMember<ExecutorApplying>> mockExecutorsApplying = Arrays
            .asList(mockExecutorApplying, mockExecutorApplying2);

        when(mockProbateCaseDetails.getCaseData()).thenReturn(mockGrantOfRepresentationData);


        when(mockGrantOfRepresentationData.getExecutorsApplying())
            .thenReturn(mockExecutorsApplying);
        when(submitServiceApi.getCaseByInvitationId(AUTHORIZATION, SERVICE_AUTHORIZATION,
            invitationId, CaseType.GRANT_OF_REPRESENTATION.name())).thenReturn(mockProbateCaseDetails);
        when(mockProbateCaseDetails.getCaseData()).thenReturn(mockGrantOfRepresentationData);
        ExecutorApplying mockExecutorApplying1 = Mockito.mock(ExecutorApplying.class);
        when(mockGrantOfRepresentationData.getExecutorApplyingByInviteId(invitationId))
            .thenReturn(mockExecutorApplying1);
        when(mockExecutorApplyingToInvitationMapper.map(mockExecutorApplying1)).thenReturn(new Invitation());

        businessService.getAllInviteData(formdataId);

        verify(mockProbateCaseDetails, times(2)).getCaseData();
        verify(mockGrantOfRepresentationData).getExecutorApplyingByInviteId(invitationId);

    }

    private void verifyGetCaseCalls() {
        verify(submitServiceApi).getCase(AUTHORIZATION, SERVICE_AUTHORIZATION,
            formdataId, ProbateType.PA.getCaseType().name());
        verify(mockProbateCaseDetails).getCaseData();
    }

    private Invitation getInvitation(String formdataId) {

        return Invitation.builder()
            .inviteId(invitationId)
            .firstName(firstName)
            .lastName(lastName)
            .executorName(executorName)
            .email(emailaddress)
            .phoneNumber(phoneNumber)
            .leadExecutorName(leadExecutorName)
            .formdataId(formdataId)
            .agreed(Boolean.TRUE)
            .build();
    }

    @Test
    public void shouldUploadSuccessfully() {
        MockMultipartFile file = new MockMultipartFile("file", "orig",
            MediaType.IMAGE_PNG_VALUE, "bar".getBytes());
        String authorizationToken = "AUTHTOKEN12345";
        String userId = "USERID12345";

        businessService.uploadDocument(authorizationToken, userId, Lists.newArrayList(file));

        verify(businessServiceDocumentsApi).uploadDocument(userId, authorizationToken, file);
    }

    @Test()
    public void shouldThrowExceptoinIfFilesAreEmpty() {
        String authorizationToken = "AUTHTOKEN12345";
        String userId = "USERID12345";
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            businessService.uploadDocument(authorizationToken, userId, Lists.newArrayList());
        });
    }

    @Test
    public void shouldDeleteSuccessfully() {
        String documentId = "DOC12345";
        String userId = "USERID12345";

        businessService.delete(userId, documentId);

        verify(businessServiceDocumentsApi).delete(userId, documentId);
    }
}
