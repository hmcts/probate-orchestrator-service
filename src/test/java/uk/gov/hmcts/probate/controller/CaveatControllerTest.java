package uk.gov.hmcts.probate.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.probate.core.service.CaveatExpiryUpdater;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {CaveatController.class}, secure = false)
public class CaveatControllerTest {

    @MockBean
    private CaveatExpiryUpdater caveatExpiryUpdater;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldExpireCaveats() throws Exception {
        List<ProbateCaseDetails> expiredCaveats = Arrays.asList(ProbateCaseDetails.builder().build(), ProbateCaseDetails.builder().build());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String expiryDate = "2020-06-12"; //dateTimeFormatter.format(LocalDate.now());
        when(caveatExpiryUpdater.expireCaveats(expiryDate)).thenReturn(expiredCaveats);

        mockMvc.perform(post("/caveat/expire")
            .content(expiryDate)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk())
            .andExpect(content().string("2 caveats expired"));
        verify(caveatExpiryUpdater, times(1)).expireCaveats(anyString());
    }


}