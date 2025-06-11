package implementation;

import interfaces.Notification;
import model.Employe;
//mwvx gxkr uatt yqdd
public class NotificationConsole implements Notification {

    @Override
    public void notifier(Employe destinataire, String message) {
//      System.out.println("[NOTIF Console] Pour " + destinataire.getPrenom() + " " + destinataire.getNom() + " : " + message);
    }
}