package com.terna.hummingbird.batch.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {

    private static final String PROPERTIES_FILE = "C:\\RjcSoft\\NTTData\\Terna\\HummingBirdMigBatch\\HummingBirdMigBatch\\config\\HBMBCredentialsDoc.properties";
    private static Properties properties;

    static {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Could not load properties file: " + PROPERTIES_FILE, e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

}
