package uk.gov.hmcts.probate.core.service.fees;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FeesCalculation {

    private BigDecimal amount;

    private String code;

    private String version;

}
