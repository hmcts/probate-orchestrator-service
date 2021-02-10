package uk.gov.hmcts.probate.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import uk.gov.hmcts.reform.auth.checker.core.RequestAuthorizer;
import uk.gov.hmcts.reform.auth.checker.core.service.Service;
import uk.gov.hmcts.reform.auth.checker.core.user.User;
import uk.gov.hmcts.reform.auth.checker.spring.serviceanduser.AuthCheckerServiceAndUserFilter;
import uk.gov.hmcts.reform.auth.checker.spring.serviceonly.AuthCheckerServiceOnlyFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Configuration
    @Order(1)
    public static class AuthCheckerServiceAndUserFilterConfigurerAdapter extends WebSecurityConfigurerAdapter {

        private final AuthCheckerServiceAndUserFilter authCheckerServiceAndUserFilter;

        private AuthenticationExceptionHandler authenticationExceptionHandler;

        public AuthCheckerServiceAndUserFilterConfigurerAdapter(RequestAuthorizer<User> userRequestAuthorizer,
                                                                RequestAuthorizer<Service> serviceRequestAuthorizer,
                                                                AuthenticationManager authenticationManager,
                                                                AuthenticationExceptionHandler authenticationExceptionHandler) {
            authCheckerServiceAndUserFilter = new AuthCheckerServiceAndUserFilter(serviceRequestAuthorizer, userRequestAuthorizer);
            authCheckerServiceAndUserFilter.setAuthenticationManager(authenticationManager);
            this.authenticationExceptionHandler = authenticationExceptionHandler;
        }



        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers("/swagger-ui.html",
                    "/webjars/springfox-swagger-ui/**",
                    "/swagger-resources/**",
                    "/v2/**",
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
                    .antMatchers("/grant/**")
                    .antMatchers("/caveat/**")
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


    @Configuration
    @Order(2)
    public static class AuthCheckerServiceOnlyFilterConfigurerAdapter extends WebSecurityConfigurerAdapter {

        private AuthCheckerServiceOnlyFilter authCheckerServiceOnlyFilter;

        private AuthenticationExceptionHandler authenticationExceptionHandler;

        public AuthCheckerServiceOnlyFilterConfigurerAdapter(RequestAuthorizer<Service> serviceRequestAuthorizer,
                                                             AuthenticationManager authenticationManager,
                                                             AuthenticationExceptionHandler authenticationExceptionHandler) {
            authCheckerServiceOnlyFilter = new AuthCheckerServiceOnlyFilter(serviceRequestAuthorizer);
            authCheckerServiceOnlyFilter.setAuthenticationManager(authenticationManager);
            this.authenticationExceptionHandler = authenticationExceptionHandler;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                    .requestMatchers()
                    .antMatchers("/payment-updates")
                    .and()
                    .addFilter(authCheckerServiceOnlyFilter)
                    .sessionManagement().sessionCreationPolicy(STATELESS).and()
                    .formLogin().disable()
                    .logout().disable()
                    .exceptionHandling()
                    .authenticationEntryPoint(authenticationExceptionHandler);

        }

        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers("/swagger-ui.html",
                    "/swagger-resources/**",
                    "/webjars/springfox-swagger-ui/**",
                    "/v2/api-docs",
                    "/health",
                    "/health/liveness",
                    "/info",
                    "/data-extract/**",
                    "/");
        }
    }

}