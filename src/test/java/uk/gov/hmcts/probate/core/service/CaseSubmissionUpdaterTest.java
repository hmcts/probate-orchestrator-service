package uk.gov.hmcts.probate.core.service;

import org.junit.Before;
import org.junit.Test;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.cases.standingsearch.StandingSearchData;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class CaseSubmissionUpdaterTest {

    private CaseSubmissionUpdater caseSubmissionUpdater;

    @Before
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
    public void shouldUpdateGrantOfRepresentationCaseForSubmission() {
        GrantOfRepresentationData grantOfRepresentationData = GrantOfRepresentationData.builder().build();

        GrantOfRepresentationData actualGopData = (GrantOfRepresentationData) caseSubmissionUpdater.updateCaseForSubmission(grantOfRepresentationData);

        assertThat(actualGopData.getApplicationSubmittedDate(), notNullValue());
    }

    @Test
    public void shouldNotUpdateForStandingSearch() {
        StandingSearchData standingSearchData = StandingSearchData.builder().build();

        StandingSearchData actualStandingSearchData = (StandingSearchData) caseSubmissionUpdater.updateCaseForSubmission(standingSearchData);

        assertThat(standingSearchData.getApplicationSubmittedDate(), nullValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenCaseDataIsNull() {
        caseSubmissionUpdater.updateCaseForSubmission(null);
    }
}
