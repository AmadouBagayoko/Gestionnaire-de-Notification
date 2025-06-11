package implementation;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import model.Employe;
import  interfaces.Notification;

import java.util.Properties;

public class NotificationEmail implements  Notification {

    private final String username = "abagayoko304@gmail.com"; //
    private final String password = "mwvx gxkr uatt yqdd"; //

    @Override
    public void notifier(Employe employe, String message) {
        // Configuration SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Authentification
        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Créer le message
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(employe.getEmail())
            );
            mimeMessage.setSubject("📢 Nouvelle notification");
            mimeMessage.setText(message);

            // Envoyer
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
