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
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ContinuousIntegrationServer {
    public static final Logger logger = LoggerFactory.getLogger(ContinuousIntegrationServer.class);
    public static void main(String[] args) {
        Spark.port(8080);
        Spark.get("/", (req, res) -> {
            res.type("text/html;charset=utf-8");
            res.status(HttpServletResponse.SC_OK);
            return "Hello, Group 25!";
        });
        Spark.post("/webhook", new WebhookHandler());
    }
    private static class WebhookHandler implements Route {

        // --- 0. Getting information from the payload ---

        @Override
        public Object handle(Request request, Response response) {
            response.type("text/html;charset=utf-8");
            response.status(HttpServletResponse.SC_OK);

            // --- 0. Fetching and parsing payload ---

            String payload = request.body();
            Gson gson = new Gson();

            JsonObject jsonPayload = gson.fromJson(payload, JsonObject.class);
            //which branch got pushed?
            String ref = jsonPayload.get("ref").getAsString();
            String sha = jsonPayload.get("after").getAsString();
            // Assuming 'payload' is a String containing the JSON payload from the webhook
            //JsonObject payloadObj = JsonParser.parseString(payload).getAsJsonObject();
            String repo = "dummy-repo";
            String owner = "ItsRkaj";
            // Perform CI tasks here
            // For example:
            // 1. Clone your repository
            // 2. Compile the code

            String branch;

            // Check if the request is coming from GitHub
            String userAgent = request.headers("User-Agent");

            if (userAgent != null && userAgent.startsWith("GitHub-Hookshot")) {
                // Parse JSON payload
                branch = ref.replace("refs/heads/", "");
                System.out.println("Changes were just made on branch: " + branch);

                // Respond with a success message
                response.status(200);
            } else {
                response.status(403); // Forbidden
                return "Request is not from GitHub.";
            }

            // --- 1. Fetching changes ---

            GitHandler gh = new GitHandler("git-repo/dummy-repo", "git@github.com:ItsRkaj/dummy-repo.git");

            gh.deleteLocalRepo();
            gh.cloneRepo();

            if (gh.checkout(branch)) {
                gh.pull(branch);
            } else {
                logger.info(gh.getCurrentBranch());
                return "Fatal error";
            }

            // --- 2. Building project ---
          
            boolean buildSuccessful = runBuild(gh);

            // --- 3. Running tests ---
            runTest(gh);

            // --- 4. Providing feedback
            NotificatitonSystem ns = new NotificatitonSystem();
            String result = buildSuccessful ? "pass" : "failed";
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
     * @param gitHandler the GitHandler object providing access to the Git repo
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

    public static void runTest(GitHandler gh) {
        try {
            if (!gh.isRepoCloned()) {
                logger.error("repo not cloned");
                return;
            }

            File projectDir = gh.getLocalRepoDirFile();
            Test test = new Test();
            Test.TestResult testResult = test.runGradleTest(projectDir);

            if (testResult.isSuccess()) {
                logger.info("Test successful");
            } else {
                logger.error("Test failed: {}", testResult.getOutput());
            }
        } catch (Exception e) {
            logger.error("Error occurred during build: {}", e.getMessage());
        }
    }
}