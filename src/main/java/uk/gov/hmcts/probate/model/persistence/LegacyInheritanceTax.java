package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import uk.gov.hmcts.probate.model.persistence.deserialization.IhtTypeDeserializer;
import uk.gov.hmcts.reform.probate.model.IhtFormType;
import uk.gov.hmcts.reform.probate.model.forms.InheritanceTax;

public class LegacyInheritanceTax extends InheritanceTax {

    @JsonDeserialize(using = IhtTypeDeserializer.class)
    private IhtFormType form;
}
