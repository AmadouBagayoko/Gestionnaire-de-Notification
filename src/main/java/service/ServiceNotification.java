package service;

import model.Employe;
import java.util.HashSet;
import java.util.Set;

public class ServiceNotification {
    private final Set<Employe> abonnes = new HashSet<>();

    // Ajouter un abonné
    public void abonner(Employe employe) {
        abonnes.add(employe);
        System.out.println(employe.getPrenom() + " est maintenant abonné au service de notification.");
    }

    // Vérifier si un employé est déjà abonné
    public boolean estAbonne(Employe employe) {
        return abonnes.contains(employe);
    }

    // Pour l'étape suivante : désabonner, envoyer message, etc.
}
