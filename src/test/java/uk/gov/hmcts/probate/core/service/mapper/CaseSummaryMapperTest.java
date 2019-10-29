package uk.gov.hmcts.probate.core.service.mapper;

import org.junit.Test;
import uk.gov.hmcts.reform.probate.model.cases.CaseInfo;
import uk.gov.hmcts.reform.probate.model.cases.CaseState;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.forms.CaseSummary;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class CaseSummaryMapperTest {

    private CaseSummaryMapper caseSummaryMapper = new CaseSummaryMapper();

    @Test
    public void shouldMapCaseSummary() {

        LocalDate now = LocalDate.now();
        ProbateCaseDetails probateCaseDetails =
                ProbateCaseDetails.builder()
                        .caseData(
                                GrantOfRepresentationData.builder()
                                        .deceasedForenames("Arthur")
                                        .deceasedSurname("Ash")
                                        .grantType(GrantType.GRANT_OF_PROBATE)
                                        .build())
                        .caseInfo(CaseInfo.builder()
                                .caseCreatedDate(now)
                                .caseId("123456678989")
                                .state(CaseState.DRAFT)
                                .build())
                        .build();
        CaseSummary caseSummary = caseSummaryMapper.createCaseSummary(probateCaseDetails);
        assertThat(caseSummary.getDateCreated()).isEqualTo(now);
        assertThat(caseSummary.getDeceasedFullName()).isEqualTo("Arthur Ash");
        assertThat(caseSummary.getCaseType()).isEqualTo("PA");
        assertThat(caseSummary.getCcdCase().getId()).isEqualTo(123456678989L);
        assertThat(caseSummary.getCcdCase().getState()).isEqualTo(CaseState.DRAFT.getName());
    }
}