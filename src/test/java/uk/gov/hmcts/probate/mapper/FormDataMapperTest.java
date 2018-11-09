package uk.gov.hmcts.probate.mapper;

import org.junit.Before;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import uk.gov.hmcts.probate.controller.mapper.FormDataMapper;
import uk.gov.hmcts.probate.domain.Applicant;
import uk.gov.hmcts.probate.domain.Deceased;
import uk.gov.hmcts.probate.domain.FormData;
import uk.gov.hmcts.probate.domain.InheritanceTax;
import uk.gov.hmcts.probate.dto.formdata.ApplicantDTO;
import uk.gov.hmcts.probate.dto.formdata.DeceasedDTO;
import uk.gov.hmcts.probate.dto.formdata.FormDataDTO;
import uk.gov.hmcts.probate.dto.formdata.InheritanceTaxDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FormDataMapperTest {

    private static final String APPLICANT_FIRST_NAME = "Rob";
    private static final String APPLICANT_LAST_NAME = "Stark";
    private static final String APPLICANT_ADDRESS = "Stark";

    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1950, 1, 1);
    private static final LocalDate DATE_OF_DEATH = LocalDate.of(2018, 1, 1);
    private static final BigDecimal GROSS_VALUE = BigDecimal.valueOf(200000);
    private static final BigDecimal NET_VALUE = BigDecimal.valueOf(100000);

    private FormDataDTO formDataDTO;

    private FormData formData;

    private FormDataMapper formDataMapper;

    @Before
    public void setUp() {
        setUpDTO();
        setUpDomain();
        formDataMapper = Mappers.getMapper(FormDataMapper.class);
    }

    private void setUpDTO() {
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

        formDataDTO = new FormDataDTO();
        formDataDTO.setDeceased(deceasedDTO);
        formDataDTO.setApplicant(applicantDTO);
        formDataDTO.setIht(inheritanceTaxDTO);
    }


    private void setUpDomain() {
        Applicant applicant = new Applicant();
        applicant.setAddress(APPLICANT_ADDRESS);
        applicant.setFirstName(APPLICANT_FIRST_NAME);
        applicant.setLastName(APPLICANT_LAST_NAME);

        Deceased deceased = new Deceased();
        deceased.setDateOfBirth(DATE_OF_BIRTH);
        deceased.setDateOfDeath(DATE_OF_DEATH);

        InheritanceTax inheritanceTax = new InheritanceTax();
        inheritanceTax.setGrossValue(GROSS_VALUE);
        inheritanceTax.setNetValue(NET_VALUE);

        formData = new FormData();
        formData.setDeceased(deceased);
        formData.setApplicant(applicant);
        formData.setIht(inheritanceTax);
    }

    @Test
    public void shouldMapFormDataDtoToFormData() {
        FormData actualFormData = formDataMapper.mapFormDataDTO(formDataDTO);

        assertThat(actualFormData, is(equalTo(formData)));
    }
}
