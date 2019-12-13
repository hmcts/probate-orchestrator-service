package uk.gov.hmcts.probate.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.probate.core.service.CaveatExpiryUpdater;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/caveat", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_VALUE)
@RestController
public class CaveatController {

    private final CaveatExpiryUpdater caveatExpiryUpdater;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Scheduled(cron = "${cron.caveat_expiry}")
    @ApiOperation(value = "Expire Raised Caveats yesterday")
    @PostMapping(path = "/expire")
    public ResponseEntity expireCaveats() {
        return expireCaveats(DATE_FORMAT.format(LocalDate.now().minusDays(1L)));
    }

    private ResponseEntity expireCaveats(String date) {
        int numxpiredCaveats = caveatExpiryUpdater.expireCaveats(date).size();

        return ResponseEntity.ok(numxpiredCaveats + " caveats expired for date: "+date);
    }


}
