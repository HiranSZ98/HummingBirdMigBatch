package com.terna.hummingbird.batch.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BatchConfig {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "C:\\RjcSoft\\NTTData\\Terna\\HummingBirdMigBatch\\HummingBirdMigBatch\\config\\HBMBCredentialsDoc.properties";

    static {
        try (InputStream input = BatchConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new RuntimeException("Unable to find " + CONFIG_FILE);
            }
            properties.load(input);
        }catch (IOException e) {
            throw new RuntimeException("Error loading configuration", e);
        }
    }

    public static String getCsvPath() {
        return properties.getProperty("csv.path");
    }

    public static String getDocumentArrivedUrl() {
        return properties.getProperty("document.arrived.url");
    }

    public static String getDocumentSentUrl() {
        return properties.getProperty("document.sent.url");
    }
}
