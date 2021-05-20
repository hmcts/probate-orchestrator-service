package uk.gov.hmcts.probate.core.service.mapper;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.forms.CaseSummary;
import uk.gov.hmcts.reform.probate.model.forms.CcdCase;

import static uk.gov.hmcts.reform.probate.model.ProbateType.INTESTACY;
import static uk.gov.hmcts.reform.probate.model.ProbateType.PA;
import static uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType.GRANT_OF_PROBATE;

@Component
public class CaseSummaryMapper {

    public CaseSummary createCaseSummary(ProbateCaseDetails caseDetails) {
        GrantOfRepresentationData gop = (GrantOfRepresentationData) caseDetails.getCaseData();
        return CaseSummary.builder().dateCreated(caseDetails.getCaseInfo().getCaseCreatedDate())
            .deceasedFullName(formatDeceasedFullName(gop))
            .caseType(GRANT_OF_PROBATE.equals(gop.getGrantType())
                ? PA.name() : INTESTACY.name())
            .ccdCase(CcdCase.builder()
                .state(caseDetails.getCaseInfo().getState().getName())
                .id(Long.parseLong(caseDetails.getCaseInfo().getCaseId())).build())
            .build();
    }

    private String formatDeceasedFullName(GrantOfRepresentationData gop) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (gop.getDeceasedForenames() != null) {
            stringBuilder.append(gop.getDeceasedForenames());
        }
        if (gop.getDeceasedSurname() != null) {
            stringBuilder.append(" " + gop.getDeceasedSurname());
        }
        return stringBuilder.toString();
    }
}
