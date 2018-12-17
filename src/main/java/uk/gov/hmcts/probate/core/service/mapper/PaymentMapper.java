package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToPennies;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToPounds;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.forms.Payment;

@Mapper(uses = PoundsConverter.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    @Mappings( {
        @Mapping(target = "amount", qualifiedBy = {ToPounds.class})
    })
    CasePayment toCasePayment(Payment payment);

    @Mappings( {
        @Mapping(target = "amount", qualifiedBy = {ToPennies.class})
    })
    Payment fromCasePayment(CasePayment casePayment);
}
