package uk.gov.hmcts.probate.core.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.probate.service.BackOfficeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GrantDelayedNotifierTest {
    @Mock
    SecurityUtils securityUtils;
    @Mock
    BackOfficeService backOfficeService;

    GrantDelayedNotifier grantDelayedNotifier;

    @Before
    public void setUp() {
        grantDelayedNotifier = new GrantDelayedNotifier(securityUtils, backOfficeService);
    }

    @Test
    public void shouldInitiateGrantDelayedNotification() {
        DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = DATE_FORMAT.format(LocalDate.now().minusDays(1));
        ResponseEntity<String> responseEntity = ResponseEntity.ok("someBody");
        when(backOfficeService.initiateGrantDelayedNotification(anyString())).thenReturn(responseEntity);

        grantDelayedNotifier.initiateGrantDelayedNotification();

        verify(backOfficeService, times(1)).initiateGrantDelayedNotification(date);
        verify(securityUtils, times(1)).setSecurityContextUserAsCaseworker();
    }
}
