package uk.gov.hmcts.probate.dto.ccd.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdditionalExecutorDTO {

    private final String additionalExecForenames;

    private final String additionalExecLastname;

    private final String additionalExecNameOnWill;

    private final String additionalExecAliasNameOnWill;

    private final String additionalApplying;

    private final AddressDTO additionalExecAddress;

    private final String additionalExecReasonNotApplying;

}
