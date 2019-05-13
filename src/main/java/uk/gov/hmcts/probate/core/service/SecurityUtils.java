package uk.gov.hmcts.probate.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.auth.checker.spring.serviceanduser.ServiceAndUserDetails;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

@Component
public class SecurityUtils {

    private final AuthTokenGenerator authTokenGenerator;

    @Autowired
    public SecurityUtils(AuthTokenGenerator authTokenGenerator) {
        this.authTokenGenerator = authTokenGenerator;
    }

    public String getServiceAuthorisation() {
        return authTokenGenerator.generate();
    }

    public String getAuthorisation() {
        return (String) SecurityContextHolder.getContext()
            .getAuthentication()
            .getCredentials();
    }

    public String getUserId() {
        checkSecurityContext();
        return ((ServiceAndUserDetails) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal())
            .getUsername();
    }

    private void checkSecurityContext() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new NoSecurityContextException();
        }
    }
}
