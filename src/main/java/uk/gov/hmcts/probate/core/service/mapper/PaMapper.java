package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromIhtMethod;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToPennies;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToRegistryLocation;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.forms.IhtMethod;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaForm;

import java.time.LocalDate;


@Mapper(componentModel = "spring", uses = {PaymentsMapper.class, AliasNameMapper.class, RegistryLocationMapper.class,
    PoundsConverter.class, IhtMethodConverter.class, LegalStatementExecutorsApplyingMapper.class,
    LegalStatementExecutorsNotApplyingMapper.class},
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
    @Mapping(target = "legalStatement.applicant", source = "declaration.legalStatement.applicant")
    @Mapping(target = "legalStatement.deceased", source = "declaration.legalStatement.deceased")
    @Mapping(target = "legalStatement.deceasedOtherNames", source = "declaration.legalStatement.deceasedOtherNames")
    @Mapping(target = "legalStatement.executorsApplying", source = "declaration.legalStatement.executorsApplying",
        qualifiedBy = {ToCollectionMember.class})
    @Mapping(target = "legalStatement.deceasedEstateValue", source = "declaration.legalStatement.deceasedEstateValue")
    @Mapping(target = "legalStatement.deceasedEstateLand", source = "declaration.legalStatement.deceasedEstateLand")
    @Mapping(target = "legalStatement.executorsNotApplying", source = "declaration.legalStatement.executorsNotApplying",
        qualifiedBy = {ToCollectionMember.class})
    @Mapping(target = "legalStatement.intro", source = "declaration.legalStatement.intro")

    @Mapping(target = "declaration.confirm", source = "declaration.declaration.confirm")
    @Mapping(target = "declaration.confirmItem1", source = "declaration.declaration.confirmItem1")
    @Mapping(target = "declaration.confirmItem2", source = "declaration.declaration.confirmItem2")
    @Mapping(target = "declaration.confirmItem3", source = "declaration.declaration.confirmItem3")
    @Mapping(target = "declaration.requests", source = "declaration.declaration.requests")
    @Mapping(target = "declaration.requestsItem1", source = "declaration.declaration.requestsItem1")
    @Mapping(target = "declaration.requestsItem2", source = "declaration.declaration.requestsItem2")
    @Mapping(target = "declaration.understand", source = "declaration.declaration.understand")
    @Mapping(target = "declaration.understandItem1", source = "declaration.declaration.understandItem1")
    @Mapping(target = "declaration.understandItem2", source = "declaration.declaration.understandItem2")
    @Mapping(target = "declaration.accept", source = "declaration.declaration.accept")

    @Mapping(target = "executorsApplying", source = "executors.list", qualifiedBy = {ToCollectionMember.class})
    @Mapping(target = "executorsNotApplying", source = "executors.list", qualifiedBy = {ToCollectionMember.class})
    @Mapping(target = "numberOfApplicants",
        expression = "java(ExecutorsMapper.getNoOfApplicants(form.getExecutors().getList()))")
    @Mapping(target = "numberOfExecutors", source = "executors.executorsNumber")

    @Mapping(target = "ihtReferenceNumber", expression = "java(form.getIht().getMethod() == IhtMethod.ONLINE ? "
        + "form.getIht().getIdentifier() : \"Not applicable\")")
    @Mapping(target = "ihtFormId", source = "iht.ihtFormId")
    @Mapping(target = "ihtFormCompletedOnline", source = "iht.method", qualifiedBy = {FromIhtMethod.class})
    @Mapping(target = "ihtNetValue", source = "iht.netValue", qualifiedBy = {ToPennies.class})
    @Mapping(target = "ihtGrossValue", source = "iht.grossValue", qualifiedBy = {ToPennies.class})

    @Mapping(target = "extraCopiesOfGrant", expression = "java(form.getCopies().getUk() != null "
        + "? form.getCopies().getUk() : 0L)")
    @Mapping(target = "outsideUkGrantCopies", expression = "java(form.getAssets().getAssetsoverseas() ? "
        + "form.getCopies().getOverseas() : 0L)")
    GrantOfRepresentationData toCaseData(PaForm form);

}
