package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.forms.Payment;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    @Mappings( {
        @Mapping(target = "amount", expression = "java(MapperUtils.poundsToPennies(payment.getAmount()))")
    })
    CasePayment toCasePayment(Payment payment);

    @Mappings( {
        @Mapping(target = "amount", expression = "java(MapperUtils.penniesToPounds(casePayment.getAmount()))")
    })
    Payment fromCasePayment(CasePayment casePayment);
}
