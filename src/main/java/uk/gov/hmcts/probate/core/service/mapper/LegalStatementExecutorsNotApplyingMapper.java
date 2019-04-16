package uk.gov.hmcts.probate.core.service.mapper;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.LegalStatementExecutorNotApplying;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LegalStatementExecutorsNotApplyingMapper {


    @ToCollectionMember
    public List<CollectionMember<LegalStatementExecutorNotApplying>> toCollectionMember(
        List<String> legalStatementExecutorNotApplyingList) {
        if (CollectionUtils.isEmpty(legalStatementExecutorNotApplyingList)) {
            return null;//NOSONAR
        }
        return legalStatementExecutorNotApplyingList
            .stream()
            .map(executor -> LegalStatementExecutorNotApplying.builder()
                .executor(executor)
                .build())
            .map(executor -> CollectionMember.<LegalStatementExecutorNotApplying>builder()
                .value(executor)
                .build())
            .collect(Collectors.toList());
    }

    @FromCollectionMember
    public List<String> fromCollectionMember(
        List<CollectionMember<LegalStatementExecutorNotApplying>> collectionMembers) {
        if (CollectionUtils.isEmpty(collectionMembers)) {
            return null;//NOSONAR
        }
        return collectionMembers
            .stream()
            .map(CollectionMember::getValue)
            .map(LegalStatementExecutorNotApplying::getExecutor)
            .collect(Collectors.toList());
    }
}

