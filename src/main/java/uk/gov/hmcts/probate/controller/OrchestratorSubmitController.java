package uk.gov.hmcts.probate.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.probate.dto.CaseInfoDTO;
import uk.gov.hmcts.probate.dto.formdata.FormDataDTO;
import uk.gov.hmcts.probate.controller.mapper.FormDataMapper;
import uk.gov.hmcts.probate.service.OrchestratorSubmitService;

@Api(tags = {"OrchestratorSubmitController"})
@SwaggerDefinition(tags = {@Tag(name = "OrchestratorSubmitController", description = "OrchestratorSubmit API")})
@RestController
public class OrchestratorSubmitController {

    private static final String SUBMIT_URL = "/submit";
    private static final String HANDSHAKE_URL = "/handshake";

    private final OrchestratorSubmitService orchestratorSubmitService;

    private final FormDataMapper formDataMapper;

    @Autowired
    private OrchestratorSubmitController(OrchestratorSubmitService orchestratorSubmitService, FormDataMapper formDataMapper) {
        this.orchestratorSubmitService = orchestratorSubmitService;
        this.formDataMapper = formDataMapper;
    }

    @ApiOperation(value = "Create submission to CCD", notes = "Create submission to CCD")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Submission to CCD created"),
            @ApiResponse(code = 400, message = "Submission to CCD failed"),
            @ApiResponse(code = 422, message = "Invalid or missing attribute")
    })
    @RequestMapping(path = SUBMIT_URL, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CaseInfoDTO> submit(@RequestBody FormDataDTO formDataDTO) {
        CaseInfoDTO caseInfoDTO = orchestratorSubmitService.submit(formDataMapper.mapFormDataDTO(formDataDTO));
        return new ResponseEntity<>(caseInfoDTO, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Handshake", notes = "Handshake with Orchestrator Service")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Handshake successful"),
            @ApiResponse(code = 400, message = "Handshake failed"),
    })
    @RequestMapping(path = HANDSHAKE_URL, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> handshake() {
        return new ResponseEntity<>("Hello from the Orchestrator Service!", HttpStatus.OK);
        }
}
