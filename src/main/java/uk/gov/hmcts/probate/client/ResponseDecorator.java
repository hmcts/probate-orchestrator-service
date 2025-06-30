package uk.gov.hmcts.probate.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Util;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.probate.configuration.ApplicationContextProvider;
import uk.gov.hmcts.reform.probate.model.client.ErrorResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ResponseDecorator {

    private Response response;

    private ObjectMapper objectMapper;

    public ResponseDecorator(Response response) {

        this.response = response;
        this.objectMapper = ApplicationContextProvider.getApplicationContext().getBean(ObjectMapper.class);
    }

    public String bodyToString() {
        String apiError = "";
        try {
            if (this.response.body() != null) {
                apiError = Util.toString(this.response.body().asReader(StandardCharsets.UTF_8));
            }
        } catch (IOException ignored) {
            log.debug("Unable to read response body");
        }
        return apiError;
    }

    public ErrorResponse mapToErrorResponse() {
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse errorResponse = null;
        try {
            errorResponse = mapper.readValue(this.bodyToString(), ErrorResponse.class);
        } catch (IOException e) {
            log.debug("Response contained empty body");
        }
        return errorResponse;
    }
}
