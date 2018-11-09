package uk.gov.hmcts.probate.core.service.mapper;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.probate.domain.FormData;
import uk.gov.hmcts.probate.dto.ccd.data.AddressDTO;
import uk.gov.hmcts.probate.dto.ccd.data.DataDTO;

import java.util.function.Function;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CaseDataMapper {

    private static final Function<Boolean, String> YES_NO = aBoolean -> aBoolean ? "Yes" : "No";

    private static final String PERSONAL_APPLICATION_TYPE = "Personal";
    private static final String ENG_WALES_DOMICILE = "live (domicile) permanently in England or Wales";
    private static final String IHT_FORM_VALUE_205 = "IHT205";
    private static final String IHT_ONLINE_FORM_PAYMENT = "online";

    @Mappings({
            @Mapping(target = "primaryApplicantForenames", source = "applicant.firstName"),
            @Mapping(target = "primaryApplicantSurname", source = "applicant.lastName"),
            @Mapping(target = "applicationType", constant = PERSONAL_APPLICATION_TYPE),
            @Mapping(target = "deceasedSurname", source = "deceased.lastName"),
            @Mapping(target = "deceasedForenames", source = "deceased.firstName"),
            @Mapping(target = "deceasedDateOfBirth", source = "deceased.dateOfBirth", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "deceasedDateOfDeath", source = "deceased.dateOfDeath", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "declaration.confirm", source = "applicant.declaration.declaration.confirm"),
            @Mapping(target = "declaration.confirmItem1", source = "applicant.declaration.declaration.confirmItem1"),
            @Mapping(target = "declaration.confirmItem2", source = "applicant.declaration.declaration.confirmItem2"),
            @Mapping(target = "declaration.confirmItem3", source = "applicant.declaration.declaration.confirmItem3"),
            @Mapping(target = "declaration.requests", source = "applicant.declaration.declaration.requests"),
            @Mapping(target = "declaration.requestsItem1", source = "applicant.declaration.declaration.requestsItem1"),
            @Mapping(target = "declaration.requestsItem2", source = "applicant.declaration.declaration.requestsItem2"),
            @Mapping(target = "declaration.understand", source = "applicant.declaration.declaration.understand"),
            @Mapping(target = "declaration.understandItem1", source = "applicant.declaration.declaration.understandItem1"),
            @Mapping(target = "declaration.understandItem2", source = "applicant.declaration.declaration.understandItem2"),
            @Mapping(target = "declaration.accept", source = "applicant.declaration.declaration.accept")

    })
    public abstract DataDTO mapFormDataToDataDTO(FormData formData);

    @AfterMapping
    public void mapDeceasedDomicileInEngWales(FormData formData, @MappingTarget DataDTO dataDTO) {
        String domicile = YES_NO.apply(ENG_WALES_DOMICILE.equalsIgnoreCase(formData.getDeceased().getDomicile()));
        dataDTO.setDeceasedDomicileInEngWales(domicile);
    }

    @AfterMapping
    public void mapIhtValues(FormData formData, @MappingTarget DataDTO dataDTO) {
        boolean ihtCompletedOnline = IHT_ONLINE_FORM_PAYMENT.equalsIgnoreCase(formData.getIht().getForm());
        String formId = StringUtils.defaultIfBlank(formData.getIht().getFormId(), "");
        dataDTO.setIhtFormId(ihtCompletedOnline ? IHT_FORM_VALUE_205 : formId);
        dataDTO.setIhtFormCompletedOnline(YES_NO.apply(ihtCompletedOnline));
    }

    @AfterMapping
    public void mapSoftStop(FormData formData, @MappingTarget DataDTO dataDTO) {
        dataDTO.setSoftStop(YES_NO.apply("True".equalsIgnoreCase(formData.getSoftStop())));
    }

    @AfterMapping
    public void mapAddresses(FormData formData, @MappingTarget DataDTO dataDTO) {
        dataDTO.setPrimaryApplicantAddress(createAddress(formData.getApplicant().getAddress()));
        dataDTO.setDeceasedAddress(createAddress(formData.getDeceased().getAddress()));
    }

    private AddressDTO createAddress(String address) {
        return AddressDTO.builder()
                .addressLine1(address)
                .build();
    }
}
