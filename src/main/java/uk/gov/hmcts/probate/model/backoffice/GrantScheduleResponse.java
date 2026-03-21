package uk.gov.hmcts.probate.model.backoffice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GrantScheduleResponse(List<String> scheduleResponseData) {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private List<String> scheduleResponseData;

        public Builder scheduleResponseData(List<String> scheduleResponseData) {
            this.scheduleResponseData = scheduleResponseData;
            return this;
        }

        public GrantScheduleResponse build() {
            return new GrantScheduleResponse(scheduleResponseData);
        }
    }
}
