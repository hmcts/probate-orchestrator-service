package uk.gov.hmcts.probate.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.probate.core.service.GrantDelayedNotifier;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/grant", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_VALUE)
@RestController
public class GrantController {

    private final GrantDelayedNotifier grantDelayedNotifier;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Scheduled(cron = "${cron.grantDelayed.schedule}")
    @ApiOperation(value = "Notify grants delayed")
    @PostMapping(path = "/delay-notification")
    public ResponseEntity initiateGrantDelayedSchedule() {
        log.info("Calling perform grant delayed notification for today ...");
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> {
            grantDelayedNotifier.initiateGrantDelayedNotification();
        });
        log.info("Perform grant delayed notification called for today");

        return ResponseEntity.ok("Perform grant delayed notification called");
    }

}
