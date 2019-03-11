package uk.gov.hmcts.probate.core.service.mapper;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.probate.model.ProbateType;

import java.util.Map;

@Configuration
public class MapperConfiguration {

    @Autowired
    public IntestacyMapper intestacyMapper;

    @Autowired
    public CaveatMapper caveatMapper;

    @Autowired
    public PaMapper paMapper;

    @Bean
    public Map<ProbateType, FormMapper> mappers() {
        return ImmutableMap.<ProbateType, FormMapper>builder()
            .put(ProbateType.INTESTACY, intestacyMapper)
            .put(ProbateType.CAVEAT, caveatMapper)
            .put(ProbateType.PA, paMapper)
            .build();
    }
}
