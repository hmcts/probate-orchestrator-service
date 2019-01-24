package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;

public interface BusinessService {

    byte[] generateCheckAnswersSummaryPdf(CheckAnswersSummary checkAnswersSummary);

}
