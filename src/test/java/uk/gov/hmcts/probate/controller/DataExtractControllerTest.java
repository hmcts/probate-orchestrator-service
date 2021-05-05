package uk.gov.hmcts.probate.controller;

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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DataExtractControllerTest {

    @MockBean
    private DataExtractController dataExtractController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldInitiateHmrcDataExtractForNoDate() throws Exception {
        mockMvc.perform(post("/data-extract/hmrc")
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateHmrcExtract();
    }

    @Test
    public void shouldInitiateHmrcDataExtractForGivenDate() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateTimeFormatter.format(LocalDate.now().minusDays(1L));

        mockMvc.perform(post("/data-extract/hmrc/" + date)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateHmrcExtractForDate(date);
    }

    @Test
    public void shouldInitiateHmrcDataExtractForGivenDates() throws Exception {
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

    @Test
    public void shouldInitiateExelaDataExtractForGivenDateRange() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fromDate = dateTimeFormatter.format(LocalDate.now().minusDays(2L));
        String toDate = dateTimeFormatter.format(LocalDate.now().minusDays(1L));

        mockMvc.perform(post("/data-extract/exela/" + fromDate + "/" + toDate)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateExelaExtractDateRange(fromDate, toDate);
    }

    @Test
    public void shouldInitiateSmeeAndFordDataExtractForNoDate() throws Exception {
        mockMvc.perform(post("/data-extract/smee-and-ford")
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateSmeeAndFordExtract();
    }

    @Test
    public void shouldInitiateSmeeAndFordDataExtractForGivenDateRange() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fromDate = dateTimeFormatter.format(LocalDate.now().minusDays(2L));
        String toDate = dateTimeFormatter.format(LocalDate.now().minusDays(1L));

        mockMvc.perform(post("/data-extract/smee-and-ford/" + fromDate + "/" + toDate)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateSmeeAndFordExtractDateRange(fromDate, toDate);
    }


}