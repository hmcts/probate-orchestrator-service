package uk.gov.hmcts.probate.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DataExtractDateValidatorTest {

    private DataExtractDateValidator dataExtractDateValidator;

    @BeforeEach
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

    @Test
    public void shouldThrowExceptionForInvalidaDate() {
        assertThrows(ApiClientException.class, () -> {
            dataExtractDateValidator.validate("2000-14-31");
        });
    }

    @Test
    public void shouldThrowExceptionForInvalidFromDate() {
        assertThrows(ApiClientException.class, () -> {
            dataExtractDateValidator.validate("2000--31", "2001-12-31");
        });
    }

    @Test
    public void shouldThrowExceptionForInvalidToDate() {
        assertThrows(ApiClientException.class, () -> {
            dataExtractDateValidator.validate("2000-12-31", "2001");
        });
    }

    @Test
    public void shouldThrowExceptionForNullDate() {
        assertThrows(ApiClientException.class, () -> {
            dataExtractDateValidator.validate(null);
        });
    }

    @Test
    public void shouldThrowExceptionForNullFromToDates() {
        assertThrows(ApiClientException.class, () -> {
            dataExtractDateValidator.validate(null, null);
        });
    }

    @Test
    public void shouldThrowExceptionForEmptyFromToDates() {
        assertThrows(ApiClientException.class, () -> {
            dataExtractDateValidator.validate("", "");
        });
    }
}
