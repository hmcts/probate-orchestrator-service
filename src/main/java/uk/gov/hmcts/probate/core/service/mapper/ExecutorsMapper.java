package uk.gov.hmcts.probate.core.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorNotApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExecutorsMapper {

    private final ExecutorApplyingMapper executorApplyingMapper;

    private final ExecutorNotApplyingMapper executorNotApplyingMapper;

    @ToCollectionMember
    List<CollectionMember<ExecutorNotApplying>> toExecutorNotApplyingCollectionMember(List<Executor> executors) {
        return executors.stream()
            .map(executor -> executorNotApplyingMapper.toExecutorNotApplying(executor))
            .collect(Collectors.toList());
    }

    @ToCollectionMember
    List<CollectionMember<ExecutorApplying>> toExecutorApplyingCollectionMember(List<Executor> executors) {
        return executors.stream()
            .map(executor -> executorApplyingMapper.toExecutorApplying(executor))
            .collect(Collectors.toList());
    }

    @FromCollectionMember
    List<Executor> fromCollectionMember(GrantOfRepresentationData grantOfRepresentationData) {
        List<Executor> executors = grantOfRepresentationData.getExecutorsNotApplying().stream()
            .map(e -> executorNotApplyingMapper.fromExecutorNotApplying(e))
            .collect(Collectors.toList());
        grantOfRepresentationData.getExecutorsApplying().stream()
            .map(e -> executorApplyingMapper.fromExecutorApplying(e))
            .forEach(executor -> executors.add(executor));
        return executors;
    }
}
