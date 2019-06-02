package uk.gov.hmcts.probate.client.business;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.Logger;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


public class BusinessServiceConfiguration {

    static final String SERVICE_AUTHORIZATION = "ServiceAuthorization";


    @Bean
    public HttpMessageConverters customConverters() {
        final AbstractJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(customObjectMapper());
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.valueOf(APPLICATION_JSON_VALUE)));
        return new HttpMessageConverters(true, Collections.singletonList(converter));
    }

    private ObjectMapper customObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        return objectMapper;
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}
