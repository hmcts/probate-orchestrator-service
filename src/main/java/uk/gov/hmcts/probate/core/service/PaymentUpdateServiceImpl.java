package uk.gov.hmcts.probate.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.mapper.PaymentDtoMapper;
import uk.gov.hmcts.probate.service.PaymentUpdateService;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

@Component
@RequiredArgsConstructor
public class PaymentUpdateServiceImpl implements PaymentUpdateService {

    private final SubmitService submitService;

    private final PaymentDtoMapper paymentDtoMapper;

    private final SecurityUtils securityUtils;

    @Async
    @Override
    public void paymentUpdate(PaymentDto paymentDto) {
        if (!PaymentStatus.SUCCESS.getName().equals(paymentDto.getStatus())) {
            return;
        }
        securityUtils.setSecurityContextUserAsCaseworker();
        String caseId = paymentDto.getCcdCaseNumber();
        CasePayment casePayment = paymentDtoMapper.toCasePayment(paymentDto);
        submitService.updatePaymentsByCaseId(caseId, casePayment);
    }
}
