package uk.gov.hmcts.probate.core.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.probate.configuration.ScheduleConfiguration;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleValidatorTest {

    @Mock
    ScheduleConfiguration scheduleConfiguration;

    @InjectMocks
    ScheduleValidator scheduleValidator;

    @Before
    public void setUp() {
        when(scheduleConfiguration.getCaveatExpiry()).thenReturn("AAAAAAAAAA");
    }

    @Test
    public void shouldValidateCaveatExpiryKeyOK() {
        scheduleValidator.validateCaveatExpiry("AAAAAAAAAA");
    }


    @Test(expected = RuntimeException.class)
    public void shouldNOTValidateCaveatExpiryKey() {
        scheduleValidator.validateCaveatExpiry("AAAAAAAAAB");
    }

}