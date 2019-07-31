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
                                .id("id")
                                .mainExecutorName("mainExecutorName")
                                .build())
                ).build()
        ).pageMetadata(new PagedResources.PageMetadata(20, 1, 1, 1))
                .build();


        when(securityUtilsMock.getAuthorisation()).thenReturn(AUTH_TOKEN);
        when(securityUtilsMock.getServiceAuthorisation()).thenReturn(SERVICE_AUTH_TOKEN);

        when(persistenceServiceApiMock.getInviteDatas()).thenReturn(inviteDataResource);
        when(persistenceServiceApiMock.getInviteDataWithPageAndSize("1", "20"))
                .thenReturn(inviteDataResource);

        when(gopMock.getPrimaryApplicantEmailAddress()).thenReturn(FORMDATA_ID);

        ProbateCaseDetails probateCaseDetails = ProbateCaseDetails.builder()
                .caseInfo(CaseInfo.builder().state(CaseState.DRAFT)
                        .build())
                .caseData(gopMock)
                .build();

        when(submitServiceApiMock.getCase(AUTH_TOKEN, SERVICE_AUTH_TOKEN,
                FORMDATA_ID, ProbateType.PA.getCaseType().getName()))
                .thenReturn(probateCaseDetails);


        inviteDataMigrator.migrateInviteData();

        verify(persistenceServiceApiMock).getInviteDatas();
        verify(persistenceServiceApiMock).getInviteDataWithPageAndSize("1", "20");

        verify(submitServiceApiMock).getCase(AUTH_TOKEN, SERVICE_AUTH_TOKEN, FORMDATA_ID,
                ProbateType.PA.getCaseType().getName());

        verify(gopMock).setInvitationDetailsForExecutorApplying(EMAIL, "id", Boolean.TRUE);

        verify(submitServiceApiMock).saveCase(AUTH_TOKEN, SERVICE_AUTH_TOKEN, FORMDATA_ID,
                probateCaseDetails);
    }
}