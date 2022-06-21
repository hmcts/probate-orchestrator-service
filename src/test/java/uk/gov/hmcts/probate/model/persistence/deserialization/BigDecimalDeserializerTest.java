package uk.gov.hmcts.probate.model.persistence.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class BigDecimalDeserializerTest {

    @Mock
    JsonParser  mockJsonParser;

    BigDecimalDeserializer bigDecimalDeserializer = new BigDecimalDeserializer();

    @Test
    public void shouldDeserializeStringWithSpecialCharacters() throws IOException {

        when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("£69,115.28");

        BigDecimal result = bigDecimalDeserializer.deserialize(mockJsonParser, null);

        assertThat(result, equalTo(new BigDecimal("69115.28")));

    }


}
