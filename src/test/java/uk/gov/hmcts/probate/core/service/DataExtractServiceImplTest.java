package uk.gov.hmcts.probate.core.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class DataExtractServiceImplTest {

    @InjectMocks
    private DataExtractServiceImpl dataExtractService;

    @Mock
    private DataExtractDateValidator dataExtractDateValidator;

    @Mock
    private BackOfficeService backOfficeService;

    @Mock
    ResponseEntity<String> responseEntity;

    private final static String FROM_DATE = "fromDate";
    private final static String TO_DATE = "toDate";

    @Test
    public void shouldInitiateHmrcExtract() {

        ResponseEntity<String> responseEntity = dataExtractService.initiateHmrcExtract(FROM_DATE, TO_DATE);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.ACCEPTED));
        assertThat(responseEntity.getBody(), equalTo("Perform HMRC data extract finished"));
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowDateExceptionOnInitiateHmrcExtract() {

        doThrow(ApiClientException.class).when(dataExtractDateValidator).validate(FROM_DATE, TO_DATE);
        dataExtractService.initiateHmrcExtract(FROM_DATE, TO_DATE);
    }

    @Test
    public void shouldInitiateIronMountainExtract() {

        ResponseEntity<String> responseEntity = dataExtractService.initiateIronMountainExtract(FROM_DATE);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.ACCEPTED));
        assertThat(responseEntity.getBody(), equalTo("Perform Iron Mountain data extract finished"));
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowDateExceptionOnInitiateIronMountainExtract() {

        doThrow(ApiClientException.class).when(dataExtractDateValidator).validate(FROM_DATE);
        dataExtractService.initiateIronMountainExtract(FROM_DATE);

    }

    @Test
    public void shouldInitiateExelaExtract() {

        ResponseEntity<String> responseEntity = dataExtractService.initiateExelaExtract(FROM_DATE);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.ACCEPTED));
        assertThat(responseEntity.getBody(), equalTo("Perform Exela data extract finished"));
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowDateExceptionOnInitiateExelaExtract() {

        doThrow(ApiClientException.class).when(dataExtractDateValidator).validate(FROM_DATE);
        dataExtractService.initiateExelaExtract(FROM_DATE);
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowDateExceptionOnInitiateExelaExtractDateRange() {

        doThrow(ApiClientException.class).when(dataExtractDateValidator).validate(FROM_DATE, TO_DATE);
        dataExtractService.initiateExelaExtractDateRange(FROM_DATE, TO_DATE);
    }
}