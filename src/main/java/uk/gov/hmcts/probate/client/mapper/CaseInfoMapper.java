package uk.gov.hmcts.probate.client.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import uk.gov.hmcts.probate.dto.CaseInfoDTO;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;

@Mapper(componentModel = "spring")
public interface CaseInfoMapper {

    @Mappings({
            @Mapping(target = "caseId", source = "id"),
            @Mapping(target = "state", source = "state")
    })
    CaseInfoDTO mapFormCaseInfoDTO(CaseDetails caseDetails);
}
