package uk.gov.hmcts.probate.core.service.mapper;

import com.google.common.collect.ImmutableMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.probate.core.service.mapper.migration.LegacyFormMapper;
import uk.gov.hmcts.probate.core.service.mapper.migration.LegacyIntestacyMapper;
import uk.gov.hmcts.probate.core.service.mapper.migration.LegacyPaMapper;

import java.util.Map;

@Configuration
public class LegacyMapperConfiguration {

    @Bean
    public Map<String, LegacyFormMapper> legacyMappers(LegacyIntestacyMapper intestacyMapper, LegacyPaMapper paMapper) {
        return ImmutableMap.<String, LegacyFormMapper>builder()
            .put("gop", paMapper)
            .put("intestacy", intestacyMapper)
            .build();
    }
}
