package uk.gov.hmcts.probate.core.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.idam.client.IdamClient;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityUtils {

    private final AuthTokenGenerator authTokenGenerator;

    @Value("${auth.idam.clientId}")
    private String authClientId;

    @Value("${auth.idam.caseworker.username}")
    private String caseworkerUserName;

    @Value("${auth.idam.caseworker.password}")
    private String caseworkerPassword;

    @Value("${auth.idam.scheduler.username}")
    private String schedulerUserName;

    @Value("${auth.idam.scheduler.password}")
    private String schedulerPassword;

    private final IdamClient idamClient;

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
        log.info("Client ID: {} . Authenticating...", authClientId);
        log.info("Getting AccessToken...");
        return idamClient.getAccessToken(username, password);
    }
}
