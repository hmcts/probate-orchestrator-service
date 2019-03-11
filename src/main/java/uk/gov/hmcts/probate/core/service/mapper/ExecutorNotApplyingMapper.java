package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorNotApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorNotApplyingReason;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {ExecutorNotApplyingReason.class})
public interface ExecutorNotApplyingMapper {

    @Mapping(target = "notApplyingExecutorName", source = "fullName")
    @Mapping(target = "notApplyingExecutorReason",
        expression = "java(ExecutorNotApplyingReason.getExecutorNotApplyingReasonByValue(executor.getNotApplyingKey()))")
    ExecutorNotApplying toExecutorNotApplying(Executor executor);
}
