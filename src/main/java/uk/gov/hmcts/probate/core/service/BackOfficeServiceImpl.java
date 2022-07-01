package uk.gov.hmcts.probate.core.service;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.probate.client.backoffice.BackOfficeApi;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCallbackRequest;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCaseDetails;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCaveatResponse;
import uk.gov.hmcts.probate.model.backoffice.GrantScheduleResponse;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.reform.probate.model.ProbateDocument;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.CollectionMember;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.cases.caveat.CaveatData;
import uk.gov.hmcts.reform.probate.model.cases.grantofrepresentation.GrantOfRepresentationData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Component
public class BackOfficeServiceImpl implements BackOfficeService {

    private static final String CAVEAT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String BEARER_PREFIX = "Bearer ";

    private final BackOfficeApi backOfficeApi;

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
        ).orElseThrow(
            () -> new IllegalArgumentException("Cannot find notification function for case type: " + caseType));
        return sendNotificationFunction.apply(probateCaseDetails);
    }

    @Override
    public ResponseEntity<String> initiateHmrcExtract(String fromDate, String toDate) {
        securityUtils.setSecurityContextUserAsScheduler();
        log.info("Calling BackOfficeAPI to initiateHmrcExtract as scheduler");
        return backOfficeApi
            .initiateHmrcExtract(securityUtils.getAuthorisation(), securityUtils.getServiceAuthorisation(),
                fromDate, toDate);
    }

    @Override
    public ResponseEntity<String> initiateIronMountainExtract(String date) {
        securityUtils.setSecurityContextUserAsScheduler();
        log.info("Calling BackOfficeAPI to initiateIronMountainExtract as scheduler");
        return backOfficeApi
            .initiateIronMountainExtract(securityUtils.getAuthorisation(), securityUtils.getServiceAuthorisation(),
                date);
    }

    @Override
    public ResponseEntity<String> initiateExelaExtract(String date) {
        securityUtils.setSecurityContextUserAsScheduler();
        log.info("Calling BackOfficeAPI to initiateExelaExtract as scheduler");
        return backOfficeApi
            .initiateExelaExtract(securityUtils.getAuthorisation(), securityUtils.getServiceAuthorisation(),
                date);
    }

    @Override
    public ResponseEntity<String> initiateExelaExtractDateRange(String fromDate, String toDate) {
        securityUtils.setSecurityContextUserAsScheduler();
        log.info("Calling BackOfficeAPI to initiateExelaExtract as scheduler");
        return backOfficeApi
            .initiateExelaExtractDateRange(securityUtils.getAuthorisation(), securityUtils.getServiceAuthorisation(),
                fromDate, toDate);
    }

    @Override
    public ResponseEntity<String> initiateSmeeAndFordExtract(String fromDate, String toDate) {
        securityUtils.setSecurityContextUserAsScheduler();
        log.info("Calling BackOfficeAPI to initiateSmeeAndFordExtract as scheduler");
        return backOfficeApi
            .initiateSmeeAndFordExtractDateRange(securityUtils.getAuthorisation(),
                securityUtils.getServiceAuthorisation(), fromDate, toDate);
    }

    @Override
    public ResponseEntity<String> makeDormant(String fromDate, String toDate) {
        securityUtils.setSecurityContextUserAsScheduler();
        log.info("Calling BackOfficeAPI to makeDormant as scheduler");
        return backOfficeApi
                .makeDormant(securityUtils.getAuthorisation(),
                        securityUtils.getServiceAuthorisation(), fromDate, toDate);
    }

    @Override
    public ResponseEntity<String> reactivateDormant(String fromDate, String toDate) {
        securityUtils.setSecurityContextUserAsScheduler();
        log.info("Calling BackOfficeAPI to Reactivate Dormant as scheduler");
        return backOfficeApi
                .reactivateDormant(securityUtils.getAuthorisation(),
                        securityUtils.getServiceAuthorisation(), fromDate, toDate);
    }

    @Override
    public GrantScheduleResponse initiateGrantDelayedNotification(String date) {
        securityUtils.setSecurityContextUserAsScheduler();
        log.info("Calling BackOfficeAPI to initiateGrantDelayedNotification as scheduler");
        return backOfficeApi.initiateGrantDelayedNotification(BEARER_PREFIX + securityUtils.getAuthorisation(),
            BEARER_PREFIX + securityUtils.getServiceAuthorisation(), date);
    }

    @Override
    public GrantScheduleResponse initiateGrantAwaitingDocumentsNotification(String date) {
        securityUtils.setSecurityContextUserAsScheduler();
        log.info("Calling BackOfficeAPI to initiateGrantAwaitingDocumentsNotification as caseworker");
        return backOfficeApi
            .initiateGrantAwaitingDocumentsNotification(BEARER_PREFIX + securityUtils.getAuthorisation(),
                BEARER_PREFIX + securityUtils.getServiceAuthorisation(), date);
    }

    @Override
    public List<String> uploadDocument(String authorizationToken, List<MultipartFile> files) {
        if (files.isEmpty()) {
            throw new IllegalArgumentException("There needs to be at least one file");
        }
        return backOfficeApi.uploadDocument(authorizationToken, securityUtils.getServiceAuthorisation(),
            files.get(0));
    }

    private Function<ProbateCaseDetails, CaseData> raiseCaveat() {
        return probateCaseDetails -> {
            BackOfficeCallbackRequest backOfficeCallbackRequest = createBackOfficeCallbackRequest(probateCaseDetails);
            log.info("Sending caveat data to back-office for case id {}",
                backOfficeCallbackRequest.getCaseDetails().getId());
            BackOfficeCaveatResponse backOfficeCaveatResponse = backOfficeApi.raiseCaveat(
                securityUtils.getAuthorisation(),
                securityUtils.getServiceAuthorisation(),
                backOfficeCallbackRequest);
            CaveatData caveatData = (CaveatData) probateCaseDetails.getCaseData();
            caveatData.setNotificationsGenerated(backOfficeCaveatResponse.getCaseData().getNotificationsGenerated());
            caveatData.setExpiryDate(getFormattedCaveatDate(backOfficeCaveatResponse.getCaseData().getExpiryDate()));
            caveatData.setApplicationSubmittedDate(
                getFormattedCaveatDate(backOfficeCaveatResponse.getCaseData().getApplicationSubmittedDate()));
            return caveatData;
        };
    }

    private Function<ProbateCaseDetails, CaseData> applicationReceived() {
        return probateCaseDetails -> {
            BackOfficeCallbackRequest backOfficeCallbackRequest = createBackOfficeCallbackRequest(probateCaseDetails);
            log.info("Sending Application Recieved notifiation rquest to back-office for case id {}",
                backOfficeCallbackRequest.getCaseDetails().getId());
            ProbateDocument probateDocument = backOfficeApi.applicationReceived(
                securityUtils.getAuthorisation(),
                securityUtils.getServiceAuthorisation(),
                backOfficeCallbackRequest);
            log.info("Received Back office response for Application Recieved notify request with response {}",
                probateDocument);
            if (probateDocument != null) {
                addProbateNotificationsGenerated(((GrantOfRepresentationData) probateCaseDetails.getCaseData()),
                    probateDocument);
            }
            return probateCaseDetails.getCaseData();
        };
    }

    private void addProbateNotificationsGenerated(GrantOfRepresentationData caseData, ProbateDocument probateDocument) {
        CollectionMember<ProbateDocument> collectionMember = new CollectionMember<>(null, probateDocument);
        if (caseData.getProbateNotificationsGenerated() == null) {
            caseData.setProbateNotificationsGenerated(new ArrayList());
        }
        caseData.getProbateNotificationsGenerated().add(collectionMember);
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
