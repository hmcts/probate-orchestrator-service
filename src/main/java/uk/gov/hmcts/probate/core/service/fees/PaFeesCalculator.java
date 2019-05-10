package uk.gov.hmcts.probate.core.service.fees;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.probate.utils.NumberUtils;
import uk.gov.hmcts.reform.probate.model.forms.Fees;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaForm;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaFeesCalculator implements FeesCalculator<PaForm> {

    private final SecurityUtils securityUtils;

    private final FeesLookupRetriever feesLookupRetriever;

    @Override
    public Fees calculateFees(PaForm paForm) {

        BigDecimal ihtNetValue = NumberUtils.defaultToZero(paForm.getIht().getNetValue());
        Long copiesUk = NumberUtils.defaultToZero(paForm.getCopies().getUk());
        Long copiesOverseas = NumberUtils.defaultToZero(paForm.getCopies().getOverseas());

        String authorization = securityUtils.getAuthorisation();
        String serviceAuthorization = securityUtils.getServiceAuthorisation();

        FeesCalculation applicationFee = feesLookupRetriever.lookupFeeForApplicationFee(ihtNetValue, authorization, serviceAuthorization);
        FeesCalculation ukCopiesFee = feesLookupRetriever.lookupFeeForCopies(copiesUk, authorization, serviceAuthorization);
        FeesCalculation overseasCopiesFee = feesLookupRetriever.lookupFeeForCopies(copiesOverseas, authorization, serviceAuthorization);
        return Fees.builder()
            .applicationFee(applicationFee.getAmount())
            .applicationFeeCode(applicationFee.getCode())
            .applicationFeeVersion(applicationFee.getVersion())
            .overseasCopiesFee(overseasCopiesFee.getAmount())
            .overseasCopiesFeeCode(overseasCopiesFee.getCode())
            .overseasCopiesFeeVersion(overseasCopiesFee.getVersion())
            .ukCopiesFee(ukCopiesFee.getAmount())
            .ukCopiesFeeCode(ukCopiesFee.getCode())
            .ukCopiesFeeVersion(ukCopiesFee.getVersion())
            .total(applicationFee.getAmount().add(ukCopiesFee.getAmount()).add(overseasCopiesFee.getAmount()))
            .build();
    }
}
