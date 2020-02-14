package uk.gov.hmcts.probate.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.probate.core.service.CaveatExpiryUpdater;
import uk.gov.hmcts.probate.core.service.ScheduleValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/caveat", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_VALUE)
@RestController
public class CaveatController {

    private final CaveatExpiryUpdater caveatExpiryUpdater;
    private final ScheduleValidator scheduleValidator;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @ApiOperation(value = "Expire Raised Caveats yesterday")
    @PostMapping(path = "/expire/{cronKeyCaveatExpiry}")
    public ResponseEntity expireCaveats(@ApiParam(value = "Cron Key for Caveat Expiry", required = true)
                                            @PathVariable("cronKeyCaveatExpiry") String cronKeyCaveatExpiry) {

        scheduleValidator.validateCaveatExpiry(cronKeyCaveatExpiry);

        String expireForDate = DATE_FORMAT.format(LocalDate.now().minusDays(1L));
        log.info("Calling perform expire caveats for date {} ...", expireForDate);
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> {
            caveatExpiryUpdater.expireCaveats(expireForDate);
        });
        log.info("Perform expire caveats called for date {}", expireForDate);

        return ResponseEntity.ok("Perform expire caveats called");
    }

}
