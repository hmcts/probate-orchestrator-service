package uk.gov.hmcts.probate.core.service.migration;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.probate.client.persistence.PersistenceServiceApi;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.probate.core.service.mapper.migration.LegacyIntestacyMapper;
import uk.gov.hmcts.probate.core.service.mapper.migration.LegacyPaMapper;
import uk.gov.hmcts.probate.model.persistence.FormDataContent;
import uk.gov.hmcts.probate.model.persistence.FormDataResource;
import uk.gov.hmcts.probate.model.persistence.FormHolder;
import uk.gov.hmcts.probate.model.persistence.LegacyForm;
import uk.gov.hmcts.probate.model.persistence.LegacyProbateType;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseInfo;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.client.ApiClientException;
import uk.gov.hmcts.reform.probate.model.client.ErrorResponse;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class FormDataMigratorTest {

    public static final String AUTH_TOKEN = "authToken";
    public static final String SERVICE_AUTH_TOKEN = "serviceAuthToken";
    public static final String INTESTACY_EMAIL = "intestacyEmail@email.com";
    public static final String PA_EMAIL = "paEmail@email.com";
    @InjectMocks
    FormDataMigrator formDataMigrator;
    @Mock
    private PersistenceServiceApi persistenceServiceApiMock;
    @Mock
    private SubmitServiceApi submitServiceApiMock;
    @Mock
    private SecurityUtils securityUtilsMock;
    @Mock
    private LegacyPaMapper legacyPaMapperMock;
    @Mock
    private LegacyIntestacyMapper legacyIntestacyMapperMock;
    @Mock
    private LegacyForm legacyFormPaMock;
    @Mock
    private LegacyForm legacyFormIntestacyMock;
    @Mock
    private ErrorResponse errorResponseMock;
    @Mock
    private IdamUsersCsvLoader mockIdamUsersCsvLoader;


    LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);


    @Test
    public void shouldMigrateFormData() {

        FormDataResource formDataResource = FormDataResource.builder().content(
                FormDataContent.builder().formdata(
                        Arrays.asList(FormHolder.builder().formdata(legacyFormPaMock)
                                .build(), FormHolder.builder().formdata(legacyFormIntestacyMock).build())
                ).build()
        ).pageMetadata(new PagedResources.PageMetadata(20, 0, 2, 1))
                .build();

        when(legacyFormPaMock.getCaseType()).thenReturn(LegacyProbateType.PROBATE_LEGACY);
        when(legacyFormIntestacyMock.getCaseType()).thenReturn(LegacyProbateType.INTESTACY_LEGACY);

        when(persistenceServiceApiMock.getFormDataByAfterCreateDate(sixMonthsAgo)).thenReturn(formDataResource);
        when(persistenceServiceApiMock.getPagedFormDataByAfterCreateDate(sixMonthsAgo, "0", "20"))
                .thenReturn(formDataResource);

        GrantOfRepresentationData intestacy_gop = GrantOfRepresentationData.builder().build();
        GrantOfRepresentationData pa_gop = GrantOfRepresentationData.builder().build();
        when(legacyIntestacyMapperMock.toCaseData(legacyFormIntestacyMock)).thenReturn(intestacy_gop);
        when(legacyPaMapperMock.toCaseData(legacyFormPaMock)).thenReturn(pa_gop);

        when(securityUtilsMock.getAuthorisation()).thenReturn(AUTH_TOKEN);
        when(securityUtilsMock.getServiceAuthorisation()).thenReturn(SERVICE_AUTH_TOKEN);

        when(legacyFormPaMock.getApplicantEmail()).thenReturn(INTESTACY_EMAIL);
        when(legacyFormIntestacyMock.getApplicantEmail()).thenReturn(PA_EMAIL);

        ProbateCaseDetails intestacyPcd = ProbateCaseDetails.builder().caseInfo(CaseInfo.builder().caseId("1232342342").build()).build();
        when(submitServiceApiMock.getCaseByApplicantEmail(AUTH_TOKEN, SERVICE_AUTH_TOKEN,
                INTESTACY_EMAIL, ProbateType.INTESTACY.getCaseType().name()))
                .thenReturn(intestacyPcd);

        when(submitServiceApiMock.getCaseByApplicantEmail(AUTH_TOKEN, SERVICE_AUTH_TOKEN,
                PA_EMAIL, ProbateType.PA.getCaseType().name()))
                .thenThrow(new ApiClientException(HttpStatus.NOT_FOUND.value(), errorResponseMock));

        when(submitServiceApiMock.initiateCaseAsCaseWorker(anyString(), anyString(),
                any(ProbateCaseDetails.class))).thenReturn(intestacyPcd);

        when(mockIdamUsersCsvLoader.loadIdamUserList("idam_ids.csv")).thenReturn(Arrays.asList(new IdamUserEmail(PA_EMAIL, "idamApi")));

        formDataMigrator.migrateFormData();

        verify(securityUtilsMock).setSecurityContextUserAsCaseworker();
        verify(persistenceServiceApiMock).getFormDataByAfterCreateDate(sixMonthsAgo);
        verify(persistenceServiceApiMock).getPagedFormDataByAfterCreateDate(sixMonthsAgo, "0", "20");

        verify(legacyPaMapperMock).toCaseData(legacyFormPaMock);
        verify(legacyIntestacyMapperMock).toCaseData(legacyFormIntestacyMock);

        verify(submitServiceApiMock).getCaseByApplicantEmail(AUTH_TOKEN, SERVICE_AUTH_TOKEN, INTESTACY_EMAIL,
                ProbateType.INTESTACY.getCaseType().name());
        verify(submitServiceApiMock).getCaseByApplicantEmail(AUTH_TOKEN, SERVICE_AUTH_TOKEN, PA_EMAIL,
                ProbateType.PA.getCaseType().name());
        verify(submitServiceApiMock).initiateCaseAsCaseWorker(anyString(), anyString(),
                any(ProbateCaseDetails.class));
    }
}

