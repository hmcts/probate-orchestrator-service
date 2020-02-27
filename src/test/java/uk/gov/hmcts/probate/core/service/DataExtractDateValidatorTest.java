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
        dataExtractDateValidator.dateValidator("2000-12-31");
    }

    @Test
    public void shouldValidateEmptyFromDate() {
        dataExtractDateValidator.dateValidator("", "2000-12-31");
    }

    @Test
    public void shouldValidatenullFromDate() {
        dataExtractDateValidator.dateValidator(null, "2000-12-31");
    }

    @Test
    public void shouldValidateDateFromTo() {
        dataExtractDateValidator.dateValidator("2000-12-31", "2001-12-31");
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowExceptionForInvalidaDate() {
        dataExtractDateValidator.dateValidator("2000-14-31");
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowExceptionForInvalidFromDate() {
        dataExtractDateValidator.dateValidator("2000--31", "2001-12-31");
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowExceptionForInvalidToDate() {
        dataExtractDateValidator.dateValidator("2000-12-31", "2001");
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowExceptionForToDateNotAfterFromDate() {
        dataExtractDateValidator.dateValidator("2000-12-31", "2000-12-31");
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowExceptionForNullDate() {
        dataExtractDateValidator.dateValidator(null);
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowExceptionForNullFromToDates() {
        dataExtractDateValidator.dateValidator(null, null);
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowExceptionForEmptyFromToDates() {
        dataExtractDateValidator.dateValidator("", "");
    }
}