package uk.gov.hmcts.probate.core.service.mapper;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromLocalDate;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToLocalDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class LocalDateTimeMapper {

    @ToLocalDate
    public LocalDate convertLocalDateTimeToLocalDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null; //NOSONAR
        }
        return localDateTime.toLocalDate();
    }

    @FromLocalDate
    public LocalDateTime convertLocalDateToLocalDateTime(LocalDate localDate) {
        if (localDate == null) {
            return null; //NOSONAR
        }
        return localDate.atStartOfDay();
    }
}
