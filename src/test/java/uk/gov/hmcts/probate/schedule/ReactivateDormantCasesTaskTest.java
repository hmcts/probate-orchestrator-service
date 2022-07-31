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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(SpringExtension.class)
class ReactivateDormantCasesTaskTest {

    @Mock
    private DataExtractDateValidator dataExtractDateValidator;

    @Mock
    private BackOfficeService backOfficeService;

    @InjectMocks
    private ReactivateDormantCasesTask reactivateDormantCasesTask;

    private static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    void shouldReactivateDormantCasesDateRange() {

        String date = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
        ResponseEntity<String> responseEntity = ResponseEntity.accepted()
                .body("Perform reactivate dormant finished");
        when(backOfficeService.reactivateDormant(date)).thenReturn(responseEntity);
        reactivateDormantCasesTask.run();
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals("Perform reactivate dormant finished", responseEntity.getBody());
        verify(dataExtractDateValidator).validate(date, date);
        verify(backOfficeService).reactivateDormant(date);
    }

    @Test
    void shouldThrowClientExceptionWithBadRequestForReactivateDormantCasesWithIncorrectDateFormat() {
        String date = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
        doThrow(new ApiClientException(HttpStatus.BAD_REQUEST.value(), null)).when(dataExtractDateValidator)
                .validate(date, date);
        reactivateDormantCasesTask.run();
        verify(dataExtractDateValidator).validate(date, date);
        verifyNoInteractions(backOfficeService);
    }

    @Test
    void shouldThrowFeignExceptionForReactivateDormantCases() {
        String date = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
        when(backOfficeService.reactivateDormant(date)).thenThrow(FeignException
                .errorStatus("reactivateDormant", Response.builder()
                        .status(404)
                        .reason("message error")
                        .request(Request.create(
                                Request.HttpMethod.POST,
                                "/data-extract/reactivate-dormant",
                                new HashMap<>(),
                                null,
                                null,
                                null))
                        .body(new byte[0])
                        .build()));
        reactivateDormantCasesTask.run();
        verify(dataExtractDateValidator).validate(date, date);
        verify(backOfficeService).reactivateDormant(date);
    }

    @Test
    void shouldThrowExceptionForReactivateDormantCases() {
        String date = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
        doThrow(new NullPointerException()).when(dataExtractDateValidator)
                .validate(date, date);
        reactivateDormantCasesTask.run();
        verify(dataExtractDateValidator).validate(date, date);
        verifyNoInteractions(backOfficeService);
    }
}
