package uk.gov.hmcts.probate.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.probate.service.FeesService;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.forms.Form;

@Slf4j
@Api(tags = {"FeesController"})
@SwaggerDefinition(tags = {@Tag(name = "FeesController", description = "Fees API")})
@RestController
@RequiredArgsConstructor
public class FeesController {

    private final FeesService feesService;

    @PostMapping(path = "/forms/{correlationId}/fees", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Form> updateFees(@PathVariable("correlationId") String correlationId,
                                           @RequestParam("probateType") ProbateType probateType) {
        return new ResponseEntity(feesService.updateFees(correlationId, probateType), HttpStatus.OK);
    }
}
