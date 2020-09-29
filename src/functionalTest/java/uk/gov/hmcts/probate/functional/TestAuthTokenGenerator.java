package uk.gov.hmcts.probate.functional;


import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.authorisation.generators.ServiceAuthTokenGenerator;

import java.util.Base64;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Component
public class TestAuthTokenGenerator {

    @Value("${idam.oauth2.redirect_uri}")
    private String redirectUri;

    @Value("${user.auth.provider.oauth2.url}")
    private String baseServiceOauth2Url;
    String clientToken;

    @Value("${idam.client}")
    private String clientId;

    @Value("${idam.secret}")
    private String secret;

    @Value("${user.auth.provider.oauth2.url}")
    private String idamUserBaseUrl;

    private String userToken;

    private String idamCreateUrl() {
        return idamUserBaseUrl + "/testing-support/accounts";
    }

    @Autowired
    private ServiceAuthTokenGenerator tokenGenerator;

    public String generateServiceAuthorisation() {
        return tokenGenerator.generate();
    }

    public String getUserId() {
        return "" + RestAssured.given()
            .header("Authorization", userToken)
            .get(idamUserBaseUrl + "/details")
            .body()
            .path("id");
    }

    public String generateAuthorisation(String userName, String password) {
        return generateClientToken(userName, password);
    }

    private String generateClientToken(String userName, String password) {
        String code = generateClientCode(userName, password);

        if (code == null) {
            throw new RuntimeException("No code from IDAM");
        }

        String token = RestAssured.given().post(idamUserBaseUrl + "/oauth2/token?code=" + code +
            "&client_secret=" + secret +
            "&client_id=" + clientId +
            "&redirect_uri=" + redirectUri +
            "&grant_type=authorization_code")
            .body().path("access_token");
        return token;
    }

    private String generateClientCode(String userName, String password) {
        final String encoded = Base64.getEncoder().encodeToString((userName + ":" + password).getBytes());
        return RestAssured.given().baseUri(idamUserBaseUrl)
            .header("Authorization", "Basic " + encoded)
            .post("/oauth2/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri)
            .prettyPeek()
            .body().path("code");
    }
}
