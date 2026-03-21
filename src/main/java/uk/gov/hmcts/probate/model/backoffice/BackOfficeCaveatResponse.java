package uk.gov.hmcts.probate.model.backoffice;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BackOfficeCaveatResponse(
    @JsonProperty("data") BackOfficeCaveatData caseData
) {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private BackOfficeCaveatData caseData;

        public Builder caseData(BackOfficeCaveatData caseData) {
            this.caseData = caseData;
            return this;
        }

        public BackOfficeCaveatResponse build() {
            return new BackOfficeCaveatResponse(caseData);
        }
    }
}
