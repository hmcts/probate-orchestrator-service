package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import uk.gov.hmcts.reform.probate.model.forms.LegalStatement;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegacyLegalStatement extends LegalStatement {
}
