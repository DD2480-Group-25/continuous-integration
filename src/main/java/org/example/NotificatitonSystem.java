package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import static org.example.Main.logger;

/**
 * Manages notifications for collaborators by updating commit statuses on GitHub API
 * and sending email notifications based on the results of build or test operations.
 */
public class NotificatitonSystem {

    /**
     * Evaluates the result of a build/test operation and modify the message based on the result, later calls other method to
     * update commit status on GitHub and sendan email. This method decides the notification state and message based on the operation
     * result ('pass' or 'failed').
     *
     * @param result    The result of the build/test operation ('pass' or 'failed').
     * @param token     The authentication token for GitHub API requests.
     * @param owner     The owner of the repository.
     * @param repo      The repository name.
     * @param sha       The SHA of the commit.
     * @param targetUrl The URL for more details about the result.
     * @return A string status handler's outcome or an error message.
     */
    public String resultCheck(String result, String token, String owner, String repo, String sha, String targetUrl) {
        String state;
        String description;

        SendEmail se = new SendEmail();

        // Evaluate the result and then set the state
        if (result.equals("pass")) {
            state = "success";
            description = "The build/test succeeded";
            se.sendEmail(description);
            return statusHandler(token, owner, repo, sha, state, targetUrl, description);
        } else if (result.equals("failed")) {
            state = "failure";
            description = "The build/test failed";
            se.sendEmail(description);
            return statusHandler(token, owner, repo, sha, state, targetUrl, description);
        }

        return "bug";

    }

    /**
     * Sends a request to GitHub's API to update the commit.
     * It constructs the request with the given parameters and handles the HTTP response.
     * This method is called internally after determining the result of build/test.
     *
     * @param token      The personal access token for GitHub API requests.
     * @param owner      The owner of the repository.
     * @param repo       The repository name.
     * @param sha        The SHA of the commit.
     * @param state      The new state of the commit, success or failure.
     * @param targetUrl  The URL for more details about the build/test result.
     * @param description A description of the build/test result.
     * @return A string containing the response status code and body, or an error message.
     */
    public String statusHandler(String token, String owner, String repo, String sha, String state, String targetUrl, String description) {
        String context = "continuous-integration/spark";
        String requestBody = String.format("{\n" +
                                            "    \"state\": \"%s\",\n" +
                                            "    \"target_url\": \"%s\",\n" +
                                            "    \"description\": \"%s\",\n" +
                                            "    \"context\": \"%s\"\n" +
                                            "}", state, targetUrl, description, context);

        String requestURL = String.format("https://api.github.com/repos/%s/%s/statuses/%s", owner, repo, sha);

        HttpClient client = HttpClient.newHttpClient();
        // Build the request (curl command) 
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestURL))
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .POST(BodyPublishers.ofString(requestBody))
                .build();
        

        try {
            // Sending the request using a client.
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            logger.info("GitHub status update response: " + response.statusCode() + " - " + response.body());
            return "Response status code: " + response.statusCode() + "\nResponse body: " + response.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted during the HTTP request", e);
            return "Failed to add Github status due to interruption";
        } catch (Exception e) { 
            logger.error("Exception during the HTTP request", e);
            return "Failed to add GitHub status";
        }
    }
}