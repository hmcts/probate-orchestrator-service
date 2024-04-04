package uk.gov.hmcts.probate.contract.tests;

import com.fasterxml.jackson.databind.DeserializationFeature;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        File file = getFile(fileName);
        Invitation encodedInvite = objectMapper.readValue(file, Invitation.class);
        Invitation decodedInvite = decodeURL(encodedInvite);
        return decodedInvite;
    }

    protected ProbateCaseDetails getProbateCaseDetails(String fileName) throws JSONException, IOException {
        File file = getFile(fileName);
        ProbateCaseDetails probateCaseDetails = objectMapper.readValue(file, ProbateCaseDetails.class);
        return probateCaseDetails;
    }

    private File getFile(String fileName) throws FileNotFoundException {
        return ResourceUtils.getFile(this.getClass().getResource("/json/" + fileName));
    }

    public Invitation decodeURL(Invitation invitation) throws UnsupportedEncodingException {
        invitation.setExecutorName(decodeURLParam(invitation.getExecutorName()));
        invitation.setFirstName(decodeURLParam(invitation.getFirstName()));
        invitation.setLastName(decodeURLParam(invitation.getLastName()));
        invitation.setLeadExecutorName(decodeURLParam(invitation.getLeadExecutorName()));
        return invitation;
    }

    private String decodeURLParam(String uriParam) throws UnsupportedEncodingException {
        return URLDecoder.decode(uriParam, StandardCharsets.UTF_8.toString());
    }
}
