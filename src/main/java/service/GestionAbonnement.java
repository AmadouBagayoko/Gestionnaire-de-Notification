package service;

import model.Employe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestionAbonnement {

    public static List<Employe> getAbonnes() {
        List<Employe> abonnes = new ArrayList<>();
        String sql = "SELECT e.id, e.nom, e.prenom, e.email\n" +
                "FROM ServiceNotification sn\n" +
                "JOIN employes e ON sn.employe_id = e.id;\n";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Employe employe = new Employe();
                employe.setId(rs.getInt("id"));
                employe.setNom(rs.getString("nom"));
                employe.setPrenom(rs.getString("prenom"));
                employe.setEmail(rs.getString("email"));
                abonnes.add(employe);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des abonnés : " + e.getMessage());
        }

        return abonnes;
    }

    public static void afficherAbonnes() {
        List<Employe> liste = getAbonnes();
        if (liste.isEmpty()) {
            System.out.println(" Aucun abonné au service de notification.");
        } else {
            System.out.println(" ===Liste des abonnés===");
            for (Employe e : liste) {
                System.out.println("- " + e.getNom() + " " + e.getPrenom() + " (" + e.getEmail() + ")");
            }
        }
    }
}
