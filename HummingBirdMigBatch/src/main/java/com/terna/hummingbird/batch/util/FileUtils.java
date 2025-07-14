package com.terna.hummingbird.batch.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

    /**
     * Appends a line to the specified file. Creates the file if it doesn't exist.
     *
     * @param filePath The full path to the file
     * @param line     The line of text to append
     */
    public static void appendLine(String filePath, String line) {
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(line);
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

}
