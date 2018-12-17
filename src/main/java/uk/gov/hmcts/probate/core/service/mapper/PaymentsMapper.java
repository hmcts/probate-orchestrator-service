package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.factory.Mappers;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.forms.Payment;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentsMapper {

    private PaymentMapper paymentMapper = Mappers.getMapper(PaymentMapper.class);

    @ToCollectionMember
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

    @FromCollectionMember
    public List<Payment> fromCasePaymentCollectionMembers(List<CollectionMember<CasePayment>> collectionMembers) {
        return collectionMembers.stream()
            .map(casePaymentCollectionMember -> casePaymentCollectionMember.getValue())
            .map(paymentMapper::fromCasePayment)
            .collect(Collectors.toList());
    }
}
