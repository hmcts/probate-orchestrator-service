package uk.gov.hmcts.probate.core.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorNotApplying;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExecutorsMapper {

    private final ExecutorApplyingMapper executorApplyingMapper;

    private final ExecutorNotApplyingMapper executorNotApplyingMapper;

    @ToCollectionMember
    public List<CollectionMember<ExecutorApplying>> toExecutorApplyingCollectionMember(List<Executor> executors) {
        return executors.stream()
            .filter(Executor::getIsApplying)
            .map(executorApplyingMapper::toExecutorApplying)
            .map(this::createCollectionMember)
            .collect(Collectors.toList());
    }

    private CollectionMember<ExecutorApplying> createCollectionMember(ExecutorApplying additionalExecutorApplying) {
        return CollectionMember.<ExecutorApplying>builder()
            .value(additionalExecutorApplying)
            .build();
    }

    @ToCollectionMember
    public List<CollectionMember<ExecutorNotApplying>> toExecutorNotApplyingCollectionMember(List<Executor> executors) {
        return executors.stream()
            .filter(Executor::getIsApplying)
            .map(executorNotApplyingMapper::toExecutorNotApplying)
            .map(this::createCollectionMember)
            .collect(Collectors.toList());
    }

    private CollectionMember<ExecutorNotApplying> createCollectionMember(ExecutorNotApplying additionalExecutorNotApplying) {
        return CollectionMember.<ExecutorNotApplying>builder()
            .value(additionalExecutorNotApplying)
            .build();
    }

    public static Long getNoOfApplicants(List<Executor> executors) {
        return executors.stream()
            .filter(Executor::getIsApplying)
            .count();
    }

}
