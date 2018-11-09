package uk.gov.hmcts.probate.dto.ccd.data.legalstatement;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LegalStatementDTO {

    private final String intro;

    private final String applicant;

    private final String deceased;

    private final String deceasedOtherNames;

    private final String deceasedEstateValue;

    private final String deceasedEstateLand;

    private final List<ExecutorsNotApplyingDTO> executorsNotApplying;

    private final List<ExecutorsApplyingDTO> executorsApplying;

}