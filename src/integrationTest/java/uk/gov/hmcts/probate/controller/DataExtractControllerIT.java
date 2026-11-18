package uk.gov.hmcts.probate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class DataExtractControllerIT {

    @MockitoBean
    private DataExtractController dataExtractController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldInitiateHmrcDataExtractForNoDate() throws Exception {
        mockMvc.perform(post("/data-extract/hmrc")
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateHmrcExtract();
    }

    @Test
    void shouldInitiateHmrcDataExtractForGivenDate() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateTimeFormatter.format(LocalDate.now().minusDays(1L));

        mockMvc.perform(post("/data-extract/hmrc/" + date)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateHmrcExtractForDate(date);
    }

    @Test
    void shouldInitiateHmrcDataExtractForGivenDates() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateFrom = dateTimeFormatter.format(LocalDate.now().minusDays(10L));
        String dateTo = dateTimeFormatter.format(LocalDate.now().minusDays(1L));

        mockMvc.perform(post("/data-extract/hmrc/" + dateFrom + "/" + dateTo)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateHmrcExtractFromToDate(dateFrom, dateTo);
    }

    @Test
    void shouldInitiateIronMountainDataExtractForNoDate() throws Exception {
        mockMvc.perform(post("/data-extract/iron-mountain")
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateIronMountainExtract();
    }

    @Test
    void shouldInitiateIronMountainDataExtractForGivenDate() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateTimeFormatter.format(LocalDate.now().minusDays(1L));

        mockMvc.perform(post("/data-extract/iron-mountain/" + date)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateIronMountainExtract(date);
    }

    @Test
    void shouldInitiateExelaDataExtractForNoDate() throws Exception {
        mockMvc.perform(post("/data-extract/exela")
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateExelaExtract();
    }

    @Test
    void shouldInitiateExelaDataExtractForGivenDate() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dateTimeFormatter.format(LocalDate.now().minusDays(1L));

        mockMvc.perform(post("/data-extract/exela/" + date)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateExelaExtract(date);
    }

    @Test
    void shouldInitiateExelaDataExtractForGivenDateRange() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fromDate = dateTimeFormatter.format(LocalDate.now().minusDays(2L));
        String toDate = dateTimeFormatter.format(LocalDate.now().minusDays(1L));

        mockMvc.perform(post("/data-extract/exela/" + fromDate + "/" + toDate)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateExelaExtractDateRange(fromDate, toDate);
    }

    @Test
    void shouldInitiateSmeeAndFordDataExtractForGivenDateRange() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fromDate = dateTimeFormatter.format(LocalDate.now().minusDays(2L));
        String toDate = dateTimeFormatter.format(LocalDate.now().minusDays(1L));

        mockMvc.perform(post("/data-extract/smee-and-ford/" + fromDate + "/" + toDate)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk());

        verify(dataExtractController, times(1)).initiateSmeeAndFordExtractDateRange(fromDate, toDate);
    }


}
