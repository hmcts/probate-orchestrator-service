package uk.gov.hmcts.probate.core.service.mapper.migration;

import org.apache.commons.lang3.BooleanUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.probate.core.service.mapper.AddressMapper;
import uk.gov.hmcts.probate.core.service.mapper.AliasNameMapper;
import uk.gov.hmcts.probate.core.service.mapper.ExecutorApplyingMapper;
import uk.gov.hmcts.probate.core.service.mapper.ExecutorNotApplyingMapper;
import uk.gov.hmcts.probate.core.service.mapper.ExecutorsMapper;
import uk.gov.hmcts.probate.core.service.mapper.FeesMapper;
import uk.gov.hmcts.probate.core.service.mapper.IhtMethodConverter;
import uk.gov.hmcts.probate.core.service.mapper.LegalStatementMapper;
import uk.gov.hmcts.probate.core.service.mapper.LocalDateTimeMapper;
import uk.gov.hmcts.probate.core.service.mapper.MapConverter;
import uk.gov.hmcts.probate.core.service.mapper.PaPaymentMapper;
import uk.gov.hmcts.probate.core.service.mapper.PaymentsMapper;
import uk.gov.hmcts.probate.core.service.mapper.PoundsConverter;
import uk.gov.hmcts.probate.core.service.mapper.RegistryLocationMapper;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromIhtMethod;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromMap;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCaseAddress;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToExecutorApplyingCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToExecutorNotApplyingCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToLocalDate;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToPennies;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToRegistryLocation;
import uk.gov.hmcts.probate.model.persistence.LegacyForm;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantType;
import uk.gov.hmcts.reform.probate.model.forms.IhtMethod;

import java.time.LocalDate;

@Mapper(componentModel = "spring", uses = {PaPaymentMapper.class, PaymentsMapper.class, AliasNameMapper.class, RegistryLocationMapper.class,
    PoundsConverter.class, IhtMethodConverter.class, LegalStatementMapper.class, ExecutorsMapper.class,
    ExecutorApplyingMapper.class, ExecutorNotApplyingMapper.class, MapConverter.class, LocalDateTimeMapper.class,
    AddressMapper.class, FeesMapper.class},
    imports = {ApplicationType.class, GrantType.class, ProbateType.class, IhtMethod.class,
        LocalDate.class, ExecutorsMapper.class, BooleanUtils.class, AddressMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface LegacyPaMapper extends LegacyFormMapper{


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
    @Mapping(target = "deceasedPostCode", source = "deceased.postCode")
    @Mapping(target = "primaryApplicantForenames", source = "applicant.firstName")
    @Mapping(target = "primaryApplicantSurname", source = "applicant.lastName")
    @Mapping(target = "primaryApplicantEmailAddress", expression = "java(form.getApplicantEmail()!=null ? form.getApplicantEmail().toLowerCase() : null)")
    @Mapping(target = "primaryApplicantAlias", source = "applicant.alias")
    @Mapping(target = "primaryApplicantAliasReason", source = "applicant.aliasReason")
    @Mapping(target = "primaryApplicantAddress", source = "applicant.address", qualifiedBy = {ToCaseAddress.class})
    @Mapping(target = "primaryApplicantPostCode", source = "applicant.postCode")
    @Mapping(target = "primaryApplicantAddressFound", source = "applicant.addressFound")
    @Mapping(target = "primaryApplicantAddresses", source = "applicant.addresses", qualifiedBy = {FromMap.class})
    @Mapping(target = "primaryApplicantSameWillName", source = "applicant.nameAsOnTheWill")
    @Mapping(target = "primaryApplicantPhoneNumber", source = "applicant.phoneNumber")
    @Mapping(target = "primaryApplicantOtherReason", source = "applicant.otherReason")
    @Mapping(target = "registryLocation", source = "registry.name", qualifiedBy = {ToRegistryLocation.class})
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
    @Mapping(target = "ihtGrossValueField", source = "iht.grossValueField")
    @Mapping(target = "ihtNetValueField", source = "iht.netValueField")

    @Mapping(target = "extraCopiesOfGrant", source = "copies.uk")
    @Mapping(target = "outsideUkGrantCopies", source = "copies.overseas")
    @Mapping(target = "legalDeclarationJson", source = "legalDeclaration", qualifiedBy = {FromMap.class})
    @Mapping(target = "checkAnswersSummaryJson", source = "checkAnswersSummary", qualifiedBy = {FromMap.class})
    @Mapping(target = "payments", source = "payment")
    GrantOfRepresentationData toCaseData(LegacyForm form);
}
