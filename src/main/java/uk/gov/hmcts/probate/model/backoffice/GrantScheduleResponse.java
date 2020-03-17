package uk.gov.hmcts.probate.model.backoffice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class GrantScheduleResponse {
    //Todo - we may want to move this to commons
    private List<String> scheduleResponseData;

    @JsonCreator
    public GrantScheduleResponse(@JsonProperty("scheduleResponseData") List<String> scheduleResponseData) {
        this.scheduleResponseData = scheduleResponseData;
    }

    public List<String> getScheduleResponseData() {
        return scheduleResponseData;
    }

}