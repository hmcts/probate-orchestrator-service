package uk.gov.hmcts.probate.core.service.mapper;

import uk.gov.hmcts.reform.probate.model.IhtFormType;

import java.math.BigDecimal;

public final class IhtValuesMapper {

    private IhtValuesMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static BigDecimal getGrossIht205(IhtFormType ihtFormType, Long ihtGrossValue) {
        if (IhtFormType.optionIHT205.equals(ihtFormType)) {
            return new PoundsConverter().penniesToPounds(ihtGrossValue);
        }
        return null;
    }

    public static BigDecimal getNetIht205(IhtFormType ihtFormType, Long ihtNetValue) {
        if (IhtFormType.optionIHT205.equals(ihtFormType)) {
            return  new PoundsConverter().penniesToPounds(ihtNetValue);
        }
        return null;
    }

    public static BigDecimal getGrossIht207(IhtFormType ihtFormType, Long ihtGrossValue) {
        if (IhtFormType.optionIHT207.equals(ihtFormType)) {
            return  new PoundsConverter().penniesToPounds(ihtGrossValue);
        }
        return null;
    }

    public static BigDecimal getNetIht207(IhtFormType ihtFormType, Long ihtNetValue) {
        if (IhtFormType.optionIHT207.equals(ihtFormType)) {
            return  new PoundsConverter().penniesToPounds(ihtNetValue);
        }
        return null;
    }

    public static BigDecimal getGrossIht400421(IhtFormType ihtFormType, Long ihtGrossValue) {
        if (IhtFormType.optionIHT400421.equals(ihtFormType)) {
            return  new PoundsConverter().penniesToPounds(ihtGrossValue);
        }
        return null;
    }

    public static BigDecimal getNetIht400421(IhtFormType ihtFormType, Long ihtNetValue) {
        if (IhtFormType.optionIHT400421.equals(ihtFormType)) {
            return  new PoundsConverter().penniesToPounds(ihtNetValue);
        }
        return null;
    }

    public static BigDecimal getGrossIht400(IhtFormType ihtFormType, Long ihtGrossValue) {
        if (IhtFormType.optionIHT400.equals(ihtFormType)) {
            return  new PoundsConverter().penniesToPounds(ihtGrossValue);
        }
        return null;
    }

    public static BigDecimal getNetIht400(IhtFormType ihtFormType, Long ihtNetValue) {
        if (IhtFormType.optionIHT400.equals(ihtFormType)) {
            return  new PoundsConverter().penniesToPounds(ihtNetValue);
        }
        return null;
    }

}
