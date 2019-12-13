package uk.gov.hmcts.probate.core.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CaveatExpiryUpdaterTest {
    @Mock
    SecurityUtils securityUtils;
    @Mock
    SubmitService submitService;

    CaveatExpiryUpdater caveatExpiryUpdater;

    @Before
    public void setUp() {
        caveatExpiryUpdater = new CaveatExpiryUpdater(securityUtils, submitService);
    }

    @Test
    public void shouldExpireCaveats() {
        String expiryDate = "2020-12-31";
        List<ProbateCaseDetails> expiredCaveats = Arrays.asList(ProbateCaseDetails.builder().build(), ProbateCaseDetails.builder().build());
        when(submitService.expireCaveats(expiryDate)).thenReturn(expiredCaveats);

        List<ProbateCaseDetails> actualExpiredCaveats = caveatExpiryUpdater.expireCaveats(expiryDate);

        assertThat(actualExpiredCaveats.size(), equalTo(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowDateException() {
        String expiryDate = "2020-13-31";

        caveatExpiryUpdater.expireCaveats(expiryDate);
    }
}