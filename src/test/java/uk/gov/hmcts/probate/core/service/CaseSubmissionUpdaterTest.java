package uk.gov.hmcts.probate.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData;
import uk.gov.hmcts.reform.probate.model.cases.standingsearch.StandingSearchData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CaseSubmissionUpdaterTest {

    private CaseSubmissionUpdater caseSubmissionUpdater;

    @BeforeEach
    public void setUp() {
        caseSubmissionUpdater = new CaseSubmissionUpdater();
    }

    @Test
    public void shouldUpdateCaveatCaseForSubmission() {
        CaveatData caveatData = CaveatData.builder().build();

        CaveatData actualCaveatData = (CaveatData) caseSubmissionUpdater.updateCaseForSubmission(caveatData);

        assertThat(actualCaveatData.getApplicationSubmittedDate(), notNullValue());
    }

    @Test
    public void shouldNotUpdateForStandingSearch() {
        StandingSearchData standingSearchData = StandingSearchData.builder().build();

        StandingSearchData actualStandingSearchData =
            (StandingSearchData) caseSubmissionUpdater.updateCaseForSubmission(standingSearchData);

        assertThat(standingSearchData.getApplicationSubmittedDate(), nullValue());
    }

    @Test
    public void shouldThrowExceptionWhenCaseDataIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            caseSubmissionUpdater.updateCaseForSubmission(null);
        });
    }
}
