package uk.gov.hmcts.probate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import uk.gov.hmcts.reform.auth.checker.core.service.ServiceRequestAuthorizer;
import uk.gov.hmcts.reform.auth.checker.core.user.User;
import uk.gov.hmcts.reform.auth.checker.core.user.UserRequestAuthorizer;
import uk.gov.hmcts.reform.auth.checker.spring.serviceanduser.AuthCheckerServiceAndUserFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private AuthCheckerServiceAndUserFilter authCheckerServiceAndUserFilter;

    private AuthenticationExceptionHandler authenticationExceptionHandler;

    @Autowired
    public SecurityConfiguration(UserRequestAuthorizer<User> userRequestAuthorizer,
                                 ServiceRequestAuthorizer serviceRequestAuthorizer,
                                 AuthenticationManager authenticationManager,
                                 AuthenticationExceptionHandler authenticationExceptionHandler) {
        authCheckerServiceAndUserFilter = new AuthCheckerServiceAndUserFilter(serviceRequestAuthorizer,
            userRequestAuthorizer);
        authCheckerServiceAndUserFilter.setAuthenticationManager(authenticationManager);
        this.authenticationExceptionHandler = authenticationExceptionHandler;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/swagger-ui.html",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/v3/**",
                "/health",
                "/health/liveness",
                "/info",
                "/migrateData",
                "/favicon.ico",
                "/");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .antMatchers("/documents/**")
                .antMatchers("/generate/**")
                .antMatchers("/forms/**")
                .antMatchers("/invite/**")
                .antMatchers("/invites/**")
                .and()
                .addFilter(authCheckerServiceAndUserFilter)
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .csrf().disable()
                .formLogin().disable()
                .logout().disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationExceptionHandler);
    }
}
