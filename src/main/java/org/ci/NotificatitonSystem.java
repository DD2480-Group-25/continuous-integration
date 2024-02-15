package org.ci;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import static org.ci.Main.logger;

/**
 * Manages the notification system for continuous integration events.
 */
public class NotificatitonSystem {

    /*public static void main(String[] args) {
        NotificatitonSystem ns = new NotificatitonSystem();
        
        String returned = ns.resultCheck("pass","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", "warlcang", "test", "f8378f85e2f998fbb13c554208f88cbea448eb0b", "https://example.com/build/status");
        System.out.println(returned);
    }*/

     /**
     * Checks the result of a build/test and handles notifications accordingly.
     *
     * @param result     the result of the build/test ("pass" or "failed")
     * @param token      the GitHub token for authorization
     * @param owner      the owner of the GitHub repository
     * @param repo       the name of the GitHub repository
     * @param sha        the SHA of the commit
     * @param targetUrl  the URL for the build/test status
     * @return a message indicating the result of the operation
     */
    public String resultCheck(String result, String token, String owner, String repo, String sha, String targetUrl) {
        String state;
        String description;

        SendEmail se = new SendEmail();

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
     * Handles the setting of the commit status on GitHub and sends notifications.
     *
     * @param token       the GitHub token for authorization
     * @param owner       the owner of the GitHub repository
     * @param repo        the name of the GitHub repository
     * @param sha         the SHA of the commit
     * @param state       the state of the commit ("success" or "failure")
     * @param targetUrl   the URL for the build/test status
     * @param description the description of the commit status
     * @return a message indicating the result of the operation
     */
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
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .POST(BodyPublishers.ofString(requestBody))
                .build();
        

        try {
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