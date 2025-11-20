package uk.gov.hmcts.probate.core.service;

import com.launchdarkly.sdk.LDContext;
import com.launchdarkly.sdk.server.interfaces.LDClientInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.probate.service.FeatureToggleService;

@Service
@Slf4j
public class FeatureToggleServiceImpl implements FeatureToggleService {
    private final LDClientInterface ldClient;
    private final LDContext ldContext;

    public FeatureToggleServiceImpl(
            final LDClientInterface ldClient,
            final LDContext ldContext) {
        this.ldClient = ldClient;
        this.ldContext = ldContext;
    }

    private boolean isFeatureToggleOn(
            final String featureToggleCode,
            final boolean defaultValue) {
        return this.ldClient.boolVariation(featureToggleCode, this.ldContext, defaultValue);
    }

    @Override
    public boolean useCcdLookupForPayments() {
        return isFeatureToggleOn("probate-use-ccd-lookup-not-elastic-when-paying", false);
    }
}
