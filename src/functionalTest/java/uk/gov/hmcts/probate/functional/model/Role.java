package uk.gov.hmcts.probate.functional.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Role {
    private String code;
}
