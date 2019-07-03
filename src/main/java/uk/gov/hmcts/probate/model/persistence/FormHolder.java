package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormHolder {

    private LegacyPaForm formdata;
}
