package uk.gov.hmcts.probate.model.backoffice;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BackOfficeCaveatResponse(
    @JsonProperty("data") BackOfficeCaveatData caseData
) {}
