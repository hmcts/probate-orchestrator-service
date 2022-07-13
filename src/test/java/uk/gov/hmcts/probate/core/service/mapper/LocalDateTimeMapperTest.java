package uk.gov.hmcts.probate.core.service.mapper;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalDateTimeMapperTest {

    private LocalDateTimeMapper localDateTimeMapper = new LocalDateTimeMapper();

    @Test
    void shouldConvertLocalDateTimeToLocalDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate date = localDateTimeMapper.convertLocalDateTimeToLocalDate(localDateTime);
        assertEquals(localDateTime.toLocalDate(), date);
    }

    @Test
    void shouldReturnNullWhenLocalDateTimeIsNull() {
        assertThat(localDateTimeMapper.convertLocalDateTimeToLocalDate(null), is(nullValue()));
    }

    @Test
    void shouldConvertLocalDateToLocalDateTime() {
        LocalDate localDate = LocalDate.now();
        LocalDateTime date = localDateTimeMapper.convertLocalDateToLocalDateTime(localDate);
        assertEquals(localDate.atStartOfDay(), date);
    }

    @Test
    void shouldReturnNullWhenLocalDateIsNull() {
        assertThat(localDateTimeMapper.convertLocalDateToLocalDateTime(null), is(nullValue()));
    }
}
