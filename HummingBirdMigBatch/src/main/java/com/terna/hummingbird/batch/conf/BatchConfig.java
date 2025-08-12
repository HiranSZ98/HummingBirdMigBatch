package com.terna.hummingbird.batch.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BatchConfig {

    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "HummingBirdMigBatch.properties"; // just the filename

    static {
        try (InputStream input = BatchConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new RuntimeException("Unable to find configuration file in classpath: " + CONFIG_FILE);
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Error loading configuration file: " + CONFIG_FILE, e);
        }
    }

    public static String getCsvRootPath() {
        return properties.getProperty("csv.root.path");
    }

    public static String getDocumentArrivedUrl() {
        return properties.getProperty("document.arrived.url");
    }

    public static String getDocumentSentUrl() {
        return properties.getProperty("document.sent.url");
    }

    public static String getRegisterUrl() {
        return properties.getProperty("register.url");
    }

    public static String getPersonUrl() {
        return properties.getProperty("person.url");
    }

    public static String getAclUrl() {
        return properties.getProperty("acl.url");
    }

    public static String getMittDesUrl() {
        return properties.getProperty("mittdes.url");
    }
}
