package uk.gov.hmcts.probate.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.probate.TestUtils;
import uk.gov.hmcts.probate.client.business.BusinessServiceApi;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.mapper.ExecutorApplyingToInvitationMapper;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseInfo;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.DocumentLink;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.UploadDocument;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
import uk.gov.hmcts.reform.probate.model.documents.DocumentNotification;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;
import uk.gov.hmcts.reform.probate.model.multiapplicant.ExecutorNotification;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
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
    private static final String RESPONSE_DATE_FORMAT = "yyyy-MM-dd";



    @BeforeEach
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(RESPONSE_DATE_FORMAT);

        when(securityUtils.getAuthorisation()).thenReturn(AUTHORIZATION);
        when(securityUtils.getServiceAuthorisation()).thenReturn(SERVICE_AUTHORIZATION);

        when(submitServiceApi.getCase(AUTHORIZATION, SERVICE_AUTHORIZATION,
            formdataId, ProbateType.PA.getCaseType().name())).thenReturn(mockProbateCaseDetails);
        when(mockProbateCaseDetails.getCaseData()).thenReturn(mockGrantOfRepresentationData);
        when(mockProbateCaseDetails.getCaseInfo()).thenReturn(mockCaseInfo);
        when(mockCaseInfo.getCaseId()).thenReturn("123456789101112");
        when(mockGrantOfRepresentationData.getDeceasedDateOfDeath()).thenReturn(LocalDate.now());
        when(mockGrantOfRepresentationData.getPrimaryApplicantForenames()).thenReturn("Testana");
        when(mockGrantOfRepresentationData.getPrimaryApplicantSurname()).thenReturn("Testerson");
        when(mockGrantOfRepresentationData.getDeceasedForenames()).thenReturn("Test");
        when(mockGrantOfRepresentationData.getDeceasedSurname()).thenReturn("Testerson");
        when(mockGrantOfRepresentationData.getCitizenResponse()).thenReturn("response");
        when(mockGrantOfRepresentationData.getExpectedResponseDate()).thenReturn(LocalDate.now()
                .plusWeeks(7).format(formatter));
        List<CollectionMember<UploadDocument>> documents = new ArrayList();
        documents.add(CollectionMember.<UploadDocument>builder().value(UploadDocument.builder()
                .documentLink(DocumentLink.builder().documentFilename("fileName.pdf").build()).build()).build());
        when(mockGrantOfRepresentationData.getBoDocumentsUploaded()).thenReturn(documents);
        when(mockGrantOfRepresentationData.getExecutorApplyingByInviteId(anyString()))
                .thenReturn(ExecutorApplying.builder().applyingExecutorName("Test Executor").build());
        when(mockGrantOfRepresentationData.getPrimaryApplicantEmailAddress()).thenReturn(emailaddress);

        pdfExample = new byte[10];
        businessService = new BusinessServiceImpl(businessServiceApi,
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
        assertEquals(invitationId, result);
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

        assertEquals(invitationId, newInvitation.getInviteId());

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

        assertEquals(invitationId, newInvitation.getInviteId());

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
        assertEquals(Boolean.TRUE, result);
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
        invitation.setBilingual(Boolean.FALSE);
        businessService.inviteAgreed(formdataId, invitation);
        verifyGetCaseCalls();
        verify(mockGrantOfRepresentationData)
            .setInvitationAgreedFlagForExecutorApplying(invitationId, invitation.getAgreed());
        verify(submitServiceApi)
            .updateCaseAsCaseWorker(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId, mockProbateCaseDetails);

    }

    @Test
    void shouldSendIfExecAgreed() {
        Invitation invitation = getInvitation(formdataId);
        when(mockGrantOfRepresentationData.haveAllExecutorsAgreed()).thenReturn(Boolean.FALSE);
        when(mockGrantOfRepresentationData.getLanguagePreferenceWelsh()).thenReturn(Boolean.FALSE);
        ExecutorNotification executorNotification = ExecutorNotification.builder()
                .ccdReference(formdataId)
                .executorName(mockGrantOfRepresentationData.getExecutorApplyingByInviteId(invitation.getInviteId())
                        .getApplyingExecutorName())
                .applicantName(mockGrantOfRepresentationData.getPrimaryApplicantForenames()
                        + " " + mockGrantOfRepresentationData.getPrimaryApplicantSurname())
                .deceasedName(mockGrantOfRepresentationData.getDeceasedForenames()
                        + " " + mockGrantOfRepresentationData.getDeceasedSurname())
                .deceasedDod(mockGrantOfRepresentationData.getDeceasedDateOfDeath().toString())
                .email(mockGrantOfRepresentationData.getPrimaryApplicantEmailAddress())
                .build();
        businessService.inviteAgreed(formdataId, invitation);
        verifyGetCaseCalls();
        verify(businessServiceApi).signedExec(executorNotification);
        verify(mockGrantOfRepresentationData)
                .setInvitationAgreedFlagForExecutorApplying(invitationId, invitation.getAgreed());
        verify(submitServiceApi)
                .updateCaseAsCaseWorker(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId, mockProbateCaseDetails);
    }

    @Test
    void shouldNotSendIfExecNotAgreed() {
        Invitation invitation = getInvitation(formdataId);
        when(mockGrantOfRepresentationData.haveAllExecutorsAgreed()).thenReturn(Boolean.FALSE);
        when(mockGrantOfRepresentationData.getLanguagePreferenceWelsh()).thenReturn(Boolean.FALSE);
        invitation.setAgreed(Boolean.FALSE);
        businessService.inviteAgreed(formdataId, invitation);
        verifyGetCaseCalls();
        verify(mockGrantOfRepresentationData)
                .setInvitationAgreedFlagForExecutorApplying(invitationId, invitation.getAgreed());
        verify(submitServiceApi)
                .updateCaseAsCaseWorker(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId, mockProbateCaseDetails);
    }

    @Test
    void shouldSendIfExecAgreedBilingual() {
        Invitation invitation = getInvitation(formdataId);
        when(mockGrantOfRepresentationData.haveAllExecutorsAgreed()).thenReturn(Boolean.FALSE);
        when(mockGrantOfRepresentationData.getLanguagePreferenceWelsh()).thenReturn(Boolean.TRUE);
        when(mockGrantOfRepresentationData.getExecutorApplyingByInviteId(invitation.getInviteId()))
                .thenReturn(ExecutorApplying.builder().applyingExecutorName("Test Executor").build());
        ExecutorNotification executorNotification = ExecutorNotification.builder()
                .ccdReference(formdataId)
                .executorName(mockGrantOfRepresentationData.getExecutorApplyingByInviteId(invitation.getInviteId())
                        .getApplyingExecutorName())
                .applicantName(mockGrantOfRepresentationData.getPrimaryApplicantForenames()
                        + " " + mockGrantOfRepresentationData.getPrimaryApplicantSurname())
                .deceasedName(mockGrantOfRepresentationData.getDeceasedForenames()
                        + " " + mockGrantOfRepresentationData.getDeceasedSurname())
                .deceasedDod(mockGrantOfRepresentationData.getDeceasedDateOfDeath().toString())
                .email(mockGrantOfRepresentationData.getPrimaryApplicantEmailAddress())
                .build();
        businessService.inviteAgreed(formdataId, invitation);
        verifyGetCaseCalls();
        verify(businessServiceApi).signedBilingual(executorNotification);
        verify(mockGrantOfRepresentationData)
                .setInvitationAgreedFlagForExecutorApplying(invitationId, invitation.getAgreed());
        verify(submitServiceApi)
                .updateCaseAsCaseWorker(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId, mockProbateCaseDetails);
    }

    @Test
    void shouldSendIfAllExecAgreed() {
        Invitation invitation = getInvitation(formdataId);
        when(mockGrantOfRepresentationData.haveAllExecutorsAgreed()).thenReturn(Boolean.TRUE);
        when(mockGrantOfRepresentationData.getLanguagePreferenceWelsh()).thenReturn(Boolean.FALSE);
        ExecutorNotification executorNotification = ExecutorNotification.builder()
                .ccdReference(formdataId)
                .executorName(mockGrantOfRepresentationData.getExecutorApplyingByInviteId(invitation.getInviteId())
                        .getApplyingExecutorName())
                .applicantName(mockGrantOfRepresentationData.getPrimaryApplicantForenames()
                        + " " + mockGrantOfRepresentationData.getPrimaryApplicantSurname())
                .deceasedName(mockGrantOfRepresentationData.getDeceasedForenames()
                        + " " + mockGrantOfRepresentationData.getDeceasedSurname())
                .deceasedDod(mockGrantOfRepresentationData.getDeceasedDateOfDeath().toString())
                .email(mockGrantOfRepresentationData.getPrimaryApplicantEmailAddress())
                .build();
        businessService.inviteAgreed(formdataId, invitation);
        verifyGetCaseCalls();
        verify(businessServiceApi).signedExecAll(executorNotification);
        verify(mockGrantOfRepresentationData)
                .setInvitationAgreedFlagForExecutorApplying(invitationId, invitation.getAgreed());
        verify(submitServiceApi)
                .updateCaseAsCaseWorker(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId, mockProbateCaseDetails);
    }

    @Test
    void shouldSendIfAllExecAgreedBilingual() {
        Invitation invitation = getInvitation(formdataId);
        when(mockGrantOfRepresentationData.haveAllExecutorsAgreed()).thenReturn(Boolean.TRUE);
        when(mockGrantOfRepresentationData.getLanguagePreferenceWelsh()).thenReturn(Boolean.TRUE);
        ExecutorNotification executorNotification = ExecutorNotification.builder()
                .ccdReference(formdataId)
                .executorName(mockGrantOfRepresentationData.getExecutorApplyingByInviteId(invitation.getInviteId())
                        .getApplyingExecutorName())
                .applicantName(mockGrantOfRepresentationData.getPrimaryApplicantForenames()
                        + " " + mockGrantOfRepresentationData.getPrimaryApplicantSurname())
                .deceasedName(mockGrantOfRepresentationData.getDeceasedForenames()
                        + " " + mockGrantOfRepresentationData.getDeceasedSurname())
                .deceasedDod(mockGrantOfRepresentationData.getDeceasedDateOfDeath().toString())
                .email(mockGrantOfRepresentationData.getPrimaryApplicantEmailAddress())
                .build();
        businessService.inviteAgreed(formdataId, invitation);
        verifyGetCaseCalls();
        verify(businessServiceApi).signedExecAllBilingual(executorNotification);
        verify(mockGrantOfRepresentationData)
                .setInvitationAgreedFlagForExecutorApplying(invitationId, invitation.getAgreed());
        verify(submitServiceApi)
                .updateCaseAsCaseWorker(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId, mockProbateCaseDetails);
    }

    @Test
    void shouldSendEmailForDocumentUploadSuccessful() {
        when(mockGrantOfRepresentationData.getDocumentUploadIssue()).thenReturn(Boolean.FALSE);
        when(mockGrantOfRepresentationData.getLanguagePreferenceWelsh()).thenReturn(Boolean.FALSE);
        DocumentNotification documentNotification = DocumentNotification.builder()
                .ccdReference(formdataId)
                .applicantName(mockGrantOfRepresentationData.getPrimaryApplicantForenames()
                        + " " + mockGrantOfRepresentationData.getPrimaryApplicantSurname())
                .deceasedName(mockGrantOfRepresentationData.getDeceasedForenames()
                        + " " + mockGrantOfRepresentationData.getDeceasedSurname())
                .deceasedDod(mockGrantOfRepresentationData.getDeceasedDateOfDeath().toString())
                .email(mockGrantOfRepresentationData.getPrimaryApplicantEmailAddress())
                .fileName(List.of("fileName.pdf"))
                .citizenResponse(mockGrantOfRepresentationData.getCitizenResponse())
                .expectedResponseDate(mockGrantOfRepresentationData.getExpectedResponseDate())
                .build();
        businessService.documentUploadNotification(formdataId, "true");
        verifyGetCaseCalls();
        verify(businessServiceApi).documentUpload(documentNotification);
        verify(submitServiceApi)
                .saveCase(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId, EVENT_DESCRIPTION, mockProbateCaseDetails);
    }

    @Test
    void shouldSendEmailForWelshDocumentUploadSuccessful() {
        when(mockGrantOfRepresentationData.getDocumentUploadIssue()).thenReturn(Boolean.FALSE);
        when(mockGrantOfRepresentationData.getLanguagePreferenceWelsh()).thenReturn(Boolean.TRUE);
        DocumentNotification documentNotification = DocumentNotification.builder()
                .ccdReference(formdataId)
                .applicantName(mockGrantOfRepresentationData.getPrimaryApplicantForenames()
                        + " " + mockGrantOfRepresentationData.getPrimaryApplicantSurname())
                .deceasedName(mockGrantOfRepresentationData.getDeceasedForenames()
                        + " " + mockGrantOfRepresentationData.getDeceasedSurname())
                .deceasedDod(mockGrantOfRepresentationData.getDeceasedDateOfDeath().toString())
                .email(mockGrantOfRepresentationData.getPrimaryApplicantEmailAddress())
                .fileName(List.of("fileName.pdf"))
                .citizenResponse(mockGrantOfRepresentationData.getCitizenResponse())
                .expectedResponseDate(mockGrantOfRepresentationData.getExpectedResponseDate()
                        .toString())
                .build();
        businessService.documentUploadNotification(formdataId, "true");
        verifyGetCaseCalls();
        verify(businessServiceApi).documentUploadBilingual(documentNotification);
        verify(submitServiceApi)
                .saveCase(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId, EVENT_DESCRIPTION, mockProbateCaseDetails);
    }

    @Test
    void shouldSendEmailForDocumentUploadIssue() {
        when(mockGrantOfRepresentationData.getDocumentUploadIssue()).thenReturn(Boolean.TRUE);
        when(mockGrantOfRepresentationData.getLanguagePreferenceWelsh()).thenReturn(Boolean.FALSE);
        DocumentNotification documentNotification = DocumentNotification.builder()
                .ccdReference(formdataId)
                .applicantName(mockGrantOfRepresentationData.getPrimaryApplicantForenames()
                        + " " + mockGrantOfRepresentationData.getPrimaryApplicantSurname())
                .deceasedName(mockGrantOfRepresentationData.getDeceasedForenames()
                        + " " + mockGrantOfRepresentationData.getDeceasedSurname())
                .deceasedDod(mockGrantOfRepresentationData.getDeceasedDateOfDeath().toString())
                .email(mockGrantOfRepresentationData.getPrimaryApplicantEmailAddress())
                .fileName(List.of("fileName.pdf"))
                .citizenResponse(mockGrantOfRepresentationData.getCitizenResponse())
                .expectedResponseDate(mockGrantOfRepresentationData.getExpectedResponseDate()
                        .toString())
                .build();
        businessService.documentUploadNotification(formdataId, "true");
        verifyGetCaseCalls();
        verify(businessServiceApi).documentUploadIssue(documentNotification);
        verify(submitServiceApi)
                .saveCase(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId, EVENT_DESCRIPTION, mockProbateCaseDetails);
    }

    @Test
    void shouldSendEmailForWelshDocumentUploadIssue() {
        when(mockGrantOfRepresentationData.getDocumentUploadIssue()).thenReturn(Boolean.TRUE);
        when(mockGrantOfRepresentationData.getLanguagePreferenceWelsh()).thenReturn(Boolean.TRUE);
        DocumentNotification documentNotification = DocumentNotification.builder()
                .ccdReference(formdataId)
                .applicantName(mockGrantOfRepresentationData.getPrimaryApplicantForenames()
                        + " " + mockGrantOfRepresentationData.getPrimaryApplicantSurname())
                .deceasedName(mockGrantOfRepresentationData.getDeceasedForenames()
                        + " " + mockGrantOfRepresentationData.getDeceasedSurname())
                .deceasedDod(mockGrantOfRepresentationData.getDeceasedDateOfDeath().toString())
                .email(mockGrantOfRepresentationData.getPrimaryApplicantEmailAddress())
                .fileName(List.of("fileName.pdf"))
                .citizenResponse(mockGrantOfRepresentationData.getCitizenResponse())
                .expectedResponseDate(mockGrantOfRepresentationData.getExpectedResponseDate())
                .build();
        businessService.documentUploadNotification(formdataId, "true");
        verifyGetCaseCalls();
        verify(businessServiceApi).documentUploadIssueBilingual(documentNotification);
        verify(submitServiceApi)
                .saveCase(AUTHORIZATION, SERVICE_AUTHORIZATION, formdataId, EVENT_DESCRIPTION, mockProbateCaseDetails);
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

}
