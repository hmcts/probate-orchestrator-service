package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import uk.gov.hmcts.probate.model.persistence.deserialization.MaritalStatusDeserializer;
import uk.gov.hmcts.reform.probate.model.cases.MaritalStatus;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyDeceased;

@Data
public class LegacyDeceased extends IntestacyDeceased {

    @ApiModelProperty(value = "Deceased marital status")
    @JsonDeserialize(using = MaritalStatusDeserializer.class)
    private MaritalStatus maritalStatus;

}
