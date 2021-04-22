package uk.gov.hmcts.probate.core.service.mapper;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.LegalStatementExecutorApplying;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class LegalStatementExecutorsApplyingMapper {


    @ToCollectionMember
    public List<CollectionMember<LegalStatementExecutorApplying>> toCollectionMember(
        List<uk.gov.hmcts.reform.probate.model.forms.LegalStatementExecutorApplying>
            legalStatementExecutorApplyingList) {
        if (CollectionUtils.isEmpty(legalStatementExecutorApplyingList)) {
            return null;//NOSONAR
        }
        return legalStatementExecutorApplyingList
            .stream()
            .map(createLegalStatementExecutorApplying())
            .map(createCollectionMember())
            .collect(Collectors.toList());
    }

    private Function<LegalStatementExecutorApplying,
        CollectionMember<LegalStatementExecutorApplying>> createCollectionMember() {
        return legalStatementExecutorApplying -> CollectionMember.<LegalStatementExecutorApplying>builder()
            .value(legalStatementExecutorApplying)
            .build();
    }

    private Function<uk.gov.hmcts.reform.probate.model.forms.LegalStatementExecutorApplying,
        LegalStatementExecutorApplying> createLegalStatementExecutorApplying() {
        return paLegalStatementExecutorApplying -> LegalStatementExecutorApplying.builder()
            .name(paLegalStatementExecutorApplying.getName())
            .sign(paLegalStatementExecutorApplying.getSign())
            .build();
    }

    @FromCollectionMember
    public List<uk.gov.hmcts.reform.probate.model.forms.LegalStatementExecutorApplying> fromCollectionMember(
        List<CollectionMember<LegalStatementExecutorApplying>> collectionMembers) {
        if (CollectionUtils.isEmpty(collectionMembers)) {
            return null;//NOSONAR
        }
        return collectionMembers
            .stream()
            .map(CollectionMember::getValue)
            .map(legalStatementExecutorApplying ->
                uk.gov.hmcts.reform.probate.model.forms.LegalStatementExecutorApplying.builder()
                .name(legalStatementExecutorApplying.getName())
                .sign(legalStatementExecutorApplying.getSign())
                .build())
            .collect(Collectors.toList());
    }
}

