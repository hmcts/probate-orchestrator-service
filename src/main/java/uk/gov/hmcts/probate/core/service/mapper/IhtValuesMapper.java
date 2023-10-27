package uk.gov.hmcts.probate.core.service.mapper;

import lombok.experimental.UtilityClass;
import uk.gov.hmcts.reform.probate.model.IhtFormType;

import java.math.BigDecimal;

@UtilityClass
public class IhtValuesMapper {


    public BigDecimal getGrossIht205(IhtFormType ihtFormType, Long ihtGrossValue) {
        if (IhtFormType.optionIHT205.equals(ihtFormType)) {
            return new PoundsConverter().penniesToPounds(ihtGrossValue);
        }
        return null;
    }

    public BigDecimal getNetIht205(IhtFormType ihtFormType, Long ihtNetValue) {
        if (IhtFormType.optionIHT205.equals(ihtFormType)) {
            return  new PoundsConverter().penniesToPounds(ihtNetValue);
        }
        return null;
    }

    public BigDecimal getGrossIht207(IhtFormType ihtFormType, Long ihtGrossValue) {
        if (IhtFormType.optionIHT207.equals(ihtFormType)) {
            return  new PoundsConverter().penniesToPounds(ihtGrossValue);
        }
        return null;
    }

    public BigDecimal getNetIht207(IhtFormType ihtFormType, Long ihtNetValue) {
        if (IhtFormType.optionIHT207.equals(ihtFormType)) {
            return  new PoundsConverter().penniesToPounds(ihtNetValue);
        }
        return null;
    }

    public BigDecimal getGrossIht400421(IhtFormType ihtFormType, Long ihtGrossValue) {
        if (IhtFormType.optionIHT400421.equals(ihtFormType)) {
            return  new PoundsConverter().penniesToPounds(ihtGrossValue);
        }
        return null;
    }

    public BigDecimal getNetIht400421(IhtFormType ihtFormType, Long ihtNetValue) {
        if (IhtFormType.optionIHT400421.equals(ihtFormType)) {
            return  new PoundsConverter().penniesToPounds(ihtNetValue);
        }
        return null;
    }
}
