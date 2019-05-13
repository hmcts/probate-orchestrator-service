package uk.gov.hmcts.probate.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.mapper.PaymentDtoMapper;
import uk.gov.hmcts.probate.service.PaymentUpdateService;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

@Component
@RequiredArgsConstructor
public class PaymentUpdateServiceImpl implements PaymentUpdateService {

    private final SubmitService submitService;

    private final PaymentDtoMapper paymentDtoMapper;

    @Async
    @Override
    public void paymentUpdate(PaymentDto paymentDto, CaseType caseType) {
        if (!PaymentStatus.SUCCESS.getName().equals(paymentDto.getStatus())) {
            return;
        }
        String caseId = paymentDto.getCcdCaseNumber();
        CasePayment casePayment = paymentDtoMapper.toCasePayment(paymentDto);
        submitService.updatePaymentsByCaseId(paymentDto.getCcdCaseNumber(), caseType, casePayment);
        submitService.updatePaymentsByCaseId(caseId, caseType, casePayment);
    }
}
