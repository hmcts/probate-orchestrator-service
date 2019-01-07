package uk.gov.hmcts.probate.core.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.test.context.TestSecurityContextHolder;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SecurityUtilsTest {

    private static final String SERVICE_TOKEN = "XXXXXX12345";
    private static final String USER_TOKEN = "1312jdhdh";

    @Mock
    private AuthTokenGenerator authTokenGenerator;

    @InjectMocks
    private SecurityUtils securityUtils;

    @Test
    public void shouldGetServiceAuthorisation() {
        when(authTokenGenerator.generate()).thenReturn(SERVICE_TOKEN);

        String serviceAuthorisation = securityUtils.getServiceAuthorisation();

        assertThat(serviceAuthorisation, equalTo(SERVICE_TOKEN));
        verify(authTokenGenerator, times(1)).generate();
    }

    @Test
    public void shouldGetAuthorisation() {
        TestSecurityContextHolder.getContext().setAuthentication(
            new TestingAuthenticationToken("user", USER_TOKEN, "ROLE_USER"));

        String authorisation = securityUtils.getAuthorisation();

        assertThat(authorisation, equalTo(USER_TOKEN));
    }
}
