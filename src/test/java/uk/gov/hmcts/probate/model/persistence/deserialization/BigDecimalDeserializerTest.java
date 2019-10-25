package uk.gov.hmcts.probate.model.persistence.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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