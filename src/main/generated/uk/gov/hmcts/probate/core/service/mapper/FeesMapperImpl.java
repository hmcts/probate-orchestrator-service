package uk.gov.hmcts.probate.core.service.mapper;

import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCalculatedFees;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCalculatedFees.ProbateCalculatedFeesBuilder;
import uk.gov.hmcts.reform.probate.model.forms.Fees;
import uk.gov.hmcts.reform.probate.model.forms.Fees.FeesBuilder;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-02-13T11:05:20+0000",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_242 (Private Build)"
)
@Component
public class FeesMapperImpl implements FeesMapper {

    @Autowired
    private PoundsConverter poundsConverter;

    @Override
    public ProbateCalculatedFees toProbateCalculatedFees(Fees fees) {
        if ( fees == null ) {
            return null;
        }

        ProbateCalculatedFeesBuilder probateCalculatedFees = ProbateCalculatedFees.builder();

        probateCalculatedFees.applicationFee( poundsConverter.poundsToPennies( fees.getApplicationFee() ) );
        probateCalculatedFees.applicationFeeCode( fees.getApplicationFeeCode() );
        probateCalculatedFees.applicationFeeVersion( fees.getApplicationFeeVersion() );
        probateCalculatedFees.ukCopiesFee( poundsConverter.poundsToPennies( fees.getUkCopiesFee() ) );
        probateCalculatedFees.ukCopiesFeeCode( fees.getUkCopiesFeeCode() );
        probateCalculatedFees.ukCopiesFeeVersion( fees.getUkCopiesFeeVersion() );
        probateCalculatedFees.overseasCopiesFee( poundsConverter.poundsToPennies( fees.getOverseasCopiesFee() ) );
        probateCalculatedFees.overseasCopiesFeeCode( fees.getOverseasCopiesFeeCode() );
        probateCalculatedFees.overseasCopiesFeeVersion( fees.getOverseasCopiesFeeVersion() );
        probateCalculatedFees.total( poundsConverter.poundsToPennies( fees.getTotal() ) );

        return probateCalculatedFees.build();
    }

    @Override
    public Fees fromProbateCalculatedFees(ProbateCalculatedFees probateCalculatedFees) {
        if ( probateCalculatedFees == null ) {
            return null;
        }

        FeesBuilder fees = Fees.builder();

        fees.applicationFee( poundsConverter.penniesToPounds( probateCalculatedFees.getApplicationFee() ) );
        fees.applicationFeeCode( probateCalculatedFees.getApplicationFeeCode() );
        fees.applicationFeeVersion( probateCalculatedFees.getApplicationFeeVersion() );
        fees.ukCopiesFee( poundsConverter.penniesToPounds( probateCalculatedFees.getUkCopiesFee() ) );
        fees.ukCopiesFeeCode( probateCalculatedFees.getUkCopiesFeeCode() );
        fees.ukCopiesFeeVersion( probateCalculatedFees.getUkCopiesFeeVersion() );
        fees.overseasCopiesFee( poundsConverter.penniesToPounds( probateCalculatedFees.getOverseasCopiesFee() ) );
        fees.overseasCopiesFeeCode( probateCalculatedFees.getOverseasCopiesFeeCode() );
        fees.overseasCopiesFeeVersion( probateCalculatedFees.getOverseasCopiesFeeVersion() );
        fees.total( poundsConverter.penniesToPounds( probateCalculatedFees.getTotal() ) );

        return fees.build();
    }
}
