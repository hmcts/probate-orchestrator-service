package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.forms.Payment;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = PaymentMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public class PaymentsMapper {

    private PaymentMapper paymentMapper = Mappers.getMapper(PaymentMapper.class);

    public List<CollectionMember<CasePayment>> toCasePaymentCollectionMembers(List<Payment> payments) {
        return payments.stream()
            .map(paymentMapper::toCasePayment)
            .map(this::createCasePayment)
            .collect(Collectors.toList());
    }

    private CollectionMember<CasePayment> createCasePayment(CasePayment casePayment) {
        CollectionMember<CasePayment> casePaymentCollectionMember = new CollectionMember<>();
        casePaymentCollectionMember.setValue(casePayment);
        return casePaymentCollectionMember;
    }

    public List<Payment> fromCasePaymentCollectionMembers(List<CollectionMember<CasePayment>> collectionMembers) {
        return collectionMembers.stream()
            .map(casePaymentCollectionMember -> casePaymentCollectionMember.getValue())
            .map(paymentMapper::fromCasePayment)
            .collect(Collectors.toList());
    }
}
