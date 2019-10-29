package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToPennies;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToPounds;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCalculatedFees;
import uk.gov.hmcts.reform.probate.model.forms.Fees;

@Mapper(componentModel = "spring", uses = PoundsConverter.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeesMapper {

    @Mapping(target = "applicationFee", qualifiedBy = {ToPennies.class})
    @Mapping(target = "ukCopiesFee", qualifiedBy = {ToPennies.class})
    @Mapping(target = "overseasCopiesFee", qualifiedBy = {ToPennies.class})
    @Mapping(target = "total", qualifiedBy = {ToPennies.class})
    ProbateCalculatedFees toProbateCalculatedFees(Fees fees);

    @Mapping(target = "applicationFee", qualifiedBy = {ToPounds.class})
    @Mapping(target = "ukCopiesFee", qualifiedBy = {ToPounds.class})
    @Mapping(target = "overseasCopiesFee", qualifiedBy = {ToPounds.class})
    @Mapping(target = "total", qualifiedBy = {ToPounds.class})
    Fees fromProbateCalculatedFees(ProbateCalculatedFees probateCalculatedFees);
}
