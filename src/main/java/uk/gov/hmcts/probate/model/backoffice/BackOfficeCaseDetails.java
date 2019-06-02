package uk.gov.hmcts.probate.model.backoffice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;

@Data
@Builder
public class BackOfficeCaseDetails {

    @JsonProperty(value = "case_data")
    private final CaseData data;

    @JsonProperty(value = "last_modified")
    private final String[] lastModified;

    private final Long id;
}
