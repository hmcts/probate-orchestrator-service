package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentation;
import uk.gov.hmcts.reform.probate.model.forms.IhtMethod;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;


@Mapper(uses = {PaymentsMapper.class, AliasNameMapper.class},
    imports = {ProbateType.class, IhtMethod.class, MapperUtils.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IntestacyMapper extends FormMapper<GrantOfRepresentation, IntestacyForm> {

    @Mappings( {
        @Mapping(target = "applicationType", source = "type"),
        @Mapping(target = "primaryApplicantForenames", source = "applicant.firstName"),
        @Mapping(target = "primaryApplicantSurname", source = "applicant.lastName"),
        @Mapping(target = "primaryApplicantRelationshipToDeceased", source = "applicant.relationshipToDeceased"),
        @Mapping(target = "primaryApplicantAdoptionInEnglandOrWales", source = "applicant.adoptionInEnglandOrWales"),
        @Mapping(target = "primaryApplicantPhoneNumber", source = "applicant.phoneNumber"),
        @Mapping(target = "primaryApplicantEmailAddress", source = "applicant.email"),
        @Mapping(target = "primaryApplicantAddressFound", source = "applicant.addressFound"),
        @Mapping(target = "primaryApplicantFreeTextAddress", source = "applicant.freeTextAddress"),
        @Mapping(target = "deceasedSurname", source = "deceased.lastName"),
        @Mapping(target = "deceasedForenames", source = "deceased.firstName"),
        @Mapping(target = "deceasedDateOfBirth", source = "deceased.dateOfBirth"),
        @Mapping(target = "deceasedDateOfDeath", source = "deceased.dateOfDeath"),
        @Mapping(target = "deceasedAddressFound", source = "deceased.addressFound"),
        @Mapping(target = "deceasedFreeTextAddress", source = "deceased.freeTextAddress"),
        @Mapping(target = "deceasedAnyOtherNames", source = "deceased.alias"),
        @Mapping(target = "deceasedMaritalStatus", source = "deceased.maritalStatus"),
        @Mapping(target = "deceasedDivorcedInEnglandOrWales", source = "deceased.divorcedInEnglandOrWales"),
        @Mapping(target = "deceasedDomicileInEngWales", source = "deceased.domiciledInEnglandOrWales"),
        @Mapping(target = "deceasedOtherChildren", source = "deceased.otherChildren"),
        @Mapping(target = "deceasedAllDeceasedChildrenOverEighteen", source = "deceased.allDeceasedChildrenOverEighteen"),
        @Mapping(target = "deceasedAnyDeceasedChildrenDieBeforeDeceased", source = "deceased.anyDeceasedChildrenDieBeforeDeceased"),
        @Mapping(target = "deceasedAnyDeceasedGrandchildrenUnderEighteen", source = "deceased.anyDeceasedGrandchildrenUnderEighteen"),
        @Mapping(target = "deceasedSpouseNotApplyingReason", source = "deceased.spouseNotApplyingReason"),
        @Mapping(target = "deceasedAnyChildren", source = "deceased.anyChildren"),
//        @Mapping(target = "deceasedAliasNameList",
//            expression = "java(AliasNameMapper.toCollectionMember(form.getDeceased().getOtherNames()))"),
        @Mapping(target = "outsideUkGrantCopies", source = "copies.overseas"),
        @Mapping(target = "extraCopiesOfGrant", source = "copies.uk"),
        @Mapping(target = "assetsOverseas", source = "assets.assetsOverseas"),
        @Mapping(target = "ihtReferenceNumber", source = "iht.identifier"),
        @Mapping(target = "ihtFormId", source = "iht.form"),
        @Mapping(target = "ihtFormCompletedOnline", expression = "java(form.getIht().getMethod() == IhtMethod.ONLINE)"),
        @Mapping(target = "ihtNetValue", expression = "java(MapperUtils.poundsToPennies(form.getIht().getNetValue()))"),
        @Mapping(target = "ihtGrossValue", expression = "java(MapperUtils.poundsToPennies(form.getIht().getGrossValue()))"),
        @Mapping(target = "registryLocation", source = "registry.name"),
        @Mapping(target = "registryAddress", source = "registry.address"),
        @Mapping(target = "registryEmail", source = "registry.email"),
        @Mapping(target = "registrySequenceNumber", source = "registry.sequenceNumber"),
        @Mapping(target = "assetsOverseasNetValue", expression = "java(MapperUtils.poundsToPennies(form.getAssets().getAssetsOverseasNetValue()))"),
        @Mapping(target = "declaration.declarationCheckbox", source = "declaration.declarationAgreement"),
        @Mapping(target = "primaryApplicantAddress.addressLine1", source = "applicant.address"),
        @Mapping(target = "primaryApplicantAddress.postCode", source = "applicant.postCode"),
        @Mapping(target = "deceasedAddress.addressLine1", source = "deceased.address")
    })
    GrantOfRepresentation toCaseData(IntestacyForm form);


    @Mappings( {
        @Mapping(target = "iht.method", expression = "java(grantOfRepresentation.getIhtFormCompletedOnline() ? IhtMethod.ONLINE : IhtMethod.BY_POST)"),
        @Mapping(target = "assets.assetsOverseasNetValue", expression = "java(MapperUtils.penniesToPounds(grantOfRepresentation.getAssetsOverseasNetValue()))"),
        @Mapping(target = "iht.netValue", expression = "java(MapperUtils.penniesToPounds(grantOfRepresentation.getIhtNetValue()))"),
        @Mapping(target = "iht.grossValue", expression = "java(MapperUtils.penniesToPounds(grantOfRepresentation.getIhtGrossValue()))"),
        //@Mapping(target = "deceased.otherNames", expression = "java(AliasNameMapper.fromCollectionMember(grantOfRepresentation.getDeceasedAliasNameList()))")
    })
    @InheritInverseConfiguration
    IntestacyForm fromCaseData(GrantOfRepresentation grantOfRepresentation);

}

