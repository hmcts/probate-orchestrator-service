package uk.gov.hmcts.probate.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.forms.Form;

@Api(tags = {"FormsController"})
@SwaggerDefinition(tags = {@Tag(name = "FormsController", description = "Forms API")})
@RestController
@Slf4j
public class FormsController {

    private static final String FORMS_ENDPOINT = "/forms/{applicantEmail}";
    private static final String SUBMISSIONS_ENDPOINT = "/submissions";
    private static final String PAYMENTS_ENDPOINT = "/payments";

    private final SubmitService submitService;

    @Autowired
    private FormsController(SubmitService submitService) {
        this.submitService = submitService;
    }

    @ApiOperation(value = "Save form data", notes = "Save form data")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Form saved successfully"),
        @ApiResponse(code = 400, message = "Saving form failed"),
        @ApiResponse(code = 422, message = "Invalid or missing attribute")
    })
    @PostMapping(path = FORMS_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Form> saveForm(@RequestBody Form form,
                                         @PathVariable("applicantEmail") String applicantEmail) {
        log.info("Save form called");
        return new ResponseEntity<>(submitService.saveDraft(applicantEmail, form), HttpStatus.OK);
    }

    @ApiOperation(value = "Get form data", notes = "Get form data")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Retrieved form successfully"),
        @ApiResponse(code = 400, message = "Retrieving form failed")
    })
    @GetMapping(path = FORMS_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Form> getForm(@PathVariable("applicantEmail") String applicantEmail,
                                        @RequestParam("probateType") ProbateType probateType) {
        log.info("Get form called");
        return new ResponseEntity<>(submitService.getCase(applicantEmail, probateType), HttpStatus.OK);
    }

    @ApiOperation(value = "Submit form data", notes = "Submit form data")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Form submitted successfully"),
        @ApiResponse(code = 400, message = "Submitting form failed"),
        @ApiResponse(code = 422, message = "Invalid or missing attribute")
    })
    @PostMapping(path = FORMS_ENDPOINT + SUBMISSIONS_ENDPOINT,
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Form> submitForm(@RequestBody Form form,
                                           @PathVariable("applicantEmail") String applicantEmail) {
        log.info("Submit form called");
        return new ResponseEntity<>(submitService.submit(applicantEmail, form), HttpStatus.OK);
    }

    @ApiOperation(value = "Update payments", notes = "Update payments")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Payments saved successfully"),
        @ApiResponse(code = 400, message = "Saving payment failed"),
        @ApiResponse(code = 422, message = "Invalid or missing attribute")
    })
    @PostMapping(path = FORMS_ENDPOINT + PAYMENTS_ENDPOINT,
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Form> updatePayments(@RequestBody Form form,
                                               @PathVariable("applicantEmail") String applicantEmail) {
        log.info("Update payments called");
        return new ResponseEntity<>(submitService.updatePayments(applicantEmail, form), HttpStatus.OK);
    }
}
