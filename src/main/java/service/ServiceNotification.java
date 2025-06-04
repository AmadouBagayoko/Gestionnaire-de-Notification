package service;

import interfaces.Notification;
import model.Employe;

import java.util.ArrayList;
import java.util.List;

public class ServiceNotification {
    private final List<Employe> abonnes = new ArrayList<>();
    private final List<Notification> canaux = new ArrayList<>();

    public void ajouterAbonne(Employe e) {
        if (!abonnes.contains(e)) {
            abonnes.add(e);
            System.out.println(e.getNom() + " est maintenant abonné.");
        }
    }

    public void retirerAbonne(Employe e) {
        abonnes.remove(e);
        System.out.println(e.getNom() + " a été désabonné.");
    }

    public boolean estAbonne(Employe e) {
        return abonnes.contains(e);
    }

    public void ajouterCanal(Notification canal) {
        canaux.add(canal);
    }

    public void envoyerMessage(String message, Employe expediteur) {
        for (Employe abonne : abonnes) {
            if (!abonne.equals(expediteur)) {
                for (Notification canal : canaux) {
                    canal.envoyer(message + " (de " + expediteur.getNom() + ")", abonne);
                }
            }
        }
    }

    public List<Employe> getAbonnes() {
        return abonnes;
    }
}
