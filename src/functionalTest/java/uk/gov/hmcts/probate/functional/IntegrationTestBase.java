package uk.gov.hmcts.probate.functional;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import net.thucydides.junit.spring.SpringIntegration;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.probate.functional.utils.TestUtils;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestContextConfiguration.class)
public abstract class IntegrationTestBase {

    @Autowired
    protected TestUtils functionalTestUtils;

    @Autowired
    public void solCcdServiceUrl(@Value("${probate.orchestrator.service.url}") String springBootUrl) {
        RestAssured.baseURI = springBootUrl;
        RestAssured.defaultParser = Parser.JSON;
    }

    @Rule
    public SpringIntegration springIntegration;

    public IntegrationTestBase() {
        this.springIntegration = new SpringIntegration();

    }
}
