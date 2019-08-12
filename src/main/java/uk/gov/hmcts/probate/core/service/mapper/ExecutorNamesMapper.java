package uk.gov.hmcts.probate.core.service.mapper;

import lombok.experimental.UtilityClass;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;

@UtilityClass
public class ExecutorNamesMapper {

    public String getFullname(Executor executor) {
        if (executor == null) {
            return null; //NOSONAR
        }
        if (executor.getFullName() !=null && !executor.getFullName().isEmpty()) {
            return executor.getFullName();
        } else if (executor.getFirstName() != null && executor.getLastName() != null) {
            return executor.getFirstName() + " " + executor.getLastName();
        }
        return null;
    }
}
