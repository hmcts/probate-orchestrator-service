package uk.gov.hmcts.probate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.probate.service.DataExtractService;
import uk.gov.hmcts.probate.service.FeatureToggleService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class DataExtractControllerUnitTest {

    @Mock
    DataExtractService dataExtractService;

    @Mock
    FeatureToggleService featureToggleService;


    DataExtractController dataExtractController;

    AutoCloseable closeableMocks;

    @BeforeEach
    void setUp() {
        closeableMocks = MockitoAnnotations.openMocks(this);

        when(featureToggleService.isIronMountainInBackOffice())
                .thenReturn(false);
        when(featureToggleService.isExelaInBackOffice())
                .thenReturn(false);

        dataExtractController = new DataExtractController(
                dataExtractService,
                featureToggleService);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeableMocks.close();
    }

    @Test
    public void shouldInitiateSmeeAndFordDataExtractForDateRange() {
        ResponseEntity responseEntity = Mockito.mock(ResponseEntity.class);
        when(dataExtractService.initiateSmeeAndFordExtractDateRange(any(), anyString())).thenReturn(responseEntity);
        ResponseEntity response = dataExtractController.initiateSmeeAndFordExtractDateRange("2020-12-30", "2020-12-31");
        assertThat(response).isEqualTo(responseEntity);
    }

    @Test
    void shouldInitiateHmrcExtractForDateRange() {
        ResponseEntity responseEntity = Mockito.mock(ResponseEntity.class);
        when(dataExtractService.initiateHmrcExtract(anyString(), anyString())).thenReturn(responseEntity);
        ResponseEntity response = dataExtractController.initiateHmrcExtractFromToDate("2020-12-30", "2020-12-31");
        assertThat(response).isEqualTo(responseEntity);
    }

    @Test
    void shouldInitiateIronMountainExtract() {
        ResponseEntity responseEntity = Mockito.mock(ResponseEntity.class);
        when(dataExtractService.initiateIronMountainExtract(anyString())).thenReturn(responseEntity);
        ResponseEntity response = dataExtractController.initiateIronMountainExtract("2020-12-30");
        assertThat(response).isEqualTo(responseEntity);
    }

    @Test
    void shouldInitiateExcelaExtractForDateRange() {
        ResponseEntity responseEntity = Mockito.mock(ResponseEntity.class);
        when(dataExtractService.initiateExelaExtractDateRange(anyString(), anyString())).thenReturn(responseEntity);
        ResponseEntity response = dataExtractController.initiateExelaExtractDateRange("2020-12-30", "2020-12-31");
        assertThat(response).isEqualTo(responseEntity);
    }

    @Test
    void shouldNotInitiateIronMountainWhenInBackOffice() {
        when(featureToggleService.isIronMountainInBackOffice())
                .thenReturn(true);

        ResponseEntity responseEntity = dataExtractController.initiateIronMountainExtract();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(500));
        assertThat(responseEntity.getBody()).isEqualTo("Iron Mountain extract task is run through back office");
        verifyNoInteractions(dataExtractService);
    }

    @Test
    void shouldNotInitiateExelaWhenInBackOffice() {
        when(featureToggleService.isExelaInBackOffice())
                .thenReturn(true);

        ResponseEntity responseEntity = dataExtractController.initiateExelaExtract();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(500));
        assertThat(responseEntity.getBody()).isEqualTo("Exela extract task is run through back office");
        verifyNoInteractions(dataExtractService);
    }
}
