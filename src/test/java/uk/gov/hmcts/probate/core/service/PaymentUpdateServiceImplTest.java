package uk.gov.hmcts.probate.core.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.mapper.PaymentDtoMapper;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentUpdateServiceImplTest {

    private static final String SERVICE_AUTHORIZATION = "SERVICEAUTH1234567";
    private static final String AUTHORIZATION = "AUTH1234567";
    private static final String CCD_CASE_NUMBER = "12312";

    @Mock
    private SubmitService submitService;

    @Mock
    private SubmitServiceApi submitServiceApi;

    @Mock
    private PaymentDtoMapper paymentDtoMapper;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private PaymentUpdateServiceImpl paymentUpdateService;

    @Test
    void shouldMakePaymentUpdateWhenPaymentStatusIsSuccess() {
        when(securityUtils.getAuthorisation()).thenReturn(AUTHORIZATION);
        when(securityUtils.getServiceAuthorisation()).thenReturn(SERVICE_AUTHORIZATION);
        PaymentDto paymentDto = PaymentDto.builder()
            .ccdCaseNumber(CCD_CASE_NUMBER)
            .status(PaymentStatus.SUCCESS.getName())
            .build();
        ProbateCaseDetails caseDetails = ProbateCaseDetails.builder()
            .caseData(CaveatData.builder().build())
            .build();

        CasePayment casePayment = CasePayment.builder().build();
        when(paymentDtoMapper.toCasePayment(paymentDto)).thenReturn(casePayment);
        when(submitServiceApi.getCaseById(eq(AUTHORIZATION), eq(SERVICE_AUTHORIZATION), eq(CCD_CASE_NUMBER)))
            .thenReturn(caseDetails);
        paymentUpdateService.paymentUpdate(paymentDto);

        verify(securityUtils).setSecurityContextUserAsCaseworker();
        verify(paymentDtoMapper).toCasePayment(paymentDto);
        verify(submitService).updateByCaseId(CCD_CASE_NUMBER, caseDetails);
    }

    @Test
    void shouldNotMakePaymentUpdateWhenPaymentStatusIsNotSuccess() {
        PaymentDto paymentDto = PaymentDto.builder()
            .ccdCaseNumber(CCD_CASE_NUMBER)
            .status(PaymentStatus.FAILED.getName())
            .build();

        paymentUpdateService.paymentUpdate(paymentDto);

        verify(securityUtils, never()).setSecurityContextUserAsCaseworker();
        verify(paymentDtoMapper, never()).toCasePayment(paymentDto);
        verify(submitService, never()).updateByCaseId(eq(CCD_CASE_NUMBER), any(ProbateCaseDetails.class));
    }
}
