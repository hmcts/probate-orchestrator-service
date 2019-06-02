package uk.gov.hmcts.probate.core.service.fees;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.probate.model.ProbateType;

import java.util.Map;

@Configuration
public class FeesCalculatorConfiguration {

    @Autowired
    private PaFeesCalculator paFeesCalculator;

    @Bean
    public Map<ProbateType, FeesCalculator> feeCalculatorMap() {
        return ImmutableMap.<ProbateType, FeesCalculator>builder()
            .put(ProbateType.PA, paFeesCalculator)
            .build();
    }
}
