package uk.gov.hmcts.probate.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.client.CcdSubmissionClient;
import uk.gov.hmcts.probate.domain.FormData;
import uk.gov.hmcts.probate.dto.CaseInfoDTO;
import uk.gov.hmcts.probate.dto.ccd.data.DataDTO;
import uk.gov.hmcts.probate.core.service.mapper.CaseDataMapper;
import uk.gov.hmcts.probate.service.OrchestratorSubmitService;

@Component
public class OrchestratorSubmitServiceImpl implements OrchestratorSubmitService {

    private final CcdSubmissionClient ccdSubmissionClient;

    private final CaseDataMapper caseDataMapper;

    @Autowired
    public OrchestratorSubmitServiceImpl(CcdSubmissionClient ccdSubmissionClient, CaseDataMapper caseDataMapper) {
        this.ccdSubmissionClient = ccdSubmissionClient;
        this.caseDataMapper = caseDataMapper;
    }

    @Override
    public CaseInfoDTO submit(FormData formData) {
        DataDTO dataDTO = caseDataMapper.mapFormDataToDataDTO(formData);
        return ccdSubmissionClient.submit(dataDTO);
    }
}
