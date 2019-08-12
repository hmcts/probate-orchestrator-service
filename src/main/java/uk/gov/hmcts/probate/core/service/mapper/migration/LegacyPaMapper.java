package uk.gov.hmcts.probate.core.service.mapper.migration;

import org.apache.commons.lang3.BooleanUtils;
import org.mapstruct.Mapper;
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


//    @Mapping(target = "applicationType", expression = "java(ApplicationType.PERSONAL)")//NOSONAR
//    @Mapping(target = "grantType", expression = "java(GrantType.GRANT_OF_PROBATE)")//NOSONAR
//    @Mapping(target = "applicationSubmittedDate", expression = "java(LocalDate.now())")//NOSONAR
//    @Mapping(target = "willHasCodicils", source = "will.codicils")//NOSONAR
//    @Mapping(target = "willNumberOfCodicils", source = "will.codicilsNumber")//NOSONAR
//    @Mapping(target = "deceasedSurname", source = "deceased.lastName")//NOSONAR
//    @Mapping(target = "deceasedForenames", source = "deceased.firstName")//NOSONAR
//    @Mapping(target = "deceasedAnyOtherNames", source = "deceased.alias")//NOSONAR
//    @Mapping(target = "deceasedAliasNameList", source = "deceased.otherNames", qualifiedBy = {ToCollectionMember.class})//NOSONAR
//    @Mapping(target = "deceasedMarriedAfterWillOrCodicilDate", source = "deceased.married")//NOSONAR
//    @Mapping(target = "deceasedAddress", source = "deceased.address", qualifiedBy = {ToCaseAddress.class})//NOSONAR
//    @Mapping(target = "deceasedDateOfBirth", source = "deceased.dateOfBirth", qualifiedBy = {ToLocalDate.class})//NOSONAR
//    @Mapping(target = "deceasedDateOfDeath", source = "deceased.dateOfDeath", qualifiedBy = {ToLocalDate.class})//NOSONAR
//    @Mapping(target = "deceasedAddressFound", source = "deceased.addressFound")             //NOSONAR
//    @Mapping(target = "deceasedAddresses", source = "deceased.addresses", qualifiedBy = {FromMap.class})//NOSONAR
//    @Mapping(target = "deceasedPostCode", source = "deceased.postCode")//NOSONAR
//    @Mapping(target = "primaryApplicantForenames", source = "applicant.firstName")//NOSONAR
//    @Mapping(target = "primaryApplicantSurname", source = "applicant.lastName")
//    @Mapping(target = "primaryApplicantEmailAddress", expression = "java(form.getApplicantEmail()!=null ? form.getApplicantEmail().toLowerCase() : null)")//NOSONAR
//    @Mapping(target = "primaryApplicantAlias", source = "applicant.alias") //NOSONAR
//    @Mapping(target = "primaryApplicantAliasReason", source = "applicant.aliasReason")//NOSONAR
//    @Mapping(target = "primaryApplicantAddress", source = "applicant.address", qualifiedBy = {ToCaseAddress.class})//NOSONAR
//    @Mapping(target = "primaryApplicantPostCode", source = "applicant.postCode")//NOSONAR
//    @Mapping(target = "primaryApplicantAddressFound", source = "applicant.addressFound")
//    @Mapping(target = "primaryApplicantAddresses", source = "applicant.addresses", qualifiedBy = {FromMap.class})//NOSONAR
//    @Mapping(target = "primaryApplicantSameWillName", source = "applicant.nameAsOnTheWill")//NOSONAR
//    @Mapping(target = "primaryApplicantPhoneNumber", source = "applicant.phoneNumber")//NOSONAR
//    @Mapping(target = "primaryApplicantOtherReason", source = "applicant.otherReason")//NOSONAR
//    @Mapping(target = "registryLocation", source = "registry.name", qualifiedBy = {ToRegistryLocation.class})//NOSONAR
//    @Mapping(target = "softStop", source = "declaration.softStop")//NOSONAR
//    @Mapping(target = "legalStatement", source = "declaration.legalStatement")//NOSONAR
//    @Mapping(target = "declaration", source = "declaration.declaration")//NOSONAR
//    @Mapping(target = "declarationCheckbox", source = "declaration.declarationCheckbox") //NOSONAR
//    @Mapping(target = "executorsApplying", source = "executors.list", qualifiedBy = {ToExecutorApplyingCollectionMember.class})//NOSONAR
//    @Mapping(target = "executorsNotApplying", source = "executors.list", qualifiedBy = {ToExecutorNotApplyingCollectionMember.class})//NOSONAR
//    @Mapping(target = "numberOfApplicants", expression = "java(form.getExecutors() == null || form.getExecutors().getList() == null ? 0L : Long.valueOf(form.getExecutors().getList().size()))")//NOSONAR
//    @Mapping(target = "numberOfExecutors", source = "executors.executorsNumber")//NOSONAR
//    @Mapping(target = "executorsAllAlive", source = "executors.allalive")//NOSONAR
//    @Mapping(target = "otherExecutorsApplying", source = "executors.otherExecutorsApplying")//NOSONAR
//    @Mapping(target = "executorsHaveAlias", source = "executors.alias")//NOSONAR
//    @Mapping(target = "ihtReferenceNumber", expression = "java(form.getIht() != null && form.getIht().getMethod() == IhtMethod.ONLINE ? "
//            + "form.getIht().getIdentifier() : \"Not applicable\")")//NOSONAR
//    @Mapping(target = "ihtFormId", source = "iht.ihtFormId")//NOSONAR
//    @Mapping(target = "ihtFormCompletedOnline", source = "iht.method", qualifiedBy = {FromIhtMethod.class})//NOSONAR
//    @Mapping(target = "ihtNetValue", source = "iht.netValue", qualifiedBy = {ToPennies.class})//NOSONAR
//    @Mapping(target = "ihtGrossValue", source = "iht.grossValue", qualifiedBy = {ToPennies.class}) //NOSONAR
//    @Mapping(target = "ihtGrossValueField", source = "iht.grossValueField")//NOSONAR
//    @Mapping(target = "ihtNetValueField", source = "iht.netValueField")//NOSONAR
//
//    @Mapping(target = "extraCopiesOfGrant", source = "copies.uk")//NOSONAR
//    @Mapping(target = "outsideUkGrantCopies", source = "copies.overseas")//NOSONAR
//    @Mapping(target = "legalDeclarationJson", source = "legalDeclaration", qualifiedBy = {FromMap.class})//NOSONAR
//    @Mapping(target = "checkAnswersSummaryJson", source = "checkAnswersSummary", qualifiedBy = {FromMap.class})//NOSONAR
//    @Mapping(target = "payments", source = "payment")//NOSONAR
    GrantOfRepresentationData toCaseData(LegacyForm form);
}
