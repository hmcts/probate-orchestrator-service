package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import uk.gov.hmcts.probate.model.persistence.deserialization.BigDecimalDeserializer;
import uk.gov.hmcts.probate.model.persistence.deserialization.IhtTypeDeserializer;
import uk.gov.hmcts.reform.probate.model.IhtFormType;
import uk.gov.hmcts.reform.probate.model.forms.InheritanceTax;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegacyInheritanceTax extends InheritanceTax {

    @JsonDeserialize(using = IhtTypeDeserializer.class)
    private IhtFormType form;

    @JsonProperty("grossValueFieldIHT205")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal grossIht205;

    @JsonProperty("netValueFieldIHT205")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal netIht205;

    @JsonProperty("grossValueFieldIHT207")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal grossIht207;

    @JsonProperty("netValueFieldIHT207")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal netIht207;

    @JsonProperty("grossValueFieldIHT400421")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal grossIht400421;

    @JsonProperty("netValueFieldIHT400421")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal netIht400421;
}
