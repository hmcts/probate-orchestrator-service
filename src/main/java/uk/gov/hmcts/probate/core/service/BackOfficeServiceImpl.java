package uk.gov.hmcts.probate.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.client.backoffice.BackOfficeApi;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
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

    private final ObjectMapper objectMapper;

    private final Map<CaseType, Consumer<CaseDetails>> sendNotificationFunctions = ImmutableMap
        .<CaseType, Consumer<CaseDetails>>builder()
        .put(CaseType.CAVEAT, raiseCaveat())
        .build();

    @Override
    public void sendNotification(ProbateCaseDetails probateCaseDetails) {
        Optional<Consumer<CaseDetails>> caveatDataConsumer = Optional.ofNullable(
            sendNotificationFunctions.get(CaseType.getCaseType(probateCaseDetails.getCaseData()))
        );

        if (caveatDataConsumer.isPresent()) {
            CaseDetails caseDetails = CaseDetails.builder()
                .id(Long.valueOf(probateCaseDetails.getCaseInfo().getCaseId()))
                .data(objectMapper.convertValue(probateCaseDetails.getCaseData(), Map.class))
                .build();
            caveatDataConsumer.get().accept(caseDetails);
        }
    }

    private Consumer<CaseDetails> raiseCaveat() {
        return caseDetails -> {
            log.info("Sending caveat data to back-office for case id {}", caseDetails.getId());
            backOfficeApi.raiseCaveat(
                securityUtils.getAuthorisation(),
                securityUtils.getServiceAuthorisation(),
                caseDetails);
        };
    }
}
