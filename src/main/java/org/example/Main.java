package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class Main {
    static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Main executed");
        Result result = JUnitCore.runClasses(GitHandlerTest.class);
        System.out.println("Test(s): " + result.getRunCount());
        System.out.println("Time: " + result.getRunTime() + "ms");
        System.out.println("Tests passed: " + result.wasSuccessful());
    }
}
