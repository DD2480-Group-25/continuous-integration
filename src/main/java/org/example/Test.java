package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class Test {

    public TestResult runGradleTest(File projectDir) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(projectDir);

            processBuilder.command("./gradlew", "test");

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
            }

            process.waitFor(20, TimeUnit.SECONDS);

            return new TestResult(testStatus, output.toString());
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return new TestResult(false, "Error occurred during test: " + e.getMessage());
        }
    }

    static class TestResult {
        private final boolean success;
        private final String output;

        public TestResult(boolean success, String output) {
            this.success = success;
            this.output = output;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getOutput() {
            return output;
        }
    }
}
