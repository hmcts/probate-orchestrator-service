package uk.gov.hmcts.probate.core.service.mapper;

import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;

public final class ExecutorNamesMapper {

    private ExecutorNamesMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static String getFullname(Executor executor) {
        if (executor == null) {
            return null; //NOSONAR
        }
        if (executor.getFullName() != null && !executor.getFullName().isEmpty()) {
            return executor.getFullName();
        } else if (executor.getFirstName() != null && executor.getLastName() != null) {
            return executor.getFirstName() + " " + executor.getLastName();
        }
        return null;
    }
}
