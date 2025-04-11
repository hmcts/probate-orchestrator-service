package uk.gov.hmcts.probate.schedule;

import feign.FeignException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.probate.core.service.DataExtractDateValidator;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ExelaExtractTaskTest {

    @Mock
    private DataExtractDateValidator dataExtractDateValidator;

    @Mock
    private BackOfficeService backOfficeService;

    private static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @InjectMocks
    private ExelaExtractTask exelaExtractTask;
    private static final String DATE = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
    private String adhocDate = "2022-09-05";
    private String adhocToDate = "2022-09-10";

    @Test
    void shouldPerformExelaExtractDateRange() {
        ResponseEntity<String> responseEntity = ResponseEntity.accepted()
                .body("Perform exela data extract from date finished");
        exelaExtractTask.run();
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals("Perform exela data extract from date finished", responseEntity.getBody());
        verify(dataExtractDateValidator).validate(DATE, DATE);
        verify(backOfficeService).initiateExelaExtractDateRange(DATE, DATE);
    }

    @Test
    void shouldPerformExelaExtractForAdhocDate() {
        exelaExtractTask.adHocJobStartDate = "2022-09-05";
        ResponseEntity<String> responseEntity = ResponseEntity.accepted()
                .body("Perform exela data extract from date finished");
        exelaExtractTask.run();
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals("Perform exela data extract from date finished", responseEntity.getBody());
        verify(dataExtractDateValidator).validate(adhocDate, adhocDate);
        verify(backOfficeService).initiateExelaExtractDateRange(adhocDate, adhocDate);
    }

    @Test
    void shouldPerformExelaExtractForAdhocDateRange() {
        exelaExtractTask.adHocJobStartDate = "2022-09-05";
        exelaExtractTask.adHocJobEndDate = "2022-09-10";
        ResponseEntity<String> responseEntity = ResponseEntity.accepted()
                .body("Perform exela data extract from date finished");
        exelaExtractTask.run();
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals("Perform exela data extract from date finished", responseEntity.getBody());
        verify(dataExtractDateValidator).validate(adhocDate, adhocToDate);
        verify(backOfficeService).initiateExelaExtractDateRange(adhocDate, adhocToDate);
    }

    @Test
    void shouldThrowClientExceptionWithBadRequestForExelaExtractWithIncorrectDateFormat() {
        doThrow(new ApiClientException(HttpStatus.BAD_REQUEST.value(), null)).when(dataExtractDateValidator)
                .dateValidator(DATE, DATE);
        exelaExtractTask.run();
        verify(dataExtractDateValidator).validate(DATE, DATE);
        verifyNoInteractions(backOfficeService);
    }

    @Test
    void shouldThrowFeignExceptionForExelaExtract() {
        String date = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
        when(backOfficeService.initiateExelaExtractDateRange(date, date)).thenThrow(FeignException
                .errorStatus("initiateExelaExtractDateRange", Response.builder()
                        .status(404)
                        .reason("message error")
                        .request(Request.create(
                                Request.HttpMethod.POST,
                                "/data-extract/exela-extract",
                                new HashMap<>(),
                                null,
                                null,
                                null))
                        .body(new byte[0])
                        .build()));
        exelaExtractTask.run();
        verify(dataExtractDateValidator).validate(date,date);
        verify(backOfficeService).initiateExelaExtractDateRange(date, date);
    }

    @Test
    void shouldThrowExceptionForExelaExtract() {
        doThrow(new NullPointerException()).when(dataExtractDateValidator)
                .validate(DATE, DATE);
        exelaExtractTask.run();
        verify(dataExtractDateValidator).validate(DATE, DATE);
        verifyNoInteractions(backOfficeService);
    }

}
