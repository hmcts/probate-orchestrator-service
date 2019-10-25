package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import uk.gov.hmcts.reform.probate.model.forms.pa.Executors;

@JsonIgnoreProperties(ignoreUnknown = true)
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
