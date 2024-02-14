package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

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

        // --- 0. Getting information from the payload ---

        @Override
        public Object handle(Request request, Response response) {
            response.type("text/html;charset=utf-8");
            response.status(HttpServletResponse.SC_OK);

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

            // --- 3. Running tests ---

            // --- 4. Providing feedback


            logger.info("CI job done");
            return "CI job done";
        }
    }
}