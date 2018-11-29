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
import uk.gov.hmcts.probate.FormDataDTOTestBuilder;
import uk.gov.hmcts.probate.controller.mapper.FormDataMapper;
import uk.gov.hmcts.probate.domain.FormData;
import uk.gov.hmcts.probate.dto.CaseInfoDTO;
import uk.gov.hmcts.probate.dto.formdata.ApplicantDTO;
import uk.gov.hmcts.probate.dto.formdata.DeceasedDTO;
import uk.gov.hmcts.probate.dto.formdata.FormDataDTO;
import uk.gov.hmcts.probate.dto.formdata.InheritanceTaxDTO;
import uk.gov.hmcts.probate.dto.validation.BusinessValidationResponseDTO;
import uk.gov.hmcts.probate.service.OrchestratorSubmitService;
import uk.gov.hmcts.probate.service.ValidationService;

import javax.validation.Validation;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ValidationController.class, secure = false)
public class ValidationControllerTest {



    @MockBean
    private ValidationService validationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldValidateIntestacy() throws Exception {
        FormDataDTO formDataDTO = FormDataDTOTestBuilder.build();

        when(validationService.validate(formDataDTO, ValidationService.ProbateType.INTESTACY)).thenReturn(new BusinessValidationResponseDTO());
        
        mockMvc.perform(post(ValidationController.INTESTACY_VALIDATION_URL)
                .content(objectMapper.writeValueAsString(formDataDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
