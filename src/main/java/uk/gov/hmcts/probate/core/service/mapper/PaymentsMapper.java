package uk.gov.hmcts.probate.core.service.mapper;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.forms.Payment;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentsMapper {

    private final PaymentMapper paymentMapper;

    public PaymentsMapper(PaymentMapper paymentMapper) {
        this.paymentMapper = paymentMapper;
    }

    @ToCollectionMember
    public List<CollectionMember<CasePayment>> toCasePaymentCollectionMembers(List<Payment> payments) {
        if (payments == null) {
            return null;//NOSONAR
        }
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
        if (collectionMembers == null) {
            return null;//NOSONAR
        }
        return collectionMembers.stream()
            .map(CollectionMember::getValue)
            .map(paymentMapper::fromCasePayment)
            .collect(Collectors.toList());
    }
}
