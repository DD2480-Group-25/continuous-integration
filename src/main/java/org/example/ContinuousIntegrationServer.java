package org.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.File;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

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
        @Override
        public Object handle(Request request, Response response) {
            response.type("text/html;charset=utf-8");
            response.status(HttpServletResponse.SC_OK);

            // Perform CI tasks here
            // For example:
            // 1. Clone your repository
            // 2. Compile the code

            // --- 1. Fetching changes ---

            GitHandler gh = new GitHandler(); // change with correct repo parameters

            // Maybe we should delete the repo completely each time to have a clean repo
            // We must also think about what happens if two concurrent request arrive at the same time, or maybe we don't care idk
            gh.deleteLocalRepo();
            gh.cloneRepo();

            String branch = "dummy-branch-for-testing"; // In the future get the name from the request

            gh.fetch(branch);

            if (gh.checkout(branch)) {
                gh.pull(branch);
            } else {
                logger.info(gh.getCurrentBranch());
                //return "Fatal error";
            }

            // --- 2. Building project ---
          
            runBuild(gh);

            // --- 3. Running tests ---

            // --- 4. Providing feedback
            NotificatitonSystem ns = new NotificatitonSystem();
            String result = "pass";
            String token = "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";
            String owner = "WarlCang";
            String repo = "test";
            String sha = "f8378f85e2f998fbb13c554208f88cbea448eb0b";
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