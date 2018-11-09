package uk.gov.hmcts.probate.dto.ccd.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdditionalExecutorApplyingDTO {

    private final String applyingExecutorName;

    private final String applyingExecutorPhoneNumber;

    private final String applyingExecutorEmail;

    private final AddressDTO applyingExecutorAddress;

    private String applyingExecutorOtherNames;

    private String applyingExecutorOtherNamesReason;

    private String applyingExecutorOtherReason;
}
