package uk.gov.hmcts.probate.configuration;

import com.launchdarkly.sdk.server.LDClient;
import com.launchdarkly.sdk.server.interfaces.LDClientInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LDClientConfiguration {
    @Bean
    public LDClientInterface ldClient(@Value("${ld.sdk_key}") String ldSdkKey) {
        return new LDClient(ldSdkKey);
    }
}
