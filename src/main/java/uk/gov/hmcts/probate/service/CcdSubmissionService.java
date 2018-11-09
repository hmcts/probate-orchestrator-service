package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.probate.dto.CaseInfoDTO;
import uk.gov.hmcts.probate.dto.ccd.data.DataDTO;

public interface CcdSubmissionService {

    CaseInfoDTO submit(DataDTO dataDTO);
}
