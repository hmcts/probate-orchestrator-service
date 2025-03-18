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
            .filter(executor -> executor.getIsApplying() == null
                || BooleanUtils.isFalse(executor.getIsApplying()))
            .map(executorNotApplyingMapper::toExecutorNotApplying)
            .sorted(Comparator.comparing(e -> e.getValue().getNotApplyingExecutorName()))
            .collect(Collectors.toList());
    }

    @ToExecutorApplyingCollectionMember
    List<CollectionMember<ExecutorApplying>> toExecutorApplyingCollectionMember(List<Executor> executors) {
        if (executors == null) {
            return null;//NOSONAR
        }
        return executors.stream()
            .filter(executor -> BooleanUtils.isTrue(executor.getIsApplying())
                && !BooleanUtils.isTrue(executor.getIsApplicant()))
            .map(executorApplyingMapper::toExecutorApplying)
            .collect(Collectors.toList());
    }

    @FromCollectionMember
    List<Executor> fromCollectionMember(GrantOfRepresentationData grantOfRepresentationData) {
        if (grantOfRepresentationData == null
            || (grantOfRepresentationData.getExecutorsApplying() == null
                && grantOfRepresentationData.getExecutorsNotApplying() == null)) {
            return null;//NOSONAR
        }
        List<Executor> executors = new ArrayList<>();

        List<Executor> applyingExecutors = new ArrayList<>();
        if (grantOfRepresentationData.getExecutorsApplying() != null) {
            applyingExecutors.addAll(grantOfRepresentationData.getExecutorsApplying().stream()
                .map(executorApplyingMapper::fromExecutorApplying)
                .collect(Collectors.toList()));
        }

        addPrimaryApplicantExecutor(grantOfRepresentationData, applyingExecutors);
        executors = applyingExecutors.stream().toList();

        if (grantOfRepresentationData.getExecutorsNotApplying() != null) {
            executors.addAll(grantOfRepresentationData.getExecutorsNotApplying().stream()
                .map(executorNotApplyingMapper::fromExecutorNotApplying)
                .collect(Collectors.toList()));
        }

        return executors;
    }

    private void addPrimaryApplicantExecutor(GrantOfRepresentationData grantOfRepresentationData,
                                             List<Executor> executors) {
        Executor primaryApplicant = Executor.builder()
            .isApplying(Boolean.TRUE)
            .fullName(grantOfRepresentationData.getPrimaryApplicantForenames()
                + " " + grantOfRepresentationData.getPrimaryApplicantSurname())
            .firstName(grantOfRepresentationData.getPrimaryApplicantForenames())
            .lastName(grantOfRepresentationData.getPrimaryApplicantSurname())
            .isApplicant(Boolean.TRUE)
            .build();

        executors.add(primaryApplicant);
    }
}
