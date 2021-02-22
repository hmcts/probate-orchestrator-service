package uk.gov.hmcts.probate.core.service.mapper;

import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.probate.model.cases.Address;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class AddressMapperTest {

    private AddressMapper addressMapper = new AddressMapper();

    private String formattedAddress = "Line1 Line2 Line3 postTown county postCode country";

    @Test
    void mapCaseAddressToFormAddress() {
        Address caseAddress = new Address();
        caseAddress.setAddressLine1("Line1");
        caseAddress.setAddressLine2("Line2");
        caseAddress.setAddressLine3("Line3");
        caseAddress.setPostTown("postTown");
        caseAddress.setPostCode("postCode");
        caseAddress.setCounty("county");
        caseAddress.setCountry("country");
        uk.gov.hmcts.reform.probate.model.forms.Address formAddress = addressMapper.toFormAddress(caseAddress);
        assertThat(formAddress.getAddressLine1(), is("Line1"));
        assertThat(formAddress.getAddressLine2(), is("Line2"));
        assertThat(formAddress.getAddressLine3(), is("Line3"));
        assertThat(formAddress.getPostTown(), is("postTown"));
        assertThat(formAddress.getPostCode(), is("postCode"));
        assertThat(formAddress.getCounty(), is("county"));
        assertThat(formAddress.getCountry(), is("country"));
        assertThat(formattedAddress, is(formAddress.getFormattedAddress()));
    }

    @Test
    void shouldHandleNullValuMapCaseAddressToFormAddress() {
        uk.gov.hmcts.reform.probate.model.forms.Address formAddress  = addressMapper.toFormAddress(null);
        Assert.isNull(formAddress);
    }

    @Test
    void mapFormAddressToCaseAddress() {
        uk.gov.hmcts.reform.probate.model.forms.Address formAddress =
                new uk.gov.hmcts.reform.probate.model.forms.Address();
        formAddress.setAddressLine1("Line1");
        formAddress.setAddressLine2("Line2");
        formAddress.setAddressLine3("Line3");
        formAddress.setPostTown("postTown");
        formAddress.setPostCode("postCode");
        formAddress.setCounty("county");
        formAddress.setCountry("country");
        Address caseAddress = addressMapper.toCaseAddress(formAddress);
        assertThat(caseAddress.getAddressLine1(), is("Line1"));
        assertThat(caseAddress.getAddressLine2(), is("Line2"));
        assertThat(caseAddress.getAddressLine3(), is("Line3"));
        assertThat(caseAddress.getPostTown(), is("postTown"));
        assertThat(caseAddress.getPostCode(), is("postCode"));
        assertThat(caseAddress.getCounty(), is("county"));
        assertThat(caseAddress.getCountry(), is("country"));
    }

    @Test
    void shouldHandleNullValuMapFormAddressToCaseAddress() {
        Address caseAddress  = addressMapper.toCaseAddress(null);
        Assert.isNull(caseAddress);
    }
}
