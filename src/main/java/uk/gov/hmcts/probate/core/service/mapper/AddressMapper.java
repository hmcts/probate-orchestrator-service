package uk.gov.hmcts.probate.core.service.mapper;

import org.apache.commons.lang3.BooleanUtils;
import uk.gov.hmcts.reform.probate.model.cases.Address;

public class AddressMapper {

    public static String getAddress(Address address) {
        if (address == null) {
            return null; //NOSONAR
        }
        return address.getAddressLine1();
    }

    public static String getPostCode(Address address) {
        if (address == null) {
            return null; //NOSONAR
        }
        return address.getPostCode();
    }

    public static String getFreeTextAddress(Boolean addressFound, Address address) {
        if (BooleanUtils.isFalse(addressFound)) {
            return getAddress(address);
        }
        return null;
    }

    public static String getPostCodeAddress(Boolean addressFound, Address address) {
        if (BooleanUtils.isTrue(addressFound)) {
            return getAddress(address);
        }
        return null;
    }
}
