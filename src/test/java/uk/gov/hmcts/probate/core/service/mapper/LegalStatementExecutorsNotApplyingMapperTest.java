package uk.gov.hmcts.probate.core.service.mapper;

import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.LegalStatementExecutorNotApplying;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LegalStatementExecutorsNotApplyingMapperTest {

    private LegalStatementExecutorsNotApplyingMapper
            legalStatementExecutorsNotApplyingMapper = new LegalStatementExecutorsNotApplyingMapper();

    @Test
    void shouldToCollectionMember() {
        List<String> legalStatementExecutorNotApplyingList = new ArrayList<>();
        legalStatementExecutorNotApplyingList.add("Test1");
        legalStatementExecutorNotApplyingList.add("Test2");
        List<CollectionMember<LegalStatementExecutorNotApplying>> list =
                legalStatementExecutorsNotApplyingMapper.toCollectionMember(legalStatementExecutorNotApplyingList);
        assertThat(list.size(), is(2));
    }

    @Test
    void shouldHandleNullValueToCollectionMember() {
        List<CollectionMember<LegalStatementExecutorNotApplying>> list =
                legalStatementExecutorsNotApplyingMapper.toCollectionMember(null);
        Assert.isNull(list);
    }

    @Test
    void shouldFromCollectionMember() {
        LegalStatementExecutorNotApplying executorObj = new LegalStatementExecutorNotApplying();
        executorObj.setExecutor("Test1");
        CollectionMember<LegalStatementExecutorNotApplying> collectionMember = new CollectionMember<>();
        collectionMember.setValue(executorObj);
        List<CollectionMember<LegalStatementExecutorNotApplying>> collectionMembers = new ArrayList<>();
        collectionMembers.add(collectionMember);
        List<String> list =
                legalStatementExecutorsNotApplyingMapper.fromCollectionMember(collectionMembers);
        assertThat(list.size(), is(1));
    }

    @Test
    void shouldHandleNullValueFromCollectionMember() {
        List<String> list =
                legalStatementExecutorsNotApplyingMapper.fromCollectionMember(null);
        Assert.isNull(list);
    }
}
