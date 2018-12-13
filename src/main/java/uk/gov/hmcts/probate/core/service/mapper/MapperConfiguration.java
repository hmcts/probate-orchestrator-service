package uk.gov.hmcts.probate.core.service.mapper;

import com.google.common.collect.ImmutableMap;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.probate.model.ProbateType;

import java.util.Map;

@Configuration
public class MapperConfiguration {

    @Bean
    public Map<ProbateType, FormMapper> mappers() {
        return ImmutableMap.<ProbateType, FormMapper>builder()
            .put(ProbateType.INTESTACY, Mappers.getMapper(IntestacyMapper.class))
            .build();
    }
}
