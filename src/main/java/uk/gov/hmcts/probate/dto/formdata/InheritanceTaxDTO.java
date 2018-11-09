package uk.gov.hmcts.probate.dto.formdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonRootName("iht")
public class InheritanceTaxDTO implements Serializable {

    @NotBlank
    private String form;

    @NotBlank
    private String formId;

    @NotNull
    @JsonProperty("netValue")
    private BigDecimal netValue;

    @NotNull
    @JsonProperty("grossValue")
    private BigDecimal grossValue;
}
