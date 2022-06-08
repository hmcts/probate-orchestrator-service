package uk.gov.hmcts.probate.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.probate.model.backoffice.GrantScheduleResponse;
import uk.gov.hmcts.probate.service.BackOfficeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class GrantAwaitingDocumentsNotifierTest {


    @Mock
    BackOfficeService backOfficeService;

    GrantAwaitingDocumentsNotifier grantAwaitingDocumentsNotifier;

    @BeforeEach
    public void setUp() {
        grantAwaitingDocumentsNotifier = new GrantAwaitingDocumentsNotifier(backOfficeService);
    }

    @Test
    public void shouldInitiateAwaitingDocsGrantNotification() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateFormat.format(LocalDate.now().minusDays(1));
        GrantScheduleResponse grantScheduleResponse = GrantScheduleResponse.builder()
            .scheduleResponseData(Arrays.asList("someBody")).build();
        when(backOfficeService.initiateGrantAwaitingDocumentsNotification(anyString()))
            .thenReturn(grantScheduleResponse);

        grantAwaitingDocumentsNotifier.initiateGrantAwaitingDocumentsNotification();

        verify(backOfficeService, times(1)).initiateGrantAwaitingDocumentsNotification(date);
    }

}
