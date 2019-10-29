package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyApplicant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegacyApplicant  extends IntestacyApplicant {

    @ApiModelProperty(value = "Relationship to the deceased")
    private String relationshipToDeceased;
}
