package service;

import model.Administrateur;

import java.sql.*;

public class AdminService {

    public AdminService() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            String sql = """
                CREATE TABLE IF NOT EXISTS administrateurs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nom TEXT NOT NULL,
                    prenom TEXT NOT NULL,
                    email TEXT NOT NULL UNIQUE,
                    motDePasse TEXT NOT NULL
                );
                """;
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insererAdminParDefaut() {
        if (!adminExiste()) {
            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO administrateurs(nom, prenom, email, motDePasse) VALUES (?, ?, ?, ?)"
                 )) {
                stmt.setString(1, "Amadou");
                stmt.setString(2, "Bagayoko");
                stmt.setString(3, "admin@gmail.com");
                stmt.setString(4, "admin123");
                stmt.executeUpdate();
                System.out.println("Administrateur par défaut ajouté !");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean adminExiste() {
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM administrateurs")) {
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Administrateur seConnecter(String email, String motDePasse) {
        String sql = "SELECT * FROM administrateurs WHERE email = ? AND motDePasse = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, motDePasse);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Administrateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("motDePasse")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Échec de connexion
    }

}
