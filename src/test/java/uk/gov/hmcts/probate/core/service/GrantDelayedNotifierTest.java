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
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrantDelayedNotifierTest {

    @Mock
    BackOfficeService backOfficeService;

    GrantDelayedNotifier grantDelayedNotifier;

    @BeforeEach
    void setUp() {
        grantDelayedNotifier = new GrantDelayedNotifier(backOfficeService);
    }

    @Test
    void shouldInitiateGrantDelayedNotification() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateFormat.format(LocalDate.now().minusDays(1));
        GrantScheduleResponse grantScheduleResponse = GrantScheduleResponse.builder()
            .scheduleResponseData(Arrays.asList("someBody")).build();
        when(backOfficeService.initiateGrantDelayedNotification(anyString())).thenReturn(grantScheduleResponse);

        grantDelayedNotifier.initiateGrantDelayedNotification();

        verify(backOfficeService, times(1)).initiateGrantDelayedNotification(date);
    }
}
