package uk.gov.hmcts.probate.core.service.mapper;

import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment.CasePaymentBuilder;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-02-13T11:05:19+0000",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_242 (Private Build)"
)
@Component
public class PaymentDtoMapperImpl implements PaymentDtoMapper {

    @Autowired
    private PoundsConverter poundsConverter;

    @Override
    public CasePayment toCasePayment(PaymentDto paymentDto) {
        if ( paymentDto == null ) {
            return null;
        }

        CasePaymentBuilder casePayment = CasePayment.builder();

        casePayment.reference( paymentDto.getReference() );
        casePayment.date( paymentDto.getDateCreated() );
        casePayment.method( paymentDto.getChannel() );
        casePayment.siteId( paymentDto.getSiteId() );
        casePayment.transactionId( paymentDto.getExternalReference() );
        casePayment.amount( poundsConverter.poundsToPennies( paymentDto.getAmount() ) );

        casePayment.status( PaymentStatus.getPaymentStatusByName(paymentDto.getStatus()) );

        return casePayment.build();
    }
}
