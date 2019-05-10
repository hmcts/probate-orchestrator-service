package uk.gov.hmcts.probate.core.service.fees;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.client.fees.FeesApi;
import uk.gov.hmcts.probate.model.fees.FeeLookupResponseDto;
import uk.gov.hmcts.probate.utils.NumberUtils;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "fees.version", havingValue = "v1")
public class FeesLookupV1Retriever implements FeesLookupRetriever {

    @Autowired
    private final FeesLookupConfiguration feesLookupConfig;

    @Value("${fees.ihtMinAmount}")
    private Long ihtMinAmount;

    private final FeesApi feesApi;

    @Override
    public FeesCalculation lookupFeeForApplicationFee(BigDecimal amount, String authorization, String serviceAuthorization) {
        if (amount.compareTo(new BigDecimal(ihtMinAmount)) <= 0) {
            return FeesCalculation.builder()
                .amount(BigDecimal.ZERO)
                .build();
        }
        FeeLookupResponseDto feeLookupResponseDto = getFeeLookupResponseDto(amount, authorization, serviceAuthorization, feesLookupConfig.getEventIssue());
        log.info("Fee look response: {}", feeLookupResponseDto);
        BigDecimal feeAmount = NumberUtils.defaultToZero(feeLookupResponseDto.getFeeAmount());
        return FeesCalculation.builder()
            .amount(feeAmount)
            .code(feeLookupResponseDto.getCode())
            .version(feeLookupResponseDto.getVersion().toString())
            .build();
    }

    private FeeLookupResponseDto getFeeLookupResponseDto(BigDecimal amount, String authorization, String serviceAuthorization, String eventIssue) {
        return feesApi.getFeesLookup(authorization, serviceAuthorization, feesLookupConfig.getService(),
            feesLookupConfig.getJurisdiction1(), feesLookupConfig.getJurisdiction2(), feesLookupConfig.getChannel(),
            eventIssue, feesLookupConfig.getApplicantType(), amount);
    }

    @Override
    public FeesCalculation lookupFeeForCopies(Long volume, String authorization, String serviceAuthorization) {
        if (volume == 0L) {
            return FeesCalculation.builder()
                .amount(BigDecimal.ZERO)
                .build();
        }
        FeeLookupResponseDto feeLookupResponseDto = getFeeLookupResponseDto(BigDecimal.ONE,
            authorization, serviceAuthorization, feesLookupConfig.getEventCopies());
        log.info("Fee look response: {}", feeLookupResponseDto);
        BigDecimal feeAmount = feeLookupResponseDto.getFeeAmount();
        if (volume > 1L) {
            feeLookupResponseDto = getFeeLookupResponseDto(BigDecimal.valueOf(volume - 1L),
                authorization, serviceAuthorization, feesLookupConfig.getEventCopies());
            feeAmount = feeAmount.add(feeLookupResponseDto.getFeeAmount());
        }
        return FeesCalculation.builder()
            .amount(NumberUtils.defaultToZero(feeAmount))
            .code(feeLookupResponseDto.getCode())
            .version(feeLookupResponseDto.getVersion().toString())
            .build();
    }
}
