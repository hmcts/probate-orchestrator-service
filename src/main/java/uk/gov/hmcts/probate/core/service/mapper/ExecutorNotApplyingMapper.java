package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorNotApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorNotApplyingReason;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
    imports = {ExecutorNotApplyingReason.class})
public interface ExecutorNotApplyingMapper {

    @Mapping(target = "value.notApplyingExecutorName", expression = "java(ExecutorNamesMapper.getFullname(executor))")
    @Mapping(target = "value.notApplyingExecutorReason",
        expression = "java(executor.getNotApplyingKey() == null ? "
            + "null : ExecutorNotApplyingReason.getExecutorNotApplyingReasonByValue(executor.getNotApplyingKey()))")
    @Mapping(target = "value.notApplyingExecutorNotified", source = "executorNotified")
    @Mapping(target = "value.notApplyingExecutorIsDead", source = "isDead")
    @Mapping(target = "value.notApplyingExecutorDiedBefore", source = "diedBefore")
    CollectionMember<ExecutorNotApplying> toExecutorNotApplying(Executor executor);

    @Mapping(target = "notApplyingKey",
        expression = "java(executorNotApplyingCollectionMember.getValue().getNotApplyingExecutorReason() == null ? "
            + "null : executorNotApplyingCollectionMember.getValue().getNotApplyingExecutorReason().getOptionValue())")
    @Mapping(target = "isApplying", expression = "java(false)")
    @Mapping(target = "fullName", source = "value.notApplyingExecutorName")
    @InheritInverseConfiguration
    Executor fromExecutorNotApplying(CollectionMember<ExecutorNotApplying> executorNotApplyingCollectionMember);
}
