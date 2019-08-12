package uk.gov.hmcts.probate.model.persistence.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import static uk.gov.hmcts.reform.probate.model.YesNo.NO;
import static uk.gov.hmcts.reform.probate.model.YesNo.YES;

public class LegacyYesNoDeserializer extends JsonDeserializer<Boolean> {

    @Override
    public Boolean deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken.equals(JsonToken.VALUE_STRING)) {
            String text = jsonParser.getText().trim();
            if (YES.getDescription().equals(text)) {
                return Boolean.TRUE;
            } else if (NO.getDescription().equalsIgnoreCase(text)) {
                return Boolean.FALSE;
            } else if (text == null || text.isEmpty()) {
                return null; //NOSONAR
            }
            throw context.weirdStringException(text, Boolean.class, "String value needs to be 'yes' or 'no' or empty");
        }
        throw new IllegalArgumentException("Cannot deserialize for non string value");
    }
}



