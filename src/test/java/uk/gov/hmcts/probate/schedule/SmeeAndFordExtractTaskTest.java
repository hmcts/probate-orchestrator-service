package uk.gov.hmcts.probate.schedule;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.probate.core.service.DataExtractServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class SmeeAndFordExtractTaskTest {

    @Mock
    private DataExtractServiceImpl dataExtractService;

    @InjectMocks
    private SmeeAndFordExtractTask smeeAndFordExtractTask;

    private static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    public void shouldInitiateSmeeAndFordExtractDateRange() {

        String date = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
        ResponseEntity<String> responseEntity = ResponseEntity.accepted()
                .body("Perform Smee And Ford data extract finished");
        when(dataExtractService.initiateSmeeAndFordExtractDateRange(date, date)).thenReturn(responseEntity);
        smeeAndFordExtractTask.run();
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals("Perform Smee And Ford data extract finished", responseEntity.getBody());
        verify(dataExtractService).initiateSmeeAndFordExtractDateRange(date,date);
    }
}
