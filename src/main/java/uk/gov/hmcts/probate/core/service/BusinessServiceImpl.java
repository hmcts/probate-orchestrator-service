package uk.gov.hmcts.probate.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.client.business.BusinessServiceApi;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.mapper.ExecutorApplyingToInvitationMapper;
import uk.gov.hmcts.probate.service.BusinessService;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
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
import java.util.List;
import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final BusinessServiceApi businessServiceApi;
    private final SubmitServiceApi submitServiceApi;
    private final SecurityUtils securityUtils;
    private final ExecutorApplyingToInvitationMapper executorApplyingToInvitationMapper;
    private static final String RESPONSE_DATE_FORMAT = "yyyy-MM-dd";
    private static final String FALSE = "false";


    @Override
    public byte[] generateCheckAnswersSummaryPdf(CheckAnswersSummary checkAnswersSummary) {
        log.info("generateCheckAnswersSummaryPdf");
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        return businessServiceApi.generateCheckAnswersSummaryPdf(
                authorisation,
                serviceAuthorisation,
                checkAnswersSummary
        );
    }

    @Override
    public byte[] generateLegalDeclarationPdf(LegalDeclaration legalDeclaration) {
        log.info("generateLegalDeclarationPdf");
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        return businessServiceApi.generateLegalDeclarationPDF(
                authorisation,
                serviceAuthorisation,
                legalDeclaration
        );
    }


    @Override
    public byte[] generateBulkScanCoverSheetPdf(BulkScanCoverSheet bulkScanCoverSheet) {
        log.info("generateBulkScanCoverSheetPdf");
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        return businessServiceApi.generateBulkScanCoverSheetPDF(
                authorisation,
                serviceAuthorisation,
                bulkScanCoverSheet
        );
    }

    @Override
    public String sendInvitation(Invitation invitation, String sessionId) {
        log.info("Send Invitation data ...calling businessServiceApi");
        String invitationId = businessServiceApi.invite(invitation, sessionId);
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails(invitation.getFormdataId());
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        grantOfRepresentationData.setInvitationDetailsForExecutorApplying(invitation.getEmail(), invitationId,
                invitation.getLeadExecutorName(), invitation.getExecutorName());
        log.info("Updating case with invitation details");
        updateCaseData(probateCaseDetails, invitation.getFormdataId());
        log.info("Invitation data saved with id: {} ", invitationId);
        return invitationId;
    }


    @Override
    public List<Invitation> sendInvitations(List<Invitation> invitations, String sessionId, Boolean isBilingual) {
        log.info("Send Invitations data ...calling businessServiceApi");
        Optional<Invitation> optionalInvitation = invitations.stream().findFirst();
        if (optionalInvitation.isPresent()) {
            final ProbateCaseDetails probateCaseDetails =
                getProbateCaseDetails(optionalInvitation.get().getFormdataId());
            String formDataId = optionalInvitation.get().getFormdataId();
            GrantOfRepresentationData grantOfRepresentationData =
                    (GrantOfRepresentationData) probateCaseDetails.getCaseData();
            invitations.stream().forEach(invitation -> {
                if (invitation.getInviteId() == null) {
                    log.info("Invitation not sent previously creating invite by calling businessServiceApi");
                    if (isBilingual) {
                        invitation.setInviteId(businessServiceApi.inviteBilingual(invitation, sessionId));
                    } else {
                        invitation.setInviteId(businessServiceApi.invite(invitation, sessionId));
                    }
                    grantOfRepresentationData.setInvitationDetailsForExecutorApplying(invitation.getEmail(),
                            invitation.getInviteId(),
                            invitation.getLeadExecutorName(), invitation.getExecutorName());
                    log.info("Invitation data saved with id: {} ", invitation.getInviteId());
                    log.info("Invitation data being saved email: {}, lead exec name: {}, exec name {}",
                        invitation.getEmail(), invitation.getLeadExecutorName(), invitation.getExecutorName());
                } else {
                    if (isBilingual) {
                        businessServiceApi.inviteBilingual(invitation.getInviteId(), invitation, sessionId);
                    } else {
                        businessServiceApi.invite(invitation.getInviteId(), invitation, sessionId);
                    }
                }

            });
            log.info("Updating case with invitation details");
            updateCaseData(probateCaseDetails, formDataId);
        }
        return invitations;
    }


    @Override
    public String resendInvitation(String inviteId, Invitation invitation, String sessionId) {
        log.info("resendInvitation");
        return businessServiceApi.invite(inviteId, invitation, sessionId);
    }

    @Override
    public Boolean haveAllIniviteesAgreed(String formdataId) {
        log.info("Have all invitees agreed");
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails(formdataId);
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        return grantOfRepresentationData.haveAllExecutorsAgreed();
    }

    @Override
    public String updateContactDetails(String formdataId, Invitation invitation) {
        log.info("Updating contact details");
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails(formdataId);
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        grantOfRepresentationData.updateInvitationContactDetailsForExecutorApplying(invitation.getInviteId(),
                invitation.getEmail(), invitation.getPhoneNumber());
        updateCaseData(probateCaseDetails, formdataId);
        return invitation.getInviteId();
    }

    @Override
    public String inviteAgreed(String formdataId, Invitation invitation) {
        log.info("Setting invite agreed");
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails(formdataId);
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        log.info("Got the case details now set agreed flag: {}", formdataId);
        log.info("Updating case with  agreed flag for {}", invitation.getInviteId());
        grantOfRepresentationData.setInvitationAgreedFlagForExecutorApplying(invitation.getInviteId(),
                invitation.getAgreed());
        updateCaseDataAsCaseWorker(probateCaseDetails, formdataId);
        ExecutorNotification executorNotification = ExecutorNotification.builder()
                .applicantName(grantOfRepresentationData.getPrimaryApplicantForenames()
                        + " " + grantOfRepresentationData.getPrimaryApplicantSurname())
                .ccdReference(formdataId)
                .deceasedDod(grantOfRepresentationData.getDeceasedDateOfDeath().toString())
                .executorName(grantOfRepresentationData.getExecutorApplyingByInviteId(invitation.getInviteId())
                        .getApplyingExecutorName())
                .deceasedName(grantOfRepresentationData.getDeceasedForenames()
                        + " " + grantOfRepresentationData.getDeceasedSurname())
                .email(grantOfRepresentationData.getPrimaryApplicantEmailAddress())
                .build();
        if (Boolean.TRUE.equals(grantOfRepresentationData.haveAllExecutorsAgreed())
                && grantOfRepresentationData.getLanguagePreferenceWelsh().equals(Boolean.TRUE)) {
            businessServiceApi.signedExecAllBilingual(executorNotification);
        } else if (Boolean.TRUE.equals(grantOfRepresentationData.haveAllExecutorsAgreed())) {
            businessServiceApi.signedExecAll(executorNotification);
        } else if (grantOfRepresentationData.getLanguagePreferenceWelsh().equals(Boolean.TRUE)
                && invitation.getAgreed().equals(Boolean.TRUE)) {
            businessServiceApi.signedBilingual(executorNotification);
        } else if (invitation.getAgreed().equals(Boolean.TRUE)) {
            businessServiceApi.signedExec(executorNotification);
        }
        return invitation.getInviteId();
    }

    @Override
    public void documentUploadNotification(String formDataId, String citizenResponseCheckbox) {
        log.info("Setting response submitted date and sending document upload notification");
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails(formDataId);
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        log.info("Got the case details now response submitted date: {}", formDataId);
        grantOfRepresentationData.setExpectedResponseDate(getResponseSubmittedDate());
        grantOfRepresentationData.setCitizenResponseCheckbox(Boolean.parseBoolean(citizenResponseCheckbox));
        probateCaseDetails.setCaseData(grantOfRepresentationData);
        if (Boolean.TRUE.equals(Boolean.parseBoolean(citizenResponseCheckbox))) {
            updateCaseData(probateCaseDetails, formDataId);
        }
        DocumentNotification documentNotification = DocumentNotification.builder()
                .applicantName(grantOfRepresentationData.getPrimaryApplicantForenames()
                        + " " + grantOfRepresentationData.getPrimaryApplicantSurname())
                .ccdReference(formDataId)
                .deceasedDod(grantOfRepresentationData.getDeceasedDateOfDeath().toString())
                .deceasedName(grantOfRepresentationData.getDeceasedForenames()
                        + " " + grantOfRepresentationData.getDeceasedSurname())
                .email(grantOfRepresentationData.getPrimaryApplicantEmailAddress())
                .citizenResponse(grantOfRepresentationData.getCitizenResponse())
                .fileName(getDocumentNames(grantOfRepresentationData.getCitizenDocumentsUploaded()))
                .expectedResponseDate(grantOfRepresentationData.getExpectedResponseDate())
                .build();
        log.info("Document notification: {}", documentNotification);
        log.info("language preference upload issue: {} {}", grantOfRepresentationData.getLanguagePreferenceWelsh(),
                grantOfRepresentationData.getDocumentUploadIssue());
        if (Boolean.FALSE.equals(grantOfRepresentationData.getDocumentUploadIssue())
                && Boolean.TRUE.equals(grantOfRepresentationData.getLanguagePreferenceWelsh())) {
            businessServiceApi.documentUploadBilingual(documentNotification);
        } else if (Boolean.FALSE.equals(grantOfRepresentationData.getDocumentUploadIssue())) {
            businessServiceApi.documentUpload(documentNotification);
        } else if (Boolean.TRUE.equals(grantOfRepresentationData.getLanguagePreferenceWelsh())
                && Boolean.TRUE.equals(grantOfRepresentationData.getDocumentUploadIssue())) {
            businessServiceApi.documentUploadIssueBilingual(documentNotification);
        } else if (Boolean.TRUE.equals(grantOfRepresentationData.getDocumentUploadIssue())
                || FALSE.equals(citizenResponseCheckbox) ) {
            log.info("Call to BS to send upload issue email: {}", formDataId);
            businessServiceApi.documentUploadIssue(documentNotification);
        }

    }

    private List<String> getDocumentNames(List<CollectionMember<UploadDocument>> citizenDocuments) {
        if (citizenDocuments == null) {
            return new ArrayList<>();
        } else {
            return citizenDocuments.stream().map(citizenDocument -> citizenDocument.getValue().getDocumentLink()
                            .getDocumentFilename()).toList();
        }
    }

    private String getResponseSubmittedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(RESPONSE_DATE_FORMAT);
        return LocalDate.now().plusWeeks(7).format(formatter);
    }

    @Override
    public void resetAgreedFlags(String formdataId) {
        log.info("Reset agreed flags");
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails(formdataId);
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        grantOfRepresentationData.resetExecutorsApplyingAgreedFlags();
        log.info("Updating case with reset agreed flag");
        updateCaseData(probateCaseDetails, formdataId);
    }


    @Override
    public String deleteInvite(String formdataId, Invitation invitation) {
        log.info("Delete invites");
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails(formdataId);
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        grantOfRepresentationData.deleteInvitation(invitation.getInviteId());
        log.info("Updating case with deleted invites");
        updateCaseData(probateCaseDetails, formdataId);
        return formdataId;
    }

    @Override
    public Invitation getInviteData(String inviteId) {

        log.info("Get invite data");
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();

        log.info("Service authorisation for getInviteData: {}", serviceAuthorisation);
        log.info("Authorisation for getInviteData: {}", authorisation);
        log.info("Invite id: {}", inviteId);
        ProbateCaseDetails probateCaseDetails = submitServiceApi.getCaseByInvitationId(authorisation,
                serviceAuthorisation, inviteId, CaseType.GRANT_OF_REPRESENTATION.name());
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        log.info("Found case for invite data as case worker");

        ExecutorApplying executorApplyingByInviteId = grantOfRepresentationData.getExecutorApplyingByInviteId(inviteId);
        Invitation invitation = executorApplyingToInvitationMapper.map(executorApplyingByInviteId);
        log.info("Got invite data for executor applying");
        invitation.setFormdataId(probateCaseDetails.getCaseInfo().getCaseId());
        invitation.setBilingual(grantOfRepresentationData.getLanguagePreferenceWelsh());
        return invitation;

    }

    @Override
    public List<Invitation> getAllInviteData(String formdataId) {

        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails(formdataId);
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        log.info("Found case for invite data as case worker");

        List<CollectionMember<ExecutorApplying>> executorsApplying = grantOfRepresentationData.getExecutorsApplying();
        List<Invitation> executorInvitations = new ArrayList();

        executorsApplying
            .stream()
            .filter(e -> e.getValue()
                .getApplyingExecutorApplicant() == null || !e.getValue()
                .getApplyingExecutorApplicant().booleanValue())
            .forEach(ea -> {
                Invitation invitation = getInviteData(ea.getValue().getApplyingExecutorInvitationId());
                executorInvitations.add(invitation);
            }
            );

        return executorInvitations;
    }


    @Override
    public String getPinNumber(String phoneNumber, String sessionId, Boolean isBilingual) {
        log.info("Get PIN number");
        if (isBilingual) {
            return businessServiceApi.pinNumberBilingual(phoneNumber, sessionId);
        } else {
            return businessServiceApi.pinNumber(phoneNumber, sessionId);
        }
    }

    private ProbateCaseDetails getProbateCaseDetails(String caseId) {
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        return submitServiceApi.getCase(authorisation, serviceAuthorisation,
                caseId, ProbateType.PA.getCaseType().name());
    }

    private void updateCaseData(ProbateCaseDetails probateCaseDetails, String formdataId) {
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        submitServiceApi.saveCase(authorisation, serviceAuthorisation,
                formdataId, "event update case data", probateCaseDetails);
    }

    private void updateCaseDataAsCaseWorker(ProbateCaseDetails probateCaseDetails, String formdataId) {
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        submitServiceApi.updateCaseAsCaseWorker(authorisation, serviceAuthorisation,
                formdataId, probateCaseDetails);
    }
}
