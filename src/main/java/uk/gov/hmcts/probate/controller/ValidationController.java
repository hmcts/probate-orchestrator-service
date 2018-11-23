package uk.gov.hmcts.probate.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.probate.client.validation.BusinessValidationClient;
import uk.gov.hmcts.probate.controller.mapper.FormDataMapper;
import uk.gov.hmcts.probate.dto.CaseInfoDTO;
import uk.gov.hmcts.probate.dto.formdata.FormDataDTO;
import uk.gov.hmcts.probate.dto.validation.BusinessValidationResponseDTO;
import uk.gov.hmcts.probate.service.OrchestratorSubmitService;
import uk.gov.hmcts.probate.service.ValidationService;

@Api(tags = {"ValidationController"})
@SwaggerDefinition(tags = {@Tag(name = "ValidationController", description = "Validation API")})
@RestController
public class ValidationController {

    protected static final String INTESTACY_VALIDATION_URL = "/probateTypes/intestacy/validations";
    private final ValidationService validationService;

    @Autowired
    private ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @ApiOperation(value = "Create submission to CCD", notes = "Create submission to CCD")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Validation call to orchestrator successful"),
            @ApiResponse(code = 400, message = "Validation  call to orchestrator failed"),
    })
    @RequestMapping(path = INTESTACY_VALIDATION_URL, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<BusinessValidationResponseDTO> validateIntestacy(@RequestBody FormDataDTO formDataDTO) {
        BusinessValidationResponseDTO businessValidationResponseDTO = validationService.validate(formDataDTO, ValidationService.ProbateType.INTESTACY);
        return new ResponseEntity<>(businessValidationResponseDTO, HttpStatus.OK);
    }



}