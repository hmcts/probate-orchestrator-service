package uk.gov.hmcts.probate.core.service.mapper;

import com.google.common.collect.ImmutableMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.probate.model.ProbateType;

import java.util.Map;

@Configuration
public class MapperConfiguration {

    @Bean
    public Map<ProbateType, FormMapper> mappers(IntestacyMapper intestacyMapper, CaveatMapper caveatMapper,
                                                PaMapper paMapper) {
        return ImmutableMap.<ProbateType, FormMapper>builder()
            .put(ProbateType.INTESTACY, intestacyMapper)
            .put(ProbateType.CAVEAT, caveatMapper)
            .put(ProbateType.PA, paMapper)
            .build();
    }
}
