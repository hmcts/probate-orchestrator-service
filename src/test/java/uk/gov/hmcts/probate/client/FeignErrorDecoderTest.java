package uk.gov.hmcts.probate.client;

import feign.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FeignErrorDecoderTest {

    private static final String ERROR_REASON = "Error Reason";
    private static final String METHOD_KEY = "Method Key";
    Response response;


    FeignErrorDecoder feignErrorDecoder;

    @Before
    public void setUp() {
        feignErrorDecoder = new FeignErrorDecoder();
    }


    @Test
    public void shouldDecodeError() {
        response = Response.builder().status(HttpStatus.SC_OK)
                .headers(new HashMap())
                .reason("Error Reason")
                .build();

        Exception exception = feignErrorDecoder.decode("methodKey", response);
        assertThat(exception.getMessage(), equalTo(ERROR_REASON));
    }

    @Test
    public void shouldDecodeErrorWithBody() {
        response = Response.builder().status(HttpStatus.SC_OK)
                .headers(new HashMap())
                .reason(ERROR_REASON)
                .body(new Response.Body() {
                    @Override
                    public Integer length() {
                        return null;
                    }

                    @Override
                    public boolean isRepeatable() {
                        return false;
                    }

                    @Override
                    public InputStream asInputStream() throws IOException {
                        return null;
                    }

                    @Override
                    public Reader asReader() throws IOException {
                        return null;
                    }

                    @Override
                    public void close() throws IOException {

                    }
                })
                .build();

        Exception exception = feignErrorDecoder.decode(METHOD_KEY, response);
        assertThat(exception.getMessage(), equalTo(ERROR_REASON));
    }

}