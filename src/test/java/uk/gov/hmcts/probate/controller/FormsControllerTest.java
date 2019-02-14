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

    @MockBean
    private SubmitService submitService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String intestacyFormJsonStr;

    private String caveatFormJsonStr;

    private IntestacyForm intestacyForm;

    private CaveatForm caveatForm;

    @Before
    public void setUp() throws Exception {
        this.intestacyFormJsonStr = TestUtils.getJSONFromFile("intestacyForm.json");
        this.intestacyForm = objectMapper.readValue(intestacyFormJsonStr, IntestacyForm.class);

        this.caveatFormJsonStr = TestUtils.getJSONFromFile("caveatForm.json");
        this.caveatForm = objectMapper.readValue(caveatFormJsonStr, CaveatForm.class);
    }

    @Test
    public void shouldSaveForm() throws Exception {
        when(submitService.saveDraft(eq(EMAIL_ADDRESS), eq(intestacyForm))).thenReturn(intestacyForm);
        when(submitService.saveDraft(eq(EMAIL_ADDRESS), eq(caveatForm))).thenReturn(caveatForm);

        mockMvc.perform(post(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS)
            .content(intestacyFormJsonStr)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(intestacyFormJsonStr, true));
        verify(submitService, times(1)).saveDraft(eq(EMAIL_ADDRESS), eq(intestacyForm));

        mockMvc.perform(post(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS)
                .content(caveatFormJsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(caveatFormJsonStr, true));
        verify(submitService, times(1)).saveDraft(eq(EMAIL_ADDRESS), eq(caveatForm));
    }


    @Test
    public void shouldUpdateForm() throws Exception {
        when(submitService.update(eq(EMAIL_ADDRESS), eq(intestacyForm))).thenReturn(intestacyForm);
        when(submitService.update(eq(EMAIL_ADDRESS), eq(caveatForm))).thenReturn(caveatForm);

        mockMvc.perform(put(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS+ "/" + SUBMISSIONS_ENDPOINT)
                .content(intestacyFormJsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(intestacyFormJsonStr, true));
        verify(submitService, times(1)).update(eq(EMAIL_ADDRESS), eq(intestacyForm));

        mockMvc.perform(put(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS+ "/" + SUBMISSIONS_ENDPOINT)
                .content(caveatFormJsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(caveatFormJsonStr, true));
        verify(submitService, times(1)).update(eq(EMAIL_ADDRESS), eq(caveatForm));

    }

    @Test
    public void shouldGetForm() throws Exception {
        when(submitService.getCase(EMAIL_ADDRESS, ProbateType.INTESTACY)).thenReturn(intestacyForm);
        when(submitService.getCase(EMAIL_ADDRESS, ProbateType.CAVEAT)).thenReturn(caveatForm);

        mockMvc.perform(get(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS)
            .param("probateType", ProbateType.INTESTACY.name())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(intestacyFormJsonStr, true));
        verify(submitService, times(1)).getCase(EMAIL_ADDRESS, ProbateType.INTESTACY);

        mockMvc.perform(get(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS)
                .param("probateType", ProbateType.CAVEAT.name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(caveatFormJsonStr, true));
        verify(submitService, times(1)).getCase(EMAIL_ADDRESS, ProbateType.CAVEAT);
    }

    @Test
    public void shouldSubmitForm() throws Exception {
        when(submitService.submit(eq(EMAIL_ADDRESS), eq(intestacyForm))).thenReturn(intestacyForm);
        when(submitService.submit(eq(EMAIL_ADDRESS), eq(caveatForm))).thenReturn(caveatForm);

        mockMvc.perform(post(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS + "/" + SUBMISSIONS_ENDPOINT)
            .content(intestacyFormJsonStr)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(intestacyFormJsonStr, true));
        verify(submitService, times(1)).submit(eq(EMAIL_ADDRESS), eq(intestacyForm));

        mockMvc.perform(post(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS + "/" + SUBMISSIONS_ENDPOINT)
                .content(caveatFormJsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(caveatFormJsonStr, true));
        verify(submitService, times(1)).submit(eq(EMAIL_ADDRESS), eq(caveatForm));
    }

    @Test
    public void shouldUpdatePayments() throws Exception {
        when(submitService.updatePayments(eq(EMAIL_ADDRESS), eq(intestacyForm))).thenReturn(intestacyForm);
        when(submitService.updatePayments(eq(EMAIL_ADDRESS), eq(caveatForm))).thenReturn(caveatForm);

        mockMvc.perform(post(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS + "/" + PAYMENTS_ENDPOINT)
            .content(intestacyFormJsonStr)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(intestacyFormJsonStr, true));
        verify(submitService, times(1)).updatePayments(eq(EMAIL_ADDRESS), eq(intestacyForm));

        mockMvc.perform(post(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS + "/" + PAYMENTS_ENDPOINT)
                .content(caveatFormJsonStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(caveatFormJsonStr, true));
        verify(submitService, times(1)).updatePayments(eq(EMAIL_ADDRESS), eq(caveatForm));
    }
}
