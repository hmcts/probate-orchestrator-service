package uk.gov.hmcts.probate.client.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.hmcts.probate.model.payment.CardPaymentRequest;
import uk.gov.hmcts.probate.model.payment.PaymentDto;
import uk.gov.hmcts.probate.model.payment.PaymentsDto;


import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(
    name = "payment-api",
    url = "${payment.api.url}",
    configuration = PaymentConfiguration.class
)
public interface PaymentApi {

    String SERVICE_AUTHORIZATION = "ServiceAuthorization";
    String CCD_CASE_NUMBER = "ccd_case_number";
    String SERVICE_NAME = "service_name";
    String RETURN_URL = "return-url";
    String PAYMENT_REFERENCE = "paymentReference";

    @GetMapping(
        value = "/payments",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    PaymentsDto getPayments(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestParam(CCD_CASE_NUMBER) String ccdCaseNumber,
        @RequestParam(SERVICE_NAME) String serviceName
    );

    @PostMapping(
        value = "/card-payments",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    PaymentDto createCardPayment(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
        @RequestHeader(RETURN_URL) String returnUrl,
        @RequestBody CardPaymentRequest cardPaymentRequest
    );

    @GetMapping(
        value = "/card-payments/{paymentReference}",
        headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    PaymentDto getCardPayment(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @RequestHeader(SERVICE_AUTHORIZATION) String serviceAuthorization,
        @PathVariable(PAYMENT_REFERENCE) String paymentReference
    );
}
