package uk.gov.hmcts.probate.model.backoffice;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BackOfficeCallbackRequest(
    @JsonProperty("case_details") BackOfficeCaseDetails caseDetails
) {}
