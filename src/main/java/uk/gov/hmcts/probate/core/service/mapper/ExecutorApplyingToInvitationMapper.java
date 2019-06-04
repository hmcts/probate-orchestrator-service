package uk.gov.hmcts.probate.core.service.mapper;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;

@Slf4j
@Component
@NoArgsConstructor
public class ExecutorApplyingToInvitationMapper {

    public Invitation map(ExecutorApplying executorApplying) {
        return Invitation.builder()
                .agreed(executorApplying.getApplyingExecutorAgreed())
                .inviteId(executorApplying.getApplyingExecutorInvitiationId())
                .phoneNumber(executorApplying.getApplyingExecutorPhoneNumber())
                .executorName(executorApplying.getApplyingExecutorName())
                .email(executorApplying.getApplyingExecutorEmail())
                .leadExecutorName(executorApplying.getApplyingExecutorLeadName())
                .build();
    }

}
