package uk.gov.hmcts.probate.model.persistence;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

public class LegacyPaAssetsTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldDeserializeLegacyPaAssetsYes() throws IOException {
        String assetsoverseasStr = "{ \"assetsoverseas\" : \"Yes\"}";

        LegacyPaAssets legacyPaAssets = objectMapper.readValue(assetsoverseasStr, LegacyPaAssets.class);

        assertThat(legacyPaAssets.getAssetsoverseas(), equalTo(true));
    }

    @Test
    public void shouldDeserializeLegacyPaAssetsNo() throws IOException {
        String assetsoverseasStr = "{ \"assetsoverseas\" : \"No\"}";

        LegacyPaAssets legacyPaAssets = objectMapper.readValue(assetsoverseasStr, LegacyPaAssets.class);

        assertThat(legacyPaAssets.getAssetsoverseas(), equalTo(false));
    }

    @Test
    public void shouldDeserializeLegacyPaAssetsNull() throws IOException {
        String assetsoverseasStr = "{ \"assetsoverseas\" : \"\"}";

        LegacyPaAssets legacyPaAssets = objectMapper.readValue(assetsoverseasStr, LegacyPaAssets.class);

        assertThat(legacyPaAssets.getAssetsoverseas(), equalTo(null));
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldDeserializeLegacyPaAssetsNonValidAssetsOverseas() throws IOException {
        String assetsoverseasStr = "{ \"assetsoverseas\" : \"sdfgdfsgdf\"}";

        objectMapper.readValue(assetsoverseasStr, LegacyPaAssets.class);
    }

    @Test(expected = JsonMappingException.class)
    public void shouldDeserializeLegacyPaAssetsNonValidTypeAssetsOverseas() throws IOException {
        String assetsoverseasStr = "{ \"assetsoverseas\" : 10000}";

        objectMapper.readValue(assetsoverseasStr, LegacyPaAssets.class);
    }
}
