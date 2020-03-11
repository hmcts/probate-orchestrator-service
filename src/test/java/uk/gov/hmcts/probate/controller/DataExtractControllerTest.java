package uk.gov.hmcts.probate.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {DataExtractController.class}, secure = false)
public class DataExtractControllerTest {

    @MockBean
    private DataExtractController dataExtractController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldInitiateHMRCDataExtractForNoDate() throws Exception {
        mockMvc.perform(post("/data-extract/hmrc")
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateHmrcExtract();
    }

    @Test
    public void shouldInitiateHMRCDataExtractForGivenDate() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateTimeFormatter.format(LocalDate.now().minusDays(1L));

        mockMvc.perform(post("/data-extract/hmrc/" + date)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateHmrcExtractForDate(date);
    }

    @Test
    public void shouldInitiateHMRCDataExtractForGivenDates() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateFrom = dateTimeFormatter.format(LocalDate.now().minusDays(10L));
        String dateTo = dateTimeFormatter.format(LocalDate.now().minusDays(1L));

        mockMvc.perform(post("/data-extract/hmrc/" + dateFrom + "/" + dateTo)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateHmrcExtractFromToDate(dateFrom, dateTo);
    }

    @Test
    public void shouldInitiateIronMountainDataExtractForNoDate() throws Exception {
        mockMvc.perform(post("/data-extract/iron-mountain")
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateIronMountainExtract();
    }

    @Test
    public void shouldInitiateIronMountainDataExtractForGivenDate() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateTimeFormatter.format(LocalDate.now().minusDays(1L));

        mockMvc.perform(post("/data-extract/iron-mountain/" + date)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateIronMountainExtract(date);
    }

    @Test
    public void shouldInitiateExelaDataExtractForNoDate() throws Exception {
        mockMvc.perform(post("/data-extract/exela")
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateExelaExtract();
    }

    @Test
    public void shouldInitiateExelaDataExtractForGivenDate() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateTimeFormatter.format(LocalDate.now().minusDays(1L));

        mockMvc.perform(post("/data-extract/exela/" + date)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateExelaExtract(date);
    }

}