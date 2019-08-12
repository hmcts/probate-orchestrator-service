package uk.gov.hmcts.probate.core.service.mapper.migration;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.probate.core.service.mapper.AliasNameMapper;
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

@Mapper(componentModel = "spring", uses = {PaPaymentMapper.class, PaymentsMapper.class, AliasNameMapper.class, RegistryLocationMapper.class, PoundsConverter.class,
        IhtMethodConverter.class, MapConverter.class, LegalStatementMapper.class, LocalDateTimeMapper.class},
        imports = {ApplicationType.class, GrantType.class, ProbateType.class, IhtMethod.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface LegacyIntestacyMapper extends LegacyFormMapper {

//    @Mapping(target = "applicationType", expression = "java(ApplicationType.PERSONAL)")//NOSONAR
//    @Mapping(target = "grantType", expression = "java(GrantType.INTESTACY)")//NOSONAR
//    @Mapping(target = "primaryApplicantForenames", source = "applicant.firstName")//NOSONAR
//    @Mapping(target = "primaryApplicantSurname", source = "applicant.lastName")//NOSONAR
//    @Mapping(target = "primaryApplicantRelationshipToDeceased", source = "applicant.relationshipToDeceased")//NOSONAR
//    @Mapping(target = "primaryApplicantAdoptionInEnglandOrWales", source = "applicant.adoptionInEnglandOrWales")//NOSONAR
//    @Mapping(target = "primaryApplicantPhoneNumber", source = "applicant.phoneNumber")//NOSONAR
//    @Mapping(target = "primaryApplicantEmailAddress",
//            expression = "java(form.getApplicantEmail() != null ? form.getApplicantEmail().toLowerCase() : null)")//NOSONAR
//    @Mapping(target = "primaryApplicantAddress", source = "applicant.address", qualifiedBy = {ToCaseAddress.class})//NOSONAR
//    @Mapping(target = "primaryApplicantAddresses", source = "applicant.addresses", qualifiedBy = {FromMap.class})//NOSONAR
//    @Mapping(target = "primaryApplicantPostCode", source = "applicant.postCode") //NOSONAR
//    @Mapping(target = "deceasedAddress", source = "deceased.address", qualifiedBy = {ToCaseAddress.class})//NOSONAR
//    @Mapping(target = "deceasedPostCode", source = "deceased.postCode")//NOSONAR
//    @Mapping(target = "deceasedAddresses", source = "deceased.addresses", qualifiedBy = {FromMap.class})//NOSONAR
//    @Mapping(target = "deceasedSurname", source = "deceased.lastName")//NOSONAR
//    @Mapping(target = "deceasedForenames", source = "deceased.firstName")//NOSONAR
//    @Mapping(target = "deceasedDateOfBirth", source = "deceased.dateOfBirth", qualifiedBy = {ToLocalDate.class})//NOSONAR
//    @Mapping(target = "deceasedDateOfDeath", source = "deceased.dateOfDeath", qualifiedBy = {ToLocalDate.class})//NOSONAR
//    @Mapping(target = "deceasedAnyOtherNames", source = "deceased.alias")//NOSONAR
//    @Mapping(target = "deceasedMartialStatus", source = "deceased.maritalStatus")//NOSONAR
//    @Mapping(target = "deceasedDivorcedInEnglandOrWales", source = "deceased.divorcedInEnglandOrWales")//NOSONAR
//    @Mapping(target = "deceasedOtherChildren", source = "deceased.otherChildren")//NOSONAR
//    @Mapping(target = "declarationCheckbox", source = "declaration.declarationCheckbox")//NOSONAR
//    @Mapping(target = "childrenOverEighteenSurvived", source = "deceased.allDeceasedChildrenOverEighteen")//NOSONAR
//    @Mapping(target = "childrenDied", source = "deceased.anyDeceasedChildrenDieBeforeDeceased")//NOSONAR
//    @Mapping(target = "grandChildrenSurvivedUnderEighteen",
//            source = "deceased.anyDeceasedGrandchildrenUnderEighteen") //NOSONAR
//    @Mapping(target = "deceasedSpouseNotApplyingReason", source = "deceased.spouseNotApplyingReason")//NOSONAR
//    @Mapping(target = "deceasedAnyChildren", source = "deceased.anyChildren")//NOSONAR
//    @Mapping(target = "deceasedAliasNameList", source = "deceased.otherNames",
//            qualifiedBy = {ToCollectionMember.class})//NOSONAR
//    @Mapping(target = "outsideUkGrantCopies", source = "copies.overseas")//NOSONAR
//    @Mapping(target = "extraCopiesOfGrant", source = "copies.uk")//NOSONAR
//    @Mapping(target = "deceasedHasAssetsOutsideUK", source = "iht.assetsOutside")//NOSONAR
//    @Mapping(target = "ihtReferenceNumber", source = "iht.identifier")//NOSONAR
//    @Mapping(target = "ihtFormId", source = "iht.ihtFormId")//NOSONAR
//    @Mapping(target = "assetsOutside", source = "iht.assetsOutside") //NOSONAR
//    @Mapping(target = "ihtFormCompletedOnline", source = "iht.method", qualifiedBy = {FromIhtMethod.class}) //NOSONAR
//    @Mapping(target = "ihtNetValue", source = "iht.netValue", qualifiedBy = {ToPennies.class})//NOSONAR
//    @Mapping(target = "ihtGrossValue", source = "iht.grossValue", qualifiedBy = {ToPennies.class}) //NOSONAR
//    @Mapping(target = "registryLocation", source = "registry.name", qualifiedBy = {ToRegistryLocation.class})//NOSONAR
//    @Mapping(target = "assetsOutsideNetValue", source = "iht.assetsOutsideNetValue",
//            qualifiedBy = {ToPennies.class})//NOSONAR
//    @Mapping(target = "legalDeclarationJson", source = "legalDeclaration", qualifiedBy = {FromMap.class})//NOSONAR
//    @Mapping(target = "checkAnswersSummaryJson", source = "checkAnswersSummary", qualifiedBy = {FromMap.class})//NOSONAR
//    @Mapping(target = "payments", source = "payment")//NOSONAR
    GrantOfRepresentationData toCaseData(LegacyForm form);
}
