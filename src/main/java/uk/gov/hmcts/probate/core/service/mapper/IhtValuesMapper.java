package uk.gov.hmcts.probate.core.service.mapper;

import uk.gov.hmcts.reform.probate.model.IhtFormType;

import java.math.BigDecimal;

public class IhtValuesMapper {


    public static BigDecimal getGrossIht205(IhtFormType ihtFormType, Long ihtGrossValue){
        if(IhtFormType.IHT205.equals(ihtFormType)){
           return  new PoundsConverter().penniesToPounds(ihtGrossValue);
        }
        return null;
    }

    public static BigDecimal getNetIht205(IhtFormType ihtFormType, Long ihtNetValue){
        if(IhtFormType.IHT205.equals(ihtFormType)){
            return  new PoundsConverter().penniesToPounds(ihtNetValue);
        }
        return null;
    }

    public static BigDecimal getGrossIht207(IhtFormType ihtFormType, Long ihtGrossValue){
        if(IhtFormType.IHT207.equals(ihtFormType)){
            return  new PoundsConverter().penniesToPounds(ihtGrossValue);
        }
        return null;
    }

    public static BigDecimal getNetIht207(IhtFormType ihtFormType, Long ihtNetValue){
        if(IhtFormType.IHT207.equals(ihtFormType)){
            return  new PoundsConverter().penniesToPounds(ihtNetValue);
        }
        return null;
    }

    public static BigDecimal getGrossIht400421(IhtFormType ihtFormType, Long ihtGrossValue){
        if(IhtFormType.IHT400421.equals(ihtFormType)){
            return  new PoundsConverter().penniesToPounds(ihtGrossValue);
        }
        return null;
    }

    public static BigDecimal getNetIht400421(IhtFormType ihtFormType, Long ihtNetValue){
        if(IhtFormType.IHT400421.equals(ihtFormType)){
            return  new PoundsConverter().penniesToPounds(ihtNetValue);
        }
        return null;
    }
}
