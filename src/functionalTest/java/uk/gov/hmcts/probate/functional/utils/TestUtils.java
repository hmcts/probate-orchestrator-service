package uk.gov.hmcts.probate.functional.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import uk.gov.hmcts.probate.functional.TestAuthTokenGenerator;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;


@Component
public class TestUtils {

    @Autowired
    protected TestAuthTokenGenerator testAuthTokenGenerator;

    private ObjectMapper objectMapper;

    private String serviceToken;

    @PostConstruct
    public void init() {
        serviceToken = testAuthTokenGenerator.generateServiceToken();
        objectMapper = new ObjectMapper();
    }

    public JsonNode getJsonNodeFromFile(String fileName) throws IOException {
        File file = ResourceUtils.getFile(this.getClass().getResource("/json/" + fileName));
        return objectMapper.readTree(file);
    }

    public Headers getHeaders() {
        return getHeaders(serviceToken);
    }

    public Headers getHeaders(String serviceToken) {
        return Headers.headers(
                new Header("ServiceAuthorization", serviceToken),
                new Header("Content-Type", ContentType.JSON.toString()));
    }

    public Headers getHeadersWithUserId() {
        return getHeadersWithUserId(serviceToken);
    }

    public Headers getHeadersWithUserId(String serviceToken) {
        return Headers.headers(
                new Header("ServiceAuthorization", serviceToken),
                new Header("Content-Type", ContentType.JSON.toString()),
                new Header("Authorization", testAuthTokenGenerator.generateUserTokenWithNoRoles()));
    }

    public String getUserId() {
        return testAuthTokenGenerator.getUserId();
    }
}
