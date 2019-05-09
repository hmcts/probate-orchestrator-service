package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;

public interface BusinessService {

    byte[] generateCheckAnswersSummaryPdf(CheckAnswersSummary checkAnswersSummary);

    byte[] generateLegalDeclarationPdf(LegalDeclaration legalDeclaration);

    byte[] generateBulkScanCoverSheetPdf(BulkScanCoverSheet bulkScanCoverSheet);

    String sendInvitation(Invitation invitation, String sessionId);

    String resendInvite(String inviteId, Invitation invitation, String sessionId);

    Boolean haveAllIniviteesAgreed(String formdataId);

    String inviteAgreed(String formdataId, Invitation invitation, String sessionId);

    String updateContactDetails(String formdataId, Invitation invitation);

    String resetAgreedFlags(String formdataId);
}
