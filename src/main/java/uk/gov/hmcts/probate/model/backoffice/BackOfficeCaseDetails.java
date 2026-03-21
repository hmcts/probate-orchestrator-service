package uk.gov.hmcts.probate.model.backoffice;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;

public record BackOfficeCaseDetails(
    @JsonProperty("case_data") CaseData data,
    @JsonProperty("last_modified") String[] lastModified,
    Long id
) {}
