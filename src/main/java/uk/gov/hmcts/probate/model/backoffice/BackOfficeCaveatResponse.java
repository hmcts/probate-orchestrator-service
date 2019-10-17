package uk.gov.hmcts.probate.model.backoffice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BackOfficeCaveatResponse {

    @JsonProperty(value = "data")
    private  BackOfficeCaveatData caseData;
}
