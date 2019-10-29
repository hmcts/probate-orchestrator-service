package uk.gov.hmcts.probate.model.persistence.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext ctxt)
        throws IOException {
        JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken.equals(JsonToken.VALUE_STRING)) {
            String text = jsonParser.getText().trim();
            String replacedCommasText = text.replace(",", "");
            String replacedPoundSignText = replacedCommasText.replace("£", "");
            return new BigDecimal(replacedPoundSignText);
        }
        throw new IllegalArgumentException("Cannot deserialize for non string value");
    }
}
