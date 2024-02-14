package org.example;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;


/**
 * Provides functionality to send email notifications through an SMTP server, 
 * specifically used for notifying users about the results of build or test operations.
 */
public class SendEmail {

    /**
     * Sends an email to a set of receivers(the group members).
     * SMTP with STARTTLS are used to set up the email session.
     * The construction of the email is also here.
     * It is designed for sending notifications about build/test results.
     *
     * @param content The body of the email message, containing build/test result.
     */

    public void sendEmail(String content) {
        // Recipient's email ID needs to be mentioned.
        String[] to = new String[]{"carlwang1124@outlook.com"};

        // Needed info
        String from = "carlwang@kth.se";
        final String username = "carlwang"; 
        final String password = "A13579jiahao"; 
        String host = "smtp.kth.se";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Get the Session object
        Session session = Session.getInstance(properties,
            new jakarta.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        try {
            // Create a default MimeMessage object
            MimeMessage message = new MimeMessage(session);

            // Set From: header field
            message.setFrom(new InternetAddress(from));

            // Set To: header field
            for (int i = 0; i < to.length; i++) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
            }

            // Set Subject: header field
            message.setSubject("Build/test result for the last commit");

            // Now set the actual message
            message.setText(content);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
}
