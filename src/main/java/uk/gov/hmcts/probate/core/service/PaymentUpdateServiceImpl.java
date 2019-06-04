package uk.gov.hmcts.probate.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.mapper.PaymentDtoMapper;
import uk.gov.hmcts.probate.service.PaymentUpdateService;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentUpdateServiceImpl implements PaymentUpdateService {

    private final SubmitService submitService;

    private final PaymentDtoMapper paymentDtoMapper;

    private final SecurityUtils securityUtils;

    @Async
    @Override
    public void paymentUpdate(PaymentDto paymentDto) {
        String caseId = paymentDto.getCcdCaseNumber();
        String reference = paymentDto.getReference();
        log.info("Attempting to update payment for case Id {} with payment reference {}", caseId,
            reference);
        if (!PaymentStatus.SUCCESS.getName().equalsIgnoreCase(paymentDto.getStatus())) {
            log.info("Not updating case, as payment not successful for case Id {} with payment reference {}", caseId,
                reference);
            return;
        }
        securityUtils.setSecurityContextUserAsCaseworker();
        CasePayment casePayment = paymentDtoMapper.toCasePayment(paymentDto);

        log.info("Submitting payment details for case Id {} with payment reference {}", caseId, reference);
        submitService.updatePaymentsByCaseId(caseId, casePayment);
    }
}
