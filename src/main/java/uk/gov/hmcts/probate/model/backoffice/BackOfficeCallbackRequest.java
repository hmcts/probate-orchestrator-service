package uk.gov.hmcts.probate.model.backoffice;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BackOfficeCallbackRequest(
    @JsonProperty("case_details") BackOfficeCaseDetails caseDetails
) {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private BackOfficeCaseDetails caseDetails;

        public Builder caseDetails(BackOfficeCaseDetails caseDetails) {
            this.caseDetails = caseDetails;
            return this;
        }

        public BackOfficeCallbackRequest build() {
            return new BackOfficeCallbackRequest(caseDetails);
        }
    }
}
