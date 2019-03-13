package uk.gov.hmcts.probate.core.service.mapper;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromCaveatCollectionMember;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToCaveatCollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.FullAliasName;
import uk.gov.hmcts.reform.probate.model.forms.AliasOtherNames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CaveatAliasNameMapper {

    @ToCaveatCollectionMember
    public List<CollectionMember<FullAliasName>> toCaveatCollectionMember(Map<String, AliasOtherNames> otherNames) {
        if (CollectionUtils.isEmpty(otherNames)) {
            return null;//NOSONAR
        }

        List<CollectionMember<FullAliasName>> result = new ArrayList<>();
        otherNames.forEach((k, v) -> {
            FullAliasName fan = new FullAliasName(v.getFirstName() + " ".concat(v.getLastName()));
            CollectionMember<FullAliasName> collectionMember = new CollectionMember<>(k, fan);
            result.add(collectionMember);
        });

        return result;
    }

    @FromCaveatCollectionMember
    public Map<String, AliasOtherNames> fromCaveatCollectionMember(List<CollectionMember<FullAliasName>> collectionMembers) {
        if (CollectionUtils.isEmpty(collectionMembers)) {
            return null;//NOSONAR
        }
        Map<String, AliasOtherNames> result = new HashMap<>();
        collectionMembers.forEach(collectionMember -> {
             result.put(collectionMember.getId(), createAliasOtherName(collectionMember.getValue()));
        });

        return result;
    }

    private AliasOtherNames createAliasOtherName(FullAliasName fullAliasName) {
        if (fullAliasName == null) {
            return null;//NOSONAR
        }
        int separation = fullAliasName.getFullAliasName().lastIndexOf(' ' );
        String value = fullAliasName.getFullAliasName();
        String firstName = value.substring(0, separation);
        String lastName = value.substring(separation + 1, value.length());
        return AliasOtherNames.builder().firstName(firstName).lastName(lastName).build();
    }
}
