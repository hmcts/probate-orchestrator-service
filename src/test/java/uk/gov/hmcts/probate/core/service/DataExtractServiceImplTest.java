package uk.gov.hmcts.probate.core.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class DataExtractServiceImplTest {

    @InjectMocks
    private DataExtractServiceImpl dataExtractService;

    @Mock
    private DataExtractDateValidator dataExtractDateValidator;

    @Mock
    private BackOfficeServiceImpl backOfficeService;

    @Mock
    ResponseEntity<String> responseEntity;

    private static final String FROM_DATE = "fromDate";
    private static final String TO_DATE = "toDate";

    @Test
    void shouldInitiateHmrcExtract() {

        ResponseEntity<String> responseEntity = dataExtractService.initiateHmrcExtract(FROM_DATE, TO_DATE);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.ACCEPTED));
        assertThat(responseEntity.getBody(), equalTo("Perform HMRC data extract finished"));
    }

    @Test
    void shouldThrowDateExceptionOnInitiateHmrcExtract() {

        doThrow(ApiClientException.class).when(dataExtractDateValidator).validate(FROM_DATE, TO_DATE);
        assertThrows(ApiClientException.class, () -> {
            dataExtractService.initiateHmrcExtract(FROM_DATE, TO_DATE);
        });
    }

    @Test
    void shouldInitiateIronMountainExtract() {

        ResponseEntity<String> responseEntity = dataExtractService.initiateIronMountainExtract(FROM_DATE);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.ACCEPTED));
        assertThat(responseEntity.getBody(), equalTo("Perform Iron Mountain data extract finished"));
    }

    @Test
    void shouldThrowDateExceptionOnInitiateIronMountainExtract() {

        doThrow(ApiClientException.class).when(dataExtractDateValidator).validate(FROM_DATE);
        assertThrows(ApiClientException.class, () -> {
            dataExtractService.initiateIronMountainExtract(FROM_DATE);
        });

    }

    @Test
    void shouldInitiateExelaExtract() {

        ResponseEntity<String> responseEntity = dataExtractService.initiateExelaExtract(FROM_DATE);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.ACCEPTED));
        assertThat(responseEntity.getBody(), equalTo("Perform Exela data extract finished"));
    }

    @Test
    void shouldThrowDateExceptionOnInitiateExelaExtract() {

        doThrow(ApiClientException.class).when(dataExtractDateValidator).validate(FROM_DATE);
        assertThrows(ApiClientException.class, () -> {
            dataExtractService.initiateExelaExtract(FROM_DATE);
        });
    }

    @Test
    void shouldThrowDateExceptionOnInitiateExelaExtractDateRange() {

        doThrow(ApiClientException.class).when(dataExtractDateValidator).validate(FROM_DATE, TO_DATE);
        assertThrows(ApiClientException.class, () -> {
            dataExtractService.initiateExelaExtractDateRange(FROM_DATE, TO_DATE);
        });
    }

    @Test
    void shouldInitiateExelaExtractDateRange() {

        ResponseEntity<String> responseEntity = dataExtractService.initiateExelaExtractDateRange(FROM_DATE, TO_DATE);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.ACCEPTED));
        assertThat(responseEntity.getBody(), equalTo("Perform Exela data extract finished"));
    }

    @Test
    void shouldInitiateSmeeAndFordExtractDateRange() {

        ResponseEntity<String> responseEntity = dataExtractService
            .initiateSmeeAndFordExtractDateRange(FROM_DATE, TO_DATE);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.ACCEPTED));
        assertThat(responseEntity.getBody(), equalTo("Perform Smee And Ford data extract finished"));
    }

    @Test
    void shouldThrowDateExceptionOnInitiateSmeeAndFordExtractDateRange() {
        doThrow(ApiClientException.class).when(dataExtractDateValidator).validate(FROM_DATE, TO_DATE);
        assertThrows(ApiClientException.class, () -> {
            dataExtractService.initiateSmeeAndFordExtractDateRange(FROM_DATE, TO_DATE);
        });

    }

}
