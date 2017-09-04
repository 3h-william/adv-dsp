package com.advdsp.service.manager.rest.resources.provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;


@Provider
public class ObjectMapperProvider
        implements ContextResolver<ObjectMapper> {
    final ObjectMapper mapper = new ObjectMapper();


    public ObjectMapperProvider() {
        // DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}