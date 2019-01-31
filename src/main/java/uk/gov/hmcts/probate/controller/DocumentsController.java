package uk.gov.hmcts.probate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.probate.service.BusinessService;
import uk.gov.hmcts.reform.probate.model.documents.CheckAnswersSummary;

import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping(value = "/documents")
public class DocumentsController {

    private static final String CHECK_ANSWERS_ENDPOINT = "/generate/check-answers-summary";

    private final BusinessService businessService;

    @Autowired
    private DocumentsController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping(path = CHECK_ANSWERS_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> generateCheckAnswersSummaryPdf(@Valid @RequestBody CheckAnswersSummary checkAnswersSummary) {
        log.info("generate pdf");
        return new ResponseEntity<>(businessService.generateCheckAnswersSummaryPdf(checkAnswersSummary), HttpStatus.OK);
    }

}
