package uk.gov.hmcts.probate.core.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.probate.model.client.ApiClientError;
import uk.gov.hmcts.reform.probate.model.client.ApiClientErrorResponse;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class DataExtractDateValidator {
    private static final Logger log = LoggerFactory.getLogger(DataExtractDateValidator.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void validate(String date) {
        validate(null, date);
    }

    public void validate(String fromDate, String toDate) {
        if (StringUtils.isBlank(toDate)) {
            throw buildClientException(HttpStatus.BAD_REQUEST.value(),
                "Error on extract dates, toDate is null or empty");
        }
        try {
            LocalDate to = LocalDate.parse(toDate, DATE_FORMAT);
            if (!StringUtils.isBlank(fromDate)) {
                LocalDate from = LocalDate.parse(fromDate, DATE_FORMAT);
                if (!from.isEqual(to) && !from.isBefore(to)) {
                    throw buildClientException(HttpStatus.BAD_REQUEST.value(),
                        "Error on extract dates, fromDate is not before toDate: " + fromDate + "," + toDate);
                }
            }
        } catch (DateTimeParseException e) {
            log.error("Error parsing date, use the format of 'yyyy-MM-dd': ");
            throw buildClientException(HttpStatus.BAD_REQUEST.value(),
                "Error parsing date, use the format of 'yyyy-MM-dd': " + e.getMessage());
        }
    }

    private ApiClientException buildClientException(int httpStatus, String errorMessage) {
        ApiClientError apiClientError = new ApiClientError(errorMessage, httpStatus, errorMessage, null);
        return new ApiClientException(HttpStatus.BAD_REQUEST.value(),
            ApiClientErrorResponse.builder().apiClientError(apiClientError).build());

    }
}
