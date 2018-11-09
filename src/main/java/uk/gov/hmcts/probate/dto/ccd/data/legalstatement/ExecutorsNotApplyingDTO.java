package uk.gov.hmcts.probate.dto.ccd.data.legalstatement;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExecutorsNotApplyingDTO {

    private final ExecutorNotApplyingDTO value;

    private final String id;

}
