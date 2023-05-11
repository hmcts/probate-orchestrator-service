package uk.gov.hmcts.probate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.forms.CaseSummaryHolder;
import uk.gov.hmcts.reform.probate.model.forms.Form;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

@Tag(name = "FormsController", description = "Forms API")
@RestController
@Slf4j
@RequiredArgsConstructor
public class FormsController {

    private static final String FORMS_CASE_ENDPOINT = "/forms/case/{identifier}";
    private static final String FORMS_CASES_ENDPOINT = "/forms/cases";
    private static final String FORMS_NEW_CASE_ENDPOINT = "/forms/newcase";
    private static final String SUBMISSIONS_ENDPOINT = "/submissions";
    private static final String FORMS_SUBMISSIONS_ENDPOINT = "/forms/{identifier}";
    private static final String VALIDATIONS_ENDPOINT = "/validations";
    private static final String PAYMENTS_ENDPOINT = "/payments";
    private static final String MIGRATE_DATA_ENDPOINT = "/migrateData";

    private final SubmitService submitService;


    @Operation(summary = "Initiate form data", description = "Initiate form data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Form initiated successfully"),
        @ApiResponse(responseCode = "400", description = "Initate form failed"),
        @ApiResponse(responseCode = "422", description = "Invalid or missing attribute")
    })
    @PostMapping(path = FORMS_NEW_CASE_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CaseSummaryHolder> initiateForm(@RequestParam("probateType") ProbateType probateType) {
        log.info("Initiate form called");
        return new ResponseEntity<>(submitService.initiateCase(probateType), HttpStatus.OK);
    }

    @Operation(summary = "Save form data", description = "Save form data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Form saved successfully"),
        @ApiResponse(responseCode = "400", description = "Saving form failed"),
        @ApiResponse(responseCode = "422", description = "Invalid or missing attribute")
    })
    @PostMapping(path = FORMS_CASE_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Form> saveForm(@RequestBody Form form,
                                         @PathVariable("identifier") String identifier) {
        log.info("Save form called");
        return new ResponseEntity<>(submitService.saveCase(identifier, form), HttpStatus.OK);
    }

    @Operation(summary = "Get form data", description = "Get form data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retrieved form successfully"),
        @ApiResponse(responseCode = "400", description = "Retrieving form failed")
    })
    @GetMapping(path = FORMS_CASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Form> getForm(@PathVariable("identifier") String identifier,
                                        @RequestParam("probateType") ProbateType probateType) {
        log.info("Get form called");
        return new ResponseEntity<>(submitService.getCase(identifier, probateType), HttpStatus.OK);
    }


    @Operation(summary = "Get form data", description = "Get form data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retrieved form successfully"),
        @ApiResponse(responseCode = "400", description = "Retrieving form failed")
    })
    @GetMapping(path = FORMS_CASES_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CaseSummaryHolder> getAllForms() {
        log.info("Get form called");
        return new ResponseEntity<>(submitService.getAllCases(), HttpStatus.OK);
    }


    @Operation(summary = "Submit form data", description = "Submit form data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Form submitted successfully"),
        @ApiResponse(responseCode = "400", description = "Submitting form failed"),
        @ApiResponse(responseCode = "422", description = "Invalid or missing attribute")
    })
    @PostMapping(path = FORMS_SUBMISSIONS_ENDPOINT + SUBMISSIONS_ENDPOINT,
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Form> submitForm(@RequestBody Form form,
                                           @PathVariable("identifier") String identifier) {
        log.info("Submit form called");
        return new ResponseEntity<>(submitService.submit(identifier, form), HttpStatus.OK);
    }

    @PutMapping(path = FORMS_SUBMISSIONS_ENDPOINT + SUBMISSIONS_ENDPOINT,
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Form> submitPayments(@PathVariable("identifier") String identifier,
                                               @RequestBody PaymentDto paymentDto,
                                               @RequestParam("probateType") ProbateType probateType) {
        log.info("Submit payments called");
        return new ResponseEntity<>(submitService.update(identifier, probateType, paymentDto), HttpStatus.OK);
    }

    @Operation(summary = "Update payments", description = "Update payments")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payments saved successfully"),
        @ApiResponse(responseCode = "400", description = "Saving payment failed"),
        @ApiResponse(responseCode = "422", description = "Invalid or missing attribute")
    })
    @PostMapping(path = FORMS_SUBMISSIONS_ENDPOINT + PAYMENTS_ENDPOINT,
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Form> updatePayments(@RequestBody Form form,
                                               @PathVariable("identifier") String identifier) {
        log.info("Update payments called");
        return new ResponseEntity<>(submitService.updatePayments(identifier, form), HttpStatus.OK);
    }

    @Operation(summary = "Validate case data", description = "validate case data via identifier and probate type")
    @PutMapping(path = FORMS_SUBMISSIONS_ENDPOINT + VALIDATIONS_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Form> validate(@PathVariable("identifier") String identifier,
                                         @RequestParam("probateType") ProbateType probateType) {
        return new ResponseEntity<>(submitService.validate(identifier, probateType), HttpStatus.OK);
    }

}
