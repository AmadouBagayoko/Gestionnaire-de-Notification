package controller;

import model.Administrateur;
import model.Employe;
import service.EmployeService;
import service.GestionAbonnement;
import service.NotificationService;
import java.util.List;
import java.util.Scanner;

public class AdminController {
    private final Scanner scanner;
    private final EmployeService employeService;

    public AdminController(Scanner scanner, EmployeService employeService) {
        this.scanner = scanner;
        this.employeService = employeService;
    }

    public void afficherMenu(Administrateur admin) {
        System.out.println("\nConnexion réussie !");
        System.out.println("Bienvenue " + admin.getPrenom() + " " + admin.getNom());

        while (true) {
            System.out.println("\n=== Menu Administrateur ===");
            System.out.println("1. Ajouter un employé");
            System.out.println("2. Lister les employés");
            System.out.println("3. Supprimer un employé");
            System.out.println("4. Ajouter un abonné");
            System.out.println("5. Rétirer un abonné");
            System.out.println("6. Voir la liste des abonnés");
            System.out.println("7. Retour au menu principal");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();
            System.out.println("\n");
            switch (choix) {
                case "1" -> ajouterEmploye();
                case "2" -> listerEmployes();
                case "3" -> supprimerEmploye();

                case "4" -> { //  Ajouter un abonné
                    System.out.print("Entrez l'email de l'employé à abonner : ");
                    String email = scanner.nextLine();
                    Employe employe = EmployeService.trouverEmployeParEmail(email);
                    if (employe != null) {
                        NotificationService notificationService = new NotificationService();
                        notificationService.abonner(employe);
                    } else {
                        System.out.println(" Aucun employé trouvé avec cet email.");
                    }
                }

                case "5" -> { // Retirer un abonné
                    System.out.print("Entrez l'email de l'employé à désabonner : ");
                    String email = scanner.nextLine();
                    Employe employe = EmployeService.trouverEmployeParEmail(email);
                    if (employe != null) {
                        NotificationService notificationService = new NotificationService();
                        notificationService.desabonner(employe);
                    } else {
                        System.out.println(" Aucun employé trouvé avec cet email.");
                    }
                }

                case "6" ->{
                    GestionAbonnement gestionAbonnement= new GestionAbonnement();
                    gestionAbonnement.afficherAbonnes();
                }

                case "7" -> {
                    System.out.println("Retour au menu principal...");
                    return;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void ajouterEmploye() {
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        System.out.print("Prénom : ");
        String prenom = scanner.nextLine();
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();
        employeService.ajouterEmploye(nom, prenom, email, motDePasse);
    }

    private void listerEmployes() {
        List<Employe> liste = employeService.listerEmployes();
        if (liste.isEmpty()) {
            System.out.println("Aucun employé enregistré.");
        } else {
            System.out.println(" ===Liste des employés===");
            for (Employe emp : liste) {
                System.out.println(emp.getId() + " - " + emp.getPrenom() + " " + emp.getNom() + " (" + emp.getEmail() + ")");
            }
        }
    }

    private void supprimerEmploye() {
        System.out.print("ID de l'employé à supprimer : ");
        int id = Integer.parseInt(scanner.nextLine());
        employeService.supprimerEmploye(id);
    }
}
