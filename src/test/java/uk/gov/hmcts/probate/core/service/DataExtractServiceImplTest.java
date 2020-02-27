package uk.gov.hmcts.probate.core.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataExtractServiceImplTest {
    private DataExtractServiceImpl dataExtractService;

    @Mock
    private DataExtractDateValidator dataExtractDateValidator;

    @Mock
    private BackOfficeService backOfficeService;

    @Mock
    ResponseEntity<String> responseEntity;

    private final static String FROM_DATE = "fromDate";
    private final static String TO_DATE = "toDate";

    @Before
    public void setup() {
        dataExtractService = new DataExtractServiceImpl(dataExtractDateValidator, backOfficeService);
    }

    @Test
    public void shouldInitiateHmrcExtract() {

        when(backOfficeService.initiateHmrcExtract(FROM_DATE, TO_DATE)).thenReturn(responseEntity);
        dataExtractService.initiateHmrcExtract(FROM_DATE, TO_DATE);
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowDateExceptionOnInitiateHmrcExtract() {

        doThrow(ApiClientException.class).when(dataExtractDateValidator).dateValidator(FROM_DATE, TO_DATE);
        dataExtractService.initiateHmrcExtract(FROM_DATE, TO_DATE);
    }

    @Test
    public void shouldInitiateIronMountainExtract() {

        when(backOfficeService.initiateIronMountainExtract(FROM_DATE)).thenReturn(responseEntity);
        dataExtractService.initiateIronMountainExtract(FROM_DATE);
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowDateExceptionOnInitiateIronMountainExtract() {

        doThrow(ApiClientException.class).when(dataExtractDateValidator).dateValidator(FROM_DATE);
        dataExtractService.initiateIronMountainExtract(FROM_DATE);
    }

    @Test
    public void shouldInitiateExelaExtract() {

        when(backOfficeService.initiateExelaExtract(FROM_DATE)).thenReturn(responseEntity);
        dataExtractService.initiateExelaExtract(FROM_DATE);
    }

    @Test(expected = ApiClientException.class)
    public void shouldThrowDateExceptionOnInitiateExelaExtract() {

        doThrow(ApiClientException.class).when(dataExtractDateValidator).dateValidator(FROM_DATE);
        dataExtractService.initiateExelaExtract(FROM_DATE);
    }
}