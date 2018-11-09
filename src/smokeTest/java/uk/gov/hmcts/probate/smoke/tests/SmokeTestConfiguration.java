package uk.gov.hmcts.probate.smoke.tests;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan("uk.gov.hmcts.probate.smoke")
@PropertySource("application.properties")
public class SmokeTestConfiguration {
}
