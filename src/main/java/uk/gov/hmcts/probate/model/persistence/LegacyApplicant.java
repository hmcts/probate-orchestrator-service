package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.probate.model.persistence.deserialization.RelationshipDeserializer;
import uk.gov.hmcts.reform.probate.model.Relationship;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyApplicant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LegacyApplicant  extends IntestacyApplicant {

    @ApiModelProperty(value = "Relationship to the deceased")
    @JsonDeserialize(using = RelationshipDeserializer.class)
    private Relationship relationshipToDeceased;
}
