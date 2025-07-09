package uk.gov.hmcts.probate.client.submit;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import uk.gov.hmcts.probate.client.ResponseDecorator;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;
import uk.gov.hmcts.reform.probate.model.client.ErrorResponse;


@Slf4j
public class SubmitServiceApiErrorDecoder implements ErrorDecoder {

    private ObjectMapper objectMapper;

    public SubmitServiceApiErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("Response status: {}", response.status());

        ResponseDecorator responseDecorator = new ResponseDecorator(response,this.objectMapper);
        ErrorResponse errorResponse = responseDecorator.mapToErrorResponse();

        return new ApiClientException(response.status(), errorResponse);
    }
}
