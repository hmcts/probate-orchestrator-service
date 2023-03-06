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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ExecutorsMapperIT {

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
        assertEquals(1, collectionMembersNonApplying.size());
        ExecutorNotApplying executorNotApplying = collectionMembersNonApplying.get(0).getValue();
        assertEquals("Jackie Smith", executorNotApplying.getNotApplyingExecutorName());
        assertEquals(Boolean.TRUE, executorNotApplying.getNotApplyingExecutorIsDead());
        assertEquals(Boolean.FALSE, executorNotApplying.getNotApplyingExecutorDiedBefore());
    }

    @Test
    public void shouldMapApplyingExecutors() {
        List<CollectionMember<ExecutorApplying>> collectionMembers =
            mapper.toExecutorApplyingCollectionMember(executorList);
        assertEquals(1, collectionMembers.size());
        ExecutorApplying executorApplying = collectionMembers.get(0).getValue();
        assertEquals("Bobby Smith", executorApplying.getApplyingExecutorName());
        assertEquals("Bobby", executorApplying.getApplyingExecutorFirstName());
        assertEquals("Smith", executorApplying.getApplyingExecutorLastName());
        assertEquals("12345", executorApplying.getApplyingExecutorInvitationId());
        assertEquals(ADDRESS_LINE_1, executorApplying.getApplyingExecutorAddress().getAddressLine1());
        assertEquals(POSTCODE, executorApplying.getApplyingExecutorAddress().getPostCode());
        assertEquals(Boolean.FALSE, executorApplying.getApplyingExecutorApplicant());
        assertEquals(Boolean.TRUE, executorApplying.getApplyingExecutorHasOtherName());
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
        assertEquals(1, collectionMembers.size());
        ExecutorApplying executorApplying = collectionMembers.get(0).getValue();
        assertEquals("Bobby Smith", executorApplying.getApplyingExecutorName());
        assertEquals("Bobby", executorApplying.getApplyingExecutorFirstName());
        assertEquals("Smith", executorApplying.getApplyingExecutorLastName());
        assertEquals("12345", executorApplying.getApplyingExecutorInvitationId());
        assertEquals(ADDRESS_LINE_1, executorApplying.getApplyingExecutorAddress().getAddressLine1());
        assertEquals(POSTCODE, executorApplying.getApplyingExecutorAddress().getPostCode());
        assertEquals(Boolean.FALSE, executorApplying.getApplyingExecutorApplicant());
        assertEquals(Boolean.TRUE, executorApplying.getApplyingExecutorHasOtherName());
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
        assertEquals(2, collectionMembers.size());
        ExecutorApplying executorApplying1 = collectionMembers.get(0).getValue();
        assertEquals("Bobby Smith", executorApplying1.getApplyingExecutorName());
        assertEquals("Bobby", executorApplying1.getApplyingExecutorFirstName());
        assertEquals("Smith", executorApplying1.getApplyingExecutorLastName());
        assertEquals("12345", executorApplying1.getApplyingExecutorInvitationId());
        assertEquals(ADDRESS_LINE_1, executorApplying1.getApplyingExecutorAddress().getAddressLine1());
        assertEquals(POSTCODE, executorApplying1.getApplyingExecutorAddress().getPostCode());
        assertEquals(Boolean.FALSE, executorApplying1.getApplyingExecutorApplicant());
        assertEquals(Boolean.TRUE, executorApplying1.getApplyingExecutorHasOtherName());

        ExecutorApplying executorApplying = collectionMembers.get(1).getValue();
        assertEquals("Robert Smyth", executorApplying.getApplyingExecutorName());
        assertEquals("Robert", executorApplying.getApplyingExecutorFirstName());
        assertEquals("Smyth", executorApplying.getApplyingExecutorLastName());
        assertEquals("56789", executorApplying.getApplyingExecutorInvitationId());
        assertEquals(ADDRESS_LINE_1, executorApplying.getApplyingExecutorAddress().getAddressLine1());
        assertEquals(POSTCODE, executorApplying.getApplyingExecutorAddress().getPostCode());
        assertNull(executorApplying.getApplyingExecutorApplicant());
        assertEquals(Boolean.TRUE, executorApplying.getApplyingExecutorHasOtherName());
    }

    @Test
    public void shouldMapExecutorsApplyingAndPlaceApplicantFirstInList() {

        List<Executor> executors =
            mapper.fromCollectionMember(PaMultipleExecutorTestDataCreator.createGrantOfRepresentation());
        assertEquals(Boolean.TRUE, executors.get(0).getIsApplicant());
    }
}
