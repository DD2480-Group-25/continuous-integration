package org.ci;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Handles the execution of Gradle tests for a given project directory.
 */
public class Test {

    /**
     * Executes Gradle tests for a specified project directory.
     *
     * @param projectDir the directory of the project to test
     * @return a TestResult object containing the status and output of the test process
     */
    public TestResult runGradleTest(File projectDir) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(projectDir);

            // Command to execute Gradle tests
            processBuilder.command("./gradlew", "test");
            processBuilder.redirectErrorStream(true); // Merge error and input stream into one


            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();
            boolean testStatus = true;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    if (line.contains("BUILD FAILED")) {
                        testStatus = false;
                    }
                }
                System.out.println(output); // Print output for debugging
            }  catch (Exception e) {
                System.out.println(e.getMessage()); // Print error message
            }

            process.waitFor(); // Wait for process to finish

            return new TestResult(testStatus, output.toString());
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return new TestResult(false, "Error occurred during test: " + e.getMessage());
        }
    }

    /**
     * Represents the result of a Gradle test operation.
     */
    static class TestResult {
        private final boolean success;
        private final String output;

        /**
         * Constructs a TestResult object.
         *
         * @param success true if the tests were successful, false otherwise
         * @param output  the output of the test process
         */
        public TestResult(boolean success, String output) {
            this.success = success;
            this.output = output;
        }

         /**
         * Checks if the tests were successful.
         *
         * @return true if the tests were successful, false otherwise
         */
        public boolean isSuccess() {
            return success;
        }

        /**
         * Retrieves the output of the test process.
         *
         * @return the output of the test process
         */
        public String getOutput() {
            return output;
        }
    }
}
