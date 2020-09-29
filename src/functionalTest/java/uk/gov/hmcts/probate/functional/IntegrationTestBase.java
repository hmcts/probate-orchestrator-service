package uk.gov.hmcts.probate.functional;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.junit.spring.SpringIntegration;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.probate.functional.utils.TestUtils;

@RunWith(SpringIntegrationSerenityRunner.class)
@ContextConfiguration(classes = TestContextConfiguration.class)
public abstract class IntegrationTestBase {

    @Autowired
    protected TestUtils utils;

    protected String orchestratorUrl;
    protected String submitServiceUrl;
    protected String idamUrl;

    @Autowired
    public void initialise(@Value("${probate.orchestrator.service.url}") String orchestratorUrl,
                           @Value("${probate.submit.url}") String submitServiceUrl,
                           @Value("${user.auth.provider.oauth2.url}") String idamUrl) {
        RestAssured.baseURI = orchestratorUrl;
        RestAssured.defaultParser = Parser.JSON;
        this.orchestratorUrl = orchestratorUrl;
        this.submitServiceUrl = submitServiceUrl;
        this.idamUrl = idamUrl;
    }

    @Rule
    public SpringIntegration springIntegration;

    public IntegrationTestBase() {
        this.springIntegration = new SpringIntegration();

    }
}
