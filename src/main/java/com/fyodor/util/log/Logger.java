package com.fyodor.util.log;

public class Logger {
    public static void logServerResponse(String message) {
        System.out.println("[SERVER] " + message);
    }

    public static void logInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    public static void logWarning(String message) {
        System.err.println("[WARNING] " + message);
    }

    public static void logError(String message) {
        System.err.println("[ERROR] " + message);
    }
}
