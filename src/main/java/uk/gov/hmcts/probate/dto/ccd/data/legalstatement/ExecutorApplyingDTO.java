package uk.gov.hmcts.probate.dto.ccd.data.legalstatement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExecutorApplyingDTO {

    private final String name;

    @JsonProperty(value = "sign")
    private final String sign;

}
