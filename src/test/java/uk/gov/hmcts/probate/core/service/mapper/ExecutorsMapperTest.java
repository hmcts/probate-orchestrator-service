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

        grantOfRepresentation = PaMultipleExecutorTestDataCreator.createGrantOfRepresentation();
        executorList.add(Executor.builder()
                .isApplying(Boolean.TRUE)
                .isApplicant(Boolean.FALSE)
                .hasOtherName(Boolean.TRUE)
                .firstName("Bobby")
                .lastName("Smith")
                .address(Address.builder()
                        .addressLine1(ADDRESS_LINE_1)
                        .postCode(POSTCODE)
                        .build())
                .inviteId("12345")
                .build());
        executorList.add(Executor.builder()
                .fullName("Jackie Smith")
                .isDead(Boolean.TRUE)
                .diedBefore(Boolean.FALSE)
                .build());
    }

    @Test
    public void shouldMapNonApplyingExecutors() {
        List<CollectionMember<ExecutorNotApplying>> collectionMembersNonApplying = mapper.toExecutorNotApplyingCollectionMember(executorList);
        Assert.assertThat(collectionMembersNonApplying.size(), equalTo(1));
        ExecutorNotApplying executorNotApplying = collectionMembersNonApplying.get(0).getValue();
        Assert.assertThat(executorNotApplying.getNotApplyingExecutorName(), equalTo("Jackie Smith"));
        Assert.assertThat(executorNotApplying.getNotApplyingExecutorIsDead(), equalTo(Boolean.TRUE));
        Assert.assertThat(executorNotApplying.getNotApplyingExecutorDiedBefore(), equalTo(Boolean.FALSE));
    }

    @Test
    public void shouldMapApplyingExecutors() {
        List<CollectionMember<ExecutorApplying>> collectionMembers = mapper.toExecutorApplyingCollectionMember(executorList);
        Assert.assertThat(collectionMembers.size(), equalTo(1));
        ExecutorApplying executorApplying = collectionMembers.get(0).getValue();
        Assert.assertThat(executorApplying.getApplyingExecutorName(), equalTo("Bobby Smith"));
        Assert.assertThat(executorApplying.getApplyingExecutorFirstName(), equalTo("Bobby"));
        Assert.assertThat(executorApplying.getApplyingExecutorLastName(), equalTo("Smith"));
        Assert.assertThat(executorApplying.getApplyingExecutorInvitationId(), equalTo("12345"));
        Assert.assertThat(executorApplying.getApplyingExecutorAddress().getAddressLine1(), equalTo(ADDRESS_LINE_1));
        Assert.assertThat(executorApplying.getApplyingExecutorAddress().getPostCode(), equalTo(POSTCODE));
        Assert.assertThat(executorApplying.getApplyingExecutorApplicant(), equalTo(Boolean.FALSE));
        Assert.assertThat(executorApplying.getApplyingExecutorHasOtherName(), equalTo(Boolean.TRUE));
    }

    @Test
    public void shouldMapApplyingExecutorsWithPrimaryApplicant() {
        executorList.add(Executor.builder()
            .isApplying(Boolean.TRUE)
            .isApplicant(Boolean.TRUE)
            .hasOtherName(Boolean.TRUE)
            .firstName("Robert")
            .lastName("Smyth")
            .address(Address.builder()
                .addressLine1(ADDRESS_LINE_1)
                .postCode(POSTCODE)
                .build())
            .inviteId("56789")
            .build());

        List<CollectionMember<ExecutorApplying>> collectionMembers = mapper.toExecutorApplyingCollectionMember(executorList);
        Assert.assertThat(collectionMembers.size(), equalTo(1));
        ExecutorApplying executorApplying = collectionMembers.get(0).getValue();
        Assert.assertThat(executorApplying.getApplyingExecutorName(), equalTo("Bobby Smith"));
        Assert.assertThat(executorApplying.getApplyingExecutorFirstName(), equalTo("Bobby"));
        Assert.assertThat(executorApplying.getApplyingExecutorLastName(), equalTo("Smith"));
        Assert.assertThat(executorApplying.getApplyingExecutorInvitationId(), equalTo("12345"));
        Assert.assertThat(executorApplying.getApplyingExecutorAddress().getAddressLine1(), equalTo(ADDRESS_LINE_1));
        Assert.assertThat(executorApplying.getApplyingExecutorAddress().getPostCode(), equalTo(POSTCODE));
        Assert.assertThat(executorApplying.getApplyingExecutorApplicant(), equalTo(Boolean.FALSE));
        Assert.assertThat(executorApplying.getApplyingExecutorHasOtherName(), equalTo(Boolean.TRUE));
    }

    @Test
    public void shouldMapApplyingExecutorsWithNullPrimaryApplicant() {
        executorList.add(Executor.builder()
            .isApplying(Boolean.TRUE)
            .hasOtherName(Boolean.TRUE)
            .firstName("Robert")
            .lastName("Smyth")
            .address(Address.builder()
                .addressLine1(ADDRESS_LINE_1)
                .postCode(POSTCODE)
                .build())
            .inviteId("56789")
            .build());

        List<CollectionMember<ExecutorApplying>> collectionMembers = mapper.toExecutorApplyingCollectionMember(executorList);
        Assert.assertThat(collectionMembers.size(), equalTo(2));
        ExecutorApplying executorApplying1 = collectionMembers.get(0).getValue();
        Assert.assertThat(executorApplying1.getApplyingExecutorName(), equalTo("Bobby Smith"));
        Assert.assertThat(executorApplying1.getApplyingExecutorFirstName(), equalTo("Bobby"));
        Assert.assertThat(executorApplying1.getApplyingExecutorLastName(), equalTo("Smith"));
        Assert.assertThat(executorApplying1.getApplyingExecutorInvitationId(), equalTo("12345"));
        Assert.assertThat(executorApplying1.getApplyingExecutorAddress().getAddressLine1(), equalTo(ADDRESS_LINE_1));
        Assert.assertThat(executorApplying1.getApplyingExecutorAddress().getPostCode(), equalTo(POSTCODE));
        Assert.assertThat(executorApplying1.getApplyingExecutorApplicant(), equalTo(Boolean.FALSE));
        Assert.assertThat(executorApplying1.getApplyingExecutorHasOtherName(), equalTo(Boolean.TRUE));
        ExecutorApplying executorApplying = collectionMembers.get(1).getValue();
        Assert.assertThat(executorApplying.getApplyingExecutorName(), equalTo("Robert Smyth"));
        Assert.assertThat(executorApplying.getApplyingExecutorFirstName(), equalTo("Robert"));
        Assert.assertThat(executorApplying.getApplyingExecutorLastName(), equalTo("Smyth"));
        Assert.assertThat(executorApplying.getApplyingExecutorInvitationId(), equalTo("56789"));
        Assert.assertThat(executorApplying.getApplyingExecutorAddress().getAddressLine1(), equalTo(ADDRESS_LINE_1));
        Assert.assertThat(executorApplying.getApplyingExecutorAddress().getPostCode(), equalTo(POSTCODE));
        Assert.assertThat(executorApplying.getApplyingExecutorApplicant(), equalTo(null));
        Assert.assertThat(executorApplying.getApplyingExecutorHasOtherName(), equalTo(Boolean.TRUE));
    }

    @Test
    public void shouldMapExecutorsApplyingAndPlaceApplicantFirstInList() {

        List<Executor> executors = mapper.fromCollectionMember(PaMultipleExecutorTestDataCreator.createGrantOfRepresentation());
        Assert.assertThat(executors.get(0).getIsApplicant(), equalTo(Boolean.TRUE));
    }
}
