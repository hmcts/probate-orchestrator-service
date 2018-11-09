package uk.gov.hmcts.probate.controller.mapper;

import org.mapstruct.Mapper;
import uk.gov.hmcts.probate.domain.FormData;
import uk.gov.hmcts.probate.dto.formdata.FormDataDTO;

@Mapper(componentModel = "spring")
public interface FormDataMapper {

    FormData mapFormDataDTO(FormDataDTO formDataDTO);
}
