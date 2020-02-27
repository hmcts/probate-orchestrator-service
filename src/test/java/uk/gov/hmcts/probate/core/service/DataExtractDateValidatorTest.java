package uk.gov.hmcts.probate.core.service;

import org.junit.Before;
import org.junit.Test;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

public class DataExtractDateValidatorTest {

    private DataExtractDateValidator dataExtractDateValidator;

    @Before
    public void setup() {
        dataExtractDateValidator = new DataExtractDateValidator();
    }

    @Test
    public void shouldValidateDate() {
        dataExtractDateValidator.validate("2000-12-31");
    }

    @Test
    public void shouldValidateEmptyFromDate() {
        dataExtractDateValidator.validate("", "2000-12-31");
    }

    @Test
    public void shouldValidatenullFromDate() {
        dataExtractDateValidator.validate(null, "2000-12-31");
    }

    @Test
    public void shouldValidateDateFromTo() {
        dataExtractDateValidator.validate("2000-12-31", "2001-12-31");
    }

    @Test
    public void shouldValidateDateFromToSame() {
        dataExtractDateValidator.validate("2000-12-31", "2000-12-31");
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowExceptionForInvalidaDate() {
        dataExtractDateValidator.validate("2000-14-31");
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowExceptionForInvalidFromDate() {
        dataExtractDateValidator.validate("2000--31", "2001-12-31");
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowExceptionForInvalidToDate() {
        dataExtractDateValidator.validate("2000-12-31", "2001");
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowExceptionForNullDate() {
        dataExtractDateValidator.validate(null);
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowExceptionForNullFromToDates() {
        dataExtractDateValidator.validate(null, null);
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowExceptionForEmptyFromToDates() {
        dataExtractDateValidator.validate("", "");
    }
}