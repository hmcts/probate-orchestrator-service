package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExecutorApplyingMapper {

    @Mapping(target = "applyingExecutorName", source = "fullName")
    @Mapping(target = "applyingExecutorPhoneNumber", source = "mobile")
    @Mapping(target = "applyingExecutorEmail", source = "email")
    @Mapping(target = "applyingExecutorAddress.addressLine1", source = "address")
    @Mapping(target = "applyingExecutorOtherNames", expression = "java(executor.getHasOtherName() ? executor.getCurrentName() : null)")
    @Mapping(target = "applyingExecutorOtherNamesReason",
        expression = "java(executor.getHasOtherName() ? executor.getCurrentNameReason() : null)")
    @Mapping(target = "applyingExecutorOtherReason", expression = "java(executor.getHasOtherName() ? executor.getOtherReason() : null)")
    ExecutorApplying toExecutorApplying(Executor executor);
}
