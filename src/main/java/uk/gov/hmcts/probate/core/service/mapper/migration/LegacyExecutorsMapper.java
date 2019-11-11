package uk.gov.hmcts.probate.core.service.mapper.migration;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.mapper.ExecutorApplyingMapper;
import uk.gov.hmcts.probate.core.service.mapper.ExecutorNotApplyingMapper;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorNotApplying;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LegacyExecutorsMapper {

    private final ExecutorApplyingMapper executorApplyingMapper;

    private final ExecutorNotApplyingMapper executorNotApplyingMapper;

    @ToLegacyExecutorNotApplyingCollectionMember
    List<CollectionMember<ExecutorNotApplying>> toExecutorNotApplyingCollectionMember(List<Executor> executors) {
        if (executors == null) {
            return null;//NOSONAR
        }
        return executors.stream()
                .filter(executor -> executor.getIsApplying() == null || BooleanUtils.isFalse(executor.getIsApplying()))
                .map(executorNotApplyingMapper::toExecutorNotApplying)
                .sorted(Comparator.comparing(e -> e.getValue().getNotApplyingExecutorName()))
                .collect(Collectors.toList());
    }

    @ToLegacyExecutorApplyingCollectionMember
    List<CollectionMember<ExecutorApplying>> toExecutorApplyingCollectionMember(List<Executor> executors) {
        if (executors == null) {
            return null;//NOSONAR
        }
        return executors.stream()
                .filter(executor ->  BooleanUtils.isTrue(executor.getIsApplying()))
                .map(executorApplyingMapper::toExecutorApplying)
                .sorted(Comparator.comparing(e -> e.getValue().getApplyingExecutorName()))
                .collect(Collectors.toList());
    }

}
