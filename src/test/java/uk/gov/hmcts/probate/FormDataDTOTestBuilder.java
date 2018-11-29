package uk.gov.hmcts.probate;

import uk.gov.hmcts.probate.dto.formdata.ApplicantDTO;
import uk.gov.hmcts.probate.dto.formdata.DeceasedDTO;
import uk.gov.hmcts.probate.dto.formdata.FormDataDTO;
import uk.gov.hmcts.probate.dto.formdata.InheritanceTaxDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FormDataDTOTestBuilder {

    private static final String APPLICANT_FIRST_NAME = "Rob";
    private static final String APPLICANT_LAST_NAME = "Stark";
    private static final String APPLICANT_ADDRESS = "Winterfell, Westeros";

    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1950, 1, 1);
    private static final LocalDate DATE_OF_DEATH = LocalDate.of(2018, 1, 1);
    private static final BigDecimal GROSS_VALUE = BigDecimal.valueOf(200000);
    private static final BigDecimal NET_VALUE = BigDecimal.valueOf(100000);

    static public FormDataDTO build() {
        ApplicantDTO applicantDTO = new ApplicantDTO();
        applicantDTO.setAddress(APPLICANT_ADDRESS);
        applicantDTO.setFirstName(APPLICANT_FIRST_NAME);
        applicantDTO.setLastName(APPLICANT_LAST_NAME);

        DeceasedDTO deceasedDTO = new DeceasedDTO();
        deceasedDTO.setDateOfBirth(DATE_OF_BIRTH);
        deceasedDTO.setDateOfDeath(DATE_OF_DEATH);

        InheritanceTaxDTO inheritanceTaxDTO = new InheritanceTaxDTO();
        inheritanceTaxDTO.setGrossValue(GROSS_VALUE);
        inheritanceTaxDTO.setNetValue(NET_VALUE);

        FormDataDTO formDataDTO = new FormDataDTO();
        formDataDTO.setDeceased(deceasedDTO);
        formDataDTO.setApplicant(applicantDTO);
        formDataDTO.setIht(inheritanceTaxDTO);
        return formDataDTO;
    }
}
