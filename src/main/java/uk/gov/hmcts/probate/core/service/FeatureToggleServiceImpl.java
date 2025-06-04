package uk.gov.hmcts.probate.core.service;

import com.launchdarkly.sdk.LDContext;
import com.launchdarkly.sdk.server.interfaces.LDClientInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.probate.service.FeatureToggleService;

@Slf4j
@Service
public class FeatureToggleServiceImpl implements FeatureToggleService {
    private final LDClientInterface ldClient;
    private final LDContext ldContext;

    public static final String IRON_MOUNTAIN_IN_BACK_OFFICE = "probate-iron-mountain-in-back-office";
    public static final String EXELA_IN_BACK_OFFICE = "probate-exela-in-back-office";

    @Autowired
    public FeatureToggleServiceImpl(
            final LDClientInterface ldClient,
            @Value("${ld.user.key}") final String ldUserKey,
            @Value("${ld.user.firstName}") final String ldUserFirstName,
            @Value("${ld.user.lastName}") final String ldUserLastName) {

        final String contextName = new StringBuilder()
                .append(ldUserFirstName)
                .append(" ")
                .append(ldUserLastName)
                .toString();

        this.ldClient = ldClient;
        this.ldContext = LDContext.builder(ldUserKey + "_orchestrator_service")
                .name(contextName)
                .kind("application")
                .set("timestamp", String.valueOf(System.currentTimeMillis()))
                .build();
    }

    private boolean isFeatureToggleOn(
            final String featureToggleCode,
            final boolean defaultValue) {
        return this.ldClient.boolVariation(featureToggleCode, this.ldContext, defaultValue);
    }


    @Override
    public boolean isIronMountainInBackOffice() {
        return this.isFeatureToggleOn(IRON_MOUNTAIN_IN_BACK_OFFICE, false);
    }

    @Override
    public boolean isExelaInBackOffice() {
        return this.isFeatureToggleOn(EXELA_IN_BACK_OFFICE, false);
    }
}
