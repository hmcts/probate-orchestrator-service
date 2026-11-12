
package uk.gov.hmcts.probate.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationExceptionHandlerTest {

    @Mock
    private ObjectMapper objectMapper;

    private AuthenticationExceptionHandler authenticationExceptionHandler;

    @BeforeEach
    void setUp() {
        authenticationExceptionHandler = new AuthenticationExceptionHandler(objectMapper);
    }

    @Test
    void shouldReturnForbiddenStatusWithJsonErrorPayload() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        BadCredentialsException exception = new BadCredentialsException("Invalid credentials");

        String expectedPayload = "{\"code\":\"403\",\"message\":\"Invalid credentials\"}";

        when(objectMapper.writeValueAsString(any())).thenReturn(expectedPayload);

        authenticationExceptionHandler.commence(request, response, exception);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeader(HttpHeaders.CONTENT_TYPE));
        assertEquals(expectedPayload, response.getContentAsString());
    }

    @Test
    void shouldSerializeCorrectErrorPayload() throws IOException {
        ObjectMapper realObjectMapper = new ObjectMapper();
        AuthenticationExceptionHandler handler = new AuthenticationExceptionHandler(realObjectMapper);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        BadCredentialsException exception = new BadCredentialsException("Access denied");

        handler.commence(request, response, exception);

        JsonNode payload = realObjectMapper.readTree(response.getContentAsString());

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeader(HttpHeaders.CONTENT_TYPE));
        assertEquals("403", payload.get("code").asText());
        assertEquals("Access denied", payload.get("message").asText());
        assertEquals(2, payload.size());
    }

    @Test
    void shouldPropagateJsonProcessingException() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        BadCredentialsException exception = new BadCredentialsException("Invalid credentials");

        JsonProcessingException jsonException = new JsonProcessingException("Serialization failed") { };

        when(objectMapper.writeValueAsString(any())).thenThrow(jsonException);

        JsonProcessingException thrown = assertThrows(
                JsonProcessingException.class,
                () -> authenticationExceptionHandler.commence(
                        request, response, exception));

        assertSame(jsonException, thrown);
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getHeader(HttpHeaders.CONTENT_TYPE));
    }
}
