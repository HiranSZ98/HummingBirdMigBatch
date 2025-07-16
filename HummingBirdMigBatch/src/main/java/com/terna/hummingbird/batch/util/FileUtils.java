package com.terna.hummingbird.batch.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

    /**
     * Appends a line to the specified file. Creates the file if it doesn't exist.
     *
     * @param filePath The full path to the file
     * @param line     The line of text to append
     */
    public static void prepareLogFiles(String okFilePath, String koFilePath) {
        createEmptyFile(okFilePath);
        createEmptyFile(koFilePath);
    }

    public static void appendOk(String okFilePath, String line) {
        appendLine(okFilePath, line, "SUCCESS");
    }

    public static void appendKo(String koFilePath, String line) {
        appendLine(koFilePath, line, "FAILED");
    }


    public static void appendLine(String filePath, String line, String status) {
        File file = new File(filePath);

        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String logLine = String.format("[%s] %s %s", timestamp, line, status);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(logLine);
            writer.newLine(); // Adds a line break
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to file: " + filePath, e);
        }
    }

    public static Map<String, String> loadFileToMap(String filePath) {
        Map<String, String> map = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Remove any leading/trailing whitespace
                if (!line.isEmpty()) {
                    map.put(line, line);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }

        return map;
    }

    private static void createEmptyFile(String filePath) {
        Path path = Paths.get(filePath);
        ensureParentDirExists(path);
        try {
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create " + filePath, e);
        }
    }

    private static void ensureParentDirExists(Path path) throws RuntimeException {
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                throw new RuntimeException("Cannot create dir " + parent, e);
            }
        }
    }

}
