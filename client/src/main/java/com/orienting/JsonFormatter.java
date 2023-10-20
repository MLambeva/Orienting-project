package com.orienting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonFormatter {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.findAndRegisterModules();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static String formatJson(Object data) {
        try {
            ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
            return objectWriter.writeValueAsString(data);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
