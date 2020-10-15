package uk.gov.hmcts.probate.core.service.mapper;

import org.apache.commons.lang3.BooleanUtils;
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
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToExecutorApplyingCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToExecutorNotApplyingCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToFormAddress;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToIhtMethod;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToLocalDate;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToMap;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToPennies;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToPounds;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToRegistryLocation;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToUploadDocs;
import uk.gov.hmcts.reform.probate.model.AliasReason;
import uk.gov.hmcts.reform.probate.model.IhtFormType;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.forms.IhtMethod;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaForm;

import java.time.LocalDate;


@Mapper(componentModel = "spring", uses = {PaPaymentMapper.class, PaymentsMapper.class, AliasNameMapper.class, RegistryLocationMapper.class,
    PoundsConverter.class, IhtMethodConverter.class, LegalStatementMapper.class, ExecutorsMapper.class,
    ExecutorApplyingMapper.class, ExecutorNotApplyingMapper.class, MapConverter.class, LocalDateTimeMapper.class,
    AddressMapper.class, FeesMapper.class, DocumentsMapper.class, StatementOfTruthMapper.class},
    imports = {ApplicationType.class, GrantType.class, ProbateType.class, IhtMethod.class, IhtFormType.class,
        LocalDate.class, ExecutorsMapper.class, BooleanUtils.class, AddressMapper.class, OverseasCopiesMapper.class, AliasReason.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)

public interface PaMapper extends FormMapper<GrantOfRepresentationData, PaForm> {

    @Mapping(target = "applicationType", expression = "java(ApplicationType.PERSONAL)")
    @Mapping(target = "grantType", expression = "java(GrantType.GRANT_OF_PROBATE)")
    @Mapping(target = "applicationSubmittedDate", expression = "java(LocalDate.now())")
    @Mapping(target = "willHasCodicils", source = "will.codicils")
    @Mapping(target = "willNumberOfCodicils", source = "will.codicilsNumber")
    @Mapping(target = "deceasedSurname", source = "deceased.lastName")
    @Mapping(target = "deceasedForenames", source = "deceased.firstName")
    @Mapping(target = "deceasedAnyOtherNames", source = "deceased.alias")
    @Mapping(target = "deceasedAliasNameList", source = "deceased.otherNames", qualifiedBy = {ToCollectionMember.class})
    @Mapping(target = "deceasedMarriedAfterWillOrCodicilDate", source = "deceased.married")
    @Mapping(target = "deceasedAddress", source = "deceased.address", qualifiedBy = {ToCaseAddress.class})
    @Mapping(target = "deceasedDateOfBirth", source = "deceased.dateOfBirth", qualifiedBy = {ToLocalDate.class})
    @Mapping(target = "deceasedDateOfDeath", source = "deceased.dateOfDeath", qualifiedBy = {ToLocalDate.class})
    @Mapping(target = "deceasedAddressFound", source = "deceased.addressFound")
    @Mapping(target = "deceasedAddresses", source = "deceased.addresses", qualifiedBy = {FromMap.class})
    @Mapping(target = "deceasedPostCode", source = "deceased.postcode")
    @Mapping(target = "primaryApplicantForenames", source = "applicant.firstName")
    @Mapping(target = "primaryApplicantSurname", source = "applicant.lastName")
    @Mapping(target = "primaryApplicantEmailAddress", source = "applicantEmail")
    @Mapping(target = "primaryApplicantAlias", source = "applicant.alias")
    @Mapping(target = "primaryApplicantAliasReason", expression = "java(form.getApplicant()!= null && form.getApplicant().getAliasReason() != null ? AliasReason.fromString(form.getApplicant().getAliasReason()) : null)")
    @Mapping(target = "primaryApplicantAddress", source = "applicant.address", qualifiedBy = {ToCaseAddress.class})
    @Mapping(target = "primaryApplicantPostCode", source = "applicant.postcode")
    @Mapping(target = "primaryApplicantAddressFound", source = "applicant.addressFound")
    @Mapping(target = "primaryApplicantAddresses", source = "applicant.addresses", qualifiedBy = {FromMap.class})
    @Mapping(target = "primaryApplicantSameWillName", source = "applicant.nameAsOnTheWill")
    @Mapping(target = "primaryApplicantPhoneNumber", source = "applicant.phoneNumber")
    @Mapping(target = "primaryApplicantOtherReason", source = "applicant.otherReason")
    @Mapping(target = "primaryApplicantIsApplying", expression = "java(Boolean.TRUE)")
    @Mapping(target = "registryLocation", source = "registry.name", qualifiedBy = {ToRegistryLocation.class})
    @Mapping(target = "registryAddress", source = "registry.address")
    @Mapping(target = "registryEmailAddress", source = "registry.email")
    @Mapping(target = "registrySequenceNumber", source = "registry.sequenceNumber")
    @Mapping(target = "softStop", source = "declaration.softStop")
    @Mapping(target = "legalStatement", source = "declaration.legalStatement.en")
    @Mapping(target = "welshLegalStatement", source = "declaration.legalStatement.cy")
    @Mapping(target = "declaration", source = "declaration.declaration.en")
    @Mapping(target = "welshDeclaration", source = "declaration.declaration.cy")
    @Mapping(target = "declarationCheckbox", source = "declaration.declarationCheckbox")
    @Mapping(target = "executorsApplying", source = "executors.list", qualifiedBy = {ToExecutorApplyingCollectionMember.class})
    @Mapping(target = "executorsNotApplying", source = "executors.list", qualifiedBy = {ToExecutorNotApplyingCollectionMember.class})
    @Mapping(target = "numberOfApplicants", expression = "java(form.getExecutors() == null || form.getExecutors().getList() == null ? 0L : Long.valueOf(form.getExecutors().getList().size()))")
    @Mapping(target = "numberOfExecutors", source = "executors.executorsNumber")
    @Mapping(target = "executorsAllAlive", source = "executors.allalive")
    @Mapping(target = "otherExecutorsApplying", source = "executors.otherExecutorsApplying")
    @Mapping(target = "executorsHaveAlias", source = "executors.alias")
    @Mapping(target = "ihtReferenceNumber", expression = "java(form.getIht() != null && form.getIht().getMethod() == IhtMethod.ONLINE ? "
        + "form.getIht().getIdentifier() : form.getIht() != null ? \"Not applicable\" : null)")
    @Mapping(target = "ihtFormId", expression = "java(form.getIht() != null && form.getIht().getMethod() == IhtMethod.ONLINE ? " +
            "IhtFormType.NOTAPPLICABLE : (form.getIht() !=null && form.getIht().getIhtFormId() !=null ? IhtFormType.valueOf(form.getIht().getIhtFormId()) : null))")
    @Mapping(target = "ihtFormCompletedOnline", source = "iht.method", qualifiedBy = {FromIhtMethod.class})
    @Mapping(target = "ihtNetValue", source = "iht.netValue", qualifiedBy = {ToPennies.class})
    @Mapping(target = "ihtGrossValue", source = "iht.grossValue", qualifiedBy = {ToPennies.class})
    @Mapping(target = "ihtGrossValueField", source = "iht.grossValueField")
    @Mapping(target = "ihtNetValueField", source = "iht.netValueField")
    @Mapping(target = "pcqId", source = "equality.pcqId")
    @Mapping(target = "extraCopiesOfGrant", source = "copies.uk")
    @Mapping(target = "outsideUkGrantCopies", expression = "java(OverseasCopiesMapper.mapOverseasCopies(form))")
    @Mapping(target = "legalDeclarationJson", source = "legalDeclaration", qualifiedBy = {FromMap.class})
    @Mapping(target = "checkAnswersSummaryJson", source = "checkAnswersSummary", qualifiedBy = {FromMap.class})
    @Mapping(target = "payments", source = "payment")
    @Mapping(target = "boDocumentsUploaded", source = "documents", qualifiedBy = {ToUploadDocs.class})
    @Mapping(target = "statementOfTruthDocument", source = "statementOfTruthDocument", qualifiedBy = {ToDocumentLink.class})
    @Mapping(target = "languagePreferenceWelsh", source = "language.bilingual")
    @Mapping(target = "deceasedDiedEngOrWales", source = "deceased.diedEngOrWales")
    @Mapping(target = "deathCertificate", source = "deceased.deathCertificate")
    GrantOfRepresentationData toCaseData(PaForm form);

    @Mapping(target = "type", expression = "java(ProbateType.PA)")
    @Mapping(target = "caseType", expression = "java(GrantType.GRANT_OF_PROBATE.getName())")
    @Mapping(target = "deceased.address", source = "deceasedAddress", qualifiedBy = {ToFormAddress.class})
    @Mapping(target = "deceased.addresses", source = "deceasedAddresses", qualifiedBy = {ToMap.class})
    @Mapping(target = "deceased.dateOfBirth", source = "deceasedDateOfBirth", qualifiedBy = {FromLocalDate.class})
    @Mapping(target = "deceased.dateOfDeath", source = "deceasedDateOfDeath", qualifiedBy = {FromLocalDate.class})
    @Mapping(target = "deceased.postcode", source = "deceasedPostCode")
    @Mapping(target = "registry.name", source = "registryLocation", qualifiedBy = {FromRegistryLocation.class})
    @Mapping(target = "registry.address", source = "registryAddress")
    @Mapping(target = "registry.email", source = "registryEmailAddress")
    @Mapping(target = "registry.sequenceNumber", source = "registrySequenceNumber")
    @Mapping(target = "iht.netValue", source = "ihtNetValue", qualifiedBy = {ToPounds.class})
    @Mapping(target = "iht.grossValue", source = "ihtGrossValue", qualifiedBy = {ToPounds.class})
    @Mapping(target = "iht.grossIht205", expression = "java(IhtValuesMapper.getGrossIht205(grantOfRepresentationData.getIhtFormId(), grantOfRepresentationData.getIhtGrossValue()))")
    @Mapping(target = "iht.netIht205", expression = "java(IhtValuesMapper.getNetIht205(grantOfRepresentationData.getIhtFormId(), grantOfRepresentationData.getIhtNetValue()))")
    @Mapping(target = "iht.grossIht207", expression = "java(IhtValuesMapper.getGrossIht207(grantOfRepresentationData.getIhtFormId(), grantOfRepresentationData.getIhtGrossValue()))")
    @Mapping(target = "iht.netIht207", expression = "java(IhtValuesMapper.getNetIht207(grantOfRepresentationData.getIhtFormId(), grantOfRepresentationData.getIhtNetValue()))")
    @Mapping(target = "iht.grossIht400421", expression = "java(IhtValuesMapper.getGrossIht400421(grantOfRepresentationData.getIhtFormId(), grantOfRepresentationData.getIhtGrossValue()))")
    @Mapping(target = "iht.netIht400421", expression = "java(IhtValuesMapper.getNetIht400421(grantOfRepresentationData.getIhtFormId(), grantOfRepresentationData.getIhtNetValue()))")
    @Mapping(target = "executors.list", source = ".", qualifiedBy = {FromCollectionMember.class})
    @Mapping(target = "executors.invitesSent", expression = "java(grantOfRepresentationData.haveInvitesBeenSent())")
    @Mapping(target = "iht.identifier", expression = "java(grantOfRepresentationData.getIhtReferenceNumber() == null || grantOfRepresentationData.getIhtReferenceNumber().equals(\"Not applicable\") ? "
            + "null : grantOfRepresentationData.getIhtReferenceNumber())")
    @Mapping(target = "iht.method", source = "ihtFormCompletedOnline", qualifiedBy = {ToIhtMethod.class})
    @Mapping(target = "iht.form", source = "ihtFormId")
    @Mapping(target = "iht.ihtFormId", source = "ihtFormId")
    @Mapping(target = "copies.overseas", source = "outsideUkGrantCopies")
    @Mapping(target = "assets.assetsoverseas", expression = "java(grantOfRepresentationData.getOutsideUkGrantCopies() == null ? null : grantOfRepresentationData.getOutsideUkGrantCopies() > 0L)")
    @Mapping(target = "applicant.aliasReason", expression = "java(grantOfRepresentationData.getPrimaryApplicantAliasReason()!=null ? grantOfRepresentationData.getPrimaryApplicantAliasReason().getDescription() : null)")
    @Mapping(target = "applicant.address", source = "primaryApplicantAddress", qualifiedBy = {ToFormAddress.class})
    @Mapping(target = "applicant.addresses", source = "primaryApplicantAddresses", qualifiedBy = {ToMap.class})
    @Mapping(target = "applicant.postcode", source = "primaryApplicantPostCode")
    @Mapping(target = "legalDeclaration", source = "legalDeclarationJson", qualifiedBy = {ToMap.class})
    @Mapping(target = "checkAnswersSummary", source = "checkAnswersSummaryJson", qualifiedBy = {ToMap.class})
    @Mapping(target = "payment", source = "payments")
    @Mapping(target = "payments", source = "payments", qualifiedBy = {FromCollectionMember.class})
    @Mapping(target = "documents", source = "boDocumentsUploaded", qualifiedBy = {FromUploadDocs.class})
    @Mapping(target = "statementOfTruthDocument", source = "statementOfTruthDocument", qualifiedBy = {FromDocumentLink.class})
    @InheritInverseConfiguration
    PaForm fromCaseData(GrantOfRepresentationData grantOfRepresentation);
}
