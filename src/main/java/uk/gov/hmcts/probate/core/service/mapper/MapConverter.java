package uk.gov.hmcts.probate.core.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.FromMap;
import uk.gov.hmcts.probate.core.service.mapper.qualifiers.ToMap;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MapConverter {

    private final ObjectMapper objectMapper;

    @FromMap
    public String fromMap(List<Map<String, String>> maps) throws JsonProcessingException {
        if (maps == null) {
            return null; //NOSONAR
        }
        return objectMapper.writeValueAsString(maps);
    }

    @ToMap
    public List<Map<String, String>> toMap(String mapStr) throws IOException {
        if (mapStr == null) {
            return null; //NOSONAR
        }
        return objectMapper.readValue(mapStr, new TypeReference<List<Map<String, String>>>() {
        });
    }
}
