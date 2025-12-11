package uk.gov.hmcts.probate.core.service;

import com.launchdarkly.sdk.LDContext;
import com.launchdarkly.sdk.server.interfaces.LDClientInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.probate.service.FeatureToggleService;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class FeatureToggleServiceImpl implements FeatureToggleService {
    private final LDClientInterface ldClient;
    private final LDContext ldContext;

    private final AtomicBoolean cacheConfirmFeatureToggle;
    private final AtomicInteger logLimit;

    static final String CONFIRM_FEATURE = "probate-confirm-feature-toggle";
    static final String PAYMENT_LOOKUP_FEATURE = "probate-use-ccd-lookup-not-elastic-when-paying";

    public FeatureToggleServiceImpl(
            final LDClientInterface ldClient,
            final LDContext ldContext) {
        this.ldClient = ldClient;
        this.ldContext = ldContext;

        this.cacheConfirmFeatureToggle = new AtomicBoolean(false);
        this.logLimit = new AtomicInteger(0);
    }

    private boolean isFeatureToggleOn(
            final String featureToggleCode,
            final boolean defaultValue) {
        return this.ldClient.boolVariation(featureToggleCode, this.ldContext, defaultValue);
    }

    void confirmFeatureToggle() {
        final boolean confirmFeatureToggle = isFeatureToggleOn(CONFIRM_FEATURE, false);
        final int currentLimit = logLimit.getAndIncrement();
        if (cacheConfirmFeatureToggle.compareAndSet(!confirmFeatureToggle, confirmFeatureToggle)) {
            log.info("confirmFeatureToggle is now {}, was previously inverted", confirmFeatureToggle);
        } else {
            if (currentLimit % 128 == 0) {
                log.info("confirmFeatureToggle is {} (called {} times)", confirmFeatureToggle, currentLimit);
            }
        }
    }

    @Override
    public boolean useCcdLookupForPayments() {
        confirmFeatureToggle();
        return isFeatureToggleOn(PAYMENT_LOOKUP_FEATURE, false);
    }
}
