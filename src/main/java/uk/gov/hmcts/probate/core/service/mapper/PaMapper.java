package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromIhtMethod;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromRegistryLocation;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToIhtMethod;
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
    ExecutorApplyingMapper.class, ExecutorNotApplyingMapper.class},
    imports = {ApplicationType.class, GrantType.class, ProbateType.class, IhtMethod.class,
        LocalDate.class, ExecutorsMapper.class},
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
    @Mapping(target = "deceasedDateOfBirth", source = "deceased.dateOfBirth")
    @Mapping(target = "deceasedDateOfDeath", source = "deceased.dateOfDeath")
    @Mapping(target = "primaryApplicantForenames", source = "applicant.firstName")
    @Mapping(target = "primaryApplicantSurname", source = "applicant.lastName")
    @Mapping(target = "primaryApplicantEmailAddress", source = "applicantEmail")
    @Mapping(target = "primaryApplicantAlias", source = "applicant.alias")
    @Mapping(target = "primaryApplicantAliasReason", source = "applicant.aliasReason")
    @Mapping(target = "primaryApplicantAddress.addressLine1", source = "applicant.address")
    @Mapping(target = "primaryApplicantSameWillName", source = "applicant.nameAsOnTheWill")
    @Mapping(target = "primaryApplicantPhoneNumber", source = "applicant.phoneNumber")
    @Mapping(target = "primaryApplicantOtherReason", source = "applicant.otherReason")
    @Mapping(target = "registryLocation", source = "registry.name", qualifiedBy = {ToRegistryLocation.class})
    @Mapping(target = "payments", source = "payments", qualifiedBy = {ToCollectionMember.class})
    @Mapping(target = "softStop", source = "declaration.softStop")
    @Mapping(target = "legalStatement", source = "declaration.legalStatement")
    @Mapping(target = "declaration", source = "declaration.declaration")
    @Mapping(target = "executorsApplying", source = "executors.list", qualifiedBy = {ToCollectionMember.class})
    @Mapping(target = "executorsNotApplying", source = "executors.list", qualifiedBy = {ToCollectionMember.class})
    @Mapping(target = "numberOfApplicants", expression = "java(Long.valueOf(form.getExecutors().getList().size()))")
    @Mapping(target = "numberOfExecutors", source = "executors.executorsNumber")
    @Mapping(target = "ihtReferenceNumber", expression = "java(form.getIht().getMethod() == IhtMethod.ONLINE ? "
        + "form.getIht().getIdentifier() : \"Not applicable\")")
    @Mapping(target = "ihtFormId", source = "iht.ihtFormId")
    @Mapping(target = "ihtFormCompletedOnline", source = "iht.method", qualifiedBy = {FromIhtMethod.class})
    @Mapping(target = "ihtNetValue", source = "iht.netValue", qualifiedBy = {ToPennies.class})
    @Mapping(target = "ihtGrossValue", source = "iht.grossValue", qualifiedBy = {ToPennies.class})
    @Mapping(target = "extraCopiesOfGrant", source = "copies.uk", defaultValue = "0L")
    @Mapping(target = "outsideUkGrantCopies", expression = "java(form.getAssets().getAssetsoverseas() ? "
        + "form.getCopies().getOverseas() : 0L)")
    GrantOfRepresentationData toCaseData(PaForm form);

    @Mapping(target = "type", expression = "java(ProbateType.PA)")
    @Mapping(target = "deceased.address", expression = "java(grantOfRepresentationData.getDeceasedAddress() == null ? null : grantOfRepresentationData.getDeceasedAddress().getAddressLine1())")
    @Mapping(target = "deceased.postcode", expression = "java(grantOfRepresentationData.getDeceasedAddress() == null ? null : grantOfRepresentationData.getDeceasedAddress().getPostCode())")
    @Mapping(target = "applicant.address", expression = "java(grantOfRepresentationData.getPrimaryApplicantAddress() == null ? null : grantOfRepresentationData.getPrimaryApplicantAddress().getAddressLine1())")
    @Mapping(target = "registry.name", source = "registryLocation", qualifiedBy = {FromRegistryLocation.class})
    @Mapping(target = "iht.netValue", source = "ihtNetValue", qualifiedBy = {ToPounds.class})
    @Mapping(target = "iht.grossValue", source = "ihtGrossValue", qualifiedBy = {ToPounds.class})
    @Mapping(target = "executors.list", source = ".", qualifiedBy = {FromCollectionMember.class})
    @Mapping(target = "iht.identifier", expression = "java(grantOfRepresentationData.getIhtReferenceNumber().equals(\"Not applicable\") ? "
        + "null : grantOfRepresentationData.getIhtReferenceNumber())")
    @Mapping(target = "iht.method", source = "ihtFormCompletedOnline", qualifiedBy = {ToIhtMethod.class})
    @Mapping(target = "copies.overseas", source = "outsideUkGrantCopies")
    @Mapping(target = "assets.assetsoverseas", expression = "java(grantOfRepresentationData.getOutsideUkGrantCopies() > 0L)")
    @InheritInverseConfiguration
    PaForm fromCaseData(GrantOfRepresentationData grantOfRepresentation);

}
