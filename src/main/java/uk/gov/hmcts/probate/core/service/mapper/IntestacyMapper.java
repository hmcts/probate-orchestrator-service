package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromIhtMethod;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToIhtMethod;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToPennies;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToPounds;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentation;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.forms.IhtMethod;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;


@Mapper(componentModel = "spring", uses = {PaymentsMapper.class, AliasNameMapper.class, PoundsConverter.class,
    IhtMethodConverter.class},
    imports = {ProbateType.class, IhtMethod.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IntestacyMapper extends FormMapper<GrantOfRepresentation, IntestacyForm> {

    @Mappings( {
        @Mapping(target = "applicationType", source = "type"),
        @Mapping(target = "primaryApplicantForenames", source = "applicant.firstName"),
        @Mapping(target = "primaryApplicantSurname", source = "applicant.lastName"),
        @Mapping(target = "primaryApplicantRelationshipToDeceased", source = "applicant.relationshipToDeceased"),
        @Mapping(target = "primaryApplicantAdoptionInEnglandOrWales", source = "applicant.adoptionInEnglandOrWales"),
        @Mapping(target = "primaryApplicantPhoneNumber", source = "applicant.phoneNumber"),
        @Mapping(target = "primaryApplicantEmailAddress",
            expression = "java(form.getApplicant() != null && form.getApplicant().getEmail() != null ? " +
                "form.getApplicant().getEmail().toLowerCase() : null)"),
        @Mapping(target = "primaryApplicantAddressFound", source = "applicant.addressFound"),
        @Mapping(target = "primaryApplicantFreeTextAddress", source = "applicant.freeTextAddress"),
        @Mapping(target = "deceasedAddress.addressLine1", source = "deceased.address"),
        @Mapping(target = "deceasedAddress.postCode", source = "deceased.postCode"),
        @Mapping(target = "deceasedSurname", source = "deceased.lastName"),
        @Mapping(target = "deceasedForenames", source = "deceased.firstName"),
        @Mapping(target = "deceasedDateOfBirth", source = "deceased.dateOfBirth"),
        @Mapping(target = "deceasedDateOfDeath", source = "deceased.dateOfDeath"),
        @Mapping(target = "deceasedAddressFound", source = "deceased.addressFound"),
        @Mapping(target = "deceasedFreeTextAddress", source = "deceased.freeTextAddress"),
        @Mapping(target = "deceasedAnyOtherNames", source = "deceased.alias"),
        @Mapping(target = "deceasedMartialStatus", source = "deceased.maritalStatus"),
        @Mapping(target = "deceasedDivorcedInEnglandOrWales", source = "deceased.divorcedInEnglandOrWales"),
        @Mapping(target = "deceasedDomicileInEngWales", source = "deceased.domiciledInEnglandOrWales"),
        @Mapping(target = "deceasedOtherChildren", source = "deceased.otherChildren"),
        @Mapping(target = "childrenOverEighteenSurvived", source = "deceased.allDeceasedChildrenOverEighteen"),
        @Mapping(target = "childrenDied", source = "deceased.anyDeceasedChildrenDieBeforeDeceased"),
        @Mapping(target = "grandChildrenSurvivedUnderEighteen", source = "deceased.anyDeceasedGrandchildrenUnderEighteen"),
        @Mapping(target = "deceasedSpouseNotApplyingReason", source = "deceased.spouseNotApplyingReason"),
        @Mapping(target = "deceasedAnyChildren", source = "deceased.anyChildren"),
        @Mapping(target = "deceasedAliasNameList", source = "deceased.otherNames", qualifiedBy = {ToCollectionMember.class}),
        @Mapping(target = "outsideUkGrantCopies", source = "copies.overseas"),
        @Mapping(target = "extraCopiesOfGrant", source = "copies.uk"),
        @Mapping(target = "deceasedHasAssetsOutsideUK", source = "assets.assetsOverseas"),
        @Mapping(target = "ihtReferenceNumber", source = "iht.identifier"),
        @Mapping(target = "ihtFormId", source = "iht.form"),
        @Mapping(target = "ihtFormCompletedOnline", source = "iht.method", qualifiedBy = {FromIhtMethod.class}),
        @Mapping(target = "ihtNetValue", source = "iht.netValue", qualifiedBy = {ToPennies.class}),
        @Mapping(target = "ihtGrossValue", source = "iht.grossValue", qualifiedBy = {ToPennies.class}),
        @Mapping(target = "registryLocation", source = "registry.name"),
        @Mapping(target = "registryAddress", source = "registry.address"),
        @Mapping(target = "registryEmail", source = "registry.email"),
        @Mapping(target = "registrySequenceNumber", source = "registry.sequenceNumber"),
        @Mapping(target = "assetsOverseasNetValue", source = "assets.assetsOverseasNetValue", qualifiedBy = {ToPennies.class}),
        @Mapping(target = "declaration.declarationCheckbox", source = "declaration.declarationAgreement"),
        @Mapping(target = "primaryApplicantAddress.addressLine1", source = "applicant.address"),
        @Mapping(target = "primaryApplicantAddress.postCode", source = "applicant.postCode"),
        @Mapping(target = "payments", source = "payments", qualifiedBy = {ToCollectionMember.class}),
    })
    GrantOfRepresentation toCaseData(IntestacyForm form);

    @Mappings( {
        @Mapping(target = "applicant.email", source = "primaryApplicantEmailAddress"),
        @Mapping(target = "iht.method", source = "ihtFormCompletedOnline", qualifiedBy = {ToIhtMethod.class}),
        @Mapping(target = "assets.assetsOverseasNetValue", source = "assetsOverseasNetValue", qualifiedBy = {ToPounds.class}),
        @Mapping(target = "iht.netValue", source = "ihtNetValue", qualifiedBy = {ToPounds.class}),
        @Mapping(target = "iht.grossValue", source = "ihtGrossValue", qualifiedBy = {ToPounds.class}),
        @Mapping(target = "deceased.otherNames", source = "deceasedAliasNameList", qualifiedBy = {FromCollectionMember.class})
    })
    @InheritInverseConfiguration
    IntestacyForm fromCaseData(GrantOfRepresentation grantOfRepresentation);
}
