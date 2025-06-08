package controller;

import model.Employe;

import java.util.Scanner;

public class EmployeController {
    private final Scanner scanner;

    public EmployeController(Scanner scanner) {
        this.scanner = scanner;
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
            System.out.println("6. Se déconnecter");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();

            if (choix.equals("6")) break;

            // Les autres fonctionnalités seront implémentées plus tard.
            System.out.println("Fonctionnalité à venir...");
        }
    }
}
