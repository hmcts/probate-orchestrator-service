package uk.gov.hmcts.probate.model.backoffice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BackOfficeCallbackRequest {

    @JsonProperty(value = "case_details")
    private final BackOfficeCaseDetails caseDetails;
}
