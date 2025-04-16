package uk.gov.hmcts.probate.security;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Feign;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.reform.authorisation.ServiceAuthorisationApi;
import uk.gov.hmcts.reform.authorisation.generators.ServiceAuthTokenGenerator;
import uk.gov.hmcts.reform.authorisation.validators.AuthTokenValidator;
import uk.gov.hmcts.reform.authorisation.validators.ServiceAuthTokenValidator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Slf4j
@Configuration
public class ServiceAuthConfiguration {

    @Bean
    public ServiceAuthTokenGenerator serviceAuthTokenGenerator(@Value("${service.auth.provider.base.url}")
                                                                       String s2sUrl,
                                                               @Value("${s2s.auth.totp.secret}") String secret,
                                                               @Value("${service.name}") String microservice) {
        try {
            final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            final byte[] shaSecret = sha256.digest(secret.getBytes(StandardCharsets.UTF_8));
            final String hexShaSecret = Hex.encodeHexString(shaSecret);

            final String toLog = new StringBuilder()
                    .append("create ServiceAuthTokenGenerator with s2sUrl: [")
                    .append(s2sUrl)
                    .append("], sha256(secret): [")
                    .append(hexShaSecret)
                    .append("], microservice: [")
                    .append(microservice)
                    .append("]")
                    .toString();
            log.info(toLog);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        final ServiceAuthorisationApi serviceAuthorisationApi = Feign.builder()
                .encoder(new JacksonEncoder())
                .contract(new SpringMvcContract())
                .target(ServiceAuthorisationApi.class, s2sUrl);
        return new ServiceAuthTokenGenerator(secret, microservice, serviceAuthorisationApi);
    }

    @Bean
    public AuthTokenValidator tokenValidator(ServiceAuthorisationApi s2sApi) {
        return new ServiceAuthTokenValidator(s2sApi);
    }
}
