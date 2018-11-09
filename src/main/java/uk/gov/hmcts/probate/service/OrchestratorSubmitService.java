package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.probate.domain.FormData;
import uk.gov.hmcts.probate.dto.CaseInfoDTO;

public interface OrchestratorSubmitService {

    CaseInfoDTO submit(FormData formData);
}
