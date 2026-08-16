package uk.gov.hmcts.probate.core.service.mapper;

import org.apache.commons.lang3.BooleanUtils;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCaseAddress;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToFormAddress;
import uk.gov.hmcts.reform.probate.model.AliasReason;
import uk.gov.hmcts.reform.probate.model.CoApplicantRelationship;
import uk.gov.hmcts.reform.probate.model.cases.ApplicantFamilyDetails;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;

@Mapper(componentModel = "spring", uses = {AddressMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {BooleanUtils.class, AddressMapper.class, AliasReason.class, CoApplicantRelationship.class}
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
               + "CoApplicantRelationship.fromString(executor.getCoApplicantRelationshipToDeceased()) : null)")

    @Mapping(target = "value.applicantFamilyDetails.childAdoptedIn",
            expression = "java(executor.getChildAdoptedIn()!= null ? executor.getChildAdoptedIn() : null)")
    @Mapping(target = "value.applicantFamilyDetails.childAdoptedOut",
            expression = "java(executor.getChildAdoptedOut()!= null ? executor.getChildAdoptedOut() : null)")
    @Mapping(target = "value.applicantFamilyDetails.childAdoptionInEnglandOrWales",
            expression = "java(executor.getChildAdoptionInEnglandOrWales()!= null ? "
                    + "executor.getChildAdoptionInEnglandOrWales() : null)")
    @Mapping(target = "value.applicantFamilyDetails.childDieBeforeDeceased",
            expression = "java(executor.getChildDieBeforeDeceased()!= null ? "
                    + "executor.getChildDieBeforeDeceased() : null)")

    @Mapping(target = "value.applicantFamilyDetails.grandchildAdoptedIn",
            expression = "java(executor.getGrandchildAdoptedIn()!= null ? executor.getGrandchildAdoptedIn() : null)")
    @Mapping(target = "value.applicantFamilyDetails.grandchildAdoptedOut",
            expression = "java(executor.getGrandchildAdoptedOut()!= null ? executor.getGrandchildAdoptedOut() : null)")
    @Mapping(target = "value.applicantFamilyDetails.grandchildAdoptionInEnglandOrWales",
            expression = "java(executor.getGrandchildAdoptionInEnglandOrWales()!= null ? "
                    + "executor.getGrandchildAdoptionInEnglandOrWales() : null)")

    @Mapping(target = "value.applicantFamilyDetails.grandchildParentAdoptedIn",
            expression = "java(executor.getGrandchildParentAdoptedIn()!= null "
                    + "? executor.getGrandchildParentAdoptedIn() : null)")
    @Mapping(target = "value.applicantFamilyDetails.grandchildParentAdoptedOut",
            expression = "java(executor.getGrandchildParentAdoptedOut()!= null "
                    + "? executor.getGrandchildParentAdoptedOut() : null)")
    @Mapping(target = "value.applicantFamilyDetails.grandchildParentAdoptionInEnglandOrWales",
            expression = "java(executor.getGrandchildParentAdoptionInEnglandOrWales()!= null ? "
                    + "executor.getGrandchildParentAdoptionInEnglandOrWales() : null)")

    @Mapping(target = "value.applicantFamilyDetails.wholeBloodSiblingAdoptedIn",
            expression = "java(resolveWholeBloodSiblingAdoptedIn(executor))")
    @Mapping(target = "value.applicantFamilyDetails.wholeBloodSiblingAdoptedOut",
            expression = "java(resolveWholeBloodSiblingAdoptedOut(executor))")
    @Mapping(target = "value.applicantFamilyDetails.wholeBloodSiblingAdoptionInEnglandOrWales",
            expression = "java(resolveWholeBloodSiblingAdoptionInEnglandOrWales(executor))")

    @Mapping(target = "value.applicantFamilyDetails.wholeBloodSiblingDiedBeforeDeceased",
            expression = "java(resolveWholeBloodSiblingDiedBeforeDeceased(executor))")
    @Mapping(target = "value.applicantFamilyDetails.wholeBloodNieceOrNephewAdoptedIn",
            expression = "java(executor.getWholeBloodNieceOrNephewAdoptedIn()!= null "
                    + "? executor.getWholeBloodNieceOrNephewAdoptedIn() : null)")
    @Mapping(target = "value.applicantFamilyDetails.wholeBloodNieceOrNephewAdoptedOut",
            expression = "java(executor.getWholeBloodNieceOrNephewAdoptedOut()!= null "
                    + "? executor.getWholeBloodNieceOrNephewAdoptedOut() : null)")
    @Mapping(target = "value.applicantFamilyDetails.wholeBloodNieceOrNephewAdoptionInEnglandOrWales",
            expression = "java(executor.getWholeBloodNieceOrNephewAdoptionInEnglandOrWales()!= null ? "
                    + "executor.getWholeBloodNieceOrNephewAdoptionInEnglandOrWales() : null)")

    @Mapping(target = "value.applicantFamilyDetails.halfBloodSiblingAdoptedIn",
            expression = "java(resolveHalfBloodSiblingAdoptedIn(executor))")
    @Mapping(target = "value.applicantFamilyDetails.halfBloodSiblingAdoptedOut",
            expression = "java(resolveHalfBloodSiblingAdoptedOut(executor))")
    @Mapping(target = "value.applicantFamilyDetails.halfBloodSiblingAdoptionInEnglandOrWales",
            expression = "java(resolveHalfBloodSiblingAdoptionInEnglandOrWales(executor))")

    @Mapping(target = "value.applicantFamilyDetails.halfBloodSiblingDiedBeforeDeceased",
            expression = "java(resolveHalfBloodSiblingDiedBeforeDeceased(executor))")
    @Mapping(target = "value.applicantFamilyDetails.halfBloodNieceOrNephewAdoptedIn",
            expression = "java(executor.getHalfBloodNieceOrNephewAdoptedIn()!= null "
                    + "? executor.getHalfBloodNieceOrNephewAdoptedIn() : null)")
    @Mapping(target = "value.applicantFamilyDetails.halfBloodNieceOrNephewAdoptedOut",
            expression = "java(executor.getHalfBloodNieceOrNephewAdoptedOut()!= null "
                    + "? executor.getHalfBloodNieceOrNephewAdoptedOut() : null)")
    @Mapping(target = "value.applicantFamilyDetails.halfBloodNieceOrNephewAdoptionInEnglandOrWales",
            expression = "java(executor.getHalfBloodNieceOrNephewAdoptionInEnglandOrWales()!= null ? "
                    + "executor.getHalfBloodNieceOrNephewAdoptionInEnglandOrWales() : null)")

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
        expression = "java(executorApplyingCollectionMember.getValue().getApplicantFamilyDetails()!= null && "
        + "executorApplyingCollectionMember.getValue().getApplicantFamilyDetails().getRelationshipToDeceased()!=null ? "
        + "executorApplyingCollectionMember.getValue().getApplicantFamilyDetails().getRelationshipToDeceased()"
        + ".getDescription() : null)")

    @Mapping(target = "childAdoptedIn", source = "value.applicantFamilyDetails.childAdoptedIn")
    @Mapping(target = "childAdoptedOut", source = "value.applicantFamilyDetails.childAdoptedOut")
    @Mapping(target = "childAdoptionInEnglandOrWales",
            source = "value.applicantFamilyDetails.childAdoptionInEnglandOrWales")
    @Mapping(target = "childDieBeforeDeceased", source = "value.applicantFamilyDetails.childDieBeforeDeceased")

    @Mapping(target = "grandchildAdoptedIn",  source = "value.applicantFamilyDetails.grandchildAdoptedIn")
    @Mapping(target = "grandchildAdoptedOut", source = "value.applicantFamilyDetails.grandchildAdoptedOut")
    @Mapping(target = "grandchildAdoptionInEnglandOrWales",
            source = "value.applicantFamilyDetails.grandchildAdoptionInEnglandOrWales")
    @Mapping(target = "grandchildParentAdoptedIn",  source = "value.applicantFamilyDetails.grandchildParentAdoptedIn")
    @Mapping(target = "grandchildParentAdoptedOut", source = "value.applicantFamilyDetails.grandchildParentAdoptedOut")
    @Mapping(target = "grandchildParentAdoptionInEnglandOrWales",
            source = "value.applicantFamilyDetails.grandchildParentAdoptionInEnglandOrWales")

    @Mapping(target = "wholeBloodSiblingAdoptedIn",  expression = "java(mapWholeBloodSiblingAdoptedIn(executorApplyingCollectionMember))")
    @Mapping(target = "wholeNieceOrNephewParentAdoptedIn",
            expression = "java(mapWholeNieceOrNephewParentAdoptedIn(executorApplyingCollectionMember))")
    @Mapping(target = "wholeBloodSiblingAdoptedOut",
            expression = "java(mapWholeBloodSiblingAdoptedOut(executorApplyingCollectionMember))")
    @Mapping(target = "wholeNieceOrNephewParentAdoptedOut",
            expression = "java(mapWholeNieceOrNephewParentAdoptedOut(executorApplyingCollectionMember))")
    @Mapping(target = "wholeBloodSiblingAdoptionInEnglandOrWales",
            expression = "java(mapWholeBloodSiblingAdoptionInEnglandOrWales(executorApplyingCollectionMember))")
    @Mapping(target = "wholeNieceOrNephewParentAdoptionInEnglandOrWales",
            expression = "java(mapWholeNieceOrNephewParentAdoptionInEnglandOrWales(executorApplyingCollectionMember))")
    @Mapping(target = "wholeBloodSiblingDiedBeforeDeceased",
            expression = "java(mapWholeBloodSiblingDiedBeforeDeceased(executorApplyingCollectionMember))")
    @Mapping(target = "wholeNieceOrNephewParentDieBeforeDeceased",
            expression = "java(mapWholeNieceOrNephewParentDieBeforeDeceased(executorApplyingCollectionMember))")
    @Mapping(target = "wholeBloodNieceOrNephewAdoptedIn",
            source = "value.applicantFamilyDetails.wholeBloodNieceOrNephewAdoptedIn")
    @Mapping(target = "wholeBloodNieceOrNephewAdoptedOut",
            source = "value.applicantFamilyDetails.wholeBloodNieceOrNephewAdoptedOut")
    @Mapping(target = "wholeBloodNieceOrNephewAdoptionInEnglandOrWales",
            source = "value.applicantFamilyDetails.wholeBloodNieceOrNephewAdoptionInEnglandOrWales")

    @Mapping(target = "halfBloodSiblingAdoptedIn",  expression = "java(mapHalfBloodSiblingAdoptedIn(executorApplyingCollectionMember))")
    @Mapping(target = "halfNieceOrNephewParentAdoptedIn",
            expression = "java(mapHalfNieceOrNephewParentAdoptedIn(executorApplyingCollectionMember))")
    @Mapping(target = "halfBloodSiblingAdoptedOut", expression = "java(mapHalfBloodSiblingAdoptedOut(executorApplyingCollectionMember))")
    @Mapping(target = "halfNieceOrNephewParentAdoptedOut",
            expression = "java(mapHalfNieceOrNephewParentAdoptedOut(executorApplyingCollectionMember))")
    @Mapping(target = "halfBloodSiblingAdoptionInEnglandOrWales",
            expression = "java(mapHalfBloodSiblingAdoptionInEnglandOrWales(executorApplyingCollectionMember))")
    @Mapping(target = "halfNieceOrNephewParentAdoptionInEnglandOrWales",
            expression = "java(mapHalfNieceOrNephewParentAdoptionInEnglandOrWales(executorApplyingCollectionMember))")

    @Mapping(target = "halfBloodSiblingDiedBeforeDeceased",
            expression = "java(mapHalfBloodSiblingDiedBeforeDeceased(executorApplyingCollectionMember))")
    @Mapping(target = "halfNieceOrNephewParentDieBeforeDeceased",
            expression = "java(mapHalfNieceOrNephewParentDieBeforeDeceased(executorApplyingCollectionMember))")
    @Mapping(target = "halfBloodNieceOrNephewAdoptedIn",
            source = "value.applicantFamilyDetails.halfBloodNieceOrNephewAdoptedIn")
    @Mapping(target = "halfBloodNieceOrNephewAdoptedOut",
            source = "value.applicantFamilyDetails.halfBloodNieceOrNephewAdoptedOut")
    @Mapping(target = "halfBloodNieceOrNephewAdoptionInEnglandOrWales",
            source = "value.applicantFamilyDetails.halfBloodNieceOrNephewAdoptionInEnglandOrWales")

    @InheritInverseConfiguration
    Executor fromExecutorApplying(CollectionMember<ExecutorApplying> executorApplyingCollectionMember);

    default Boolean resolveWholeBloodSiblingAdoptedIn(Executor executor) {
        if (CoApplicantRelationship.WHOLE_BLOOD_NIECE_OR_NEPHEW.getDescription()
            .equalsIgnoreCase(executor.getCoApplicantRelationshipToDeceased())) {
            return executor.getWholeNieceOrNephewParentAdoptedIn();
        }
        return executor.getWholeBloodSiblingAdoptedIn();
    }

    default Boolean resolveWholeBloodSiblingAdoptedOut(Executor executor) {
        if (CoApplicantRelationship.WHOLE_BLOOD_NIECE_OR_NEPHEW.getDescription()
            .equalsIgnoreCase(executor.getCoApplicantRelationshipToDeceased())) {
            return executor.getWholeNieceOrNephewParentAdoptedOut();
        }
        return executor.getWholeBloodSiblingAdoptedOut();
    }

    default Boolean resolveWholeBloodSiblingAdoptionInEnglandOrWales(Executor executor) {
        if (CoApplicantRelationship.WHOLE_BLOOD_NIECE_OR_NEPHEW.getDescription()
            .equalsIgnoreCase(executor.getCoApplicantRelationshipToDeceased())) {
            return executor.getWholeNieceOrNephewParentAdoptionInEnglandOrWales();
        }
        return executor.getWholeBloodSiblingAdoptionInEnglandOrWales();
    }

    default Boolean resolveWholeBloodSiblingDiedBeforeDeceased(Executor executor) {
        if (CoApplicantRelationship.WHOLE_BLOOD_NIECE_OR_NEPHEW.getDescription()
            .equalsIgnoreCase(executor.getCoApplicantRelationshipToDeceased())) {
            return executor.getWholeNieceOrNephewParentDieBeforeDeceased();
        }
        return executor.getWholeBloodSiblingDiedBeforeDeceased();
    }

    default Boolean resolveHalfBloodSiblingAdoptedIn(Executor executor) {
        if (CoApplicantRelationship.HALF_BLOOD_NIECE_OR_NEPHEW.getDescription()
            .equalsIgnoreCase(executor.getCoApplicantRelationshipToDeceased())) {
            return executor.getHalfNieceOrNephewParentAdoptedIn();
        }
        return executor.getHalfBloodSiblingAdoptedIn();
    }

    default Boolean resolveHalfBloodSiblingAdoptedOut(Executor executor) {
        if (CoApplicantRelationship.HALF_BLOOD_NIECE_OR_NEPHEW.getDescription()
            .equalsIgnoreCase(executor.getCoApplicantRelationshipToDeceased())) {
            return executor.getHalfNieceOrNephewParentAdoptedOut();
        }
        return executor.getHalfBloodSiblingAdoptedOut();
    }

    default Boolean resolveHalfBloodSiblingAdoptionInEnglandOrWales(Executor executor) {
        if (CoApplicantRelationship.HALF_BLOOD_NIECE_OR_NEPHEW.getDescription()
            .equalsIgnoreCase(executor.getCoApplicantRelationshipToDeceased())) {
            return executor.getHalfNieceOrNephewParentAdoptionInEnglandOrWales();
        }
        return executor.getHalfBloodSiblingAdoptionInEnglandOrWales();
    }

    default Boolean resolveHalfBloodSiblingDiedBeforeDeceased(Executor executor) {
        if (CoApplicantRelationship.HALF_BLOOD_NIECE_OR_NEPHEW.getDescription()
            .equalsIgnoreCase(executor.getCoApplicantRelationshipToDeceased())) {
            return executor.getHalfNieceOrNephewParentDieBeforeDeceased();
        }
        return executor.getHalfBloodSiblingDiedBeforeDeceased();
    }

    default Boolean mapWholeBloodSiblingAdoptedIn(CollectionMember<ExecutorApplying> source) {
        ApplicantFamilyDetails afd = applicantFamilyDetails(source);
        return relationship(afd) == CoApplicantRelationship.WHOLE_BLOOD_NIECE_OR_NEPHEW
            ? null : valueOrNull(afd != null ? afd.getWholeBloodSiblingAdoptedIn() : null);
    }

    default Boolean mapWholeNieceOrNephewParentAdoptedIn(CollectionMember<ExecutorApplying> source) {
        ApplicantFamilyDetails afd = applicantFamilyDetails(source);
        return relationship(afd) == CoApplicantRelationship.WHOLE_BLOOD_NIECE_OR_NEPHEW
            ? valueOrNull(afd.getWholeBloodSiblingAdoptedIn()) : null;
    }

    default Boolean mapWholeBloodSiblingAdoptedOut(CollectionMember<ExecutorApplying> source) {
        ApplicantFamilyDetails afd = applicantFamilyDetails(source);
        return relationship(afd) == CoApplicantRelationship.WHOLE_BLOOD_NIECE_OR_NEPHEW
            ? null : valueOrNull(afd != null ? afd.getWholeBloodSiblingAdoptedOut() : null);
    }

    default Boolean mapWholeNieceOrNephewParentAdoptedOut(CollectionMember<ExecutorApplying> source) {
        ApplicantFamilyDetails afd = applicantFamilyDetails(source);
        return relationship(afd) == CoApplicantRelationship.WHOLE_BLOOD_NIECE_OR_NEPHEW
            ? valueOrNull(afd.getWholeBloodSiblingAdoptedOut()) : null;
    }

    default Boolean mapWholeBloodSiblingAdoptionInEnglandOrWales(CollectionMember<ExecutorApplying> source) {
        ApplicantFamilyDetails afd = applicantFamilyDetails(source);
        return relationship(afd) == CoApplicantRelationship.WHOLE_BLOOD_NIECE_OR_NEPHEW
                ? null : valueOrNull(afd != null ? afd.getWholeBloodSiblingAdoptionInEnglandOrWales() : null);
    }

    default Boolean mapWholeNieceOrNephewParentAdoptionInEnglandOrWales(CollectionMember<ExecutorApplying> source) {
        ApplicantFamilyDetails afd = applicantFamilyDetails(source);
        return relationship(afd) == CoApplicantRelationship.WHOLE_BLOOD_NIECE_OR_NEPHEW
                ? valueOrNull(afd.getWholeBloodSiblingAdoptionInEnglandOrWales()) : null;
    }

    default Boolean mapWholeBloodSiblingDiedBeforeDeceased(CollectionMember<ExecutorApplying> source) {
        ApplicantFamilyDetails afd = applicantFamilyDetails(source);
        return relationship(afd) == CoApplicantRelationship.WHOLE_BLOOD_NIECE_OR_NEPHEW
                ? null : valueOrNull(afd != null ? afd.getWholeBloodSiblingDiedBeforeDeceased() : null);
    }

    default Boolean mapWholeNieceOrNephewParentDieBeforeDeceased(CollectionMember<ExecutorApplying> source) {
        ApplicantFamilyDetails afd = applicantFamilyDetails(source);
        return relationship(afd) == CoApplicantRelationship.WHOLE_BLOOD_NIECE_OR_NEPHEW
                ? valueOrNull(afd.getWholeBloodSiblingDiedBeforeDeceased()) : null;
    }

    default Boolean mapHalfBloodSiblingAdoptedIn(CollectionMember<ExecutorApplying> source) {
        ApplicantFamilyDetails afd = applicantFamilyDetails(source);
        return relationship(afd) == CoApplicantRelationship.HALF_BLOOD_NIECE_OR_NEPHEW
            ? null : valueOrNull(afd != null ? afd.getHalfBloodSiblingAdoptedIn() : null);
    }

    default Boolean mapHalfNieceOrNephewParentAdoptedIn(CollectionMember<ExecutorApplying> source) {
        ApplicantFamilyDetails afd = applicantFamilyDetails(source);
        return relationship(afd) == CoApplicantRelationship.HALF_BLOOD_NIECE_OR_NEPHEW
            ? valueOrNull(afd.getHalfBloodSiblingAdoptedIn()) : null;
    }

    default Boolean mapHalfBloodSiblingAdoptedOut(CollectionMember<ExecutorApplying> source) {
        ApplicantFamilyDetails afd = applicantFamilyDetails(source);
        return relationship(afd) == CoApplicantRelationship.HALF_BLOOD_NIECE_OR_NEPHEW
            ? null : valueOrNull(afd != null ? afd.getHalfBloodSiblingAdoptedOut() : null);
    }

    default Boolean mapHalfNieceOrNephewParentAdoptedOut(CollectionMember<ExecutorApplying> source) {
        ApplicantFamilyDetails afd = applicantFamilyDetails(source);
        return relationship(afd) == CoApplicantRelationship.HALF_BLOOD_NIECE_OR_NEPHEW
            ? valueOrNull(afd.getHalfBloodSiblingAdoptedOut()) : null;
    }

    default Boolean mapHalfBloodSiblingAdoptionInEnglandOrWales(CollectionMember<ExecutorApplying> source) {
        ApplicantFamilyDetails afd = applicantFamilyDetails(source);
        return relationship(afd) == CoApplicantRelationship.HALF_BLOOD_NIECE_OR_NEPHEW
                ? null : valueOrNull(afd != null ? afd.getHalfBloodSiblingAdoptionInEnglandOrWales() : null);
    }

    default Boolean mapHalfNieceOrNephewParentAdoptionInEnglandOrWales(CollectionMember<ExecutorApplying> source) {
        ApplicantFamilyDetails afd = applicantFamilyDetails(source);
        return relationship(afd) == CoApplicantRelationship.HALF_BLOOD_NIECE_OR_NEPHEW
                ? valueOrNull(afd.getHalfBloodSiblingAdoptionInEnglandOrWales()) : null;
    }

    default Boolean mapHalfBloodSiblingDiedBeforeDeceased(CollectionMember<ExecutorApplying> source) {
        ApplicantFamilyDetails afd = applicantFamilyDetails(source);
        return relationship(afd) == CoApplicantRelationship.HALF_BLOOD_NIECE_OR_NEPHEW
                ? null : valueOrNull(afd != null ? afd.getHalfBloodSiblingDiedBeforeDeceased() : null);
    }

    default Boolean mapHalfNieceOrNephewParentDieBeforeDeceased(CollectionMember<ExecutorApplying> source) {
        ApplicantFamilyDetails afd = applicantFamilyDetails(source);
        return relationship(afd) == CoApplicantRelationship.HALF_BLOOD_NIECE_OR_NEPHEW
                ? valueOrNull(afd.getHalfBloodSiblingDiedBeforeDeceased()) : null;
    }

    default ApplicantFamilyDetails applicantFamilyDetails(CollectionMember<ExecutorApplying> source) {
        return source != null && source.getValue() != null ? source.getValue().getApplicantFamilyDetails() : null;
    }

    default CoApplicantRelationship relationship(ApplicantFamilyDetails applicantFamilyDetails) {
        return applicantFamilyDetails != null ? applicantFamilyDetails.getRelationshipToDeceased() : null;
    }


    default Boolean valueOrNull(Boolean value) {
        return value != null ? value : null;
    }
}
