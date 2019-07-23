package uk.gov.hmcts.probate.core.service.mapper;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCaseAddress;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToFormAddress;
import uk.gov.hmcts.reform.probate.model.cases.Address;

@Component
public class AddressMapper {

    @ToCaseAddress
    public Address toCaseAddress(uk.gov.hmcts.reform.probate.model.forms.Address formAddress) {
        Address caseAddress = Address.builder()
                .addressLine1(formAddress.getAddressLine1())
                .addressLine2(formAddress.getAddressLine2())
                .addressLine3(formAddress.getAddressLine3())
                .postTown(formAddress.getPostTown())
                .county(formAddress.getCounty())
                .postCode(formAddress.getPostCode())
                .country(formAddress.getCountry())
                .build();
        return caseAddress;
    }

    @ToFormAddress
    public uk.gov.hmcts.reform.probate.model.forms.Address toFormAddress(Address address) {
        uk.gov.hmcts.reform.probate.model.forms.Address formAddress = uk.gov.hmcts.reform.probate.model.forms.Address.builder()
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .addressLine3(address.getAddressLine3())
                .postTown(address.getPostTown())
                .county(address.getCounty())
                .postCode(address.getPostCode())
                .country(address.getCountry())
                .formattedAddress(getFormattedAddress(address))
                .build();
        return formAddress;
    }

    private String getFormattedAddress(Address address) {
        StringBuilder formattedAddress = new StringBuilder();
        if (address.getAddressLine1() != null) {
            formattedAddress.append(address.getAddressLine1() + " ");
        };
        if (address.getAddressLine2() != null) {
            formattedAddress.append(address.getAddressLine2() + " ");
        };
        if (address.getAddressLine3() != null) {
            formattedAddress.append(address.getAddressLine3() + " ");
        };
        if (address.getPostTown() != null) {
            formattedAddress.append(address.getPostTown() + " ");
        };
        if (address.getCounty() != null) {
            formattedAddress.append(address.getCounty() + " ");
        };
        if (address.getPostCode() != null) {
            formattedAddress.append(address.getPostCode() + " ");
        };
        if (address.getCountry() != null) {
            formattedAddress.append(address.getCountry());
        };
        return formattedAddress.toString().trim();
    }
}
