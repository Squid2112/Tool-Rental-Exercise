/**
 * LoggerConfig class for the Tool Rental System application.
 *
 * This class provides configuration for the logging system used in the application.
 * It sets up console and file handlers for logging messages at different levels,
 * and formats the log messages in a simple format. It also provides a method to get
 * the configured logger.
 *
 * Methods:
 * - void setup(): Configures the logging handlers and their levels.
 * - Logger getLogger(): Returns the configured logger.
 *
 * Example usage:
 * - Used in the Main class to initialize logging configuration at application startup.
 *
 * @version 1.0
 */
package com.example.toolrental;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfig {

    private static final Logger logger = Logger.getLogger(LoggerConfig.class.getName());

    public static void setup() throws IOException {
        // Remove the default console handler
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }

        // Create a console handler
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.SEVERE);
        consoleHandler.setFormatter(new SimpleFormatter());
        rootLogger.addHandler(consoleHandler);

        // Create a file handler for exceptions
        FileHandler fileHandler = new FileHandler("exceptions.log", true);
        fileHandler.setLevel(Level.WARNING);
        fileHandler.setFormatter(new SimpleFormatter());
        rootLogger.addHandler(fileHandler);

        // Create a file handler for rental agreements
        FileHandler rentalAgreementHandler = new FileHandler("rental_agreements.log", true);
        rentalAgreementHandler.setLevel(Level.INFO);
        rentalAgreementHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(rentalAgreementHandler);

        // Set the logger level to ALL
        rootLogger.setLevel(Level.ALL);
    }

    public static Logger getLogger() {
        return logger;
    }
}
