package uk.gov.hmcts.probate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import uk.gov.hmcts.reform.auth.checker.core.service.ServiceRequestAuthorizer;
import uk.gov.hmcts.reform.auth.checker.core.user.User;
import uk.gov.hmcts.reform.auth.checker.core.user.UserRequestAuthorizer;
import uk.gov.hmcts.reform.auth.checker.spring.serviceanduser.AuthCheckerServiceAndUserFilter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthCheckerServiceAndUserFilter authCheckerServiceAndUserFilter;
    private final AuthenticationExceptionHandler authenticationExceptionHandler;

    @Autowired
    public SecurityConfiguration(UserRequestAuthorizer<User> userRequestAuthorizer,
                                 ServiceRequestAuthorizer serviceRequestAuthorizer,
                                 AuthenticationManager authenticationManager,
                                 AuthenticationExceptionHandler authenticationExceptionHandler) {
        this.authCheckerServiceAndUserFilter =
                new AuthCheckerServiceAndUserFilter(serviceRequestAuthorizer, userRequestAuthorizer);
        this.authCheckerServiceAndUserFilter.setAuthenticationManager(authenticationManager);
        this.authenticationExceptionHandler = authenticationExceptionHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/v3/**",
                                "/health",
                                "/health/liveness",
                                "/info",
                                "/migrateData",
                                "/favicon.ico",
                                "/"
                        ).permitAll()
                        .requestMatchers(
                                "/documents/**",
                                "/generate/**",
                                "/forms/**",
                                "/invite/**",
                                "/invites/**"
                        ).authenticated()
                        .anyRequest().authenticated()
                )
                .addFilter(authCheckerServiceAndUserFilter)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationExceptionHandler)
                );

        return http.build();
    }
}
