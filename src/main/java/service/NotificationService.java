package service;

import interfaces.Notification;
import model.Employe;

import java.sql.*;
import java.util.*;

public class NotificationService {
    private final Set<Employe> abonnes = new HashSet<>();
    private final List<Notification> canauxNotification = new ArrayList<>(); // Liste des canaux (observers)

    public NotificationService() {
        createTableIfNotExists();
        chargerAbonnesDepuisBDD();
        createTableNotificationsConsole();
    }

    //  Permet d'ajouter un canal (Observer)
    public void ajouterCanalNotification(Notification canal) {
        canauxNotification.add(canal);
    }

    //  Créer la table ServiceNotification si elle n'existe pas
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
// creation de la table NotificationConsole
    private void createTableNotificationsConsole() {
        String sql = """
        CREATE TABLE IF NOT EXISTS notificationsConsole (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            destinataire_id INTEGER,
            message TEXT,
            lu BOOLEAN DEFAULT 0,
            date_envoi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (destinataire_id) REFERENCES employes(id)
        )
    """;

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //  Ajouter un employé abonné
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
            System.out.println(" Abonnement réussi !");
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                System.out.println(" Vous êtes déjà abonné (en base).");
            } else {
                System.out.println(" Erreur lors de l'abonnement : " + e.getMessage());
            }
        }
    }

    //  Vérifier si un employé est abonné
    public boolean estAbonne(Employe employe) {
        return abonnes.contains(employe);
    }

    //  Supprimer un employé abonné
    public void desabonner(Employe employe) {
        if (!estAbonne(employe)) {
            System.out.println(" Vous n'êtes pas encore abonné.");
            return;
        }
        abonnes.remove(employe);

        String sql = "DELETE FROM ServiceNotification WHERE employe_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employe.getId());
            pstmt.executeUpdate();
            System.out.println(" Désabonnement effectué.");
        } catch (SQLException e) {
            System.out.println("Erreur lors du désabonnement : " + e.getMessage());
        }
    }

    // Charger les abonnés depuis la BDD
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

    //  Envoyer un message à tous les abonnés sauf à l'expéditeur
    public void envoyerMessage(Employe expediteur, String message) {
        for (Employe abonne : abonnes) {
            if (!abonne.equals(expediteur)) {
                // Enregistrer dans la base (contenu sera formaté à l'intérieur de la méthode)
                enregistrerNotification(abonne.getId(), expediteur, message);

                // Afficher via les canaux (contenu formaté ici uniquement pour affichage)
                String contenuAffichage = "De " + expediteur.getPrenom() + " " + expediteur.getNom() + " : " + message;
                for (Notification canal : canauxNotification) {
                    canal.notifier(abonne, contenuAffichage);
                }
            }
        }
    }


    private void enregistrerNotification(int destinataireId, Employe expediteur, String message) {
        String contenu = "De " + expediteur.getPrenom() + " " + expediteur.getNom() + " : " + message;

        String sql = "INSERT INTO notificationsConsole (destinataire_id, message) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, destinataireId);
            pstmt.setString(2, contenu);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void afficherNotifications(Employe employe) {
        String sql = """
        SELECT id, message, date_envoi FROM notificationsConsole 
        WHERE destinataire_id = ? AND lu = 0
        ORDER BY date_envoi ASC
    """;

        List<Integer> idsMessagesLus = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, employe.getId());
            ResultSet rs = pstmt.executeQuery();

            boolean aDesMessages = false;

            while (rs.next()) {
                aDesMessages = true;
                int idNotif = rs.getInt("id");
                String message = rs.getString("message");
                String date = rs.getString("date_envoi");

                System.out.println("🔔 " + date + " - " + message);
                idsMessagesLus.add(idNotif); // enregistrer à marquer plus tard
            }

            if (!aDesMessages) {
                System.out.println("📭 Aucune nouvelle notification.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Étape 2 : marquer comme lues APRÈS avoir fermé le ResultSet
        for (Integer id : idsMessagesLus) {
            marquerCommeLue(id);
        }
    }

    private void marquerCommeLue(int notificationId) {
        String sql = "UPDATE notificationsConsole SET lu = 1 WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, notificationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
