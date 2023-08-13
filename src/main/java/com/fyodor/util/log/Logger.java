package com.fyodor.util.log;

public class Logger {
    private static boolean loggingEnabled = true;

    public static void setLoggingEnabled(boolean enabled) {
        loggingEnabled = enabled;
    }

    public static void logServerResponse(String message) {
        if (loggingEnabled) {
            log("[SERVER]", message);
        }
    }

    public static void logInfo(String message) {
        if (loggingEnabled) {
            log("[INFO]", message);
        }
    }

    public static void logWarning(String message) {
        if (loggingEnabled) {
            log("[WARNING]", message);
        }
    }

    public static void logError(String message) {
        if (loggingEnabled) {
            log("[ERROR]", message);
        }
    }

    private static void log(String level, String message) {
        System.out.println(level + " " + message);
    }
}
