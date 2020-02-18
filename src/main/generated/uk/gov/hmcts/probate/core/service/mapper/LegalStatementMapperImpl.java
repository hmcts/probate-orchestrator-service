package uk.gov.hmcts.probate.core.service.mapper;

import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.LegalStatement;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.LegalStatement.LegalStatementBuilder;
import uk.gov.hmcts.reform.probate.model.forms.LegalStatementHolder;
import uk.gov.hmcts.reform.probate.model.forms.LegalStatementHolder.LegalStatementHolderBuilder;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-02-18T10:16:52+0000",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_242 (Private Build)"
)
@Component
public class LegalStatementMapperImpl implements LegalStatementMapper {

    @Autowired
    private LegalStatementExecutorsApplyingMapper legalStatementExecutorsApplyingMapper;
    @Autowired
    private LegalStatementExecutorsNotApplyingMapper legalStatementExecutorsNotApplyingMapper;

    @Override
    public LegalStatement toCaseLegalStatement(LegalStatementHolder legalStatement) {
        if ( legalStatement == null ) {
            return null;
        }

        LegalStatementBuilder legalStatement1 = LegalStatement.builder();

        legalStatement1.executorsNotApplying( legalStatementExecutorsNotApplyingMapper.toCollectionMember( legalStatement.getExecutorsNotApplying() ) );
        legalStatement1.executorsApplying( legalStatementExecutorsApplyingMapper.toCollectionMember( legalStatement.getExecutorsApplying() ) );
        legalStatement1.intro( legalStatement.getIntro() );
        legalStatement1.applicant( legalStatement.getApplicant() );
        legalStatement1.deceased( legalStatement.getDeceased() );
        legalStatement1.deceasedOtherNames( legalStatement.getDeceasedOtherNames() );
        legalStatement1.deceasedEstateValue( legalStatement.getDeceasedEstateValue() );
        legalStatement1.deceasedEstateLand( legalStatement.getDeceasedEstateLand() );

        return legalStatement1.build();
    }

    @Override
    public LegalStatementHolder fromCaseLegalStatement(LegalStatement legalStatement) {
        if ( legalStatement == null ) {
            return null;
        }

        LegalStatementHolderBuilder legalStatementHolder = LegalStatementHolder.builder();

        legalStatementHolder.executorsNotApplying( legalStatementExecutorsNotApplyingMapper.fromCollectionMember( legalStatement.getExecutorsNotApplying() ) );
        legalStatementHolder.executorsApplying( legalStatementExecutorsApplyingMapper.fromCollectionMember( legalStatement.getExecutorsApplying() ) );
        legalStatementHolder.applicant( legalStatement.getApplicant() );
        legalStatementHolder.deceased( legalStatement.getDeceased() );
        legalStatementHolder.deceasedOtherNames( legalStatement.getDeceasedOtherNames() );
        legalStatementHolder.deceasedEstateValue( legalStatement.getDeceasedEstateValue() );
        legalStatementHolder.deceasedEstateLand( legalStatement.getDeceasedEstateLand() );
        legalStatementHolder.intro( legalStatement.getIntro() );

        return legalStatementHolder.build();
    }
}
