package com.gt.config;

import java.io.IOException;
import java.util.logging.*;

public final class LoggingConfig {

    private LoggingConfig() {
    }

    public static void configure() {

        Logger rootLogger = Logger.getLogger("");

        // Vorhandene Handler entfernen
        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        rootLogger.setLevel(Level.ALL);

        // Konsolenausgabe
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        consoleHandler.setFormatter(new SimpleFormatter());

        rootLogger.addHandler(consoleHandler);

        // Logging für Manager
        Logger managerLogger =
                Logger.getLogger("de.javatech.lms.manager");

        managerLogger.setLevel(Level.FINE);

        try {
            // Logdatei erstellen
            FileHandler fileHandler =
                    new FileHandler("lms.log", true);

            fileHandler.setLevel(Level.FINE);
            fileHandler.setFormatter(new SimpleFormatter());

            rootLogger.addHandler(fileHandler);

        } catch (IOException e) {
            rootLogger.log(
                    Level.SEVERE,
                    "Logdatei konnte nicht erstellt werden.",
                    e
            );
        }
    }
}