package uk.gov.hmcts.probate.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.probate.client.business.BusinessServiceApi;
import uk.gov.hmcts.probate.client.business.BusinessServiceDocumentsApi;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.mapper.ExecutorApplyingToInvitationMapper;
import uk.gov.hmcts.probate.service.BusinessService;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final BusinessServiceApi businessServiceApi;
    private final BusinessServiceDocumentsApi businessServiceDocumentsApi;
    private final SubmitServiceApi submitServiceApi;
    private final SecurityUtils securityUtils;
    private final ExecutorApplyingToInvitationMapper executorApplyingToInvitationMapper;


    @Override
    public byte[] generateCheckAnswersSummaryPdf(CheckAnswersSummary checkAnswersSummary){
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
    public byte[] generateLegalDeclarationPdf(LegalDeclaration legalDeclaration){
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
    public byte[] generateBulkScanCoverSheetPdf(BulkScanCoverSheet bulkScanCoverSheet){
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
        String invitationId = businessServiceApi.invite(invitation, sessionId);
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails(invitation.getFormdataId());
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        grantOfRepresentationData.setInvitationDetailsForExecutorApplying(invitation.getEmail(), invitationId,
                invitation.getLeadExecutorName());
        updateCaseData(probateCaseDetails, invitation.getFormdataId());
        return invitationId;
    }

    @Override
    public String resendInvitation(String inviteId, Invitation invitation, String sessionId) {
        return businessServiceApi.invite(inviteId, invitation, sessionId);
    }

    @Override
    public Boolean haveAllIniviteesAgreed(String formdataId) {
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails(formdataId);
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        return grantOfRepresentationData.haveAllExecutorsAgreed();
    }

    @Override
    public String updateContactDetails(String formdataId, Invitation invitation) {
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
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails(formdataId);
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        grantOfRepresentationData.setInvitationAgreedFlagForExecutorApplying(invitation.getInviteId(),
                invitation.getAgreed());
        updateCaseData(probateCaseDetails, formdataId);
        return invitation.getInviteId();
    }

    @Override
    public void resetAgreedFlags(String formdataId) {
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails(formdataId);
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        grantOfRepresentationData.resetExecutorsApplyingAgreedFlags();
        updateCaseData(probateCaseDetails, formdataId);
    }


    @Override
    public String deleteInvite(String formdataId, Invitation invitation) {
        ProbateCaseDetails probateCaseDetails = getProbateCaseDetails(formdataId);
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        grantOfRepresentationData.deleteInvitation(invitation.getInviteId());
        updateCaseData(probateCaseDetails, formdataId);
        return formdataId;
    }

    @Override
    public Invitation getInviteData(String inviteId) {

        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();

        ProbateCaseDetails probateCaseDetails = submitServiceApi.getCaseByInvitationId(authorisation,
                serviceAuthorisation, inviteId, CaseType.GRANT_OF_REPRESENTATION.name());
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();

        ExecutorApplying executorApplyingByInviteId = grantOfRepresentationData.getExecutorApplyingByInviteId(inviteId);
        return executorApplyingToInvitationMapper.map(executorApplyingByInviteId);

    }


    @Override
    public String getPinNumber(String phoneNumber, String sessionId) {
        return businessServiceApi.pinNumber(phoneNumber, sessionId);
    }

    @Override
    public List<String> uploadDocument(String authorizationToken, String userID, List<MultipartFile> files) {
        if (files.isEmpty()) {
            throw new IllegalArgumentException("There needs to be at least one file") ;
        }
        return businessServiceDocumentsApi.uploadDocument(userID, authorizationToken, files.get(0));
    }

    @Override
    public String delete(String userID, String documentId) {
        return businessServiceDocumentsApi.delete(userID, documentId);
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
                formdataId, probateCaseDetails);
    }

}
