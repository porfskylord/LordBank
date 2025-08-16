package util;

import java.io.*;
import java.nio.file.*;

public class FileUtil {
    private static final String DATA_DIR = "data";
    
    static {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Failed to create data directory: " + e.getMessage());
        }
    }
    
    public static synchronized void saveToFile(String filename, Object obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_DIR + File.separator + filename))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            System.err.println("Error saving to file " + filename + ": " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T loadFromFile(String filename, T defaultValue) {
        File file = new File(DATA_DIR + File.separator + filename);
        if (!file.exists()) {
            return defaultValue;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading from file " + filename + ": " + e.getMessage());
            return defaultValue;
        }
    }
    
    public static void deleteFile(String filename) {
        try {
            Files.deleteIfExists(Paths.get(DATA_DIR + File.separator + filename));
        } catch (IOException e) {
            System.err.println("Error deleting file " + filename + ": " + e.getMessage());
        }
    }
}
