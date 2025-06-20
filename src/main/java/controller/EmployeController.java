package controller;

import model.Employe;
import service.GestionAbonnement;
import service.NotificationService;
import java.util.Scanner;

public class EmployeController {
    private final Scanner scanner;
    private final NotificationService notificationService;


    // Injection de NotificationService dans le contrôleur
    public EmployeController(Scanner scanner, NotificationService notificationService) {
        this.scanner = scanner;
        this.notificationService = notificationService;
    }

    public void afficherMenu(Employe employe) {
        System.out.println("\nConnexion réussie !");
        System.out.println("Bienvenue " + employe.getPrenom() + " " + employe.getNom());

        while (true) {
            System.out.println("\n==== MENU EMPLOYÉ ====");
            System.out.println("1. Voir mes notifications");
            System.out.println("2. Vérifier si je suis abonné");
            System.out.println("3. M’abonner");
            System.out.println("4. Me désabonner");
            System.out.println("5. Envoyer un message");
            System.out.println("6. Voir la liste des abonnés");
            System.out.println("7. Se déconnecter");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();
            System.out.println("\n");

            switch (choix) {
                case "1":
                    notificationService.afficherNotifications(employe);
                    break;

                case "2":
                    if (notificationService.estAbonne(employe)) {
                        System.out.println("Vous êtes abonné au service de notifications.");
                    } else {
                        System.out.println("Vous n'êtes pas abonné.");
                    }
                    break;

                case "3":
                    if (notificationService.estAbonne(employe)) {
                        System.out.println(" Vous êtes déjà abonné.");
                    } else {
                        notificationService.abonner(employe);
                        System.out.println(" Vous êtes maintenant abonné au service de notification.");
                    }
                    break;

                case "4":
                    if (notificationService.estAbonne(employe)) {
                        notificationService.desabonner(employe);
                        System.out.println("Vous vous êtes désabonné au service de Notification.");
                    } else {
                        System.out.println("Vous n'êtes pas encore abonné.");
                    }
                    break;

                case "5":
                    // Vérifie si l'employé est abonné
                    if (!notificationService.estAbonne(employe)) {
                        System.out.println(" Vous devez être abonné pour envoyer un message.");
                    } else {
                        System.out.print(" Entrez le message à envoyer : ");
                        //scanner.nextLine(); // pour consommer l'éventuel retour à la ligne
                        String message = scanner.nextLine();

                        // Envoie le message à tous les abonnés sauf l'expéditeur
                        notificationService.envoyerMessage(employe, message);

                        System.out.println(" Message envoyé aux abonnés !");
                    }
                    break;
                case "6":
                    GestionAbonnement gestionAbonnement= new GestionAbonnement();
                    gestionAbonnement.afficherAbonnes();
                    break;


                case "7":
                    System.out.println("Déconnexion...");
                    return;

                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
}
