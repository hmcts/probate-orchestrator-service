package uk.gov.hmcts.probate.core.service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import uk.gov.hmcts.probate.client.IdamClientApi;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.probate.model.idam.TokenRequest;
import uk.gov.hmcts.reform.probate.model.idam.TokenResponse;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityUtils {

    private final AuthTokenGenerator authTokenGenerator;

    private static final String BEARER = "Bearer ";
    private static final String OPENID_GRANT_TYPE = "password";

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
    private TokenResponse cacheCaseworkerTokenResponse;
    private TokenResponse cacheSchedulerTokenResponse;

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
        if (ObjectUtils.isEmpty(cacheCaseworkerTokenResponse) || isExpired(cacheCaseworkerTokenResponse)) {
            log.info("No cached Case Worker IDAM token found, requesting from IDAM service.");
            cacheCaseworkerTokenResponse = getOpenIdTokenResponse(caseworkerUserName, caseworkerPassword);
        } else {
            log.info("Using cached Case Worker IDAM token.");
        }
        log.info("Getting AccessToken...");
        return BEARER + cacheCaseworkerTokenResponse.accessToken;
    }

    private String getSchedulerToken() {
        if (ObjectUtils.isEmpty(cacheSchedulerTokenResponse) || isExpired(cacheSchedulerTokenResponse)) {
            log.info("No cached Scheduler IDAM token found, requesting from IDAM service.");
            cacheSchedulerTokenResponse = getOpenIdTokenResponse(schedulerUserName, schedulerPassword);
        } else {
            log.info("Using cached Scheduler IDAM token.");
        }
        log.info("Getting AccessToken...");
        return BEARER + cacheSchedulerTokenResponse.accessToken;
    }

    private TokenResponse getOpenIdTokenResponse(String username, String password) {
        log.info("Client ID: {} . Authenticating...", authClientId);
        try {
            TokenResponse tokenResponse = idamClient.generateOpenIdToken(
                    new TokenRequest(
                            authClientId,
                            authClientSecret,
                            OPENID_GRANT_TYPE,
                            authRedirectUrl,
                            username,
                            password,
                            "openid profile roles",
                            null,
                            null
                    ));
            return tokenResponse;
        } catch (Exception e) {
            log.error("Exception" + e.getMessage());
            throw e;
        }
    }

    private boolean isExpired(TokenResponse tokenResponse) {
        Instant now = Instant.now();
        Instant expiresAt = ZonedDateTime.parse(tokenResponse.expiresIn).toInstant();
        return now.isAfter(expiresAt.minus(Duration.ofMinutes(1L)));
    }
}
