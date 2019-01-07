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
import uk.gov.hmcts.reform.probate.model.forms.Form;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private String formJsonStr;

    private Form form;

    @Before
    public void setUp() throws Exception {
        this.formJsonStr = TestUtils.getJSONFromFile("intestacyForm.json");
        this.form = objectMapper.readValue(formJsonStr, Form.class);
    }

    @Test
    public void shouldSaveForm() throws Exception {
        when(submitService.saveDraft(eq(EMAIL_ADDRESS), eq(form))).thenReturn(form);

        mockMvc.perform(post(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS)
            .content(formJsonStr)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(formJsonStr, true));
        verify(submitService, times(1)).saveDraft(eq(EMAIL_ADDRESS), eq(form));
    }

    @Test
    public void shouldGetForm() throws Exception {
        when(submitService.getCase(EMAIL_ADDRESS, ProbateType.INTESTACY)).thenReturn(form);

        mockMvc.perform(get(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS)
            .param("probateType", ProbateType.INTESTACY.name())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(formJsonStr, true));
        verify(submitService, times(1)).getCase(EMAIL_ADDRESS, ProbateType.INTESTACY);
    }

    @Test
    public void shouldSubmitForm() throws Exception {
        when(submitService.submit(eq(EMAIL_ADDRESS), eq(form))).thenReturn(form);

        mockMvc.perform(post(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS + "/" + SUBMISSIONS_ENDPOINT)
            .content(formJsonStr)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(formJsonStr, true));
        verify(submitService, times(1)).submit(eq(EMAIL_ADDRESS), eq(form));
    }

    @Test
    public void shouldUpdatePayments() throws Exception {
        when(submitService.updatePayments(eq(EMAIL_ADDRESS), eq(form))).thenReturn(form);

        mockMvc.perform(post(FORMS_ENDPOINT + "/" + EMAIL_ADDRESS + "/" + PAYMENTS_ENDPOINT)
            .content(formJsonStr)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(formJsonStr, true));
        verify(submitService, times(1)).updatePayments(eq(EMAIL_ADDRESS), eq(form));
    }
}
