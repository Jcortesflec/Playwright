package blass.academy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            Logs.error(e.getMessage());
        }
    }

    public static String get(String key) {
        final var systemValue = System.getProperty(key);

        if (systemValue != null) {
            return systemValue;
        }

        return properties.getProperty(key);
    }
}