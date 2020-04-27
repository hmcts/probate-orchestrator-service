package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromCaveatCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromRegistryLocation;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCaveatCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToRegistryLocation;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.ApplicationType;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData;
import uk.gov.hmcts.reform.probate.model.forms.caveat.CaveatForm;


@Mapper(componentModel = "spring", uses = {PaymentsMapper.class, CaveatAliasNameMapper.class, RegistryLocationMapper.class, PoundsConverter.class},
        imports = {ApplicationType.class, ProbateType.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CaveatMapper extends FormMapper<CaveatData, CaveatForm> {


    @Mapping(target = "applicationId", source = "applicationId")
    @Mapping(target = "applicationType", expression = "java(ApplicationType.PERSONAL)")

    @Mapping(target = "caveatorForenames", source = "applicant.firstName")
    @Mapping(target = "caveatorSurname", source = "applicant.lastName")
    @Mapping(target = "caveatorEmailAddress",
            expression = "java(form.getApplicant() != null && form.getApplicant().getEmail() != null ? " +
                    "form.getApplicant().getEmail().toLowerCase() : null)")
    @Mapping(target = "caveatorAddress", source = "applicant.address")

    @Mapping(target = "deceasedAddress", source = "deceased.address")
    @Mapping(target = "deceasedSurname", source = "deceased.lastName")
    @Mapping(target = "deceasedForenames", source = "deceased.firstName")
    @Mapping(target = "deceasedDateOfBirth", source = "deceased.dateOfBirth")
    @Mapping(target = "deceasedDateOfDeath", source = "deceased.dateOfDeath")
    @Mapping(target = "deceasedFullAliasNameList", source = "deceased.otherNames",
            qualifiedBy = {ToCaveatCollectionMember.class})

    @Mapping(target = "registryLocation", source ="registry.name", qualifiedBy = {ToRegistryLocation.class})
    @Mapping(target = "payments", source = "payments", qualifiedBy = {ToCollectionMember.class})
    @Mapping(target = "expiryDate", source = "expiryDate")
    @Mapping(target = "caveatRaisedEmailNotificationRequested", expression = "java(Boolean.TRUE)")
    @Mapping(target = "paperForm", expression = "java(Boolean.FALSE)")
    @Mapping(target = "languagePreferenceWelsh", source = "language.bilingual")
    @Mapping(target = "pcqId", source = "equality.pcqId")
    CaveatData toCaseData(CaveatForm form);

    @Mapping(target = "type", expression = "java(ProbateType.CAVEAT)")
    @Mapping(target = "applicant.email", source = "caveatorEmailAddress")
    @Mapping(target = "deceased.otherNames", source = "deceasedFullAliasNameList", qualifiedBy = {FromCaveatCollectionMember.class})
    @Mapping(target = "registry.name", source ="registryLocation", qualifiedBy = {FromRegistryLocation.class})
    @InheritInverseConfiguration
    CaveatForm fromCaseData(CaveatData caveatdata);
}
