package uk.gov.hmcts.probate.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import uk.gov.hmcts.probate.client.IdamClientApi;
import uk.gov.hmcts.probate.model.exception.InvalidTokenException;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.authorisation.validators.AuthTokenValidator;
import uk.gov.hmcts.reform.probate.model.idam.TokenRequest;
import uk.gov.hmcts.reform.probate.model.idam.TokenResponse;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityUtils {

    private static final String BASIC = "Basic ";
    private static final String BEARER = "Bearer ";
    private static final String OPENID_GRANT_TYPE = "password";
    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String CODE = "code";
    private final AuthTokenGenerator authTokenGenerator;
    private final AuthTokenValidator authTokenValidator;
    private final List<String> allowedToUpdateDetails;
    private final IdamClientApi idamClient;
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

    private TokenResponse cacheCaseworkerTokenResponse;
    private TokenResponse cacheSchedulerTokenResponse;

    @Autowired
    public SecurityUtils(IdamClientApi idamClient,
                         AuthTokenValidator authTokenValidator,
                         AuthTokenGenerator authTokenGenerator,
                         @Value("${auth.idam.s2s-auth.services-allowed-to-payment-update}")
                                 List<String> allowedToUpdateDetails) {
        this.authTokenGenerator = authTokenGenerator;
        this.authTokenValidator = authTokenValidator;
        this.allowedToUpdateDetails = allowedToUpdateDetails;
        this.idamClient = idamClient;
    }

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
        Instant expiresAt = ZonedDateTime.parse(tokenResponse.getExpiresAtTime()).toInstant();
        return now.isAfter(expiresAt.minus(Duration.ofMinutes(1L)));
    }

    public Boolean checkIfServiceIsAllowed(String token) throws InvalidTokenException {
        String serviceName = this.authenticate(token);
        if (Objects.nonNull(serviceName)) {
            return allowedToUpdateDetails.contains(serviceName);
        } else {
            log.info("Service name from token is null");
            return Boolean.FALSE;
        }
    }

    public String getBearerToken(String token) {
        if (StringUtils.isBlank(token)) {
            return token;
        }

        return token.startsWith(BEARER) ? token : BEARER.concat(token);
    }

    public String authenticate(String authHeader) throws InvalidTokenException {
        if (isBlank(authHeader)) {
            throw new InvalidTokenException("Provided S2S token is missing or invalid");
        }
        String bearerAuthToken = getBearerToken(authHeader);
        log.info("S2S token found in the request");

        return authTokenValidator.getServiceName(bearerAuthToken);
    }
}
