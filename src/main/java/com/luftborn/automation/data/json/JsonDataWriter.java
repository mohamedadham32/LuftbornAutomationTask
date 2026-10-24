package com.luftborn.automation.data.json;

import com.luftborn.automation.data.DataAccessException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class JsonDataWriter {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .findAndRegisterModules().enable(SerializationFeature.INDENT_OUTPUT);
    private JsonDataWriter() {}

    public static void write(Path path, Object value) {
        try {
            Path absolute = path.toAbsolutePath();
            if (absolute.getParent() != null) Files.createDirectories(absolute.getParent());
            MAPPER.writeValue(absolute.toFile(), value);
        } catch (IOException e) {
            throw new DataAccessException("Could not write JSON: " + path, e);
        }
    }
}
