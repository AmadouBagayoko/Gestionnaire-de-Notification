package implementation;

import interfaces.Notification;
import model.Employe;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class NotificationEmail implements Notification {
    private final String username = "abagayoko304@gmail.com";
    private final String password = "ccix mwiz dgdn udux"; // mot de passe d'application

    @Override
    public void notifier(Employe employe, String message) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        //session.setDebug(true);

        try {
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(employe.getEmail()));
            mimeMessage.setSubject("📢 Nouvelle notification");
            mimeMessage.setText(message);

            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
