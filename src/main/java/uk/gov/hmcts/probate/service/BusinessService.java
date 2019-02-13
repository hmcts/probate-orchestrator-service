package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;

public interface BusinessService {

    byte[] generateCheckAnswersSummaryPdf(CheckAnswersSummary checkAnswersSummary);

    byte[] generateLegalDeclarationPdf(LegalDeclaration legalDeclaration);

    byte[] generateBulkScanCoverSheetPdf(BulkScanCoverSheet bulkScanCoverSheet);

}
