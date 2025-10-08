package uk.gov.hmcts.probate.core.service.mapper;

import org.apache.commons.lang3.BooleanUtils;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCaseAddress;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToFormAddress;
import uk.gov.hmcts.reform.probate.model.AliasReason;
import uk.gov.hmcts.reform.probate.model.Relationship;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;

@Mapper(componentModel = "spring", uses = {AddressMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {BooleanUtils.class, AddressMapper.class, AliasReason.class, Relationship.class}
)
public interface ExecutorApplyingMapper {

    @Mapping(target = "value.applyingExecutorName", expression = "java(ExecutorNamesMapper.getFullname(executor))")
    @Mapping(target = "value.applyingExecutorFirstName", source = "firstName")
    @Mapping(target = "value.applyingExecutorLastName", source = "lastName")
    @Mapping(target = "value.applyingExecutorPhoneNumber", source = "mobile")
    @Mapping(target = "value.applyingExecutorEmail", source = "email")
    @Mapping(target = "value.applyingExecutorAddress", source = "address", qualifiedBy = {ToCaseAddress.class})
    @Mapping(target = "value.applyingExecutorHasOtherName", source = "hasOtherName")
    @Mapping(target = "value.applyingExecutorEmailChanged", source = "emailChanged")
    @Mapping(target = "value.applyingExecutorEmailSent", source = "emailSent")
    @Mapping(target = "value.applyingExecutorOtherNames",
        expression = "java(BooleanUtils.isTrue(executor.getHasOtherName()) ? executor.getCurrentName() : null)")
    @Mapping(target = "value.applyingExecutorOtherNamesReason",
        expression = "java(BooleanUtils.isTrue(executor.getHasOtherName()) ? "
            + "AliasReason.fromString(executor.getCurrentNameReason()) : null)")
    @Mapping(target = "value.applyingExecutorOtherReason",
        expression = "java(BooleanUtils.isTrue(executor.getHasOtherName()) ? executor.getOtherReason() : null)")
    @Mapping(target = "value.applyingExecutorInvitationId", source = "inviteId")
    @Mapping(target = "value.applyingExecutorLeadName", source = "leadExecutorName")
    @Mapping(target = "value.applyingExecutorAgreed", source = "executorAgreed")
    @Mapping(target = "value.applyingExecutorApplicant", source = "isApplicant")
    @Mapping(target = "value.applyingExecutorPostCode", source = "postcode")
    @Mapping(target = "value.applicantFamilyDetails.relationshipToDeceased",
            expression = "java(executor.getCoApplicantRelationshipToDeceased()!= null ? "
               + " Relationship.fromString(executor.getCoApplicantRelationshipToDeceased()) : null)")

    @Mapping(target = "value.applicantFamilyDetails.childAdoptedIn",
            expression = "java(executor.getChildAdoptedIn()!= null ? executor.getChildAdoptedIn() : null)")
    @Mapping(target = "value.applicantFamilyDetails.childAdoptedOut",
            expression = "java(executor.getChildAdoptedOut()!= null ? executor.getChildAdoptedOut() : null)")
    @Mapping(target = "value.applicantFamilyDetails.childAdoptionInEnglandOrWales",
            expression = "java(executor.getChildAdoptionInEnglandOrWales()!= null ? executor.getChildAdoptionInEnglandOrWales() : null)")

    @Mapping(target = "value.applicantFamilyDetails.grandchildAdoptedIn",
            expression = "java(executor.getGrandchildAdoptedIn()!= null ? executor.getGrandchildAdoptedIn() : null)")
    @Mapping(target = "value.applicantFamilyDetails.grandchildAdoptedOut",
            expression = "java(executor.getGrandchildAdoptedOut()!= null ? executor.getGrandchildAdoptedOut() : null)")
    @Mapping(target = "value.applicantFamilyDetails.grandchildAdoptionInEnglandOrWales",
            expression = "java(executor.getGrandchildAdoptionInEnglandOrWales()!= null ? executor.getGrandchildAdoptionInEnglandOrWales() : null)")


    @Mapping(target = "value.applicantFamilyDetails.applicantParentAdoptedIn",
            expression = "java(executor.getApplicantParentAdoptedIn()!= null ? executor.getApplicantParentAdoptedIn() : null)")
    @Mapping(target = "value.applicantFamilyDetails.applicantParentAdoptedOut",
            expression = "java(executor.getApplicantParentAdoptedOut()!= null ? executor.getApplicantParentAdoptedOut() : null)")
    @Mapping(target = "value.applicantFamilyDetails.applicantParentAdoptionInEnglandOrWales",
            expression = "java(executor.getApplicantParentAdoptionInEnglandOrWales()!= null ? executor.getApplicantParentAdoptionInEnglandOrWales() : null)")
    CollectionMember<ExecutorApplying> toExecutorApplying(Executor executor);


    @Mapping(target = "currentName", source = "value.applyingExecutorOtherNames")
    @Mapping(target = "currentNameReason",
        expression = "java(executorApplyingCollectionMember.getValue().getApplyingExecutorOtherNamesReason()!=null ? "
        + "executorApplyingCollectionMember.getValue().getApplyingExecutorOtherNamesReason().getDescription() : null)")
    @Mapping(target = "address", source = "value.applyingExecutorAddress", qualifiedBy = {ToFormAddress.class})
    @Mapping(target = "otherReason", source = "value.applyingExecutorOtherReason")
    @Mapping(target = "isApplying", expression = "java(true)")
    @Mapping(target = "fullName", source = "value.applyingExecutorName")
    @Mapping(target = "coApplicantRelationshipToDeceased",
            expression = "java(executorApplyingCollectionMember.getValue().getApplicantFamilyDetails()!= null && executorApplyingCollectionMember.getValue().getApplicantFamilyDetails().getRelationshipToDeceased()!= null ? "
            + "executorApplyingCollectionMember.getValue().getApplicantFamilyDetails().getRelationshipToDeceased().getDescription() : null)")

    @Mapping(target = "childAdoptedIn", source = "value.applicantFamilyDetails.childAdoptedIn")
    @Mapping(target = "childAdoptedOut", source = "value.applicantFamilyDetails.childAdoptedOut")
    @Mapping(target = "childAdoptionInEnglandOrWales", source = "value.applicantFamilyDetails.childAdoptionInEnglandOrWales")

    @Mapping(target = "grandchildAdoptedIn",  source = "value.applicantFamilyDetails.grandchildAdoptedIn")
    @Mapping(target = "grandchildAdoptedOut", source = "value.applicantFamilyDetails.grandchildAdoptedOut")
    @Mapping(target = "grandchildAdoptionInEnglandOrWales", source = "value.applicantFamilyDetails.grandchildAdoptionInEnglandOrWales")

    @Mapping(target = "applicantParentAdoptedIn", source = "value.applicantFamilyDetails.applicantParentAdoptedIn")
    @Mapping(target = "applicantParentAdoptedOut", source = "value.applicantFamilyDetails.applicantParentAdoptedOut")
    @Mapping(target = "applicantParentAdoptionInEnglandOrWales", source = "value.applicantFamilyDetails.applicantParentAdoptionInEnglandOrWales")

    @InheritInverseConfiguration
    Executor fromExecutorApplying(CollectionMember<ExecutorApplying> executorApplyingCollectionMember);
}
