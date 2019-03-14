package uk.gov.hmcts.probate.core.service.mapper;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.probate.model.cases.Address;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaDeceased;

@Component
public class DeceasedAddressMapper {


    public Address convert(PaDeceased paDeceased) {
        return Address.builder()
            .addressLine1(paDeceased.getAddress())
            .postCode(paDeceased.getPostcode())
            .build();
    }
}
