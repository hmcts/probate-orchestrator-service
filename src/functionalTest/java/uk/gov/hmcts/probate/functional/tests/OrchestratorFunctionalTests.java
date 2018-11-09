package uk.gov.hmcts.probate.functional.tests;

import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.gov.hmcts.probate.functional.IntegrationTestBase;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SerenityRunner.class)
public class OrchestratorFunctionalTests extends IntegrationTestBase {

    @Test
    public void shouldSubmit() throws IOException {
        String formData = functionalTestUtils.getJsonNodeFromFile("formData.json").toString();

        given()
                .headers(functionalTestUtils.getHeadersWithUserId())
                .body(formData)
                .when().post("/submit").
        then()
                .statusCode(201)
                .body("caseId", is(notNullValue()))
                .body("state", is("PAAppCreated"));
    }
}
