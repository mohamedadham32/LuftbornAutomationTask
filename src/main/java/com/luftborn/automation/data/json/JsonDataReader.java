package com.luftborn.automation.data.json;

import com.luftborn.automation.data.DataAccessException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public final class JsonDataReader {
    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();
    private JsonDataReader() {}

    public static JsonNode tree(String classpathResource) { return read(classpathResource, JsonNode.class); }
    public static <T> T read(String classpathResource, Class<T> type) {
        try (InputStream stream = resource(classpathResource)) { return MAPPER.readValue(stream, type); }
        catch (IOException e) { throw new DataAccessException("Could not read JSON: " + classpathResource, e); }
    }
    public static <T> T read(String classpathResource, TypeReference<T> type) {
        try (InputStream stream = resource(classpathResource)) { return MAPPER.readValue(stream, type); }
        catch (IOException e) { throw new DataAccessException("Could not read JSON: " + classpathResource, e); }
    }
    private static InputStream resource(String name) {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
        if (stream == null) throw new DataAccessException("JSON resource not found: " + name);
        return stream;
    }
}
