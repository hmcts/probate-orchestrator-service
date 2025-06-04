package uk.gov.hmcts.probate.service;

public interface FeatureToggleService {
    boolean isIronMountainInBackOffice();

    boolean isExelaInBackOffice();
}
