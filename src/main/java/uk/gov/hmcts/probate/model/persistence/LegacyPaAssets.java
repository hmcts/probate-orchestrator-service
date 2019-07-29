package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import uk.gov.hmcts.probate.model.persistence.deserialization.LegacyYesNoDeserializer;
import uk.gov.hmcts.reform.probate.model.YesNo;
import uk.gov.hmcts.reform.probate.model.jackson.YesNoSerializer;

public class LegacyPaAssets {

    @ApiModelProperty(value = "Was adoption in England or Wales", allowableValues = YesNo.Constants.ALLOWABLE_VALUES)
    @JsonDeserialize(using = LegacyYesNoDeserializer.class)
    @JsonSerialize(using = YesNoSerializer.class)
    protected Boolean assetsoverseas;
}
