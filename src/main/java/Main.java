import model.Administrateur;
import service.AdminService;
import service.EmployeService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialiser la BDD et l'admin
        AdminService adminService = new AdminService();
        adminService.insererAdminParDefaut();

        System.out.println("\n=== Bienvenue dans le Système de Notification ===");

        while (true) {
            System.out.println("\n Vouliez vous vous connecter entant : ?");
            System.out.println("1. Administrateur");
            System.out.println("2. Employé");
            System.out.println("3. Quitter");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();

            switch (choix) {
                case "1":
                    System.out.print("Email : ");
                    String email = scanner.nextLine();

                    System.out.print("Mot de passe : ");
                    String mdp = scanner.nextLine();

                    Administrateur admin = adminService.seConnecter(email, mdp);

                    if (admin != null) {
                        System.out.println("\n Connexion réussie !");
                        System.out.println("Bienvenue " + admin.getPrenom() + " " + admin.getNom());

                        EmployeService employeService = new EmployeService();

                        while (true) {
                            System.out.println("\n=== Menu Administrateur ===");
                            System.out.println("1. Ajouter un employé");
                            System.out.println("2. Lister les employés");
                            System.out.println("3. Supprimer un employé");
                            System.out.println("4. Retour au menu principal");
                            System.out.print("Votre choix : ");
                            String choixAdmin = scanner.nextLine();

                            switch (choixAdmin) {
                                case "1":
                                    System.out.print("Nom : ");
                                    String nomEmp = scanner.nextLine();
                                    System.out.print("Prénom : ");
                                    String prenomEmp = scanner.nextLine();
                                    System.out.print("Email : ");
                                    String emailEmp = scanner.nextLine();
                                    String motDePasselEmp = scanner.nextLine();
                                    System.out.print("Mot de Passe : ");
                                    employeService.ajouterEmploye(nomEmp, prenomEmp, emailEmp,motDePasselEmp);
                                    break;

                                case "2":
                                    System.out.println("\nListe des employés :");
                                    var liste = employeService.listerEmployes();
                                    for (var emp : liste) {
                                        System.out.println(emp.getId() + " - " + emp.getPrenom() + " " + emp.getNom() + " (" + emp.getEmail() + ")");
                                    }
                                    break;

//                                case "3":
//                                    System.out.print("ID de l'employé à supprimer : ");
//                                    int idSup = Integer.parseInt(scanner.nextLine());
//                                    employeService.supprimerEmploye(idSup);
//                                    break;

                                case "4":
                                    System.out.println("Retour au menu principal...");
                                    return;

                                default:
                                    System.out.println("Choix invalide.");
                            }
                        }

                    } else {
                        System.out.println(" Email ou mot de passe incorrect.");
                    }
                    break;

                case "2":
                    System.out.println("\n[EMPLOYÉ] - Connexion à venir...");
                    break;
                case "3":
                    System.out.println("Au revoir !");
                    return;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
}
