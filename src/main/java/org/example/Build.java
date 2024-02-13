package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class Build {

    public BuildResult runGradleBuild(File projectDir) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(projectDir);

            processBuilder.command("./gradlew", "build");

            Process process = processBuilder.start();
             
            StringBuilder output = new StringBuilder();
            boolean buildStatus = true;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    if (line.contains("compileJava FAILED")) {
                        buildStatus = false;
                    }
                }
            }

            process.waitFor(20, TimeUnit.SECONDS);

            return new BuildResult(buildStatus, output.toString());
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return new BuildResult(false, "Error occurred during build: " + e.getMessage());
        }
    }

    static class BuildResult {
        private final boolean success;
        private final String output;

        public BuildResult(boolean success, String output) {
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