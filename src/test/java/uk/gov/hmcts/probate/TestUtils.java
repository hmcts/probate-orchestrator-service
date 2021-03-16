package uk.gov.hmcts.probate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class TestUtils {

    public static String getJsonFromFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/test/resources", fileName)));
    }

    public static JsonNode getJsonNodeFromFile(String fileName) throws IOException {
        return new ObjectMapper().readTree(getJsonFromFile(fileName));
    }

    public static Map<String, JsonNode> getJsonMapFromFile(String fileName) throws IOException {
        String json = getJsonFromFile(fileName);
        return new ObjectMapper().readValue(json, new TypeReference<HashMap<String, JsonNode>>(){});
    }

    private TestUtils(){
    }
}

