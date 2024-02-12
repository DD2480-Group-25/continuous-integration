package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    static final Logger logger = LoggerFactory.getLogger(Main.class);

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        logger.info(new Main().getGreeting());
    }
}
