package uk.gov.hmcts.probate.core.service.fees;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("fees.lookup")
public class FeesLookupConfiguration {

    private String service;

    private String jurisdiction1;

    private String jurisdiction2;

    private String eventCopies;

    private String eventIssue;

    private String applicantType;

    private String channel;

    private String keywordCopies;

    private String keywordIssue;
}
