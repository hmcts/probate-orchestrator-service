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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.probate.service.BusinessService;
import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;

import javax.validation.Valid;


@Api(tags = {"DocumentsController"})
@SwaggerDefinition(tags = {@Tag(name = "DocumentsController", description = "Documents API")})
@Slf4j
@RestController
@RequestMapping(value = DocumentsController.DOCUMENTS_BASEURL)
public class DocumentsController {

    protected static final String DOCUMENTS_BASEURL = "/documents";
    protected static final String CHECK_ANSWERS_ENDPOINT = "/generate/checkAnswersSummary";
    protected static final String LEGAL_DECLARATION_ENDPOINT = "/generate/legalDeclarationSummary";
    protected static final String BULK_SCAN_COVERSHEET_ENDPOINT = "/generate/bulkScanCoversheet";

    private final BusinessService businessService;

    @Autowired
    private DocumentsController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @ApiOperation(value = "Generate PDF for Check Answers Summary")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "PDF generated successfully")})
    @PostMapping(path = CHECK_ANSWERS_ENDPOINT, consumes = DocumentControllerConfiguration.APPLICATION_BUSINESSDOCUMENT_JSON)
    public ResponseEntity<byte[]> generateCheckAnswersSummaryPdf(@Valid @RequestBody CheckAnswersSummary checkAnswersSummary) {
        log.info("generate pdf");
        return new ResponseEntity<>(businessService.generateCheckAnswersSummaryPdf(checkAnswersSummary), HttpStatus.OK);
    }

    @ApiOperation(value = "Generate PDF for Legal Declaration")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "PDF generated successfully")})
    @PostMapping(path = LEGAL_DECLARATION_ENDPOINT, consumes = DocumentControllerConfiguration.APPLICATION_BUSINESSDOCUMENT_JSON)
    public ResponseEntity<byte[]> generateLegalDeclarationPdf(@Valid @RequestBody LegalDeclaration legalDeclaration) {
        log.info("generate pdf");
        return new ResponseEntity<>(businessService.generateLegalDeclarationPdf(legalDeclaration), HttpStatus.OK);
    }


    @ApiOperation(value = "Generate PDF for Bulk Scan Coversheet")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "PDF generated successfully")})
    @PostMapping(path = BULK_SCAN_COVERSHEET_ENDPOINT, consumes = DocumentControllerConfiguration.APPLICATION_BUSINESSDOCUMENT_JSON)
    public ResponseEntity<byte[]> generateBulkScanCoversheet(@Valid @RequestBody BulkScanCoverSheet bulkScanCoverSheet) {
        log.info("generate pdf");
        return new ResponseEntity<>(businessService.generateBulkScanCoverSheetPdf(bulkScanCoverSheet), HttpStatus.OK);
    }

}
