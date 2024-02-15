package org.ci;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Utility class to send email notifications using JavaMail API.
 */
public class SendEmail {

    /**
     * Sends an email with the specified content.
     *
     * @param content the content of the email message
     */
    public void sendEmail(String content) {
        // Recipient's email ID needs to be mentioned.
        String[] to = new String[]{"carlwang1124@outlook.com"};

        // Sender's email ID and password needs to be mentioned
        String from = "carlwang@kth.se";
        final String username = "carlwang"; // username for email
        final String password = "A13579jiahao"; // password for email

        // Assuming you are sending email through Gmail
        String host = "smtp.kth.se";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS

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
