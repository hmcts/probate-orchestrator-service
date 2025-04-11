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
class GrantAwaitingDocsExtractTaskTest {

    @Mock
    private DataExtractDateValidator dataExtractDateValidator;

    @Mock
    private BackOfficeService backOfficeService;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @InjectMocks
    private GrantAwaitingDocsExtractTask grantAwaitingDocsExtractTask;
    private static final String DATE = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
    private String adhocDate = "2022-09-05";

    @Test
    void shouldPerformGrantAwaitingDocumentationExtractYesterdayDate() {
        ResponseEntity<String> responseEntity = ResponseEntity.accepted()
                .body("Perform grant awaiting documentation data extract from date finished");
        grantAwaitingDocsExtractTask.run();
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals("Perform grant awaiting documentation data extract from date finished",
                responseEntity.getBody());
        verify(dataExtractDateValidator).validate(DATE);
        verify(backOfficeService).initiateGrantAwaitingDocumentsNotification(DATE);
    }

    @Test
    void shouldPerformGrantAwaitingDocumentationExtractForAdhocDate() {
        grantAwaitingDocsExtractTask.adHocJobFromDate = "2022-09-05";
        ResponseEntity<String> responseEntity = ResponseEntity.accepted()
                .body("Perform grant awaiting documentation data extract from date finished");
        grantAwaitingDocsExtractTask.run();
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals("Perform grant awaiting documentation data extract from date finished",
                responseEntity.getBody());
        verify(dataExtractDateValidator).validate(adhocDate);
        verify(backOfficeService).initiateGrantAwaitingDocumentsNotification(adhocDate);
    }

    @Test
    void shouldThrowClientExceptionWithBadRequestForGrantAwaitingDocumentationExtractWithIncorrectDateFormat() {
        doThrow(new ApiClientException(HttpStatus.BAD_REQUEST.value(), null)).when(dataExtractDateValidator)
                .validate(DATE);
        grantAwaitingDocsExtractTask.run();
        verify(dataExtractDateValidator).validate(DATE);
        verifyNoInteractions(backOfficeService);
    }

    @Test
    void shouldThrowFeignExceptionForGrantAwaitingDocExtract() {
        String date = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
        when(backOfficeService.initiateGrantAwaitingDocumentsNotification(date)).thenThrow(FeignException
                .errorStatus("initiateGrantAwaitingDocumentsNotification", Response.builder()
                        .status(404)
                        .reason("message error")
                        .request(Request.create(
                                Request.HttpMethod.POST,
                                "/data-extract/grant-awaiting-docs-extract",
                                new HashMap<>(),
                                null,
                                null,
                                null))
                        .body(new byte[0])
                        .build()));
        grantAwaitingDocsExtractTask.run();
        verify(dataExtractDateValidator).validate(date);
        verify(backOfficeService).initiateGrantAwaitingDocumentsNotification(date);
    }

    @Test
    void shouldThrowExceptionForGrantAwaitingDocumentationExtract() {
        doThrow(new NullPointerException()).when(dataExtractDateValidator)
                .validate(DATE);
        grantAwaitingDocsExtractTask.run();
        verify(dataExtractDateValidator).validate(DATE);
        verifyNoInteractions(backOfficeService);
    }

}
