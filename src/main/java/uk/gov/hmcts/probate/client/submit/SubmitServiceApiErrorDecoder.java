package uk.gov.hmcts.probate.client.submit;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.hmcts.probate.client.ResponseDecorator;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;
import uk.gov.hmcts.reform.probate.model.client.ErrorResponse;


public class SubmitServiceApiErrorDecoder implements ErrorDecoder {
    private static final Logger log = LoggerFactory.getLogger(SubmitServiceApiErrorDecoder.class);

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
