package uk.gov.hmcts.probate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.probate.TestUtils;
import uk.gov.hmcts.probate.service.SubmitService;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.forms.CaseSummaryHolder;
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
@SpringBootTest
@AutoConfigureMockMvc
public class FormsControllerTest {

    private static final String EMAIL_ADDRESS = "1570130475566595";

    private static final String FORMS_ENDPOINT = "/forms";
    private static final String FORMS_CASE_ENDPOINT = "/forms/case";
    private static final String FORMS_CASES_ENDPOINT = "/forms/cases";
    private static final String FORMS_NEW_CASE_ENDPOINT = "/forms/newcase";
    private static final String SUBMISSIONS_ENDPOINT = "/submissions";
    private static final String PAYMENTS_ENDPOINT = "/payments";
    private static final String VALIDATIONS_ENDPOINT = "/validations";
    private static final String MIGRATE_DATA_ENDPOINT = "/migrateData";

    @MockBean
    private SubmitService submitService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String intestacyFormJsonStr;

    private String caveatFormJsonStr;

    private IntestacyForm intestacyForm;

    private PaForm paForm;

    private String paFormJsonStr;

    private CaveatForm caveatForm;

    private PaymentDto paymentDto;

    private CaseSummaryHolder caseSummaryHolder;
    private String caseSummaryHolderStr;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        this.intestacyFormJsonStr = TestUtils.getJSONFromFile("intestacyFormTest.json");
        this.intestacyForm = objectMapper.readValue(intestacyFormJsonStr, IntestacyForm.class);

        this.caveatFormJsonStr = TestUtils.getJSONFromFile("caveatForm.json");
        this.caveatForm = objectMapper.readValue(caveatFormJsonStr, CaveatForm.class);

        this.paFormJsonStr = TestUtils.getJSONFromFile("paForm.json");
        this.paForm = objectMapper.readValue(paFormJsonStr, PaForm.class);

        this.caseSummaryHolderStr = TestUtils.getJSONFromFile("caseSummary.json");
        this.caseSummaryHolder = objectMapper.readValue(caseSummaryHolderStr, CaseSummaryHolder.class);

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

        mockMvc.perform(post(FORMS_CASE_ENDPOINT + "/" + EMAIL_ADDRESS)
            .content(caveatFormJsonStr)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(caveatFormJsonStr, true));
        verify(submitService, times(1)).saveCase(eq(EMAIL_ADDRESS), eq(caveatForm));
    }

    @Test
    public void shouldSaveIntestacyForm() throws Exception {
        when(submitService.saveCase(eq(EMAIL_ADDRESS), eq(intestacyForm))).thenReturn(intestacyForm);

        mockMvc.perform(post(FORMS_CASE_ENDPOINT + "/" + EMAIL_ADDRESS)
            .content(intestacyFormJsonStr)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(intestacyFormJsonStr, true));

        verify(submitService, times(1)).saveCase(eq(EMAIL_ADDRESS), eq(intestacyForm));
    }

    @Test
    public void shouldSavePAForm() throws Exception {
        when(submitService.saveCase(eq(EMAIL_ADDRESS), eq(paForm))).thenReturn(paForm);

        mockMvc.perform(post(FORMS_CASE_ENDPOINT + "/" + EMAIL_ADDRESS)
                .content(paFormJsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(paFormJsonStr, true));

        verify(submitService, times(1)).saveCase(eq(EMAIL_ADDRESS), eq(paForm));
    }

    @Test
    public void shouldInitateCase() throws Exception {
        when(submitService.initiateCase(eq(ProbateType.INTESTACY))).thenReturn(caseSummaryHolder);

        mockMvc.perform(post(FORMS_NEW_CASE_ENDPOINT)
            .param("probateType", ProbateType.INTESTACY.name())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(caseSummaryHolderStr, true));

        verify(submitService, times(1)).initiateCase(eq(ProbateType.INTESTACY));
    }

    @Test
    public void shouldInitateCaveatCase() throws Exception {
        when(submitService.initiateCase(eq(ProbateType.CAVEAT))).thenReturn(caseSummaryHolder);

        mockMvc.perform(post(FORMS_NEW_CASE_ENDPOINT)
                .param("probateType", ProbateType.CAVEAT.name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(caseSummaryHolderStr, true));

        verify(submitService, times(1)).initiateCase(eq(ProbateType.CAVEAT));
    }

    @Test
    public void shouldInitatePAtCase() throws Exception {
        when(submitService.initiateCase(eq(ProbateType.PA))).thenReturn(caseSummaryHolder);

        mockMvc.perform(post(FORMS_NEW_CASE_ENDPOINT)
                .param("probateType", ProbateType.PA.name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(caseSummaryHolderStr, true));

        verify(submitService, times(1)).initiateCase(eq(ProbateType.PA));
    }

    @Test
    public void shouldSubmitCaveatFormWithPayment() throws Exception {
        when(submitService.update(eq(EMAIL_ADDRESS), eq(ProbateType.CAVEAT), eq(paymentDto))).thenReturn(caveatForm);
        String paymentDtoStr = objectMapper.writeValueAsString(paymentDto);

        mockMvc.perform(put(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS + "/" + SUBMISSIONS_ENDPOINT)
                .param("probateType", ProbateType.CAVEAT.name())
                .content(paymentDtoStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(caveatFormJsonStr, true));

        verify(submitService, times(1)).update(eq(EMAIL_ADDRESS), eq(ProbateType.CAVEAT), eq(paymentDto));
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
    public void shouldSubmitIntestacyFormWithPayment() throws Exception {
        when(submitService.update(eq("123456"), eq(ProbateType.INTESTACY), eq(paymentDto))).thenReturn(paForm);

        //PaymentDto dto = PaymentDto.builder().status("not_required").build();
        String paymentDtoStr = objectMapper.writeValueAsString(paymentDto);

        mockMvc.perform(put(FORMS_ENDPOINT + "/" + "123456" + "/" + SUBMISSIONS_ENDPOINT)
            .param("probateType", ProbateType.INTESTACY.name())
            .content(paymentDtoStr)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(paFormJsonStr, true));

        verify(submitService, times(1)).update(eq("123456"), eq(ProbateType.INTESTACY), eq(paymentDto));
    }

    @Test
    public void shouldGetCaveatForm() throws Exception {
        when(submitService.getCase(EMAIL_ADDRESS, ProbateType.CAVEAT)).thenReturn(caveatForm);

        mockMvc.perform(get(FORMS_CASE_ENDPOINT + "/" + EMAIL_ADDRESS)
            .param("probateType", ProbateType.CAVEAT.name())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(caveatFormJsonStr, true));
        verify(submitService, times(1)).getCase(EMAIL_ADDRESS, ProbateType.CAVEAT);
    }

    @Test
    public void shouldGetPAForm() throws Exception {
        when(submitService.getCase(EMAIL_ADDRESS, ProbateType.PA)).thenReturn(paForm);

        mockMvc.perform(get(FORMS_CASE_ENDPOINT + "/" + EMAIL_ADDRESS)
                .param("probateType", ProbateType.PA.name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(paFormJsonStr, true));
        verify(submitService, times(1)).getCase(EMAIL_ADDRESS, ProbateType.PA);
    }

    @Test
    public void shouldGetIntestacyForm() throws Exception {
        when(submitService.getCase(EMAIL_ADDRESS, ProbateType.INTESTACY)).thenReturn(intestacyForm);

        mockMvc.perform(get(FORMS_CASE_ENDPOINT + "/" + EMAIL_ADDRESS)
            .param("probateType", ProbateType.INTESTACY.name())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(intestacyFormJsonStr, true));
        verify(submitService, times(1)).getCase(EMAIL_ADDRESS, ProbateType.INTESTACY);
    }

    @Test
    public void shouldGetAllCases() throws Exception {
        when(submitService.getAllCases()).thenReturn(caseSummaryHolder);

        mockMvc.perform(get(FORMS_CASES_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(caseSummaryHolderStr, true));
        verify(submitService, times(1)).getAllCases();
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
    public void shouldSubmitPAForm() throws Exception {
        when(submitService.submit(eq(EMAIL_ADDRESS), eq(paForm))).thenReturn(paForm);

        mockMvc.perform(post(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS + "/" + SUBMISSIONS_ENDPOINT)
                .content(paFormJsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(paFormJsonStr, true));
        verify(submitService, times(1)).submit(eq(EMAIL_ADDRESS), eq(paForm));
    }

    @Test
    public void shouldSubmitIntestacyForm() throws Exception {
        when(submitService.submit(eq(EMAIL_ADDRESS), eq(intestacyForm))).thenReturn(intestacyForm);

        mockMvc.perform(post(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS + "/" + SUBMISSIONS_ENDPOINT)
                .content(intestacyFormJsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(intestacyFormJsonStr, true));
        verify(submitService, times(1)).submit(eq(EMAIL_ADDRESS), eq(intestacyForm));
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
    public void shouldUpdatePAPayments() throws Exception {
        when(submitService.updatePayments(eq(EMAIL_ADDRESS), eq(paForm))).thenReturn(paForm);

        mockMvc.perform(post(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS + "/" + PAYMENTS_ENDPOINT)
                .content(paFormJsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(paFormJsonStr, true));
        verify(submitService, times(1)).updatePayments(eq(EMAIL_ADDRESS), eq(paForm));
    }

    @Test
    public void shouldUpdateIntestacyPayments() throws Exception {
        when(submitService.updatePayments(eq(EMAIL_ADDRESS), eq(intestacyForm))).thenReturn(intestacyForm);

        mockMvc.perform(post(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS + "/" + PAYMENTS_ENDPOINT)
                .content(intestacyFormJsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(intestacyFormJsonStr, true));
        verify(submitService, times(1)).updatePayments(eq(EMAIL_ADDRESS), eq(intestacyForm));
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
