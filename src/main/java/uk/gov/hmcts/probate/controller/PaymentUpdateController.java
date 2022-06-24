package uk.gov.hmcts.probate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.probate.service.PaymentUpdateService;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

@Tag(name = "Payment Update Controller", description = "Payment Update API")
@RestController
@Slf4j
public class PaymentUpdateController {

    private static final String PAYMENT_UPDATES = "/payment-updates";

    private final PaymentUpdateService paymentUpdateService;

    @Autowired
    private PaymentUpdateController(PaymentUpdateService paymentUpdateService) {
        this.paymentUpdateService = paymentUpdateService;
    }

    @Operation(summary = "Update payment", description = "Update payment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated payment successfully")
    })
    @PutMapping(path = PAYMENT_UPDATES, consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updatePayment(@RequestBody PaymentDto paymentDto) {
        paymentUpdateService.paymentUpdate(paymentDto);
    }
}
