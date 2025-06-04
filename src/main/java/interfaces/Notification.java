package interfaces;

import model.Employe;

public interface Notification {
    void envoyer(String message, Employe destinataire);
}
