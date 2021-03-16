package uk.gov.hmcts.probate.core.service.mapper;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromRegistryLocation;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToRegistryLocation;
import uk.gov.hmcts.reform.probate.model.cases.RegistryLocation;

@Component
public class RegistryLocationMapper {


    @FromRegistryLocation
    public String fromRegistryLocation(RegistryLocation location) {
        return location.getName();
    }

    @ToRegistryLocation
    public RegistryLocation toRegistryLocation(String strLocation) {
        return RegistryLocation.valueOf(strLocation.toUpperCase());
    }
}
