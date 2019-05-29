package uk.gov.hmcts.probate.model.backoffice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;

@Data
@Builder
public class BackOfficeCallbackResponse {

    @JsonProperty(value = "data")
    private final CaseData caseData;
}
