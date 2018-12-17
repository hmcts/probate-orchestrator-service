package uk.gov.hmcts.probate.core.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import uk.gov.hmcts.reform.probate.model.cases.AliasName;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.forms.AliasOtherNames;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public class AliasNameMapper {

    public List<CollectionMember<AliasName>> toCollectionMember(Map<String, AliasOtherNames> otherNames) {
        return otherNames.values()
            .stream()
            .map(aliasOtherNames -> AliasName.builder().forenames(aliasOtherNames.getFirstName())
                .lastName(aliasOtherNames.getLastName()).build())
            .map(aliasName -> CollectionMember.<AliasName>builder().value(aliasName).build())
            .collect(Collectors.toList());
    }


    public Map<String, AliasOtherNames> fromCollectionMember(List<CollectionMember<AliasName>> collectionMembers) {
        List<AliasOtherNames> aliasOtherNamesList = collectionMembers
            .stream()
            .map(aliasNameCollectionMember -> aliasNameCollectionMember.getValue())
            .map(aliasName -> AliasOtherNames.builder().firstName(aliasName.getForenames())
                .lastName(aliasName.getLastName()).build())
            .collect(Collectors.toList());
        Map<String, AliasOtherNames> aliasOtherNamesMap = new HashMap<>();
        int count = 0;
        for (AliasOtherNames aliasOtherNames : aliasOtherNamesList) {
            aliasOtherNamesMap.put("name_" + count, aliasOtherNames);
            count++;
        }
        return aliasOtherNamesMap;
    }
}
