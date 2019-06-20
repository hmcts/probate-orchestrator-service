package uk.gov.hmcts.probate.core.service.mapper;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToExecutorApplyingCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToExecutorNotApplyingCollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorNotApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExecutorsMapper {

    private final ExecutorApplyingMapper executorApplyingMapper;

    private final ExecutorNotApplyingMapper executorNotApplyingMapper;

    @ToExecutorNotApplyingCollectionMember
    List<CollectionMember<ExecutorNotApplying>> toExecutorNotApplyingCollectionMember(List<Executor> executors) {
        if (executors == null) {
            return null;//NOSONAR
        }
        return executors.stream()
                .filter(executor -> executor.getIsApplying() == null || BooleanUtils.isFalse(executor.getIsApplying()))
                .map(executor -> executorNotApplyingMapper.toExecutorNotApplying(executor))
                .sorted(Comparator.comparing(e -> e.getValue().getNotApplyingExecutorName()))
                .collect(Collectors.toList());
    }

    @ToExecutorApplyingCollectionMember
    List<CollectionMember<ExecutorApplying>> toExecutorApplyingCollectionMember(List<Executor> executors) {
        if (executors == null) {
            return null;//NOSONAR
        }
        return executors.stream()
                .filter(executor ->  BooleanUtils.isTrue(executor.getIsApplying()))
                .map(executor -> executorApplyingMapper.toExecutorApplying(executor))
                .sorted(Comparator.comparing(e -> e.getValue().getApplyingExecutorName()))
                .collect(Collectors.toList());
    }

    @FromCollectionMember
    List<Executor> fromCollectionMember(GrantOfRepresentationData grantOfRepresentationData) {
        if (grantOfRepresentationData == null ||
                (grantOfRepresentationData.getExecutorsApplying() == null && grantOfRepresentationData.getExecutorsNotApplying() == null)) {
            return null;//NOSONAR
        }
        List<Executor> executors = new ArrayList<>();

        if (grantOfRepresentationData.getExecutorsApplying() != null) {
            executors.addAll(grantOfRepresentationData.getExecutorsApplying().stream()
                    .map(e -> executorApplyingMapper.fromExecutorApplying(e))
                    .sorted(Comparator.comparing(e -> e.getIsApplicant() == null || !e.getIsApplicant()))
                    .collect(Collectors.toList()));
        }
        if (grantOfRepresentationData.getExecutorsNotApplying() != null) {
            executors.addAll(grantOfRepresentationData.getExecutorsNotApplying().stream()
                    .map(e -> executorNotApplyingMapper.fromExecutorNotApplying(e))
                    .collect(Collectors.toList()));
        }
        return executors;
    }
}
