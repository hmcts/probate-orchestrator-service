package uk.gov.hmcts.probate.core.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.probate.model.cases.AliasName;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.forms.AliasOtherNames;

import java.util.Collections;
import java.util.List;
import java.util.Map;

class AliasNameMapperTest {

    private static final String ALIAS_NAME = "aliasName";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Boyega";
    private static final String NAME_0 = "name_0";
    private AliasNameMapper mapper = new AliasNameMapper();

    @Test
    void toAandFromCollectionMember() {

        Map<String, AliasOtherNames> aliasOtherNamesMap =
            Collections.singletonMap(ALIAS_NAME, new AliasOtherNames(FIRST_NAME, LAST_NAME));
        List<CollectionMember<AliasName>> collectionMembers = mapper.toCollectionMember(aliasOtherNamesMap);
        Assertions.assertThat(collectionMembers).hasSize(1);
        CollectionMember<AliasName> aliasNameCollectionMember = collectionMembers.get(0);
        Assertions.assertThat(aliasNameCollectionMember.getValue().getForenames()).isEqualTo(FIRST_NAME);
        Assertions.assertThat(aliasNameCollectionMember.getValue().getLastName()).isEqualTo(LAST_NAME);

        Map<String, AliasOtherNames> resultAliasOtherNamesMap = mapper.fromCollectionMember(collectionMembers);
        Assertions.assertThat(resultAliasOtherNamesMap).hasSize(1);
        Assertions.assertThat(resultAliasOtherNamesMap.get(NAME_0).getFirstName()).isEqualTo(FIRST_NAME);
        Assertions.assertThat(resultAliasOtherNamesMap.get(NAME_0).getLastName()).isEqualTo(LAST_NAME);

    }

    @Test
    void toNullCollectionMember() {
        List<CollectionMember<AliasName>> result = mapper.toCollectionMember(null);
        Assertions.assertThat(result).isNull();
    }

    @Test
    void fromNullCollectionMember() {
        Map<String, AliasOtherNames> result = mapper.fromCollectionMember(null);
        Assertions.assertThat(result).isNull();
    }
}