package uk.gov.hmcts.probate.model.persistence;

import uk.gov.hmcts.reform.probate.model.forms.pa.Executors;

public class LegacyExecutors  extends Executors {

    private Boolean invitesSent;

    @Override
    public Boolean getInvitesSent() {
        return invitesSent;
    }

    @Override
    public void setInvitesSent(Boolean invitesSent) {
        this.invitesSent = invitesSent;
    }

}
