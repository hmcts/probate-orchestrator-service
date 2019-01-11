package uk.gov.hmcts.probate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import uk.gov.hmcts.reform.auth.checker.core.RequestAuthorizer;
import uk.gov.hmcts.reform.auth.checker.core.service.Service;
import uk.gov.hmcts.reform.auth.checker.core.user.User;
import uk.gov.hmcts.reform.auth.checker.spring.serviceanduser.AuthCheckerServiceAndUserFilter;


@Profile("SECURITY_MOCK")
@Configuration
@EnableWebSecurity(debug = true)
@Order(1)
public class SecurityMockConfiguration extends WebSecurityConfigurerAdapter {

    private AuthCheckerServiceAndUserFilter authCheckerServiceAndUserFilter;


    private AuthenticationExceptionHandler authenticationExceptionHandler;

    @Autowired
    public SecurityMockConfiguration(RequestAuthorizer<User> userRequestAuthorizer,
                                     RequestAuthorizer<Service> serviceRequestAuthorizer,
                                     AuthenticationManager authenticationManager,
                                     AuthenticationExceptionHandler authenticationExceptionHandler) {
        authCheckerServiceAndUserFilter = new AuthCheckerServiceAndUserFilter(serviceRequestAuthorizer, userRequestAuthorizer);
        authCheckerServiceAndUserFilter.setAuthenticationManager(authenticationManager);
        this.authenticationExceptionHandler = authenticationExceptionHandler;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/forms/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/").permitAll();

    }
}
