package uk.gov.hmcts.probate.core.service.payment;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("payment")
public class PaymentConfiguration {

    private String serviceId;

    private String siteId;

    private String version;

    private String currency;

}
