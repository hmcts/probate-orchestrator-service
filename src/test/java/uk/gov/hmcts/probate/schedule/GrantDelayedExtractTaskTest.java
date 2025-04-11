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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class GrantDelayedExtractTaskTest {

    @Mock
    private DataExtractDateValidator dataExtractDateValidator;

    @Mock
    private BackOfficeService backOfficeService;

    private static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @InjectMocks
    private GrantDelayedExtractTask grantDelayedExtractTask;
    private static final String DATE = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
    private String adhocDate = "2022-09-05";

    @Test
    void shouldPerformGrantDelayedExtractYesterdayDate() {
        ResponseEntity<String> responseEntity = ResponseEntity.accepted()
                .body("Perform grant delayed data extract from date finished");
        grantDelayedExtractTask.run();
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals("Perform grant delayed data extract from date finished", responseEntity.getBody());
        verify(dataExtractDateValidator).validate(DATE);
        verify(backOfficeService).initiateGrantDelayedNotification(DATE);
    }

    @Test
    void shouldPerformGrantDelayedExtractForAdhocDate() {
        grantDelayedExtractTask.adHocJobFromDate = "2022-09-05";
        ResponseEntity<String> responseEntity = ResponseEntity.accepted()
                .body("Perform grant delayed data extract from date finished");
        grantDelayedExtractTask.run();
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals("Perform grant delayed data extract from date finished", responseEntity.getBody());
        verify(dataExtractDateValidator).validate(adhocDate);
        verify(backOfficeService).initiateGrantDelayedNotification(adhocDate);
    }

    @Test
    void shouldThrowClientExceptionWithBadRequestForGrantDelayedExtractWithIncorrectDateFormat() {
        doThrow(new ApiClientException(HttpStatus.BAD_REQUEST.value(), null)).when(dataExtractDateValidator)
                .dateValidator(DATE);
        grantDelayedExtractTask.run();
        verify(dataExtractDateValidator).validate(DATE);
        verifyNoInteractions(backOfficeService);
    }

    @Test
    void shouldThrowFeignExceptionForGrantDelayedExtract() {
        String date = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
        when(backOfficeService.initiateGrantDelayedNotification(date)).thenThrow(FeignException
                .errorStatus("initiateGrantDelayedNotification", Response.builder()
                        .status(404)
                        .reason("message error")
                        .request(Request.create(
                                Request.HttpMethod.POST,
                                "/data-extract/grant-delayed-extract",
                                new HashMap<>(),
                                null,
                                null,
                                null))
                        .body(new byte[0])
                        .build()));
        grantDelayedExtractTask.run();
        verify(dataExtractDateValidator).validate(date);
        verify(backOfficeService).initiateGrantDelayedNotification(date);
    }

    @Test
    void shouldThrowExceptionForGrantDelayedExtract() {
        doThrow(new NullPointerException()).when(dataExtractDateValidator)
                .validate(DATE);
        grantDelayedExtractTask.run();
        verify(dataExtractDateValidator).validate(DATE);
        verifyNoInteractions(backOfficeService);
    }

}
