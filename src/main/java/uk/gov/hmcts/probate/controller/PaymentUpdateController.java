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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.probate.service.PaymentUpdateService;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

@Api(tags = {"Payment Update Controller"})
@SwaggerDefinition(tags = {@Tag(name = "PaymentUpdateController", description = "Payment Update API")})
@RestController
@Slf4j
public class PaymentUpdateController {

    private static final String PAYMENT_UPDATES = "/payment-updates";

    private final PaymentUpdateService paymentUpdateService;

    @Autowired
    private PaymentUpdateController(PaymentUpdateService paymentUpdateService) {
        this.paymentUpdateService = paymentUpdateService;
    }

    @ApiOperation(value = "Update payment", notes = "Update payment")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Updated payment successfully")
    })
    @PostMapping(path = PAYMENT_UPDATES, consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updatePayment(@RequestBody PaymentDto paymentDto) {
        paymentUpdateService.paymentUpdate(paymentDto);
    }
}
