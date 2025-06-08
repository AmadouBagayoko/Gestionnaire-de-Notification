import controller.AdminController;
import controller.EmployeController;
import model.Administrateur;
import model.Employe;
import service.AdminService;
import service.EmployeService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        AdminService adminService = new AdminService();
        EmployeService employeService = new EmployeService();

        // Création d’un admin par défaut
        adminService.insererAdminParDefaut();

        System.out.println("\n=== Bienvenue dans le Système de Notification ===");

        while (true) {
            System.out.println("\nConnectez-vous en tant que :");
            System.out.println("1. Administrateur");
            System.out.println("2. Employé");
            System.out.println("3. Quitter");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();

            switch (choix) {
                case "1" -> {
                    System.out.print("Email : ");
                    String email = scanner.nextLine();
                    System.out.print("Mot de passe : ");
                    String mdp = scanner.nextLine();

                    Administrateur admin = adminService.seConnecter(email, mdp);
                    if (admin != null) {
                        new AdminController(scanner, employeService).afficherMenu(admin);
                    } else {
                        System.out.println("Email ou mot de passe incorrect.");
                    }
                }

                case "2" -> {
                    System.out.print("Email : ");
                    String emailEmp = scanner.nextLine();
                    System.out.print("Mot de passe : ");
                    String mdpEmp = scanner.nextLine();

                    Employe employe = employeService.seConnecter(emailEmp, mdpEmp);
                    if (employe != null) {
                        new EmployeController(scanner).afficherMenu(employe);
                    } else {
                        System.out.println("Email ou mot de passe incorrect.");
                    }
                }

                case "3" -> {
                    System.out.println("Au revoir !");
                    return;
                }

                default -> System.out.println("Choix invalide.");
            }
        }
    }
}
