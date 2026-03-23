package uk.gov.hmcts.probate.model.backoffice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GrantScheduleResponse(List<String> scheduleResponseData) {}
