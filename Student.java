package mypackage;


import java.util.*;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * Created by TinTin on 6/26/16.
 * Process and Store student information to List class
 * Student information include their username and ID
 * Invoke method sendMail() to send every student
 * a receipt of their submission on that day
 */
public class Student {

    private static List<List<Objects>> studentInfo = new ArrayList<>();
    private static String USER_NAME = "jetbrain92";  // GMail user name
    private static String PASSWORD = "B3bygirl!"; // GMail password



    /**
     * Sotre student info
     * such as their username
     * and student ID
     * into List class
     * @param username
     * @param id
     */
    public void storeStudentInfo(String username, String id) {
        List<String> list = new ArrayList<>();
        list.add(username);
        list.add(id);
    }


    /**
     * Send email to recipient
     * by concatenating the username
     * with "@csus.edu"
     * @param email
     */

    public static void sendMail(String email) {

        email = email + "@csus.edu";
        String to[] = {email};

        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", USER_NAME);
        props.put("mail.smtp.password", PASSWORD);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(USER_NAME));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for (int i = 0; i < to.length; i++) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for (int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject("Your attendance");
            message.setText("Your submission was at: " + new java.util.Date());
            Transport transport = session.getTransport("smtp");
            transport.connect(host, USER_NAME, PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}