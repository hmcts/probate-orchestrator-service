package uk.gov.hmcts.probate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.probate.service.DataExtractService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class DataExtractControllerUnitTest {

    @Mock
    DataExtractService dataExtractService;

    @InjectMocks
    DataExtractController dataExtractController;

    @Test
    public void shouldInitiateSmeeAndFordDataExtractForNoDate() {
        ResponseEntity responseEntity = Mockito.mock(ResponseEntity.class);
        when(dataExtractService.initiateSmeeAndFordExtractDateRange(any(), anyString())).thenReturn(responseEntity);
        ResponseEntity response = dataExtractController.initiateSmeeAndFordExtract();
        assertThat(response).isEqualTo(responseEntity);
    }

    @Test
    public void shouldInitiateSmeeAndFordDataExtractForDateRange() {
        ResponseEntity responseEntity = Mockito.mock(ResponseEntity.class);
        when(dataExtractService.initiateSmeeAndFordExtractDateRange(any(), anyString())).thenReturn(responseEntity);
        ResponseEntity response = dataExtractController.initiateSmeeAndFordExtractDateRange("2020-12-30", "2020-12-31");
        assertThat(response).isEqualTo(responseEntity);
    }
}
