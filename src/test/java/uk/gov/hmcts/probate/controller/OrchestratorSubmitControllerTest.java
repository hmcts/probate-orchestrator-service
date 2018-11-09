package uk.gov.hmcts.probate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.probate.domain.FormData;
import uk.gov.hmcts.probate.dto.formdata.ApplicantDTO;
import uk.gov.hmcts.probate.dto.CaseInfoDTO;
import uk.gov.hmcts.probate.dto.formdata.DeceasedDTO;
import uk.gov.hmcts.probate.dto.formdata.FormDataDTO;
import uk.gov.hmcts.probate.dto.formdata.InheritanceTaxDTO;
import uk.gov.hmcts.probate.controller.mapper.FormDataMapper;
import uk.gov.hmcts.probate.service.OrchestratorSubmitService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = OrchestratorSubmitController.class, secure = false)
public class OrchestratorSubmitControllerTest {

    private static final String SUBMIT_URL = "/submit";
    private static final String APPLICANT_FIRST_NAME = "Rob";
    private static final String APPLICANT_LAST_NAME = "Stark";
    private static final String APPLICANT_ADDRESS = "Winterfell, Westeros";

    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1950, 1, 1);
    private static final LocalDate DATE_OF_DEATH = LocalDate.of(2018, 1, 1);
    private static final BigDecimal GROSS_VALUE = BigDecimal.valueOf(200000);
    private static final BigDecimal NET_VALUE = BigDecimal.valueOf(100000);

    @MockBean
    private OrchestratorSubmitService submitService;

    @MockBean
    private FormDataMapper formDataMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldSubmit() throws Exception {
        FormDataDTO formDataDTO = createFormDataDTO();
        FormData formData = new FormData();

        when(formDataMapper.mapFormDataDTO(formDataDTO)).thenReturn(formData);
        when(submitService.submit(formData)).thenReturn(new CaseInfoDTO());
        
        mockMvc.perform(post(SUBMIT_URL)
                .content(objectMapper.writeValueAsString(formDataDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    private FormDataDTO createFormDataDTO() {
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
