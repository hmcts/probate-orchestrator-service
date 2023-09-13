package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromDocumentLink;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromIhtMethod;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromLocalDate;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromMap;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromRegistryLocation;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromUploadDocs;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCaseAddress;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToDocumentLink;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToFormAddress;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToIhtMethod;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToLocalDate;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToMap;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToPennies;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToPounds;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToPoundsString;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToRegistryLocation;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToUploadDocs;
import uk.gov.hmcts.reform.probate.model.IhtFormType;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.Relationship;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;
import uk.gov.hmcts.reform.probate.model.cases.DeathCertificate;
import uk.gov.hmcts.reform.probate.model.cases.MaritalStatus;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.SpouseNotApplyingReason;
import uk.gov.hmcts.reform.probate.model.forms.IhtMethod;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;

import java.time.LocalDate;


@Mapper(componentModel = "spring", uses = {PaPaymentMapper.class, PaymentsMapper.class, AliasNameMapper.class,
    RegistryLocationMapper.class, PoundsConverter.class,
    IhtMethodConverter.class, MapConverter.class, LegalStatementMapper.class, LocalDateTimeMapper.class,
    DocumentsMapper.class, StatementOfTruthMapper.class, AddressMapper.class},
    imports = {ApplicationType.class, GrantType.class, LocalDate.class, ProbateType.class, IhtMethod.class,
        MaritalStatus.class, DeathCertificate.class, Relationship.class, SpouseNotApplyingReason.class,
        AddressMapper.class, IhtFormType.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface IntestacyMapper extends FormMapper<GrantOfRepresentationData, IntestacyForm> {

    @Mapping(target = "applicationType", expression = "java(ApplicationType.PERSONAL)")
    @Mapping(target = "applicationSubmittedDate", expression = "java(LocalDate.now())")
    @Mapping(target = "grantType", expression = "java(GrantType.INTESTACY)")
    @Mapping(target = "primaryApplicantForenames", source = "applicant.firstName")
    @Mapping(target = "primaryApplicantSurname", source = "applicant.lastName")
    @Mapping(target = "primaryApplicantRelationshipToDeceased", expression =
        "java(form.getApplicant()!= null && form.getApplicant().getRelationshipToDeceased() != null ? "
        + "Relationship.fromString(form.getApplicant().getRelationshipToDeceased()) : null)")
    @Mapping(target = "primaryApplicantAdoptionInEnglandOrWales", source = "applicant.adoptionInEnglandOrWales")
    @Mapping(target = "primaryApplicantPhoneNumber", source = "applicant.phoneNumber")
    @Mapping(target = "primaryApplicantEmailAddress",
        expression = "java(form.getApplicantEmail() != null ? form.getApplicantEmail().toLowerCase() : null)")
    @Mapping(target = "primaryApplicantAddress", source = "applicant.address", qualifiedBy = {ToCaseAddress.class})
    @Mapping(target = "primaryApplicantAddresses", source = "applicant.addresses", qualifiedBy = {FromMap.class})
    @Mapping(target = "primaryApplicantPostCode", source = "applicant.postcode")
    @Mapping(target = "primaryApplicantNotRequiredToSendDocuments",
        source = "applicant.notRequiredToSendDocuments")
    @Mapping(target = "deceasedAddress", source = "deceased.address", qualifiedBy = {ToCaseAddress.class})
    @Mapping(target = "deceasedPostCode", source = "deceased.postcode")
    @Mapping(target = "deceasedAddresses", source = "deceased.addresses", qualifiedBy = {FromMap.class})
    @Mapping(target = "deceasedSurname", source = "deceased.lastName")
    @Mapping(target = "deceasedForenames", source = "deceased.firstName")
    @Mapping(target = "deceasedDateOfBirth", source = "deceased.dateOfBirth", qualifiedBy = {ToLocalDate.class})
    @Mapping(target = "deceasedDateOfDeath", source = "deceased.dateOfDeath", qualifiedBy = {ToLocalDate.class})
    @Mapping(target = "deceasedAnyOtherNames", source = "deceased.alias")
    @Mapping(target = "deceasedForeignDeathCertInEnglish", source = "deceased.englishForeignDeathCert")
    @Mapping(target = "deceasedForeignDeathCertTranslation", source = "deceased.foreignDeathCertTranslation")
    @Mapping(target = "deceasedMaritalStatus", expression = "java(form.getDeceased()!= null ? "
        + "MaritalStatus.fromString(form.getDeceased().getMaritalStatus()) : null)")
    @Mapping(target = "deceasedDivorcedInEnglandOrWales", source = "deceased.divorcedInEnglandOrWales")
    @Mapping(target = "deceasedOtherChildren", source = "deceased.otherChildren")
    @Mapping(target = "declarationCheckbox", source = "declaration.declarationCheckbox")
    @Mapping(target = "legalStatement", source = "declaration.legalStatement")
    @Mapping(target = "allDeceasedChildrenOverEighteen", source = "deceased.allDeceasedChildrenOverEighteen")
    @Mapping(target = "childrenDied", source = "deceased.anyDeceasedChildrenDieBeforeDeceased")
    @Mapping(target = "anyDeceasedGrandChildrenUnderEighteen",
        source = "deceased.anyDeceasedGrandchildrenUnderEighteen")
    @Mapping(target = "deceasedSpouseNotApplyingReason", expression = "java(form.getApplicant()!= null && "
        + "form.getApplicant().getSpouseNotApplyingReason() != null ? "
        + "SpouseNotApplyingReason.fromString(form.getApplicant().getSpouseNotApplyingReason()) : null)")
    @Mapping(target = "deceasedAnyChildren", source = "deceased.anyChildren")
    @Mapping(target = "deceasedAliasNameList", source = "deceased.otherNames",
        qualifiedBy = {ToCollectionMember.class})
    @Mapping(target = "outsideUkGrantCopies", source = "copies.overseas")
    @Mapping(target = "extraCopiesOfGrant", source = "copies.uk")
    @Mapping(target = "deceasedHasAssetsOutsideUK", source = "iht.assetsOutside")
    @Mapping(target = "ihtReferenceNumber", expression =
        "java(form.getIht() != null && form.getIht().getMethod() == IhtMethod.ONLINE ? "
        + "form.getIht().getIdentifier() : form.getIht() != null ? \"Not applicable\" : null)")
    @Mapping(target = "ihtFormId", expression =
        "java(form.getIht() != null && form.getIht().getMethod() == IhtMethod.ONLINE ? "
        + "IhtFormType.NOTAPPLICABLE : (form.getIht() !=null && form.getIht().getIhtFormId() !=null ? "
        + "IhtFormType.valueOf(form.getIht().getIhtFormId()) : null))")
    @Mapping(target = "ihtFormCompletedOnline", source = "iht.method", qualifiedBy = {FromIhtMethod.class})
    @Mapping(target = "ihtNetValue", source = "iht.netValue", qualifiedBy = {ToPennies.class})
    @Mapping(target = "ihtGrossValue", source = "iht.grossValue", qualifiedBy = {ToPennies.class})
    @Mapping(target = "ihtGrossValueField", source = "iht.grossValueField")
    @Mapping(target = "ihtNetValueField", source = "iht.netValueField")
    @Mapping(target = "ihtFormEstateValuesCompleted", source = "iht.estateValueCompleted")
    @Mapping(target = "ihtFormEstate", source = "iht.ihtFormEstateId")
    @Mapping(target = "ihtEstateGrossValue", source = "iht.estateGrossValue", qualifiedBy = {ToPennies.class})
    @Mapping(target = "ihtEstateGrossValueField", source = "iht.estateGrossValueField")
    @Mapping(target = "ihtEstateNetValue", source = "iht.estateNetValue", qualifiedBy = {ToPennies.class})
    @Mapping(target = "ihtEstateNetValueField", source = "iht.estateNetValueField")
    @Mapping(target = "ihtEstateNetQualifyingValue", source = "iht.estateNetQualifyingValue",
        qualifiedBy = {ToPennies.class})
    @Mapping(target = "ihtEstateNetQualifyingValueField", source = "iht.estateNetQualifyingValueField")
    @Mapping(target = "deceasedHadLateSpouseOrCivilPartner", source = "iht.deceasedHadLateSpouseOrCivilPartner")
    @Mapping(target = "ihtUnusedAllowanceClaimed", source = "iht.unusedAllowanceClaimed")
    @Mapping(target = "pcqId", source = "equality.pcqId")
    @Mapping(target = "registryLocation", source = "registry.name", qualifiedBy = {ToRegistryLocation.class})
    @Mapping(target = "registryAddress", source = "registry.address")
    @Mapping(target = "registryEmailAddress", source = "registry.email")
    @Mapping(target = "registrySequenceNumber", source = "registry.sequenceNumber")
    @Mapping(target = "assetsOutsideNetValue", source = "iht.assetsOutsideNetValue",
        qualifiedBy = {ToPennies.class})
    @Mapping(target = "legalDeclarationJson", source = "legalDeclaration", qualifiedBy = {FromMap.class})
    @Mapping(target = "checkAnswersSummaryJson", source = "checkAnswersSummary", qualifiedBy = {FromMap.class})
    @Mapping(target = "payments", source = "payment")
    @Mapping(target = "uniqueProbateCodeId", source = "uniqueProbateCodeId")
    @Mapping(target = "boDocumentsUploaded", source = "documents", qualifiedBy = {ToUploadDocs.class})
    @Mapping(target = "statementOfTruthDocument", source = "statementOfTruthDocument", qualifiedBy = {
        ToDocumentLink.class})
    @Mapping(target = "languagePreferenceWelsh", source = "language.bilingual")
    @Mapping(target = "deceasedDiedEngOrWales", source = "deceased.diedEngOrWales")
    @Mapping(target = "deceasedDeathCertificate", expression = "java(form.getDeceased()!= null ? "
        + "DeathCertificate.fromString(form.getDeceased().getDeathCertificate()) : null)")
    GrantOfRepresentationData toCaseData(IntestacyForm form);

    @Mapping(target = "type", expression = "java(ProbateType.INTESTACY)")
    @Mapping(target = "caseType", expression = "java(GrantType.INTESTACY.getName())")
    @Mapping(target = "deceased.address", source = "deceasedAddress", qualifiedBy = {ToFormAddress.class})
    @Mapping(target = "deceased.addresses", source = "deceasedAddresses", qualifiedBy = {ToMap.class})
    @Mapping(target = "deceased.dateOfBirth", source = "deceasedDateOfBirth", qualifiedBy = {FromLocalDate.class})
    @Mapping(target = "deceased.dateOfDeath", source = "deceasedDateOfDeath", qualifiedBy = {FromLocalDate.class})
    @Mapping(target = "deceased.otherNames", source = "deceasedAliasNameList", qualifiedBy = {
        FromCollectionMember.class})
    @Mapping(target = "deceased.maritalStatus", expression =
        "java(grantOfRepresentationData.getDeceasedMaritalStatus()!=null ? "
        + "grantOfRepresentationData.getDeceasedMaritalStatus().getDescription() : null)")
    @Mapping(target = "deceased.foreignDeathCertTranslation",
        expression = "java(grantOfRepresentationData.isDeceasedForeignDeathCertTranslated())")
    @Mapping(target = "deceased.deathCertificate",
        expression = "java(grantOfRepresentationData.getDeceasedDeathCert())")
    @Mapping(target = "deceased.postcode", source = "deceasedPostCode")
    @Mapping(target = "applicant.spouseNotApplyingReason",
        expression = "java(grantOfRepresentationData.getDeceasedSpouseNotApplyingReason()!=null ? "
        + "grantOfRepresentationData.getDeceasedSpouseNotApplyingReason().getDescription() : null)")
    @Mapping(target = "registry.name", source = "registryLocation", qualifiedBy = {FromRegistryLocation.class})
    @Mapping(target = "registry.address", source = "registryAddress")
    @Mapping(target = "registry.email", source = "registryEmailAddress")
    @Mapping(target = "registry.sequenceNumber", source = "registrySequenceNumber")
    @Mapping(target = "iht.netValue", source = "ihtNetValue", qualifiedBy = {ToPounds.class})
    @Mapping(target = "iht.grossValue", source = "ihtGrossValue", qualifiedBy = {ToPounds.class})
    @Mapping(target = "iht.grossIht205",
        expression = "java(IhtValuesMapper.getGrossIht205(grantOfRepresentationData.getIhtFormId(), "
        + "grantOfRepresentationData.getIhtGrossValue()))")
    @Mapping(target = "iht.netIht205",
        expression = "java(IhtValuesMapper.getNetIht205(grantOfRepresentationData.getIhtFormId(), "
        + "grantOfRepresentationData.getIhtNetValue()))")
    @Mapping(target = "iht.grossIht207",
        expression = "java(IhtValuesMapper.getGrossIht207(grantOfRepresentationData.getIhtFormId(), "
        + "grantOfRepresentationData.getIhtGrossValue()))")
    @Mapping(target = "iht.netIht207",
        expression = "java(IhtValuesMapper.getNetIht207(grantOfRepresentationData.getIhtFormId(), "
        + "grantOfRepresentationData.getIhtNetValue()))")
    @Mapping(target = "iht.grossIht400421",
        expression = "java(IhtValuesMapper.getGrossIht400421(grantOfRepresentationData.getIhtFormId(), "
        + "grantOfRepresentationData.getIhtGrossValue()))")
    @Mapping(target = "iht.netIht400421",
        expression = "java(IhtValuesMapper.getNetIht400421(grantOfRepresentationData.getIhtFormId(), "
        + "grantOfRepresentationData.getIhtNetValue()))")
    @Mapping(target = "iht.form", source = "ihtFormId")
    @Mapping(target = "iht.ihtFormId", source = "ihtFormId")
    @Mapping(target = "iht.method", source = "ihtFormCompletedOnline", qualifiedBy = {ToIhtMethod.class})
    @Mapping(target = "iht.identifier", expression =
        "java(grantOfRepresentationData.getIhtReferenceNumber() == null || "
        + "grantOfRepresentationData.getIhtReferenceNumber().equals(\"Not applicable\") ? "
        + "null : grantOfRepresentationData.getIhtReferenceNumber())")
    @Mapping(target = "applicant.address", source = "primaryApplicantAddress", qualifiedBy = {ToFormAddress.class})
    @Mapping(target = "applicant.addresses", source = "primaryApplicantAddresses", qualifiedBy = {ToMap.class})
    @Mapping(target = "applicant.postcode", source = "primaryApplicantPostCode")
    @Mapping(target = "applicant.relationshipToDeceased",
        expression = "java(grantOfRepresentationData.getPrimaryApplicantRelationshipToDeceased()!=null ? "
        + "grantOfRepresentationData.getPrimaryApplicantRelationshipToDeceased().getDescription() : null)")
    @Mapping(target = "iht.assetsOutsideNetValue", source = "assetsOutsideNetValue", qualifiedBy = {ToPounds.class})
    @Mapping(target = "iht.netValueAssetsOutsideField", source = "assetsOutsideNetValue", qualifiedBy = {
        ToPoundsString.class})
    @Mapping(target = "legalDeclaration", source = "legalDeclarationJson", qualifiedBy = {ToMap.class})
    @Mapping(target = "checkAnswersSummary", source = "checkAnswersSummaryJson", qualifiedBy = {ToMap.class})
    @Mapping(target = "payment", source = "payments")
    @Mapping(target = "payments", source = "payments", qualifiedBy = {FromCollectionMember.class})
    @Mapping(target = "documents", source = "boDocumentsUploaded", qualifiedBy = {FromUploadDocs.class})
    @Mapping(target = "uniqueProbateCodeId", source = "uniqueProbateCodeId")
    @Mapping(target = "statementOfTruthDocument", source = "statementOfTruthDocument", qualifiedBy = {
        FromDocumentLink.class})
    @Mapping(target = "documentsReceivedNotificationSent", source = "documentsReceivedNotificationSent")
    @InheritInverseConfiguration
    IntestacyForm fromCaseData(GrantOfRepresentationData grantOfRepresentation);
}
