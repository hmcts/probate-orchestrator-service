package uk.gov.hmcts.probate.core.service.mapper;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExecutorApplyingToInvitationMapperTest {

    static final String APPLYING_EXECUTOR_NAME = "applyingExecutorName";
    static final String EXECUTOR_EMAIL = "applyingExecutorEmail";
    static final String PHONE_NUMBER = "phoneNumber";
    static final String FIRST_NAME = "firstName";
    static final String LAST_NAME = "lastName";
    static final String LEAD_NAME = "leadName";
    static final String APPLYING_EXECUTOR_INVITATION_ID = "applyingExecutorInvitationId";
    ExecutorApplyingToInvitationMapper executorApplyingToInvitationMapper = new ExecutorApplyingToInvitationMapper();

    @Test
    void shouldMapCorrectly() {

        ExecutorApplying executorApplying = ExecutorApplying.builder()
                .applyingExecutorName(APPLYING_EXECUTOR_NAME)
                .applyingExecutorEmail(EXECUTOR_EMAIL)
                .applyingExecutorPhoneNumber(PHONE_NUMBER)
                .applyingExecutorFirstName(FIRST_NAME)
                .applyingExecutorLastName(LAST_NAME)
                .applyingExecutorLeadName(LEAD_NAME)
                .applyingExecutorAgreed(Boolean.TRUE)
                .applyingExecutorInvitationId(APPLYING_EXECUTOR_INVITATION_ID)
                .build();
        Invitation invitation = executorApplyingToInvitationMapper.map(executorApplying);

        assertEquals(APPLYING_EXECUTOR_NAME, invitation.getExecutorName());
        assertEquals(EXECUTOR_EMAIL, invitation.getEmail());
        assertEquals(PHONE_NUMBER, invitation.getPhoneNumber());
        assertEquals(FIRST_NAME, invitation.getFirstName());
        assertEquals(LAST_NAME, invitation.getLastName());
        assertEquals(LEAD_NAME, invitation.getLeadExecutorName());
        assertEquals(APPLYING_EXECUTOR_INVITATION_ID, invitation.getInviteId());
        assertEquals(Boolean.TRUE, invitation.getAgreed());
    }
}
