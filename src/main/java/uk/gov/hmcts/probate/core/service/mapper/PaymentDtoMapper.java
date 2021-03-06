package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToPennies;
import uk.gov.hmcts.reform.probate.model.PaymentStatus;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

@Mapper(componentModel = "spring", uses = PoundsConverter.class, imports = PaymentStatus.class,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentDtoMapper {

    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "amount", qualifiedBy = {ToPennies.class})
    @Mapping(target = "status", expression = "java(PaymentStatus.getPaymentStatusByName(paymentDto.getStatus()))")
    @Mapping(target = "date", source = "dateCreated")
    @Mapping(target = "transactionId", source = "externalReference")
    @Mapping(target = "method", source = "channel")
    @Mapping(target = "siteId", source = "siteId")
    CasePayment toCasePayment(PaymentDto paymentDto);

}
