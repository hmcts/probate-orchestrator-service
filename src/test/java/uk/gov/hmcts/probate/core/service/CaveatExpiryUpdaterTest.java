package uk.gov.hmcts.probate.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CaveatExpiryUpdaterTest {
    @Mock
    SecurityUtils securityUtils;
    @Mock
    SubmitService submitService;

    CaveatExpiryUpdater caveatExpiryUpdater;

    @BeforeEach
    public void setUp() {
        caveatExpiryUpdater = new CaveatExpiryUpdater(securityUtils, submitService);
    }

    @Test
    public void shouldExpireCaveats() {
        String expiryDate = "2020-12-31";
        List<ProbateCaseDetails> expiredCaveats =
            Arrays.asList(ProbateCaseDetails.builder().build(), ProbateCaseDetails.builder().build());
        when(submitService.expireCaveats(expiryDate)).thenReturn(expiredCaveats);

        caveatExpiryUpdater.expireCaveats(expiryDate);

        verify(submitService, times(1)).expireCaveats(expiryDate);
    }

    @Test
    public void shouldThrowDateException() {
        String expiryDate = "2020-13-31";

        assertThrows(IllegalArgumentException.class, () -> {
            caveatExpiryUpdater.expireCaveats(expiryDate);
        });
    }
}
