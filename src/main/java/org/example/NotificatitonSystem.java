package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import static org.example.Main.logger;

public class NotificatitonSystem {

    public String resultCheck(String result, String token, String owner, String repo, String sha, String targetUrl) {

        String state;
        String description;

        if (result == "pass") {
            state = "success";
            description = "The build/test succeeded";
            return statusHandler(token, owner, repo, sha, state, targetUrl, description);
        } else if (result == "failed") {
            state = "fail";
            description = "The build/test failed";
            return statusHandler(token, owner, repo, sha, state, targetUrl, description);
        }

        return "bug";

    }
    public String statusHandler(String token, String owner, String repo, String sha, String state, String targetUrl, String description) {
        /* When a test result is received, the status handler resolve the result
         *
         * If failed, the handler calls the server to change the commit status to
         * failure(or error?)
         * 
         * Then the handler will call SOMETHING to send an email to the person who tried to commit
         * 
         * If success, the handler calls the server to change the commit status to
         * success
         * 
         * Question: when the current status is failure/error?
         * Question: when the current status is pending? (Assume that we just change it).
         */
        String context = "continuous-integration/spark";

        String requestBody = String.format("{\n" +
                                            "    \"state\": \"%s\",\n" +
                                            "    \"target_url\": \"%s\",\n" +
                                            "    \"description\": \"%s\",\n" +
                                            "    \"context\": \"%s\"\n" +
                                            "}", state, targetUrl, description, context);

        String requestURL = String.format("https://api.github.com/repos/%s/%s/statuses/%s", owner, repo, sha);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestURL))
                //.header("Accept", "application/vnd.github+json")
                .header("Accept", "application/vnd.github.v3+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .POST(BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            logger.info("GitHub status update response: " + response.statusCode() + " - " + response.body());
            System.out.println("GitHub status update response: " + response.statusCode() + " - " + response.body());

            
            return "Response status code: " + response.statusCode() + "\nResponse body: " + response.body();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to add Github status");
            return "Failed to add Github status";
        }
    }
}