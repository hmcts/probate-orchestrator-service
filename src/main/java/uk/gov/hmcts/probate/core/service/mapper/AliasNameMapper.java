package uk.gov.hmcts.probate.core.service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import uk.gov.hmcts.reform.probate.model.cases.AliasName;
import uk.gov.hmcts.reform.probate.model.forms.AliasOtherNames;

@Mapper
public interface AliasNameMapper {

    @Mappings({
            @Mapping(target = "forenames", source = "firstName"),
    })
    AliasName map(AliasOtherNames aliasOtherName);
}
