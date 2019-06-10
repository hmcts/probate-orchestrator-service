package uk.gov.hmcts.probate.core.service.mapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorNotApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.forms.Address;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExecutorsMapperTest {

    public static final String ADDRESS_LINE_1 = "addressLine1";
    public static final String POSTCODE = "postcode";
    @Autowired
    private ExecutorsMapper mapper;

    private GrantOfRepresentationData grantOfRepresentation;
    private List<Executor> executorList = new ArrayList<>();

    @Before
    public void setUptest() {

        grantOfRepresentation = PaTestDataCreator.createGrantOfRepresentation();
        executorList.add(Executor.builder()
                .isApplying(Boolean.TRUE)
                .firstName("Bobby")
                .lastName("Smith")
                .address(Address.builder()
                        .addressLine1(ADDRESS_LINE_1)
                        .postCode(POSTCODE)
                        .build())
                .inviteId("12345")
                .build());
        executorList.add(Executor.builder().fullName("Jackie Smith").build());
    }

    @Test
    public void shouldMapExecutors() {
        List<CollectionMember<ExecutorNotApplying>> collectionMembersNonApplying = mapper.toExecutorNotApplyingCollectionMember(executorList);
        Assert.assertThat(collectionMembersNonApplying.size(), equalTo(1));
        Assert.assertThat(collectionMembersNonApplying.get(0).getValue().getNotApplyingExecutorName(), equalTo("Jackie Smith"));


        List<CollectionMember<ExecutorApplying>> collectionMembers = mapper.toExecutorApplyingCollectionMember(executorList);
        Assert.assertThat(collectionMembers.size(), equalTo(1));
        ExecutorApplying executorApplying = collectionMembers.get(0).getValue();
        Assert.assertThat(executorApplying.getApplyingExecutorName(), equalTo("Bobby Smith"));
        Assert.assertThat(executorApplying.getApplyingExecutorInvitationId(), equalTo("12345"));
        Assert.assertThat(executorApplying.getApplyingExecutorAddress().getAddressLine1(), equalTo(ADDRESS_LINE_1));
        Assert.assertThat(executorApplying.getApplyingExecutorAddress().getPostCode(), equalTo(POSTCODE));
    }

    @Test
    public void shouldMapFromCase() {
        List<Executor> executors = mapper.fromCollectionMember(grantOfRepresentation);
        Assert.assertThat(executors.size(), equalTo(4));
        Assert.assertThat(executors.get(0).getAddress().getFormattedAddress(), equalTo("Address"));

    }

}
