package org.ci;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple class demonstrating basic logging functionality.
 */
public class Main {
    /**
     * The logger instance for Main class.
     */
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * Retrieves a greeting message.
     *
     * @return a greeting message
     */
    public String getGreeting() {
        return "Hello World!";
    }

    /**
     * Entry point of the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        logger.info(new Main().getGreeting());
    }
}