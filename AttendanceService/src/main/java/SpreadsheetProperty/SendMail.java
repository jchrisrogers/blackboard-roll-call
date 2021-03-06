package SpreadsheetProperty;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by TinTin on 6/26/16.
 * Process and Store student information to List class
 * Student information include their username and ID
 * Invoke method sendMail() to send every student
 * a receipt of their submission on that day
 */
public class SendMail {
    private static String USER_NAME = "jetbrain92";  // GMail user name
    private static String PASSWORD = "B3bygirl!"; // GMail password


    /**
     * Send email to recipient
     * by concatenating the username
     * with "@csus.edu"
     * @param username
     */
    public SendMail(String username, String courseTitle) {

        String to[] = {username};           // Convert to String array

        Properties props = System.getProperties();
        String host = "smtp.gmail.com"; // Declare gmail as host

        // Set properties
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", USER_NAME);
        props.put("mail.smtp.password", PASSWORD);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);     // Create new message

        try {
            message.setFrom(new InternetAddress(USER_NAME));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for (int i = 0; i < to.length; i++) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            // Add recipient from the google mail address and send it to whomever
            for (InternetAddress toAddres : toAddress) {
                message.addRecipient(Message.RecipientType.TO, toAddres);
            }

            message.setSubject("Your attendance");  // Title
            message.setText("Your submission for" + courseTitle + " was at: " + new java.util.Date()); // Body of email
            Transport transport = session.getTransport("smtp");
            transport.connect(host, USER_NAME, PASSWORD);   // Connect to gmail
            transport.sendMessage(message, message.getAllRecipients()); // Send mail
            transport.close();  // Close the port

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
