package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.provider.junit.IgnoreNoPactsToVerify;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

@RunWith(SpringRestPactRunner.class)
@ExtendWith(SpringExtension.class)
@PactBroker(scheme = "${pact.broker.scheme}", host = "${pact.broker.baseUrl}", port = "${pact.broker.port}", tags={"${pact.broker.consumer.tag}", "latest", "master"})
@IgnoreNoPactsToVerify
abstract public class ControllerProviderTest {

    @Autowired
    ObjectMapper objectMapper;

    @Value("${pact.broker.version}")
    private String providerVersion;

    @Before
    public void setUpTest() {
        System.getProperties().setProperty("pact.verifier.publishResults", "true");
        System.getProperties().setProperty("pact.provider.version", providerVersion);
    }

    protected JSONObject createJsonObject(String fileName) throws JSONException, IOException {
        File file = getFile(fileName);
        String jsonString = new String(Files.readAllBytes(file.toPath()));
        return new JSONObject(jsonString);
    }

    protected ProbateCaseDetails getProbateCaseDetails(String fileName) throws JSONException, IOException {
        File file = getFile(fileName);
        ProbateCaseDetails probateCaseDetails = objectMapper.readValue(file, ProbateCaseDetails.class);
        return probateCaseDetails;
    }

    private File getFile(String fileName) throws FileNotFoundException {
        return ResourceUtils.getFile(this.getClass().getResource("/json/" + fileName));
    }
}
