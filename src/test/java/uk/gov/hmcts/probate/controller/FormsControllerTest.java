package uk.gov.hmcts.probate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.probate.TestUtils;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.forms.caveat.CaveatForm;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaForm;
import uk.gov.hmcts.reform.probate.model.payments.PaymentDto;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {FormsController.class}, secure = false)
public class FormsControllerTest {

    private static final String EMAIL_ADDRESS = "test@test.com";

    private static final String FORMS_ENDPOINT = "/forms";
    private static final String SUBMISSIONS_ENDPOINT = "/submissions";
    private static final String PAYMENTS_ENDPOINT = "/payments";
    private static final String VALIDATIONS_ENDPOINT = "/validations";

    @MockBean
    private SubmitService submitService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String intestacyFormJsonStr;

    private String caveatFormJsonStr;

    private IntestacyForm intestacyForm;

    private String paFormJsonStr;

    private PaForm paForm;

    private CaveatForm caveatForm;

    private PaymentDto paymentDto;

    @Before
    public void setUp() throws Exception {
        this.intestacyFormJsonStr = TestUtils.getJSONFromFile("intestacyForm.json");
        this.intestacyForm = objectMapper.readValue(intestacyFormJsonStr, IntestacyForm.class);

        this.caveatFormJsonStr = TestUtils.getJSONFromFile("caveatForm.json");
        this.caveatForm = objectMapper.readValue(caveatFormJsonStr, CaveatForm.class);

        this.paFormJsonStr = TestUtils.getJSONFromFile("paForm.json");
        this.paForm = objectMapper.readValue(paFormJsonStr, PaForm.class);

        paymentDto = PaymentDto.builder()
            .status("Success")
            .ccdCaseNumber("123214")
            .reference("Ref-123456789")
            .amount(BigDecimal.valueOf(10000L))
            .build();
    }

    @Test
    public void shouldSaveCaveatForm() throws Exception {
        when(submitService.saveCase(eq(EMAIL_ADDRESS), eq(caveatForm))).thenReturn(caveatForm);

        mockMvc.perform(post(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS)
                .content(caveatFormJsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(caveatFormJsonStr, true));
        verify(submitService, times(1)).saveCase(eq(EMAIL_ADDRESS), eq(caveatForm));
    }

    @Test
    public void shouldSaveIntestacyForm() throws Exception {
        when(submitService.saveCase(eq(EMAIL_ADDRESS), eq(intestacyForm))).thenReturn(intestacyForm);

        mockMvc.perform(post(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS)
            .content(intestacyFormJsonStr)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(intestacyFormJsonStr, true));

        verify(submitService, times(1)).saveCase(eq(EMAIL_ADDRESS), eq(intestacyForm));
    }


    @Test
    public void shouldSubmitPaFormWithPayment() throws Exception {
        when(submitService.update(eq(EMAIL_ADDRESS), eq(ProbateType.PA), eq(paymentDto))).thenReturn(paForm);
        String paymentDtoStr = objectMapper.writeValueAsString(paymentDto);

        mockMvc.perform(put(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS + "/" + SUBMISSIONS_ENDPOINT)
            .param("probateType", ProbateType.PA.name())
            .content(paymentDtoStr)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
                .andExpect(content().json(paFormJsonStr, true));

        verify(submitService, times(1)).update(eq(EMAIL_ADDRESS), eq(ProbateType.PA), eq(paymentDto));
    }

    @Test
    public void shouldGetCaveatForm() throws Exception {
        when(submitService.getCase(EMAIL_ADDRESS, ProbateType.CAVEAT)).thenReturn(caveatForm);

        mockMvc.perform(get(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS)
                .param("probateType", ProbateType.CAVEAT.name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(caveatFormJsonStr, true));
        verify(submitService, times(1)).getCase(EMAIL_ADDRESS, ProbateType.CAVEAT);
    }

    @Test
    public void shouldGetIntestacyForm() throws Exception {
        when(submitService.getCase(EMAIL_ADDRESS, ProbateType.INTESTACY)).thenReturn(intestacyForm);

        mockMvc.perform(get(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS)
            .param("probateType", ProbateType.INTESTACY.name())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(intestacyFormJsonStr, true));
        verify(submitService, times(1)).getCase(EMAIL_ADDRESS, ProbateType.INTESTACY);
    }
    
    @Test
    public void shouldSubmitCaveatForm() throws Exception {
        when(submitService.submit(eq(EMAIL_ADDRESS), eq(caveatForm))).thenReturn(caveatForm);

        mockMvc.perform(post(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS + "/" + SUBMISSIONS_ENDPOINT)
                .content(caveatFormJsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(caveatFormJsonStr, true));
        verify(submitService, times(1)).submit(eq(EMAIL_ADDRESS), eq(caveatForm));
    }

    @Test
    public void shouldUpdateCaveatPayments() throws Exception {
        when(submitService.updatePayments(eq(EMAIL_ADDRESS), eq(caveatForm))).thenReturn(caveatForm);

        mockMvc.perform(post(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS + "/" + PAYMENTS_ENDPOINT)
                .content(caveatFormJsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(caveatFormJsonStr, true));
        verify(submitService, times(1)).updatePayments(eq(EMAIL_ADDRESS), eq(caveatForm));
    }

    @Test
    public void shouldValidate() throws Exception {
        when(submitService.validate(eq(EMAIL_ADDRESS), eq(ProbateType.PA))).thenReturn(paForm);

        mockMvc.perform(put(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS + "/" + VALIDATIONS_ENDPOINT)
            .param("probateType", ProbateType.PA.name())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(paFormJsonStr, true));

        verify(submitService, times(1)).validate(eq(EMAIL_ADDRESS), eq(ProbateType.PA));
    }
}
