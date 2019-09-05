package uk.gov.hmcts.probate.service;

import org.springframework.web.multipart.MultipartFile;
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

    String getPinNumber(String phoneNumber, String sessionId);

    List<String> uploadDocument(String authorizationToken, String userID, List<MultipartFile> files);

    String delete(String userID, String documentId);

    List<Invitation> sendInvitations(List<Invitation> invitations, String sessionId);
}

