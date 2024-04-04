package uk.gov.hmcts.probate.contract.tests;

import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;
import uk.gov.hmcts.reform.auth.checker.core.RequestAuthorizer;
import uk.gov.hmcts.reform.auth.checker.core.SubjectResolver;
import uk.gov.hmcts.reform.auth.checker.core.service.Service;
import uk.gov.hmcts.reform.auth.checker.core.user.User;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.forms.intestacy.IntestacyForm;
import uk.gov.hmcts.reform.probate.model.forms.pa.PaForm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import javax.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@PactBroker(scheme = "${pact.broker.scheme}", host = "${pact.broker.baseUrl}", port = "${pact.broker.port}",
        tags = {"${pact.broker.consumer.tag}"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ControllerProviderTest {

    @Autowired
    ObjectMapper objectMapper;

    private static final String PRINCIPAL = "probate_backend";

    private static final String SERVICE_AUTHORIZATION = "ServiceAuthorization";

    private static final String AUTHORIZATION = "Authorization";

    @MockBean
    private SubjectResolver<Service> serviceResolver;

    @MockBean
    private RequestAuthorizer<User> userRequestAuthorizer;

    private Service service;

    @Value("${pact.broker.version}")
    private String providerVersion;

    @Before
    public void setUpTest() {

        service = new Service(PRINCIPAL);
        when(serviceResolver.getTokenDetails(anyString())).thenReturn(service);

        User user = new User("123", new HashSet<>());
        when(userRequestAuthorizer.authorise(any(HttpServletRequest.class))).thenReturn(user);
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

    protected IntestacyForm getIntestacyForm(String fileName) throws JSONException, IOException {
        File file = getFile(fileName);
        IntestacyForm intestacyForm = objectMapper.readValue(file, IntestacyForm.class);
        return intestacyForm;
    }


    protected PaForm getPaForm(String fileName) throws JSONException, IOException {
        File file = getFile(fileName);
        PaForm paForm = objectMapper.readValue(file, PaForm.class);
        return paForm;
    }

    private File getFile(String fileName) throws FileNotFoundException {
        return ResourceUtils.getFile(this.getClass().getResource("/json/" + fileName));
    }
}
