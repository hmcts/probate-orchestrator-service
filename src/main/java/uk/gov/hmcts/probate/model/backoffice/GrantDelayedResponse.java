package uk.gov.hmcts.probate.model.backoffice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class GrantDelayedResponse {
    //Todo - we may want to move this to commons
    private List<String> delayResponseData;

    @JsonCreator
    public GrantDelayedResponse(@JsonProperty("delayResponseData") List<String> delayResponseData) {
        this.delayResponseData = delayResponseData;
    }

    public List<String> getDelayResponseData() {
        return delayResponseData;
    }

}