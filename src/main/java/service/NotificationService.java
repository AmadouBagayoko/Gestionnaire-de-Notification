package service;

import model.Employe;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class NotificationService {
    private final Set<Employe> abonnes = new HashSet<>();

    public NotificationService() {
        createTableIfNotExists();
        chargerAbonnesDepuisBDD();
    }

    // Créer la table d’abonnement si elle n’existe pas
    private void createTableIfNotExists() {
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            String sql = """
                CREATE TABLE IF NOT EXISTS ServiceNotification (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    employe_id INTEGER UNIQUE,
                    FOREIGN KEY (employe_id) REFERENCES employes(id)
                )
            """;
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ajouter un employé aux abonnés
    public void abonner(Employe employe) {
        if (estAbonne(employe)) {
            System.out.println(" Vous êtes déjà abonné au service de notifications.");
            return;
        }

        abonnes.add(employe);

        String sql = "INSERT INTO ServiceNotification (employe_id) VALUES (?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employe.getId());
            pstmt.executeUpdate();
            System.out.println("Abonnement réussi !");
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                System.out.println("Vous êtes déjà abonné (en base).");
            } else {
                System.out.println(" Erreur lors de l'abonnement : " + e.getMessage());
            }
        }
    }

    // Vérifie si un employé est abonné
    public boolean estAbonne(Employe employe) {
        return abonnes.contains(employe);
    }

    // Supprimer un employé de la liste des abonnés
    public void desabonner(Employe employe) {
        if (!estAbonne(employe)) {
            System.out.println("️ Vous n'êtes pas encore abonné.");
            return;
        }

        abonnes.remove(employe);

        String sql = "DELETE FROM ServiceNotification WHERE employe_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employe.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(" Erreur lors du désabonnement : " + e.getMessage());
        }
    }

    // Charger les abonnés en mémoire depuis la BDD
    private void chargerAbonnesDepuisBDD() {
        String sql = "SELECT employes.id, nom, prenom, email, motDePasse FROM ServiceNotification " +
                "JOIN employes ON employes.id = ServiceNotification.employe_id";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Employe e = new Employe(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("motDePasse")
                );
                abonnes.add(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void envoyerMessage(Employe expediteur, String message) {
        for (Employe abonne : abonnes) {
            if (!abonne.equals(expediteur)) {
                abonne.recevoirNotification(
                        "De " + expediteur.getPrenom() + " " + expediteur.getNom() + " : " + message
                );
            }
        }
    }

}
