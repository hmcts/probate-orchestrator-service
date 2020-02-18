package uk.gov.hmcts.probate.core.service.mapper;

import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment.CasePaymentBuilder;
import uk.gov.hmcts.reform.probate.model.forms.Payment;
import uk.gov.hmcts.reform.probate.model.forms.Payment.PaymentBuilder;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-02-13T11:05:20+0000",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_242 (Private Build)"
)
@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Autowired
    private PoundsConverter poundsConverter;

    @Override
    public CasePayment toCasePayment(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        CasePaymentBuilder casePayment = CasePayment.builder();

        casePayment.status( payment.getStatus() );
        casePayment.date( payment.getDate() );
        casePayment.reference( payment.getReference() );
        casePayment.amount( poundsConverter.poundsToPennies( payment.getAmount() ) );
        casePayment.method( payment.getMethod() );
        casePayment.transactionId( payment.getTransactionId() );
        casePayment.siteId( payment.getSiteId() );

        return casePayment.build();
    }

    @Override
    public Payment fromCasePayment(CasePayment casePayment) {
        if ( casePayment == null ) {
            return null;
        }

        PaymentBuilder payment = Payment.builder();

        payment.reference( casePayment.getReference() );
        payment.transactionId( casePayment.getTransactionId() );
        payment.date( casePayment.getDate() );
        payment.amount( poundsConverter.penniesToPounds( casePayment.getAmount() ) );
        payment.siteId( casePayment.getSiteId() );
        payment.status( casePayment.getStatus() );
        payment.method( casePayment.getMethod() );

        return payment.build();
    }
}
