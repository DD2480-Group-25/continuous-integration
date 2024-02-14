package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * Handles the execution of Gradle build for a given project directory.
*/

public class Build {

    /** 
     * Executes a Gradle build.
     *
     * @param projectDir the directory of the project to build
     * @return a BuildResult object containing status of the build
     */

    public BuildResult runGradleBuild(File projectDir) {
        try {
            // Sets up GradleBuild using Process builder
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(projectDir);

            processBuilder.command("./gradlew", "build");
            processBuilder.redirectErrorStream(true); // This is used to merge the error and input stream into one

            Process process = processBuilder.start();
             
            StringBuilder output = new StringBuilder();

            boolean buildStatus = true;
            // Reads output and updates build status
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    if (line.contains("compileJava FAILED")) {
                        buildStatus = false;
                    }
                }
                System.out.println(output);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            // Wait for process for 20 seconds to avoid getting stuck
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

    /**
         * Constructs a BuildResult object.
         *
         * @param success true if the build was successful, false otherwise
         * @param output  the output of the build process
         */

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