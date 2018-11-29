package uk.gov.hmcts.probate.dto.validation;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessValidationResponseDTO implements Serializable {


    private BusinessValidationStatusDTO status;
    private List<BusinessValidationErrorDTO> errors;

    public BusinessValidationResponseDTO() {
    }

    public BusinessValidationResponseDTO(BusinessValidationStatusDTO status, List<BusinessValidationErrorDTO> errors) {
        this.status = status;
        this.errors = errors;
    }

    public BusinessValidationStatusDTO getStatus() {
        return status;
    }


    public void setStatus(BusinessValidationStatusDTO status) {
        this.status = status;
    }

    public List<BusinessValidationErrorDTO> getErrors() {
        return errors;
    }

    public void setBusinessErrors(List<BusinessValidationErrorDTO> errors) {
        this.errors = errors;
    }
}