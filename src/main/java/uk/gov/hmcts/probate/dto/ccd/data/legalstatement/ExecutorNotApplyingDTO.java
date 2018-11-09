package uk.gov.hmcts.probate.dto.ccd.data.legalstatement;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExecutorNotApplyingDTO {

    private final String executor;
}
