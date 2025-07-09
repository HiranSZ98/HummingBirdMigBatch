package com.terna.hummingbird.batch.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PayloadLoggerUtil {

    private static final String LOG_DIR = "C:/RjcSoft/NTTData/Terna/HummingBirdMigBatch/HummingBirdMigBatch/logs";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void logPayload(Object payload, String moduleName, boolean success, String errorMessage) {
        final String status = success ? "SUCCESS" : "ERROR";
        final String timestamp = "[" + TIME_FORMAT.format(LocalDateTime.now()) + "]";
        final String fileName = String.format("%s/%s_%s.log", LOG_DIR, moduleName, DATE_FORMAT.format(LocalDateTime.now()));

        final String systemId = extractSystemId(payload);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(String.format("%s [%s] SYSTEM_ID: %s", timestamp, status, systemId));
            if (!success && errorMessage != null) {
                writer.write(" | ERROR: " + errorMessage);
            }
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Log error: " + e.getMessage());
        }
    }

    private static String extractSystemId(Object payload) {
        try {
            var method = payload.getClass().getMethod("getSystemId");
            var value = method.invoke(payload);
            return value != null ? value.toString() : "null";
        } catch (Exception e) {
            return "NOT_AVAILABLE";
        }
    }
}
