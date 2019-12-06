package uk.gov.hmcts.probate.model.backoffice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BackOfficeCaseDetails {

    @JsonProperty(value = "case_data")
    private CaseData data;

    @JsonProperty(value = "last_modified")
    private String[] lastModified;

    private Long id;
}
