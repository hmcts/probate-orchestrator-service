package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;

@ApiModel(value = "ProbateType", description = "Represents probate type")
@RequiredArgsConstructor
public enum LegacyProbateType {

    @JsonProperty("intestacy") INTESTACY_LEGACY("intestacy", CaseType.GRANT_OF_REPRESENTATION),
    @JsonProperty("gop") PROBATE_LEGACY("gop", CaseType.GRANT_OF_REPRESENTATION);

    @Getter
    private final String name;

    @Getter
    private final CaseType caseType;

}
