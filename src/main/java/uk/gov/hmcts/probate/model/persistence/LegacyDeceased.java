package uk.gov.hmcts.probate.model.persistence;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyDeceased;

@Data
public class LegacyDeceased extends IntestacyDeceased {

    @ApiModelProperty(value = "Deceased marital status")
    private String maritalStatus;

}
