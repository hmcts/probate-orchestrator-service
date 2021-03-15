package uk.gov.hmcts.probate.contract.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import uk.gov.hmcts.reform.probate.model.cases.ProbateCaseDetails;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class ContractTestUtils {

    @Autowired
    ObjectMapper objectMapper;

    protected JSONObject createJsonObject(String fileName) throws JSONException, IOException {
        File file = getFile(fileName);
        String jsonString = new String(Files.readAllBytes(file.toPath()));
        return new JSONObject(jsonString);
    }

    protected Invitation getInvitation(String fileName) throws JSONException, IOException {
        File file = getFile(fileName);
        Invitation invite = objectMapper.readValue(file, Invitation.class);
        return invite;
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
