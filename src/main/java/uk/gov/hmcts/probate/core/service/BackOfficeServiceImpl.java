package uk.gov.hmcts.probate.core.service;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.client.backoffice.BackOfficeApi;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCallbackRequest;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCaseDetails;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class BackOfficeServiceImpl implements BackOfficeService {

    private final BackOfficeApi backOfficeApi;

    private final SecurityUtils securityUtils;

    private final Map<CaseType, Consumer<ProbateCaseDetails>> sendNotificationFunctions = ImmutableMap
        .<CaseType, Consumer<ProbateCaseDetails>>builder()
        .put(CaseType.CAVEAT, raiseCaveat())
        .build();

    @Override
    public void sendNotification(ProbateCaseDetails probateCaseDetails) {
        CaseType caseType = CaseType.getCaseType(probateCaseDetails.getCaseData());
        Consumer<ProbateCaseDetails> caveatDataConsumer = Optional.ofNullable(
            sendNotificationFunctions.get(caseType)
        ).orElseThrow(() -> new IllegalArgumentException("Cannot find notification function for case type: " + caseType));
        caveatDataConsumer.accept(probateCaseDetails);
    }

    private Consumer<ProbateCaseDetails> raiseCaveat() {
        return probateCaseDetails -> {
            BackOfficeCallbackRequest backOfficeCallbackRequest = createBackOfficeCallbackRequest(probateCaseDetails);
            log.info("Sending caveat data to back-office for case id {}", backOfficeCallbackRequest.getCaseDetails().getId());
            backOfficeApi.raiseCaveat(
                securityUtils.getAuthorisation(),
                securityUtils.getServiceAuthorisation(),
                backOfficeCallbackRequest);
        };
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
