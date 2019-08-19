package uk.gov.hmcts.probate.core.service.mapper;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.reform.probate.model.cases.CasePayment;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.forms.Payment;

import java.util.List;

@Component
public class PaPaymentMapper {

    private final PaymentMapper paymentMapper;

    public PaPaymentMapper(PaymentMapper paymentMapper) {
        this.paymentMapper = paymentMapper;
    }

    private CollectionMember<CasePayment> createCasePayment(CasePayment casePayment) {
        CollectionMember<CasePayment> casePaymentCollectionMember = new CollectionMember<>();
        casePaymentCollectionMember.setValue(casePayment);
        return casePaymentCollectionMember;
    }

    public List<CollectionMember<CasePayment>> paymentToCasePaymentCollectionMembers(Payment payment) {
        if (payment == null) {
            return null;//NOSONAR
        }
        return Lists.newArrayList(createCasePayment(paymentMapper.toCasePayment(payment)));
    }

    public Payment paymentFromCasePaymentCollectionMembers(List<CollectionMember<CasePayment>> collectionMembers) {
        if (CollectionUtils.isEmpty(collectionMembers)) {
            return null;//NOSONAR
        }
        return collectionMembers.stream()
            .map(CollectionMember::getValue)
            .map(paymentMapper::fromCasePayment)
            .findFirst().get();
    }
}
