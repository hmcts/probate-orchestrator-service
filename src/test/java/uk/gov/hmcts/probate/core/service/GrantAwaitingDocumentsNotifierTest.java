package uk.gov.hmcts.probate.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.probate.model.backoffice.GrantScheduleResponse;
import uk.gov.hmcts.probate.service.BackOfficeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrantAwaitingDocumentsNotifierTest {


    @Mock
    BackOfficeService backOfficeService;

    GrantAwaitingDocumentsNotifier grantAwaitingDocumentsNotifier;

    @BeforeEach
    void setUp() {
        grantAwaitingDocumentsNotifier = new GrantAwaitingDocumentsNotifier(backOfficeService);
    }

    @Test
    void shouldInitiateAwaitingDocsGrantNotification() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateFormat.format(LocalDate.now().minusDays(1));
        GrantScheduleResponse grantScheduleResponse = GrantScheduleResponse.builder()
            .scheduleResponseData(List.of("someBody")).build();
        when(backOfficeService.initiateGrantAwaitingDocumentsNotification(anyString()))
            .thenReturn(grantScheduleResponse);

        grantAwaitingDocumentsNotifier.initiateGrantAwaitingDocumentsNotification();

        verify(backOfficeService, times(1)).initiateGrantAwaitingDocumentsNotification(date);
    }
}
