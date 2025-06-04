package service;

import model.Employe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeService {

    public EmployeService() {
        creerTableEmploye();
    }

    private void creerTableEmploye() {
        String sql = """
            CREATE TABLE IF NOT EXISTS employes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT NOT NULL,
                prenom TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                motDePasse TEXT NOT NULL
            );
        """;

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ajouterEmploye(String nom, String prenom, String email, String motDePasse) {
        String sql = "INSERT INTO employes(nom, prenom, email,motDePasse) VALUES (?, ?, ?,?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, email);
            stmt.setString(4, motDePasse);
            stmt.executeUpdate();
            System.out.println("Employé ajouté !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    public List<Employe> listerEmployes() {
        List<Employe> liste = new ArrayList<>();
        String sql = "SELECT * FROM employes";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                liste.add(new Employe(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("motDePasse")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }
}
