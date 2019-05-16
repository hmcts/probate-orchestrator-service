package uk.gov.hmcts.probate.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.client.BusinessServiceApi;
import uk.gov.hmcts.probate.client.SubmitServiceApi;
import uk.gov.hmcts.probate.service.BusinessService;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;

@Slf4j
@Component
public class BusinessServiceImpl implements BusinessService {

    private final BusinessServiceApi businessServiceApi;
    private final SubmitServiceApi submitServiceApi;
    private final SecurityUtils securityUtils;

    @Autowired
    public BusinessServiceImpl(BusinessServiceApi businessServiceApi,
                               SubmitServiceApi submitServiceApi, SecurityUtils securityUtils) {
        this.businessServiceApi = businessServiceApi;
        this.securityUtils = securityUtils;
        this.submitServiceApi = submitServiceApi;
    }

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
        String inviationId =businessServiceApi.invite(invitation, sessionId);
        ProbateCaseDetails probateCaseDetails =getProbateCaseDetails(invitation.getFormdataId());
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        grantOfRepresentationData.setInvitationDetailsForExecutorApplying(inviationId, invitation.getEmail(),
                invitation.getLeadExecutorName());
        updateCaseData(probateCaseDetails, invitation.getFormdataId());
        return inviationId;
    }

    @Override
    public String resendInvite(String inviteId, Invitation invitation, String sessionId) {
        return businessServiceApi.invite(inviteId, invitation, sessionId);
    }

    @Override
    public Boolean haveAllIniviteesAgreed(String formdataId) {
        ProbateCaseDetails probateCaseDetails =getProbateCaseDetails(formdataId);
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        return grantOfRepresentationData.haveAllExecutorsAgreed();
    }

    @Override
    public String updateContactDetails(String formdataId, Invitation invitation) {
        ProbateCaseDetails probateCaseDetails =getProbateCaseDetails(formdataId);
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        grantOfRepresentationData.updateInvitationContactDetailsForExecutorApplying(invitation.getInviteId(),
                invitation.getEmail(), invitation.getPhoneNumber());
        updateCaseData(probateCaseDetails, invitation.getFormdataId());
        return invitation.getInviteId();
    }

    private void updateCaseData(ProbateCaseDetails probateCaseDetails, String formdataId) {
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        submitServiceApi.update(authorisation, serviceAuthorisation, formdataId, probateCaseDetails);
    }


    @Override
    public String inviteAgreed(String formdataId, Invitation invitation) {
        ProbateCaseDetails probateCaseDetails =getProbateCaseDetails(invitation.getFormdataId());
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        grantOfRepresentationData.setInvitationAgreedFlagForExecutorApplying(invitation.getInviteId(),
                invitation.getAgreed());
        updateCaseData(probateCaseDetails, invitation.getFormdataId());
        return invitation.getInviteId();
    }

    @Override
    public void resetAgreedFlags(String formdataId) {
        ProbateCaseDetails probateCaseDetails =getProbateCaseDetails(formdataId);
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        grantOfRepresentationData.resetExecutorsApplyingAgreedFlags();
        updateCaseData(probateCaseDetails, formdataId);
    }


    @Override
    public String deleteInvite(String formdataId, Invitation invitation) {
        ProbateCaseDetails probateCaseDetails =getProbateCaseDetails(formdataId);
        GrantOfRepresentationData grantOfRepresentationData =
                (GrantOfRepresentationData) probateCaseDetails.getCaseData();
        grantOfRepresentationData.deleteInvitation(invitation.getInviteId());
        updateCaseData(probateCaseDetails, formdataId);
        return formdataId;
    }


    private ProbateCaseDetails getProbateCaseDetails(String caseId) {
        String serviceAuthorisation = securityUtils.getServiceAuthorisation();
        String authorisation = securityUtils.getAuthorisation();
        ProbateCaseDetails probateCaseDetails = submitServiceApi.getCase(authorisation, serviceAuthorisation,
                caseId, ProbateType.PA.getCaseType().name());
        return probateCaseDetails;
    }


}
