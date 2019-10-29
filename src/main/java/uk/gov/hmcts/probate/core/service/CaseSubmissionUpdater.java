package uk.gov.hmcts.probate.core.service;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
@Slf4j
public class CaseSubmissionUpdater {

    private Map<CaseType, Function<CaseData, CaseData>> caseTypeUpdateFunctionMap =
        ImmutableMap.<CaseType, Function<CaseData, CaseData>>builder()
            .put(CaseType.CAVEAT, this::updateCaveat)
            .put(CaseType.GRANT_OF_REPRESENTATION, this::updateGrantOfRepresentation)
            .build();

    public CaseData updateCaseForSubmission(CaseData caseData) {
        Assert.notNull(caseData, "Case data cannot be null!");
        CaseType caseType = CaseType.getCaseType(caseData);
        Optional<Function<CaseData, CaseData>> optionalCaseDataCaseDataFunction = Optional.ofNullable(caseTypeUpdateFunctionMap.get(caseType));
        if (optionalCaseDataCaseDataFunction.isPresent()) {
            return optionalCaseDataCaseDataFunction.get().apply(caseData);
        }
        return caseData;
    }

    private CaseData updateCaveat(CaseData caseData) {
        CaveatData caveatData = (CaveatData) caseData;
        caveatData.setApplicationSubmittedDate(LocalDate.now());
        return caveatData;
    }

    private CaseData updateGrantOfRepresentation(CaseData caseData) {
        GrantOfRepresentationData grantOfRepresentationData = (GrantOfRepresentationData) caseData;
        grantOfRepresentationData.setApplicationSubmittedDate(LocalDate.now());
        return grantOfRepresentationData;
    }
}
