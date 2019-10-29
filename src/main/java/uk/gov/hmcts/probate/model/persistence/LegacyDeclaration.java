package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import uk.gov.hmcts.reform.probate.model.forms.Declaration;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegacyDeclaration extends Declaration {

    private LegacyLegalStatement legalStatement;

}
