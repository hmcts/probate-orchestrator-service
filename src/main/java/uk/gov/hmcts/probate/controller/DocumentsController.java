package uk.gov.hmcts.probate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.probate.service.BusinessService;
import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;
import uk.gov.hmcts.reform.probate.model.documents.LegalDeclaration;

import jakarta.validation.Valid;

import java.util.List;


@Tag(name = "DocumentsController", description = "Documents API")
@Slf4j
@RestController
@RequestMapping(value = DocumentsController.DOCUMENTS_BASEURL)
public class DocumentsController {

    protected static final String DOCUMENTS_BASEURL = "/documents";
    protected static final String CHECK_ANSWERS_ENDPOINT = "/generate/checkAnswersSummary";
    protected static final String LEGAL_DECLARATION_ENDPOINT = "/generate/legalDeclaration";
    protected static final String BULK_SCAN_COVERSHEET_ENDPOINT = "/generate/bulkScanCoversheet";
    protected static final String DOCUMENT_UPLOAD_ENDPOINT = "/upload";
    protected static final String DOCUMENT_UPLOAD_NOTIFICATION_ENDPOINT = "/notification";
    protected static final String DOCUMENT_DELETE_ENDPOINT = "/delete/{documentId}";

    private final BusinessService businessService;
    private final BackOfficeService backOfficeService;

    @Autowired
    private DocumentsController(BusinessService businessService, BackOfficeService backOfficeService) {
        this.businessService = businessService;
        this.backOfficeService = backOfficeService;
    }

    @Operation(summary = "Generate PDF for Check Answers Summary")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check Answers Summary PDF generated successfully")})
    @PostMapping(path = CHECK_ANSWERS_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> generateCheckAnswersSummaryPdf(
        @Valid @RequestBody CheckAnswersSummary checkAnswersSummary) {
        log.info("Check Answers generate pdf");
        return new ResponseEntity<>(businessService.generateCheckAnswersSummaryPdf(checkAnswersSummary), HttpStatus.OK);
    }

    @Operation(summary = "Generate PDF for Legal Declaration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Legal Declaration PDF generated successfully")})
    @PostMapping(path = LEGAL_DECLARATION_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> generateLegalDeclarationPdf(@Valid @RequestBody LegalDeclaration legalDeclaration) {
        log.info("Legal Declaration generate pdf");
        return new ResponseEntity<>(businessService.generateLegalDeclarationPdf(legalDeclaration), HttpStatus.OK);
    }


    @Operation(summary = "Generate PDF for Bulk Scan Coversheet")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bulk Scan Coversheet PDF generated successfully")})
    @PostMapping(path = BULK_SCAN_COVERSHEET_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> generateBulkScanCoversheet(@Valid @RequestBody
                                                                     BulkScanCoverSheet bulkScanCoverSheet) {
        log.info("Bulk Scan Coversheet generate pdf");
        return new ResponseEntity<>(businessService.generateBulkScanCoverSheetPdf(bulkScanCoverSheet), HttpStatus.OK);
    }


    @Operation(summary = "Upload document")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Document uploaded successfully")})
    @PostMapping(path = DOCUMENT_UPLOAD_ENDPOINT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<String>> upload(@RequestHeader(value = "Authorization", required = false)
                                                       String authorizationToken,
                                               @RequestHeader("user-id") String userID,
                                               @RequestPart("file") List<MultipartFile> files) {
        log.info("Uploading document to backoffice");

        return new ResponseEntity<>(backOfficeService.uploadDocument(authorizationToken, files), HttpStatus.OK);
    }

    @PostMapping(path = DOCUMENT_UPLOAD_NOTIFICATION_ENDPOINT + "/{formdataId}/{citizenResponseCheckbox}")
    public void uploadNotification(@PathVariable("formdataId") String formdataId,
                                                   @PathVariable("citizenResponseCheckbox")
                                                   String citizenResponseCheckbox) {
        log.info("Sending notification for document upload to Business service");
        businessService.documentUploadNotification(formdataId,citizenResponseCheckbox);
    }

}
