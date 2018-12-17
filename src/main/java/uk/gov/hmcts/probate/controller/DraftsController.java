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
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.forms.Form;

@Api(tags = {"OrchestratorSubmitController"})
@SwaggerDefinition(tags = {@Tag(name = "OrchestratorSubmitController", description = "OrchestratorSubmit API")})
@RestController
public class DraftsController {

    private static final String DRAFTS_URL = "/drafts";
    private static final String HANDSHAKE_URL = "/handshake";

    private final SubmitService submitService;

    @Autowired
    private DraftsController(SubmitService submitService) {
        this.submitService = submitService;
    }

    @ApiOperation(value = "Create submission to CCD", notes = "Create submission to CCD")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Draft created"),
        @ApiResponse(code = 400, message = "Saving draft failed failed"),
        @ApiResponse(code = 422, message = "Invalid or missing attribute")
    })
    @RequestMapping(path = DRAFTS_URL, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Form> saveDrafts(@RequestBody Form form) {
        //Form formResponse = submitService.saveDraft(form);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
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
