package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.reform.probate.model.cases.Address;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentation;
import uk.gov.hmcts.reform.probate.model.forms.Form;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class IntestacyMapper implements FormMapper<GrantOfRepresentation, IntestacyForm> {

    @Mappings( {
        @Mapping(target = "primaryApplicantForenames", source = "applicant.firstName"),
        @Mapping(target = "primaryApplicantSurname", source = "applicant.lastName"),
//        @Mapping(target = "applicationType", constant = ProbateType.INTESTACY),
        @Mapping(target = "type", constant = CaseType.Constants.GRANT_OF_REPRESENTATION_NAME),
        @Mapping(target = "deceasedSurname", source = "deceased.lastName"),
        @Mapping(target = "deceasedForenames", source = "deceased.firstName"),
        @Mapping(target = "deceasedDateOfBirth", source = "deceased.dateOfBirth"),
        @Mapping(target = "deceasedDateOfDeath", source = "deceased.dateOfDeath")
    })
    public abstract GrantOfRepresentation map(IntestacyForm form);

    @AfterMapping
    public void mapAddresses(Form form, @MappingTarget GrantOfRepresentation grantOfRepresentation) {
        grantOfRepresentation.setPrimaryApplicantAddress(createAddress(form.getApplicant().getAddress()));
        grantOfRepresentation.setDeceasedAddress(createAddress(form.getDeceased().getAddress()));
    }

    private Address createAddress(String addressStr) {
        Address address = new Address();
        address.setAddressLine1(addressStr);
        return address;
    }
}

