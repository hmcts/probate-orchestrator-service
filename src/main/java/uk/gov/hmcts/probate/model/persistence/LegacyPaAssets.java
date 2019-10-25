package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import uk.gov.hmcts.probate.model.persistence.deserialization.LegacyYesNoDeserializer;
import uk.gov.hmcts.reform.probate.model.jackson.YesNoSerializer;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegacyPaAssets {

    @JsonDeserialize(using = LegacyYesNoDeserializer.class)
    @JsonSerialize(using = YesNoSerializer.class)
    protected Boolean assetsoverseas;
}
