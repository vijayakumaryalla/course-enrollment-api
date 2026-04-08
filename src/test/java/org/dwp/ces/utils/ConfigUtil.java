package org.dwp.ces.utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
    private static Properties properties = new Properties();

    static {
        try (InputStream input = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) throw new RuntimeException("config.properties not found!");
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        String envValue = System.getenv(key.toUpperCase().replace(".", "_"));
        return (envValue != null) ? envValue : properties.getProperty(key);
    }
}
