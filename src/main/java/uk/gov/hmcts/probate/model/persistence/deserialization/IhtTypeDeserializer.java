package uk.gov.hmcts.probate.model.persistence.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import uk.gov.hmcts.reform.probate.model.IhtFormType;

import java.io.IOException;

public class IhtTypeDeserializer extends JsonDeserializer {
    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken.equals(JsonToken.VALUE_STRING)) {
            String text = jsonParser.getText().trim();
            if(text.equals("207") || text.equals("IHT207")) {
                return IhtFormType.IHT207;
            }
            else if(text.equals("205") || text.equals("IHT205")){
                return IhtFormType.IHT205;
            }
            else if(text.equals("400421") || text.equals("IHT400421")){
                return IhtFormType.IHT400421;
            }
        }
        throw new IllegalArgumentException("Cannot deserialize for non string value:" );
    }
}
