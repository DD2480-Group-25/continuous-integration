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
            JsonObject payloadObj = JsonParser.parseString(payload).getAsJsonObject();
            String repo = payloadObj.getAsJsonObject("repository").get("name").getAsString();
            String owner = payloadObj.getAsJsonObject("repository").get("owner").getAsString();
            // Perform CI tasks here
            // For example:
            // 1. Clone your repository
            // 2. Compile the code
            Gson gson = new Gson();

            String branch;

            // Check if the request is coming from GitHub
            String userAgent = request.headers("User-Agent");

            if (userAgent != null && userAgent.startsWith("GitHub-Hookshot")) {
                // Parse JSON payload
                JsonObject payload = gson.fromJson(request.body(), JsonObject.class);

                // Read branch information
                String ref = payload.get("ref").getAsString();
                branch = ref.replace("refs/heads/", "");
                System.out.println("Changes were made on branch: " + branch);

                // Respond with a success message
                response.status(200);
            } else {
                response.status(403); // Forbidden
                return "Request is not from GitHub.";
            }

            // --- 1. Fetching changes ---

            GitHandler gh = new GitHandler(); // change with correct repo parameters

            gh.deleteLocalRepo();
            gh.cloneRepo();

            gh.fetch(branch);

            if (gh.checkout(branch)) {
                gh.pull(branch);
            } else {
                logger.info(gh.getCurrentBranch());
                return "Fatal error";
            }

            // --- 2. Building project ---
          
            runBuild(gh);

            // --- 3. Running tests ---

            // --- 4. Providing feedback
            NotificatitonSystem ns = new NotificatitonSystem();
            String result = "pass";
            String token = "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";
            //String owner = "WarlCang";
            //String owner = "DD2480-Group-25";
            //String repo = "test";
            //String repo = "continuous-integration";
            //String sha = "f8378f85e2f998fbb13c554208f88cbea448eb0b";
            //String sha = jsonPayload.get("after").getAsString();
            String targetUrl = "https://example.com/build/status";
            String returned = ns.resultCheck(result,token, owner, repo, sha, targetUrl);
            logger.info(returned);

            logger.info("CI job done");
            return "CI job done";
        }
    }

    public static void runBuild(GitHandler gh) {
        try {

            if (!gh.isRepoCloned()) {
                logger.error("repo not cloned");
                return;
            }

            File projectDir = gh.getLocalRepoDirFile();
            Build build = new Build();
            Build.BuildResult buildResult = build.runGradleBuild(projectDir);

            if (buildResult.isSuccess()) {
                logger.info("Build successful");
            } else {
                logger.error("Build failed: {}", buildResult.getOutput());
            }
        } catch (Exception e) {
            logger.error("Error occurred during build: {}", e.getMessage());
        }
    }

}