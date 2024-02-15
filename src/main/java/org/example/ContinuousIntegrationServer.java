package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

/**
 * A continuous integration server that handles webhook events from GitHub.
 */

public class ContinuousIntegrationServer {

    /**
     * The logger instance for ContinuousIntegrationServer.
     */
    public static final Logger logger = LoggerFactory.getLogger(ContinuousIntegrationServer.class);

     /**
     * Main method to start the continuous integration server.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Spark.port(8080);
        Spark.get("/", (req, res) -> {
            res.type("text/html;charset=utf-8");
            res.status(HttpServletResponse.SC_OK);
            return "Hello, Group 25!";
        });
        Spark.post("/webhook", new WebhookHandler());
    }

    /**
     * Handles webhook requests from GitHub.
     */
    private static class WebhookHandler implements Route {

        // --- 0. Getting information from the payload ---

        @Override
        public Object handle(Request request, Response response) {
            response.type("text/html;charset=utf-8");
            response.status(HttpServletResponse.SC_OK);

            // --- 0. Fetching and parsing payload ---

            Gson gson = new Gson();

            String repo = "dummy-repo";
            String owner = "goodtimeswithpaul";

            String branch, sha;

            // Check if the request is coming from GitHub
            String userAgent = request.headers("User-Agent");

            if (userAgent != null && userAgent.startsWith("GitHub-Hookshot")) {
                String payload = request.body();

                JsonObject jsonPayload = gson.fromJson(payload, JsonObject.class);

                String ref = jsonPayload.get("ref").getAsString();
                sha = jsonPayload.get("after").getAsString();

                // Parse JSON payload
                branch = ref.replace("refs/heads/", "");
                System.out.println("Incoming changes on branch: " + branch);

                // Respond with a success message
                response.status(200);
            } else {
                response.status(403); // Forbidden
                return "Request is not from GitHub.";
            }

            // --- 1. Fetching changes ---

            System.out.println("Fetching changes");

            GitHandler gh = new GitHandler("git-repo/dummy-repo", "git@github.com:goodtimeswithpaul/dummy-repo.git");

            gh.deleteLocalRepo();
            gh.cloneRepo();

            gh.fetch(branch);

            if (gh.checkout(branch)) {
                gh.pull(branch);
                System.out.println("Changes fetched successfully");
            } else {
                logger.info(gh.getCurrentBranch());
                return "Fatal error";
            }

            // --- 2. Building project ---

            System.out.println("Trying to build the project");

            boolean buildSuccessful = runBuild(gh);

            System.out.println("Build finished");

            // --- 3. Running tests ---

            System.out.println("Trying to run the project's unit tests");

            boolean testSuccessful = runTest(gh);

            System.out.println("Testing finished");

            // --- 4. Providing feedback
            System.out.println("Sending feedback");
            NotificatitonSystem ns = new NotificatitonSystem();
            String result = buildSuccessful && testSuccessful ? "pass" : "failed";
            String token = "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";
            String targetUrl = "https://example.com/build/status";
            String returned = ns.resultCheck(result, token, owner, repo, sha, targetUrl);
            logger.info(returned);

            logger.info("CI job done");
            return "CI job done";
        }
    }

    /**
     * Executes a Gradle build for a Git repo.
     *
     * @param gh the GitHandler object providing access to the Git repo
     * @return true if the build is successful, false otherwise
     */

    public static boolean runBuild(GitHandler gh) {

        try {

            if (!gh.isRepoCloned()) {
                logger.error("repo not cloned");
                return false;
            }

            File projectDir = gh.getLocalRepoDirFile();
            Build build = new Build();
            Build.BuildResult buildResult = build.runGradleBuild(projectDir);

            if (buildResult.isSuccess()) {
                logger.info("Build successful");
                return true;
            } else {
                logger.error("Build failed: {}", buildResult.getOutput());
                return false;
            }
        } catch (Exception e) {
            logger.error("Error occurred during build: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Executes unit tests for a Git repo.
     *
     * @param gh the GitHandler object providing access to the Git repo
     * @return true if the tests are successful, false otherwise
     */

    public static boolean runTest(GitHandler gh) {
        try {
            if (!gh.isRepoCloned()) {
                logger.error("repo not cloned");
                return false;
            }

            File projectDir = gh.getLocalRepoDirFile();
            Test test = new Test();
            Test.TestResult testResult = test.runGradleTest(projectDir);

            if (testResult.isSuccess()) {
                logger.info("Test successful");
                return true;
            } else {
                logger.error("Test failed: {}", testResult.getOutput());
                return false;
            }
        } catch (Exception e) {
            logger.error("Error occurred during build: {}", e.getMessage());
            return false;
        }
    }
}