package uk.gov.hmcts.probate.core.service.migration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.hateoas.PagedResources;
import uk.gov.hmcts.probate.client.persistence.PersistenceServiceApi;
import uk.gov.hmcts.probate.client.submit.SubmitServiceApi;
import uk.gov.hmcts.probate.core.service.SecurityUtils;
import uk.gov.hmcts.probate.model.persistence.InviteDataContent;
import uk.gov.hmcts.probate.model.persistence.InviteDataResource;
import uk.gov.hmcts.reform.probate.model.ProbateType;
import uk.gov.hmcts.reform.probate.model.cases.CaseInfo;
import uk.gov.hmcts.reform.probate.model.cases.CaseState;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.ExecutorApplying;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.client.ErrorResponse;
import uk.gov.hmcts.reform.probate.model.multiapplicant.InviteData;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class InviteDataMigratorTest {

    public static final String AUTH_TOKEN = "authToken";
    public static final String SERVICE_AUTH_TOKEN = "serviceAuthToken";
    public static final String INTESTACY_EMAIL = "intestacyEmail";
    public static final String PA_EMAIL = "paEmail";
    public static final String FORMDATA_ID = "formdataId";
    public static final String EMAIL = "email";
    public static final String INVITE_ID = "inviteId";

    @InjectMocks
    InviteDataMigrator inviteDataMigrator;

    @Mock
    private PersistenceServiceApi persistenceServiceApiMock;

    @Mock
    private SubmitServiceApi submitServiceApiMock;

    @Mock
    private SecurityUtils securityUtilsMock;

    @Mock
    private ErrorResponse errorResponseMock;

    @Mock
    GrantOfRepresentationData gopMock;

    LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);


    @Test
    public void shouldMigrateInviteData() {

        InviteDataResource inviteDataResource = InviteDataResource.builder().content(
                InviteDataContent.builder().invitedata(
                        Arrays.asList(InviteData.builder().agreed(Boolean.TRUE)
                                .email(EMAIL)
                                .formdataId(FORMDATA_ID)
                                .id(INVITE_ID)
                                .mainExecutorName("mainExecutorName")
                                .build())
                ).build()
        ).pageMetadata(new PagedResources.PageMetadata(20, 0, 1, 1))
                .build();

        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);

        when(securityUtilsMock.getAuthorisation()).thenReturn(AUTH_TOKEN);
        when(securityUtilsMock.getServiceAuthorisation()).thenReturn(SERVICE_AUTH_TOKEN);

        when(persistenceServiceApiMock.getInviteDataByAfterCreateDate(sixMonthsAgo)).thenReturn(inviteDataResource);
        when(persistenceServiceApiMock.getInviteDataByAfterCreateDate(sixMonthsAgo, "0", "20"))
                .thenReturn(inviteDataResource);

        ProbateCaseDetails probateCaseDetails = ProbateCaseDetails.builder()
                .caseInfo(CaseInfo.builder().state(CaseState.DRAFT).caseId(FORMDATA_ID)
                        .build())
                .caseData(gopMock)
                .build();

        when(submitServiceApiMock.getCaseByApplicantEmail(AUTH_TOKEN, SERVICE_AUTH_TOKEN,
                FORMDATA_ID, ProbateType.PA.getCaseType().name()))
                .thenReturn(probateCaseDetails);


        when(gopMock.getExecutorApplyingByInviteId(INVITE_ID))
                .thenReturn(new ExecutorApplying());

        inviteDataMigrator.migrateInviteData();

        verify(persistenceServiceApiMock).getInviteDataByAfterCreateDate(sixMonthsAgo);
        verify(persistenceServiceApiMock).getInviteDataByAfterCreateDate(sixMonthsAgo, "0", "20");

        verify(submitServiceApiMock).getCaseByApplicantEmail(AUTH_TOKEN, SERVICE_AUTH_TOKEN, FORMDATA_ID,
                ProbateType.PA.getCaseType().name());

        verify(gopMock).getExecutorApplyingByInviteId(INVITE_ID);

        verify(submitServiceApiMock).updateCaseAsCaseWorker(AUTH_TOKEN, SERVICE_AUTH_TOKEN, FORMDATA_ID,
                probateCaseDetails);
    }
}