package uk.gov.hmcts.probate.model.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.probate.model.multiapplicant.InviteData;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class InviteDataHolder {

    private InviteData inviteData;
}
