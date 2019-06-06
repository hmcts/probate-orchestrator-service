package uk.gov.hmcts.probate.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.fees.FeesCalculator;
import uk.gov.hmcts.probate.service.FeesService;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.forms.Fees;
import uk.gov.hmcts.reform.probate.model.forms.Form;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FeesServiceImpl implements FeesService {

    private final Map<ProbateType, FeesCalculator> feesCalculatorMap;

    private final SubmitService submitService;

    @Override
    public Form updateFees(String correlationId, ProbateType probateType) {
        Form form = submitService.getCase(correlationId, probateType);
        Fees fees = calculateFees(probateType, form);
        form.setFees(fees);
        return submitService.saveCase(correlationId, form);
    }

    public Fees calculateFees(ProbateType probateType, Form form) {
        FeesCalculator feesCalculator = getFeesCalculator(probateType);
        return feesCalculator.calculateFees(form);
    }

    private FeesCalculator getFeesCalculator(ProbateType probateType) {
        return Optional.ofNullable(feesCalculatorMap.get(probateType))
            .orElseThrow(() -> new IllegalArgumentException("Fees calculator not implemented for probateType: " + probateType));
    }
}
