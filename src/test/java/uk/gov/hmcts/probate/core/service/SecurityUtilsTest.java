package uk.gov.hmcts.probate.core.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.probate.client.IdamClientApi;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.probate.model.idam.TokenRequest;
import uk.gov.hmcts.reform.probate.model.idam.TokenResponse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class SecurityUtilsTest {

    private static final String SERVICE_TOKEN = "XXXXXX12345";
    private static final String USER_TOKEN = "1312jdhdh";
    private static final String CASEWORKER_PASSWORD = "caseworkerPassword";
    private static final String CASEWORKER_USER_NAME = "caseworkerUserName";
    private static final String SCHEDULER_PASSWORD = "schedulerPassword";
    private static final String SCHEDULER_USER_NAME = "schedulerUserName";
    private static final String AUTH_CLIENT_SECRET = "authClientSecret";
    private static final String AUTH_CLIENT_ID = "authClientId";
    private static final String REDIRECT = "http://redirect";
    public static final String CODE = "CODE_VAL";
    private static final String BEARER = "Bearer ";

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @Mock
    private IdamClientApi idamClient;

    @InjectMocks
    private SecurityUtils securityUtils;

    @Test
    public void shouldGetServiceAuthorisation() {
        when(authTokenGenerator.generate()).thenReturn(SERVICE_TOKEN);

        String serviceAuthorisation = securityUtils.getServiceAuthorisation();

        assertThat(serviceAuthorisation, equalTo(SERVICE_TOKEN));
        verify(authTokenGenerator, atMostOnce()).generate();
    }

    @Test
    public void shouldGetAuthorisation() {
        TestSecurityContextHolder.getContext().setAuthentication(
            new TestingAuthenticationToken("user", USER_TOKEN, "ROLE_USER"));

        String authorisation = securityUtils.getAuthorisation();

        assertThat(authorisation, equalTo(USER_TOKEN));
    }

    @Test
    public void shouldSecurityContextUserAsCaseworker() {
        ReflectionTestUtils.setField(securityUtils, "authRedirectUrl", REDIRECT);
        ReflectionTestUtils.setField(securityUtils, "authClientId", AUTH_CLIENT_ID);
        ReflectionTestUtils.setField(securityUtils, "authClientSecret", AUTH_CLIENT_SECRET);
        ReflectionTestUtils.setField(securityUtils, "caseworkerUserName", CASEWORKER_USER_NAME);
        ReflectionTestUtils.setField(securityUtils, "caseworkerPassword", CASEWORKER_PASSWORD);

        TokenResponse tokenResponse = new TokenResponse(USER_TOKEN,"360000",USER_TOKEN,null,null,null);
        when(idamClient.generateOpenIdToken(any(TokenRequest.class)))
                .thenReturn(tokenResponse);

        securityUtils.setSecurityContextUserAsCaseworker();

        assertThat(securityUtils.getAuthorisation(), equalTo(BEARER + USER_TOKEN));
    }

    @Test
    public void shouldSecurityContextUserAsScheduler() {
        ReflectionTestUtils.setField(securityUtils, "authRedirectUrl", REDIRECT);
        ReflectionTestUtils.setField(securityUtils, "authClientId", AUTH_CLIENT_ID);
        ReflectionTestUtils.setField(securityUtils, "authClientSecret", AUTH_CLIENT_SECRET);
        ReflectionTestUtils.setField(securityUtils, "schedulerUserName", SCHEDULER_USER_NAME);
        ReflectionTestUtils.setField(securityUtils, "schedulerPassword", SCHEDULER_PASSWORD);

        TokenResponse tokenResponse = new TokenResponse(USER_TOKEN,"360000",USER_TOKEN,null,null,null);
        when(idamClient.generateOpenIdToken(any(TokenRequest.class)))
                .thenReturn(tokenResponse);

        securityUtils.setSecurityContextUserAsScheduler();

        assertThat(securityUtils.getAuthorisation(), equalTo(BEARER + USER_TOKEN));
    }

    @Test
    public void shouldReturnCacheTokenAsCaseworker() {
        ReflectionTestUtils.setField(securityUtils, "caseworkerUserName", CASEWORKER_USER_NAME);
        ReflectionTestUtils.setField(securityUtils, "caseworkerPassword", CASEWORKER_PASSWORD);

        TokenResponse tokenResponse = new TokenResponse(USER_TOKEN,"360000",USER_TOKEN,null,null,null);
        when(idamClient.generateOpenIdToken(any(TokenRequest.class)))
                .thenReturn(tokenResponse);

        // first time
        securityUtils.setSecurityContextUserAsCaseworker();

        assertThat(securityUtils.getAuthorisation(), equalTo(BEARER + USER_TOKEN));

        // second time
        securityUtils.setSecurityContextUserAsCaseworker();

        assertThat(securityUtils.getAuthorisation(), equalTo(BEARER + USER_TOKEN));

        verify(idamClient, atMostOnce()).generateOpenIdToken(any(TokenRequest.class));
    }

    @Test
    public void shouldReturnCacheTokenAsScheduler() {
        ReflectionTestUtils.setField(securityUtils, "schedulerUserName", SCHEDULER_USER_NAME);
        ReflectionTestUtils.setField(securityUtils, "schedulerPassword", SCHEDULER_PASSWORD);

        TokenResponse tokenResponse = new TokenResponse(USER_TOKEN,"360000",USER_TOKEN,null,null,null);
        when(idamClient.generateOpenIdToken(any(TokenRequest.class)))
                .thenReturn(tokenResponse);

        // first time
        securityUtils.setSecurityContextUserAsScheduler();

        assertThat(securityUtils.getAuthorisation(), equalTo(BEARER + USER_TOKEN));

        // second time
        securityUtils.setSecurityContextUserAsScheduler();

        assertThat(securityUtils.getAuthorisation(), equalTo(BEARER + USER_TOKEN));

        verify(idamClient, atMostOnce()).generateOpenIdToken(any(TokenRequest.class));
    }

    @Test
    public void shouldSecurityContextUserAsCaseworkerThrowException() {
        when(idamClient.generateOpenIdToken(any(TokenRequest.class)))
                .thenThrow(new NullPointerException());

        assertThrows(NullPointerException.class, () -> {
            securityUtils.setSecurityContextUserAsCaseworker();
        });
    }
}
