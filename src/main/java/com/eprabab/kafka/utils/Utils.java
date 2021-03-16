package com.eprabab.kafka.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class Utils {

    public static Map<String, Object> loadPropertiesAsMap(final String propertiesFile) throws IOException {

        final Properties producerProperties = new Properties();
        producerProperties.load(new FileInputStream(propertiesFile));

        return producerProperties.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toString(),
                        e -> e.getValue()));
    }
}
