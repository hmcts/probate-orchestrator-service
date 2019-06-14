package uk.gov.hmcts.probate.core.service.mapper;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;

import static org.hamcrest.CoreMatchers.*;

public class ExecutorApplyingToInvitationMapperTest {

    public static final String APPLYING_EXECUTOR_NAME = "applyingExecutorName";
    public static final String EXECUTOR_EMAIL = "applyingExecutorEmail";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String LEAD_NAME = "leadName";
    public static final String APPLYING_EXECUTOR_INVITATION_ID = "applyingExecutorInvitationId";
    ExecutorApplyingToInvitationMapper executorApplyingToInvitationMapper = new ExecutorApplyingToInvitationMapper();

    @Test
    public void shouldMapCorrectly() {

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

        Assert.assertThat(invitation.getExecutorName(), equalTo(APPLYING_EXECUTOR_NAME));
        Assert.assertThat(invitation.getEmail(), equalTo(EXECUTOR_EMAIL));
        Assert.assertThat(invitation.getPhoneNumber(), equalTo(PHONE_NUMBER));
        Assert.assertThat(invitation.getFirstName(), equalTo(FIRST_NAME));
        Assert.assertThat(invitation.getLastName(), equalTo(LAST_NAME));
        Assert.assertThat(invitation.getLeadExecutorName(), equalTo(LEAD_NAME));
        Assert.assertThat(invitation.getInviteId(), equalTo(APPLYING_EXECUTOR_INVITATION_ID));
        Assert.assertThat(invitation.getAgreed(), equalTo(Boolean.TRUE));
    }
}