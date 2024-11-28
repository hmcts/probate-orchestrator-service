package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;

import java.util.List;

public interface BusinessService {

    byte[] generateCheckAnswersSummaryPdf(CheckAnswersSummary checkAnswersSummary);

    byte[] generateLegalDeclarationPdf(LegalDeclaration legalDeclaration);

    byte[] generateBulkScanCoverSheetPdf(BulkScanCoverSheet bulkScanCoverSheet);

    String sendInvitation(Invitation invitation, String sessionId);

    String resendInvitation(String inviteId, Invitation invitation, String sessionId);

    Boolean haveAllIniviteesAgreed(String formdataId);

    String inviteAgreed(String formdataId, Invitation invitation);

    String updateContactDetails(String formdataId, Invitation invitation);

    void resetAgreedFlags(String formdataId);

    String deleteInvite(String formdataId, Invitation invitation);

    Invitation getInviteData(String inviteId);

    List<Invitation> getAllInviteData(String formdataId);

    String getPinNumber(String phoneNumber, String sessionId, Boolean isBilingual);

    List<Invitation> sendInvitations(List<Invitation> invitations, String sessionId, Boolean isBilingual);

    void documentUploadNotification(String formDataId, String citizenResponseCheckbox);
}

