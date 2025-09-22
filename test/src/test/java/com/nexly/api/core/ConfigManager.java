package com.nexly.api.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@SuppressWarnings({"ConvertToTryWithResources", "CallToPrintStackTrace"})
public class ConfigManager {
    private static Properties properties;

    static {
        properties = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream("src/main/resources/config/config.properties");
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load configuration properties");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getBaseUrl() {
        return getProperty("base.url");
    }
}