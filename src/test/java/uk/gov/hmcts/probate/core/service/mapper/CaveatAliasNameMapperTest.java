package uk.gov.hmcts.probate.core.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.FullAliasName;
import uk.gov.hmcts.reform.probate.model.forms.AliasOtherNames;

import java.util.Collections;
import java.util.List;
import java.util.Map;

class CaveatAliasNameMapperTest {

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Boyega";
    private static final String NAME_0 = "name_0";
    private CaveatAliasNameMapper mapper = new CaveatAliasNameMapper();

    @Test
    void toAandFromCollectionMember() {

        Map<String, AliasOtherNames> aliasOtherNamesMap = Collections.singletonMap(NAME_0, new AliasOtherNames(FIRST_NAME, LAST_NAME));

        List<CollectionMember<FullAliasName>> collectionMembers = mapper.toCaveatCollectionMember(aliasOtherNamesMap);
        Assertions.assertThat(collectionMembers).hasSize(1);
        CollectionMember<FullAliasName> fullAliasNameCollectionMember = collectionMembers.get(0);
        Assertions.assertThat(fullAliasNameCollectionMember.getValue().getFullAliasName()).isEqualTo(FIRST_NAME + ' ' + LAST_NAME);
        Assertions.assertThat(fullAliasNameCollectionMember.getId()).isEqualTo(NAME_0);


        Map<String, AliasOtherNames> resultAliasOtherNamesMap = mapper.fromCaveatCollectionMember(collectionMembers);
        Assertions.assertThat(resultAliasOtherNamesMap).hasSize(1);
        Assertions.assertThat(resultAliasOtherNamesMap.get(NAME_0).getFirstName()).isEqualTo(FIRST_NAME);
        Assertions.assertThat(resultAliasOtherNamesMap.get(NAME_0).getLastName()).isEqualTo(LAST_NAME);

    }

    @Test
    void toNullCollectionMember() {
        List<CollectionMember<FullAliasName>> result = mapper.toCaveatCollectionMember(null);
        Assertions.assertThat(result).isNull();
    }

    @Test
    void fromNullCollectionMember() {
        Map<String, AliasOtherNames> result = mapper.fromCaveatCollectionMember(null);
        Assertions.assertThat(result).isNull();
    }
}
