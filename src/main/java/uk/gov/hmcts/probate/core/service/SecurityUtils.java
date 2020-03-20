package uk.gov.hmcts.probate.core.service;

import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.probate.client.IdamClientApi;
import uk.gov.hmcts.probate.model.idam.AuthenticateUserResponse;
import uk.gov.hmcts.probate.model.idam.TokenExchangeResponse;
import uk.gov.hmcts.reform.auth.checker.spring.serviceanduser.ServiceAndUserDetails;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityUtils {

    private final AuthTokenGenerator authTokenGenerator;

    private static final String BASIC = "Basic ";
    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String CODE = "code";

    @Value("${auth.idam.redirectUrl}")
    private String authRedirectUrl;

    @Value("${auth.idam.clientId}")
    private String authClientId;

    @Value("${auth.idam.clientSecret}")
    private String authClientSecret;

    @Value("${auth.idam.caseworker.username}")
    private String caseworkerUserName;

    @Value("${auth.idam.caseworker.password}")
    private String caseworkerPassword;

    @Value("${auth.idam.scheduler.username}")
    private String schedulerUserName;

    @Value("${auth.idam.scheduler.password}")
    private String schedulerPassword;

    private final IdamClientApi idamClient;

    public String getServiceAuthorisation() {
        return authTokenGenerator.generate();
    }

    public String getAuthorisation() {
        return (String) SecurityContextHolder.getContext()
            .getAuthentication()
            .getCredentials();
    }

    public void setSecurityContextUserAsCaseworker() {
        SecurityContextHolder.getContext()
            .setAuthentication(new UsernamePasswordAuthenticationToken(caseworkerUserName, getCaseworkerToken()));
    }

    public void setSecurityContextUserAsScheduler() {
        SecurityContextHolder.getContext()
            .setAuthentication(new UsernamePasswordAuthenticationToken(schedulerUserName, getSchedulerToken()));
    }

    private String getCaseworkerToken() {
        return getIdamOauth2Token(caseworkerUserName, caseworkerPassword);
    }

    private String getSchedulerToken() {
        return getIdamOauth2Token(schedulerUserName, schedulerPassword);
    }

    private String getIdamOauth2Token(String username, String password) {
        String basicAuthHeader = getBasicAuthHeader(username, password);
        
        log.info("Client ID: {} . Authenticating...", authClientId);

        AuthenticateUserResponse authenticateUserResponse = idamClient.authenticateUser(
            basicAuthHeader,
            CODE,
            authClientId,
            authRedirectUrl
        );

        log.info("Authenticated. Exchanging...");
        TokenExchangeResponse tokenExchangeResponse = idamClient.exchangeCode(
            authenticateUserResponse.getCode(),
            AUTHORIZATION_CODE,
            authRedirectUrl,
            authClientId,
            authClientSecret
        );

        log.info("Getting AccessToken...");
        return tokenExchangeResponse.getAccessToken();
    }

    private String getBasicAuthHeader(String username, String password) {
        String authorisation = username + ":" + password;
        return BASIC + Base64.getEncoder().encodeToString(authorisation.getBytes());
    }
}
