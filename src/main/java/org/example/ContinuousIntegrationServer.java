package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import javax.servlet.http.HttpServletResponse;

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

            logger.info("CI job done");
            return "CI job done";
        }
    }
}