package uk.gov.hmcts.probate.core.service.mapper;

import org.apache.commons.lang3.BooleanUtils;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromIhtMethod;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromLocalDate;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromMap;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromRegistryLocation;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToExecutorApplyingCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToExecutorNotApplyingCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToIhtMethod;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToLocalDate;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToMap;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToPennies;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToPounds;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToRegistryLocation;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.forms.IhtMethod;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaForm;

import java.time.LocalDate;


@Mapper(componentModel = "spring", uses = {PaymentsMapper.class, AliasNameMapper.class, RegistryLocationMapper.class,
    PoundsConverter.class, IhtMethodConverter.class, LegalStatementMapper.class, ExecutorsMapper.class,
    ExecutorApplyingMapper.class, ExecutorNotApplyingMapper.class, MapConverter.class, LocalDateTimeMapper.class},
    imports = {ApplicationType.class, GrantType.class, ProbateType.class, IhtMethod.class,
        LocalDate.class, ExecutorsMapper.class, BooleanUtils.class, AddressMapper.class},
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
    @Mapping(target = "deceasedAddress.addressLine1", source = "deceased.address")
    @Mapping(target = "deceasedAddress.postCode", source = "deceased.postcode")
    @Mapping(target = "deceasedDateOfBirth", source = "deceased.dateOfBirth", qualifiedBy = {ToLocalDate.class})
    @Mapping(target = "deceasedDateOfDeath", source = "deceased.dateOfDeath", qualifiedBy = {ToLocalDate.class})
    @Mapping(target = "deceasedAddressFound", source = "deceased.addressFound")
    @Mapping(target = "deceasedAddresses", source = "deceased.addresses", qualifiedBy = {FromMap.class})
    @Mapping(target = "primaryApplicantForenames", source = "applicant.firstName")
    @Mapping(target = "primaryApplicantSurname", source = "applicant.lastName")
    @Mapping(target = "primaryApplicantEmailAddress", source = "applicantEmail")
    @Mapping(target = "primaryApplicantAlias", source = "applicant.alias")
    @Mapping(target = "primaryApplicantAliasReason", source = "applicant.aliasReason")
    @Mapping(target = "primaryApplicantAddress.addressLine1", source = "applicant.address")
    @Mapping(target = "primaryApplicantAddress.postCode", source = "applicant.postcode")
    @Mapping(target = "primaryApplicantAddressFound", source = "applicant.addressFound")
    @Mapping(target = "primaryApplicantAddresses", source = "applicant.addresses", qualifiedBy = {FromMap.class})
    @Mapping(target = "primaryApplicantSameWillName", source = "applicant.nameAsOnTheWill")
    @Mapping(target = "primaryApplicantPhoneNumber", source = "applicant.phoneNumber")
    @Mapping(target = "primaryApplicantOtherReason", source = "applicant.otherReason")
    @Mapping(target = "registryLocation", source = "registry.name", qualifiedBy = {ToRegistryLocation.class})
    @Mapping(target = "payments", source = "payment", qualifiedBy = {ToCollectionMember.class})
    @Mapping(target = "softStop", source = "declaration.softStop")
    @Mapping(target = "legalStatement", source = "declaration.legalStatement")
    @Mapping(target = "declaration", source = "declaration.declaration")
    @Mapping(target = "declarationCheckbox", source = "declaration.declarationCheckbox")
    @Mapping(target = "executorsApplying", source = "executors.list", qualifiedBy = {ToExecutorApplyingCollectionMember.class})
    @Mapping(target = "executorsNotApplying", source = "executors.list", qualifiedBy = {ToExecutorNotApplyingCollectionMember.class})
    @Mapping(target = "numberOfApplicants", expression = "java(form.getExecutors() == null || form.getExecutors().getList() == null ? 0L : Long.valueOf(form.getExecutors().getList().size()))")
    @Mapping(target = "numberOfExecutors", source = "executors.executorsNumber")
    @Mapping(target = "executorsAllAlive", source = "executors.allalive")
    @Mapping(target = "otherExecutorsApplying", source = "executors.otherExecutorsApplying")
    @Mapping(target = "executorsHaveAlias", source = "executors.alias")
    @Mapping(target = "ihtReferenceNumber", expression = "java(form.getIht() != null && form.getIht().getMethod() == IhtMethod.ONLINE ? "
        + "form.getIht().getIdentifier() : \"Not applicable\")")
    @Mapping(target = "ihtFormId", source = "iht.ihtFormId")
    @Mapping(target = "ihtFormCompletedOnline", source = "iht.method", qualifiedBy = {FromIhtMethod.class})
    @Mapping(target = "ihtNetValue", source = "iht.netValue", qualifiedBy = {ToPennies.class})
    @Mapping(target = "ihtGrossValue", source = "iht.grossValue", qualifiedBy = {ToPennies.class})
    @Mapping(target = "extraCopiesOfGrant", source = "copies.uk")
    @Mapping(target = "outsideUkGrantCopies", expression = "java(form.getAssets() != null && BooleanUtils.isTrue(form.getAssets().getAssetsoverseas()) ? "
        + "form.getCopies().getOverseas() : null)")
    @Mapping(target = "legalDeclarationJson", source = "legalDeclaration", qualifiedBy = {FromMap.class})
    @Mapping(target = "checkAnswersSummaryJson", source = "checkAnswersSummary", qualifiedBy = {FromMap.class})
    @Mapping(target = "paymentPending", source = "paymentPending")
    @Mapping(target = "creatingPayment", source = "creatingPayment")
    GrantOfRepresentationData toCaseData(PaForm form);

    @Mapping(target = "type", expression = "java(ProbateType.PA)")
    @Mapping(target = "deceased.address", expression = "java(AddressMapper.getAddress(grantOfRepresentationData.getDeceasedAddress()))")
    @Mapping(target = "deceased.postcode", expression = "java(AddressMapper.getPostCode(grantOfRepresentationData.getDeceasedAddress()))")
    @Mapping(target = "deceased.freeTextAddress", expression = "java(AddressMapper.getFreeTextAddress(grantOfRepresentationData.getDeceasedAddressFound(), grantOfRepresentationData.getDeceasedAddress()))")
    @Mapping(target = "deceased.postcodeAddress", expression = "java(AddressMapper.getPostCodeAddress(grantOfRepresentationData.getDeceasedAddressFound(), grantOfRepresentationData.getDeceasedAddress()))")
    @Mapping(target = "deceased.dateOfBirth", source = "deceasedDateOfBirth", qualifiedBy = {FromLocalDate.class})
    @Mapping(target = "deceased.dateOfDeath", source = "deceasedDateOfDeath", qualifiedBy = {FromLocalDate.class})
    @Mapping(target = "applicant.address", expression = "java(AddressMapper.getAddress(grantOfRepresentationData.getPrimaryApplicantAddress()))")
    @Mapping(target = "applicant.postcode", expression = "java(AddressMapper.getPostCode(grantOfRepresentationData.getPrimaryApplicantAddress()))")
    @Mapping(target = "applicant.freeTextAddress", expression = "java(AddressMapper.getFreeTextAddress(grantOfRepresentationData.getPrimaryApplicantAddressFound(), grantOfRepresentationData.getPrimaryApplicantAddress()))")
    @Mapping(target = "applicant.postcodeAddress", expression = "java(AddressMapper.getPostCodeAddress(grantOfRepresentationData.getPrimaryApplicantAddressFound(), grantOfRepresentationData.getPrimaryApplicantAddress()))")
    @Mapping(target = "registry.name", source = "registryLocation", qualifiedBy = {FromRegistryLocation.class})
    @Mapping(target = "iht.netValue", source = "ihtNetValue", qualifiedBy = {ToPounds.class})
    @Mapping(target = "iht.grossValue", source = "ihtGrossValue", qualifiedBy = {ToPounds.class})
    @Mapping(target = "iht.grossIht205", expression = "java(IhtValuesMapper.getGrossIht205(grantOfRepresentationData.getIhtFormId(), grantOfRepresentationData.getIhtGrossValue()))")
    @Mapping(target = "iht.netIht205", expression = "java(IhtValuesMapper.getNetIht205(grantOfRepresentationData.getIhtFormId(), grantOfRepresentationData.getIhtNetValue()))")
    @Mapping(target = "iht.grossIht207", expression = "java(IhtValuesMapper.getGrossIht207(grantOfRepresentationData.getIhtFormId(), grantOfRepresentationData.getIhtGrossValue()))")
    @Mapping(target = "iht.netIht207", expression = "java(IhtValuesMapper.getNetIht207(grantOfRepresentationData.getIhtFormId(), grantOfRepresentationData.getIhtNetValue()))")
    @Mapping(target = "iht.grossIht400421", expression = "java(IhtValuesMapper.getGrossIht400421(grantOfRepresentationData.getIhtFormId(), grantOfRepresentationData.getIhtGrossValue()))")
    @Mapping(target = "iht.netIht400421", expression = "java(IhtValuesMapper.getNetIht400421(grantOfRepresentationData.getIhtFormId(), grantOfRepresentationData.getIhtNetValue()))")
    @Mapping(target = "executors.list", source = ".", qualifiedBy = {FromCollectionMember.class})
    @Mapping(target = "iht.identifier", expression = "java(grantOfRepresentationData.getIhtReferenceNumber() == null || grantOfRepresentationData.getIhtReferenceNumber().equals(\"Not applicable\") ? "
        + "null : grantOfRepresentationData.getIhtReferenceNumber())")
    @Mapping(target = "iht.method", source = "ihtFormCompletedOnline", qualifiedBy = {ToIhtMethod.class})
    @Mapping(target = "iht.form", source = "ihtFormId")
    @Mapping(target = "copies.overseas", source = "outsideUkGrantCopies")
    @Mapping(target = "assets.assetsoverseas", expression = "java(grantOfRepresentationData.getOutsideUkGrantCopies() == null ? null : " +
        "grantOfRepresentationData.getOutsideUkGrantCopies() > 0L)")
    @Mapping(target = "deceased.addresses", source = "deceasedAddresses", qualifiedBy = {ToMap.class})
    @Mapping(target = "applicant.addresses", source = "primaryApplicantAddresses", qualifiedBy = {ToMap.class})
    @Mapping(target = "legalDeclaration", source = "legalDeclarationJson", qualifiedBy = {ToMap.class})
    @Mapping(target = "checkAnswersSummary", source = "checkAnswersSummaryJson", qualifiedBy = {ToMap.class})
    @Mapping(target = "payment", source = "payments", qualifiedBy = {FromCollectionMember.class})
    @InheritInverseConfiguration
    PaForm fromCaseData(GrantOfRepresentationData grantOfRepresentation);

}
