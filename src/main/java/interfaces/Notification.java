package interfaces;

import model.Employe;

public interface Notification {
    void notifier(Employe destinataire, String message);
}