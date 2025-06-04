package model;

public class Administrateur {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
   // constructeur
    public Administrateur(int id, String nom, String prenom, String email, String motDePasse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }

    // Setters
}
