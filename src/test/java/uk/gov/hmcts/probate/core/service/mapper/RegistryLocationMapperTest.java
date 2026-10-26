package uk.gov.hmcts.probate.core.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.probate.model.cases.RegistryLocation;

class RegistryLocationMapperTest {

    RegistryLocationMapper mapper = new RegistryLocationMapper();

    @Test
    void shouldConvertToRegistryLocation() {
        String result = mapper.fromRegistryLocation(RegistryLocation.BIRMINGHAM);
        Assertions.assertThat(result).isEqualTo("Birmingham");
    }

    @Test
    void shouldConvertFromRegistryLocation() {
        RegistryLocation result = mapper.toRegistryLocation("Birmingham");
        Assertions.assertThat(result).isEqualTo(RegistryLocation.BIRMINGHAM);
    }
}
