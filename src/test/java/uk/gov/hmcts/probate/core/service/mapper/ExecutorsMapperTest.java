package uk.gov.hmcts.probate.core.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorNotApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.forms.Address;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executor;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ExecutorsMapperTest {

    public static final String ADDRESS_LINE_1 = "addressLine1";
    public static final String POSTCODE = "postcode";
    @Autowired
    private ExecutorsMapper mapper;

    private GrantOfRepresentationData grantOfRepresentation;
    private List<Executor> executorList = new ArrayList<>();

    @BeforeEach
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
        List<CollectionMember<ExecutorNotApplying>> collectionMembersNonApplying =
            mapper.toExecutorNotApplyingCollectionMember(executorList);
        assertEquals(collectionMembersNonApplying.size(), equalTo(1));
        ExecutorNotApplying executorNotApplying = collectionMembersNonApplying.get(0).getValue();
        assertEquals(executorNotApplying.getNotApplyingExecutorName(), equalTo("Jackie Smith"));
        assertEquals(executorNotApplying.getNotApplyingExecutorIsDead(), equalTo(Boolean.TRUE));
        assertEquals(executorNotApplying.getNotApplyingExecutorDiedBefore(), equalTo(Boolean.FALSE));
    }

    @Test
    public void shouldMapApplyingExecutors() {
        List<CollectionMember<ExecutorApplying>> collectionMembers =
            mapper.toExecutorApplyingCollectionMember(executorList);
        assertEquals(collectionMembers.size(), equalTo(1));
        ExecutorApplying executorApplying = collectionMembers.get(0).getValue();
        assertEquals(executorApplying.getApplyingExecutorName(), equalTo("Bobby Smith"));
        assertEquals(executorApplying.getApplyingExecutorFirstName(), equalTo("Bobby"));
        assertEquals(executorApplying.getApplyingExecutorLastName(), equalTo("Smith"));
        assertEquals(executorApplying.getApplyingExecutorInvitationId(), equalTo("12345"));
        assertEquals(executorApplying.getApplyingExecutorAddress().getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertEquals(executorApplying.getApplyingExecutorAddress().getPostCode(), equalTo(POSTCODE));
        assertEquals(executorApplying.getApplyingExecutorApplicant(), equalTo(Boolean.FALSE));
        assertEquals(executorApplying.getApplyingExecutorHasOtherName(), equalTo(Boolean.TRUE));
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

        List<CollectionMember<ExecutorApplying>> collectionMembers =
            mapper.toExecutorApplyingCollectionMember(executorList);
        assertEquals(collectionMembers.size(), equalTo(1));
        ExecutorApplying executorApplying = collectionMembers.get(0).getValue();
        assertEquals(executorApplying.getApplyingExecutorName(), equalTo("Bobby Smith"));
        assertEquals(executorApplying.getApplyingExecutorFirstName(), equalTo("Bobby"));
        assertEquals(executorApplying.getApplyingExecutorLastName(), equalTo("Smith"));
        assertEquals(executorApplying.getApplyingExecutorInvitationId(), equalTo("12345"));
        assertEquals(executorApplying.getApplyingExecutorAddress().getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertEquals(executorApplying.getApplyingExecutorAddress().getPostCode(), equalTo(POSTCODE));
        assertEquals(executorApplying.getApplyingExecutorApplicant(), equalTo(Boolean.FALSE));
        assertEquals(executorApplying.getApplyingExecutorHasOtherName(), equalTo(Boolean.TRUE));
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

        List<CollectionMember<ExecutorApplying>> collectionMembers =
            mapper.toExecutorApplyingCollectionMember(executorList);
        assertEquals(collectionMembers.size(), equalTo(2));
        ExecutorApplying executorApplying1 = collectionMembers.get(0).getValue();
        assertEquals(executorApplying1.getApplyingExecutorName(), equalTo("Bobby Smith"));
        assertEquals(executorApplying1.getApplyingExecutorFirstName(), equalTo("Bobby"));
        assertEquals(executorApplying1.getApplyingExecutorLastName(), equalTo("Smith"));
        assertEquals(executorApplying1.getApplyingExecutorInvitationId(), equalTo("12345"));
        assertEquals(executorApplying1.getApplyingExecutorAddress().getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertEquals(executorApplying1.getApplyingExecutorAddress().getPostCode(), equalTo(POSTCODE));
        assertEquals(executorApplying1.getApplyingExecutorApplicant(), equalTo(Boolean.FALSE));
        assertEquals(executorApplying1.getApplyingExecutorHasOtherName(), equalTo(Boolean.TRUE));
        ExecutorApplying executorApplying = collectionMembers.get(1).getValue();
        assertEquals(executorApplying.getApplyingExecutorName(), equalTo("Robert Smyth"));
        assertEquals(executorApplying.getApplyingExecutorFirstName(), equalTo("Robert"));
        assertEquals(executorApplying.getApplyingExecutorLastName(), equalTo("Smyth"));
        assertEquals(executorApplying.getApplyingExecutorInvitationId(), equalTo("56789"));
        assertEquals(executorApplying.getApplyingExecutorAddress().getAddressLine1(), equalTo(ADDRESS_LINE_1));
        assertEquals(executorApplying.getApplyingExecutorAddress().getPostCode(), equalTo(POSTCODE));
        assertEquals(executorApplying.getApplyingExecutorApplicant(), equalTo(null));
        assertEquals(executorApplying.getApplyingExecutorHasOtherName(), equalTo(Boolean.TRUE));
    }

    @Test
    public void shouldMapExecutorsApplyingAndPlaceApplicantFirstInList() {

        List<Executor> executors =
            mapper.fromCollectionMember(PaMultipleExecutorTestDataCreator.createGrantOfRepresentation());
        assertEquals(executors.get(0).getIsApplicant(), equalTo(Boolean.TRUE));
    }
}
