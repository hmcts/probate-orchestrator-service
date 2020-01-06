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
import uk.gov.hmcts.probate.core.service.ScheduleValidator;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
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
    @MockBean
    private ScheduleValidator scheduleValidator;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldExpireCaveats() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String expiryDate = dateTimeFormatter.format(LocalDate.now().minusDays(1L));

        mockMvc.perform(post("/caveat/expire/DDDDDDDDDD")
            .content(expiryDate)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk())
            .andExpect(content().string("Perform expire caveats called"));
        verify(caveatExpiryUpdater, times(1)).expireCaveats(anyString());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionForExpireCaveatsWithIncorrectAuth() throws Exception {
        doThrow(RuntimeException.class).when(scheduleValidator).validateCaveatExpiry(anyString());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String expiryDate = dateTimeFormatter.format(LocalDate.now().minusDays(1L));

        mockMvc.perform(post("/caveat/expire/XXXXXXXXXX")
            .content(expiryDate)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk())
            .andExpect(content().string("Perform expire caveats called"));
        verify(caveatExpiryUpdater, times(1)).expireCaveats(anyString());
    }

}