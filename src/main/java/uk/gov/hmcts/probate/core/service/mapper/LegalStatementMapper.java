package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.LegalStatement;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaLegalStatement;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {LegalStatementExecutorsApplyingMapper.class,
    LegalStatementExecutorsNotApplyingMapper.class})
public interface LegalStatementMapper {


    @Mapping(target = "executorsApplying", source = "executorsApplying", qualifiedBy = {ToCollectionMember.class})
    @Mapping(target = "executorsNotApplying", source = "executorsNotApplying", qualifiedBy = {ToCollectionMember.class})
    LegalStatement toCaseLegalStatement(PaLegalStatement paLegalStatement);

    @Mapping(target = "executorsApplying", source = "executorsApplying", qualifiedBy = {FromCollectionMember.class})
    @Mapping(target = "executorsNotApplying", source = "executorsNotApplying", qualifiedBy = {FromCollectionMember.class})
    @InheritInverseConfiguration
    PaLegalStatement fromCaseLegalStatement(LegalStatement legalStatement);

}
