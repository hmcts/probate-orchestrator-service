package uk.gov.hmcts.probate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.probate.core.service.GrantAwaitingDocumentsNotifier;
import uk.gov.hmcts.probate.core.service.GrantDelayedNotifier;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GrantControllerIT {

    @MockitoBean
    private GrantDelayedNotifier grantDelayedNotifier;
    @MockitoBean
    private GrantAwaitingDocumentsNotifier grantAwaitingDocumentsNotifier;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldInitiateGrantDelayedFromSchedule() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String expiryDate = dateTimeFormatter.format(LocalDate.now().minusDays(1L));

        mockMvc.perform(post("/grant/delay-notification")
            .content(expiryDate)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk())
            .andExpect(content().string("Perform grant delayed notification called"));
    }

    @Test
    public void shouldInitiateGrantAwaitingDocsFromSchedule() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String expiryDate = dateTimeFormatter.format(LocalDate.now().minusDays(1L));

        mockMvc.perform(post("/grant/awaiting-documents-notification")
            .content(expiryDate)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
            .andExpect(status().isOk())
            .andExpect(content().string("Perform grant Awaiting Documents notification called"));
    }

}
