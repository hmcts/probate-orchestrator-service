package uk.gov.hmcts.probate.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;


import java.util.Collections;

@Configuration
public class DocumentControllerConfiguration {

    public static final String APPLICATION_BUSINESSDOCUMENT_JSON = "application/businessdocument+json";

    @Bean
    public HttpMessageConverters customConverters() {
        final AbstractJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(customObjectMapper());
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.valueOf(APPLICATION_BUSINESSDOCUMENT_JSON)));
        return new HttpMessageConverters(true, Collections.singletonList(converter));
    }


    private ObjectMapper customObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        return objectMapper;
    }

}
