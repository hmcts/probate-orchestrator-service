package uk.gov.hmcts.probate.core.service;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.client.backoffice.BackOfficeApi;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCallbackRequest;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCallbackResponse;
import uk.gov.hmcts.probate.model.backoffice.BackOfficeCaseDetails;
import uk.gov.hmcts.probate.service.BackOfficeService;
import uk.gov.hmcts.reform.probate.model.cases.CaseData;
import uk.gov.hmcts.reform.probate.model.cases.CaseType;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Component
public class BackOfficeServiceImpl implements BackOfficeService {

    private final BackOfficeApi backOfficeApi;

    private final SecurityUtils securityUtils;

    private final Map<CaseType, Function<CaseData, CaseData>> sendNotificationFunctions = ImmutableMap
        .<CaseType, Function<CaseData, CaseData>>builder()
        .put(CaseType.CAVEAT, raiseCaveat())
        .build();

    @Override
    public CaseData sendNotification(CaseData caseData) {
        CaseType caseType = CaseType.getCaseType(caseData);
        Function<CaseData, CaseData> sendNotificationFunction = Optional.ofNullable(
            sendNotificationFunctions.get(caseType)
        ).orElseThrow(() -> new IllegalArgumentException("Cannot find notification function for case type: " + caseType));
        return sendNotificationFunction.apply(caseData);
    }

    private Function<CaseData, CaseData> raiseCaveat() {
        return caseData -> {
            BackOfficeCallbackRequest backOfficeCallbackRequest = createBackOfficeCallbackRequest(caseData);
            log.info("Sending caveat data to back-office for case id {}", backOfficeCallbackRequest.getCaseDetails().getId());
            BackOfficeCallbackResponse backOfficeCallbackResponse = backOfficeApi.raiseCaveat(
                securityUtils.getAuthorisation(),
                securityUtils.getServiceAuthorisation(),
                backOfficeCallbackRequest);
            return backOfficeCallbackResponse.getCaseData();
        };
    }

    private BackOfficeCallbackRequest createBackOfficeCallbackRequest(CaseData caseData) {
        return BackOfficeCallbackRequest.builder()
            .caseDetails(BackOfficeCaseDetails.builder()
                .data(caseData)
                .build())
            .build();
    }
}
