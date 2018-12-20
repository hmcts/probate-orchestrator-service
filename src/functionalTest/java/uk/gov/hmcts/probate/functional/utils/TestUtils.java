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
import java.nio.file.Files;


@Component
public class TestUtils {

    @Autowired
    protected TestAuthTokenGenerator testAuthTokenGenerator;

    public String getJsonFromFile(String fileName) {
        try {
            File file = ResourceUtils.getFile(this.getClass().getResource("/json/" + fileName));
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Headers getHeaders(String sessionId) {
        return Headers.headers(
            new Header("Content-Type", ContentType.JSON.toString()),
            new Header("Session-ID", sessionId));
    }

    public Headers submitHeaders(String sessionId) {
        return Headers.headers(
            new Header("Content-Type", ContentType.JSON.toString()),
            new Header("UserId", sessionId),
            new Header("Authorization", "DUMMY_KEY"));
    }

    public Headers getHeaders(String userName, String password) {
        return Headers.headers(
            new Header("ServiceAuthorization", testAuthTokenGenerator.generateServiceAuthorisation()),
            new Header("Content-Type", ContentType.JSON.toString()),
            new Header("Authorization", testAuthTokenGenerator.generateAuthorisation(userName, password)));
    }
}
