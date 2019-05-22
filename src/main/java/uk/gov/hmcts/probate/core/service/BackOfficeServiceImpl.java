package uk.gov.hmcts.probate.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final Map<CaseType, Consumer<BackOfficeCallbackRequest>> sendNotificationFunctions = ImmutableMap
        .<CaseType, Consumer<BackOfficeCallbackRequest>>builder()
        .put(CaseType.CAVEAT, raiseCaveat())
        .build();

    @Override
    public void sendNotification(ProbateCaseDetails probateCaseDetails) {
        Optional<Consumer<BackOfficeCallbackRequest>> caveatDataConsumer = Optional.ofNullable(
            sendNotificationFunctions.get(CaseType.getCaseType(probateCaseDetails.getCaseData()))
        );

        if (caveatDataConsumer.isPresent()) {
            BackOfficeCallbackRequest backOfficeCallbackRequest = BackOfficeCallbackRequest.builder()
                .caseDetails(BackOfficeCaseDetails.builder()
                    .data(probateCaseDetails.getCaseData())
                    .id(Long.valueOf(probateCaseDetails.getCaseInfo().getCaseId()))
                    .build())
                .build();

            caveatDataConsumer.get().accept(backOfficeCallbackRequest);
        }
    }

    private Consumer<BackOfficeCallbackRequest> raiseCaveat() {
        return backOfficeCallbackRequest -> {
            log.info("Sending caveat data to back-office for case id {}", backOfficeCallbackRequest.getCaseDetails().getId());
            backOfficeApi.raiseCaveat(
                securityUtils.getAuthorisation(),
                securityUtils.getServiceAuthorisation(),
                backOfficeCallbackRequest);
        };
    }
}
