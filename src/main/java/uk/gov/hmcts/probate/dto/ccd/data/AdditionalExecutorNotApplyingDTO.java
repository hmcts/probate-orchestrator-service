package uk.gov.hmcts.probate.dto.ccd.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdditionalExecutorNotApplyingDTO {

    private final String notApplyingExecutorName;

    private final String notApplyingExecutorNameOnWill;

    private final String notApplyingExecutorNameDifferenceComment;

    private final String notApplyingExecutorReason;

    private final String notApplyingExecutorNotified;
}
