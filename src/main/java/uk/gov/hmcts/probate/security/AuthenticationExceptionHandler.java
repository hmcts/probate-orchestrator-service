package uk.gov.hmcts.probate.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
@Slf4j
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint, Serializable {

    private final ObjectMapper objectMapper;

    public AuthenticationExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authenticationException) throws IOException {
        log.warn("Forbidden error", authenticationException);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(createErrorPayload(authenticationException));
    }

    private String createErrorPayload(AuthenticationException authenticationException) throws JsonProcessingException {
        ImmutableMap<String, String> payload = ImmutableMap.of("code", String.valueOf(HttpStatus.FORBIDDEN.value()),
                "message", authenticationException.getMessage());
        return objectMapper.writeValueAsString(payload);
    }
}
