package uk.gov.hmcts.probate.model.backoffice;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;

public record BackOfficeCaseDetails(
    @JsonProperty("case_data") CaseData data,
    @JsonProperty("last_modified") String[] lastModified,
    Long id
) {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private CaseData data;
        private String[] lastModified;
        private Long id;

        public Builder data(CaseData data) {
            this.data = data;
            return this;
        }

        public Builder lastModified(String[] lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public BackOfficeCaseDetails build() {
            return new BackOfficeCaseDetails(data, lastModified, id);
        }
    }
}
