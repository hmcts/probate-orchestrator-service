package uk.gov.hmcts.probate.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.dto.CaseInfoDTO;
import uk.gov.hmcts.probate.dto.ccd.data.DataDTO;
import uk.gov.hmcts.probate.client.mapper.CaseInfoMapper;
import uk.gov.hmcts.probate.service.CcdSubmissionService;
import uk.gov.hmcts.reform.auth.checker.spring.serviceanduser.ServiceAndUserDetails;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.Event;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;

@Component
public class CcdSubmissionClient implements CcdSubmissionService {

    private static final String APPLY_FOR_GRANT_CCD_EVENT_ID = "applyForGrant";
    private static final String JURISDICTION_ID = "PROBATE";
    private static final String CASE_TYPE = "GrantOfRepresentation";

    private final CoreCaseDataApi coreCaseDataApi;

    private final AuthTokenGenerator authTokenGenerator;

    private final CaseInfoMapper caseInfoMapper;

    @Autowired
    public CcdSubmissionClient(CoreCaseDataApi coreCaseDataApi, AuthTokenGenerator authTokenGenerator,
                               CaseInfoMapper caseInfoMapper) {
        this.coreCaseDataApi = coreCaseDataApi;
        this.authTokenGenerator = authTokenGenerator;
        this.caseInfoMapper = caseInfoMapper;
    }

    @Override
    public CaseInfoDTO submit(DataDTO dataDTO) {
        StartEventResponse startEventResponse = coreCaseDataApi.startForCitizen(getUserToken(), generateServiceToken(),
                getUserId(), JURISDICTION_ID, CASE_TYPE, APPLY_FOR_GRANT_CCD_EVENT_ID);
        CaseDataContent caseDataContent = createCaseDataContent(dataDTO, startEventResponse);
        CaseDetails caseDetails = coreCaseDataApi.submitForCitizen(getUserToken(), generateServiceToken(), getUserId(),
                JURISDICTION_ID, CASE_TYPE, true, caseDataContent);
        return caseInfoMapper.mapFormCaseInfoDTO(caseDetails);
    }

    private CaseDataContent createCaseDataContent(DataDTO dataDTO, StartEventResponse startEventResponse) {
        return CaseDataContent.builder()
                .event(createEvent())
                .eventToken(startEventResponse.getToken())
                .data(dataDTO)
                .build();
    }

    private Event createEvent() {
        return Event.builder()
                .id(APPLY_FOR_GRANT_CCD_EVENT_ID)
                .description("Probate application")
                .summary("Probate application")
                .build();
    }

    private String generateServiceToken() {
        return authTokenGenerator.generate();
    }

    public static String getUserToken() {
        return (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getCredentials();
    }

    public static String getUserId() {
        return ((ServiceAndUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();
    }
}
