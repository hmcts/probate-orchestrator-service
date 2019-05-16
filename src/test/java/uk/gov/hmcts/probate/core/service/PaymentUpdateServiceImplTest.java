package uk.gov.hmcts.probate.core.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.probate.core.service.mapper.PaymentDtoMapper;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentUpdateServiceImplTest {

    public static final String CCD_CASE_NUMBER = "12312";
    @Mock
    private SubmitService submitService;

    @Mock
    private PaymentDtoMapper paymentDtoMapper;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private PaymentUpdateServiceImpl paymentUpdateService;


    @Test
    public void shouldMakePaymentUpdateWhenPaymentStatusIsSuccess() {
        PaymentDto paymentDto = PaymentDto.builder()
            .ccdCaseNumber(CCD_CASE_NUMBER)
            .status(PaymentStatus.SUCCESS.getName())
            .build();
        CasePayment casePayment = CasePayment.builder().build();
        when(paymentDtoMapper.toCasePayment(paymentDto)).thenReturn(casePayment);

        paymentUpdateService.paymentUpdate(paymentDto);

        verify(securityUtils).setSecurityContextUserAsCitizen();
        verify(paymentDtoMapper).toCasePayment(paymentDto);
        verify(submitService).updatePaymentsByCaseId(CCD_CASE_NUMBER, casePayment);
    }

    @Test
    public void shouldNotMakePaymentUpdateWhenPaymentStatusIsNotSuccess() {
        PaymentDto paymentDto = PaymentDto.builder()
            .ccdCaseNumber(CCD_CASE_NUMBER)
            .status(PaymentStatus.FAILED.getName())
            .build();

        verify(securityUtils, never()).setSecurityContextUserAsCitizen();
        verify(paymentDtoMapper, never()).toCasePayment(paymentDto);
        verify(submitService, never()).updatePaymentsByCaseId(eq(CCD_CASE_NUMBER), any(CasePayment.class));
    }
}
