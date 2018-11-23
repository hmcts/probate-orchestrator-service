package uk.gov.hmcts.probate.service;

import uk.gov.hmcts.probate.dto.formdata.FormDataDTO;
import uk.gov.hmcts.probate.dto.validation.BusinessValidationResponseDTO;

public interface ValidationService {
    enum ProbateType {
        INTESTACY
    }

    BusinessValidationResponseDTO validate(FormDataDTO formDataDTO, ProbateType probateType);
}
