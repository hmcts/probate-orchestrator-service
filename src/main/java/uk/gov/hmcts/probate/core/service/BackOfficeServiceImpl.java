package uk.gov.hmcts.probate.core.service;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.client.backoffice.BackOfficeApi;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCallbackRequest;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCaseDetails;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCaveatResponse;
import uk.gov.hmcts.probate.model.backoffice.GrantScheduleResponse;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.probate.service.BusinessService;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;
import uk.gov.hmcts.reform.probate.model.notification.ApplicationReceivedDetails;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Component
public class BackOfficeServiceImpl implements BackOfficeService {

    private static final String CAVEAT_DATE_FORMAT = "yyyy-MM-dd";

    private final BackOfficeApi backOfficeApi;
    private final BusinessService businessService;
    private final SecurityUtils securityUtils;

    private final Map<CaseType, Function<ProbateCaseDetails, CaseData>> sendNotificationFunctions = ImmutableMap
            .<CaseType, Function<ProbateCaseDetails, CaseData>>builder()
            .put(CaseType.CAVEAT, raiseCaveat())
            .put(CaseType.GRANT_OF_REPRESENTATION, applicationReceived())
            .build();

    @Override
    public CaseData sendNotification(ProbateCaseDetails probateCaseDetails) {
        CaseType caseType = CaseType.getCaseType(probateCaseDetails.getCaseData());
        Function<ProbateCaseDetails, CaseData> sendNotificationFunction = Optional.ofNullable(
                sendNotificationFunctions.get(caseType)
        ).orElseThrow(() -> new IllegalArgumentException("Cannot find notification function for case type: " + caseType));
        return sendNotificationFunction.apply(probateCaseDetails);
    }

    @Override
    public ResponseEntity<String> initiateHmrcExtract(String fromDate, String toDate) {
        securityUtils.setSecurityContextUserAsScheduler();
        log.info("Calling BackOfficeAPI to initiateHmrcExtract as caseworker");
        return backOfficeApi.initiateHmrcExtract(securityUtils.getAuthorisation(), securityUtils.getServiceAuthorisation(),
                fromDate, toDate);
    }

    @Override
    public ResponseEntity<String> initiateIronMountainExtract(String date) {
        securityUtils.setSecurityContextUserAsScheduler();
        log.info("Calling BackOfficeAPI to initiateIronMountainExtract as caseworker");
        return backOfficeApi.initiateIronMountainExtract(securityUtils.getAuthorisation(), securityUtils.getServiceAuthorisation(),
                date);
    }

    @Override
    public ResponseEntity<String> initiateExelaExtract(String date) {
        securityUtils.setSecurityContextUserAsScheduler();
        log.info("Calling BackOfficeAPI to initiateExelaExtract as caseworker");
        return backOfficeApi.initiateExelaExtract(securityUtils.getAuthorisation(), securityUtils.getServiceAuthorisation(),
                date);
    }

    @Override
    public GrantScheduleResponse initiateGrantDelayedNotification(String date) {
        securityUtils.setSecurityContextUserAsScheduler();
        log.info("Calling BackOfficeAPI to initiateGrantDelayedNotification as caseworker");
        return backOfficeApi.initiateGrantDelayedNotification(securityUtils.getAuthorisation(), securityUtils.getServiceAuthorisation(),
                date);
    }

    @Override
    public GrantScheduleResponse initiateGrantAwaitingDocumentsNotification(String date) {
        securityUtils.setSecurityContextUserAsScheduler();
        log.info("Calling BackOfficeAPI to initiateGrantAwaitingDocumentsNotification as caseworker");
        return backOfficeApi.initiateGrantAwaitingDocumentsNotification(securityUtils.getAuthorisation(), securityUtils.getServiceAuthorisation(),
                date);
    }

    private Function<ProbateCaseDetails, CaseData> raiseCaveat() {
        return probateCaseDetails -> {
            BackOfficeCallbackRequest backOfficeCallbackRequest = createBackOfficeCallbackRequest(probateCaseDetails);
            log.info("Sending caveat data to back-office for case id {}", backOfficeCallbackRequest.getCaseDetails().getId());
            BackOfficeCaveatResponse backOfficeCaveatResponse = backOfficeApi.raiseCaveat(
                    securityUtils.getAuthorisation(),
                    securityUtils.getServiceAuthorisation(),
                    backOfficeCallbackRequest);
            CaveatData caveatData = (CaveatData) probateCaseDetails.getCaseData();
            caveatData.setNotificationsGenerated(backOfficeCaveatResponse.getCaseData().getNotificationsGenerated());
            caveatData.setExpiryDate(getFormattedCaveatDate(backOfficeCaveatResponse.getCaseData().getExpiryDate()));
            caveatData.setApplicationSubmittedDate(getFormattedCaveatDate(backOfficeCaveatResponse.getCaseData().getApplicationSubmittedDate()));
            return caveatData;
        };
    }

    private Function<ProbateCaseDetails, CaseData> applicationReceived() {
        return probateCaseDetails -> {
            ApplicationReceivedDetails applicationReceivedDetails = createApplicationReceivedDetails(probateCaseDetails);
            log.info("Sending Application Recieved notifiation rquest to business service for case id {}", probateCaseDetails.getCaseInfo().getCaseId());
            businessService.notifyApplicationReceived(applicationReceivedDetails);
            return probateCaseDetails.getCaseData();
        };
    }

    private ApplicationReceivedDetails createApplicationReceivedDetails(ProbateCaseDetails probateCaseDetails) {
        GrantOfRepresentationData grantOfRepresentationData =(GrantOfRepresentationData)probateCaseDetails.getCaseData();
        return ApplicationReceivedDetails.builder()
                .bilingual(grantOfRepresentationData.getLanguagePreferenceWelsh())
                .deceasedName(grantOfRepresentationData.getDeceasedForenames() + " " +grantOfRepresentationData.getDeceasedSurname())
                .applicantEmail(grantOfRepresentationData.getPrimaryApplicantEmailAddress())
                .applicantName(grantOfRepresentationData.getPrimaryApplicantForenames() + " " +grantOfRepresentationData.getPrimaryApplicantSurname())
                .build();
    }


    private LocalDate getFormattedCaveatDate(String expiryDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(CAVEAT_DATE_FORMAT);
        return LocalDate.parse(expiryDate, dateTimeFormatter);
    }

    private BackOfficeCallbackRequest createBackOfficeCallbackRequest(ProbateCaseDetails probateCaseDetails) {
        return BackOfficeCallbackRequest.builder()
                .caseDetails(BackOfficeCaseDetails.builder()
                        .data(probateCaseDetails.getCaseData())
                        .id(Long.valueOf(probateCaseDetails.getCaseInfo().getCaseId()))
                        .build())
                .build();
    }
}
