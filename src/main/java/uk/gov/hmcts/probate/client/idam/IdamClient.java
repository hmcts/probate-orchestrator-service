package uk.gov.hmcts.probate.client.idam;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.hmcts.probate.model.idam.AuthenticateUserResponse;
import uk.gov.hmcts.probate.model.idam.TokenExchangeResponse;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@FeignClient(
    name = "idamClient",
    url = "${auth.idam.client.baseUrl}",
    qualifier = "idamClient"
)
public interface IdamClient {

    @RequestMapping(
        method = RequestMethod.POST,
        value = "/oauth2/authorize",
        headers = CONTENT_TYPE + "=" + APPLICATION_FORM_URLENCODED_VALUE,
        consumes = APPLICATION_FORM_URLENCODED_VALUE
    )
    AuthenticateUserResponse authenticateUser(
        @RequestHeader(HttpHeaders.AUTHORIZATION) final String authorisation,
        @RequestParam("response_type") final String responseType,
        @RequestParam("client_id") final String clientId,
        @RequestParam("redirect_uri") final String redirectUri
    );

    @RequestMapping(
        method = RequestMethod.POST,
        value = "/oauth2/token",
        headers = CONTENT_TYPE + "=" + APPLICATION_FORM_URLENCODED_VALUE,
        consumes = APPLICATION_FORM_URLENCODED_VALUE
    )
    TokenExchangeResponse exchangeCode(
        @RequestParam("code") final String code,
        @RequestParam("grant_type") final String grantType,
        @RequestParam("redirect_uri") final String redirectUri,
        @RequestParam("client_id") final String clientId,
        @RequestParam("client_secret") final String clientSecret
    );
}
