package uk.gov.hmcts.probate.core.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.probate.model.backoffice.GrantDelayedResponse;
import uk.gov.hmcts.probate.service.BackOfficeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GrantDelayedNotifierTest {

    @Mock
    BackOfficeService backOfficeService;

    GrantDelayedNotifier grantDelayedNotifier;

    @Before
    public void setUp() {
        grantDelayedNotifier = new GrantDelayedNotifier(backOfficeService);
    }

    @Test
    public void shouldInitiateGrantDelayedNotification() {
        DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = DATE_FORMAT.format(LocalDate.now().minusDays(1));
        GrantDelayedResponse grantDelayedResponse = GrantDelayedResponse.builder()
            .delayResponseData(Arrays.asList("someBody")).build();
        when(backOfficeService.initiateGrantDelayedNotification(anyString())).thenReturn(grantDelayedResponse);

        grantDelayedNotifier.initiateGrantDelayedNotification();

        verify(backOfficeService, times(1)).initiateGrantDelayedNotification(date);
    }
}
