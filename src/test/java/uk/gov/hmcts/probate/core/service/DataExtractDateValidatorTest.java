package uk.gov.hmcts.probate.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DataExtractDateValidatorTest {

    private DataExtractDateValidator dataExtractDateValidator;

    @BeforeEach
    void setup() {
        dataExtractDateValidator = new DataExtractDateValidator();
    }

    @Test
    void shouldValidateDate() {
        dataExtractDateValidator.validate("2000-12-31");
    }

    @Test
    void shouldValidateEmptyFromDate() {
        dataExtractDateValidator.validate("", "2000-12-31");
    }

    @Test
    void shouldValidatenullFromDate() {
        dataExtractDateValidator.validate(null, "2000-12-31");
    }

    @Test
    void shouldValidateDateFromTo() {
        dataExtractDateValidator.validate("2000-12-31", "2001-12-31");
    }

    @Test
    void shouldValidateDateFromToSame() {
        dataExtractDateValidator.validate("2000-12-31", "2000-12-31");
    }

    @Test
    void shouldThrowExceptionForInvalidaDate() {
        assertThrows(ApiClientException.class, () -> {
            dataExtractDateValidator.validate("2000-14-31");
        });
    }

    @Test
    void shouldThrowExceptionForInvalidFromDate() {
        assertThrows(ApiClientException.class, () -> {
            dataExtractDateValidator.validate("2000--31", "2001-12-31");
        });
    }

    @Test
    void shouldThrowExceptionForInvalidToDate() {
        assertThrows(ApiClientException.class, () -> {
            dataExtractDateValidator.validate("2000-12-31", "2001");
        });
    }

    @Test
    void shouldThrowExceptionForNullDate() {
        assertThrows(ApiClientException.class, () -> {
            dataExtractDateValidator.validate(null);
        });
    }

    @Test
    void shouldThrowExceptionForNullFromToDates() {
        assertThrows(ApiClientException.class, () -> {
            dataExtractDateValidator.validate(null, null);
        });
    }

    @Test
    void shouldThrowExceptionForEmptyFromToDates() {
        assertThrows(ApiClientException.class, () -> {
            dataExtractDateValidator.validate("", "");
        });
    }

    @Test
    void shouldThrowExceptionForFromDateBefore() {
        ApiClientException exception = assertThrows(ApiClientException.class, () -> {
            dataExtractDateValidator.validate("2001-12-31", "2000-12-31");

        });
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatus());
    }
}
