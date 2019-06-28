package uk.gov.hmcts.probate.core.service;

import org.junit.Before;
import org.junit.Test;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class CaseSubmissionUpdaterTest {

    private CaseSubmissionUpdater caseSubmissionUpdater;

    @Before
    public void setUp() {
        caseSubmissionUpdater = new CaseSubmissionUpdater();
    }

    @Test
    public void shouldUpdateCaseForSubmission() {
        CaveatData caveatData = CaveatData.builder().build();

        CaveatData actualCaveatData = (CaveatData) caseSubmissionUpdater.updateCaseForSubmission(caveatData);

        assertThat(actualCaveatData.getApplicationSubmittedDate(), notNullValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenCaseDataIsNull() {
        caseSubmissionUpdater.updateCaseForSubmission(null);
    }
}
