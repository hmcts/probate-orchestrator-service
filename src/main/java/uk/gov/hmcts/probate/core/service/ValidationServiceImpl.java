package uk.gov.hmcts.probate.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.client.validation.BusinessValidationClient;
import uk.gov.hmcts.probate.dto.formdata.FormDataDTO;
import uk.gov.hmcts.probate.dto.validation.BusinessValidationResponseDTO;
import uk.gov.hmcts.probate.service.ValidationService;

@Component
public class ValidationServiceImpl implements ValidationService {
    private final BusinessValidationClient businessValidationClient;
    private final SecurityUtils securityManager;

    @Autowired
    ValidationServiceImpl(SecurityUtils securityManager, BusinessValidationClient businessValidationClient){
        this.securityManager = securityManager;
        this.businessValidationClient = businessValidationClient;
    }

    public BusinessValidationResponseDTO validate(FormDataDTO formDataDTO, ProbateType probateType){

        BusinessValidationResponseDTO businessValidationResponseDTO =  null;
        if(ProbateType.INTESTACY.equals(probateType)) {
            businessValidationResponseDTO = businessValidationClient.valididateIntestacy(securityManager.getUserToken(), securityManager.generateServiceToken(), formDataDTO);
        }
        return businessValidationResponseDTO;
    }
}
