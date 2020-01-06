package uk.gov.hmcts.probate.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.probate.configuration.ScheduleConfiguration;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleValidator {
    private final ScheduleConfiguration scheduleConfiguration;

    public void validateCaveatExpiry(String pathKey) {
        validateScheduleKey(scheduleConfiguration.getCaveatExpiry(), pathKey);
    }

    private void validateScheduleKey(String cronConfig, String pathKey) {
        if (!cronConfig.equals(pathKey)) {
            log.error("Schedule for {} does not have a valid auth key", cronConfig);
            throw new RuntimeException("Schedule for "+cronConfig+" does not have a valid auth key");
        }
    }

}
