package model;

public class Employe {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private  String motDePasse;

    public Employe(int id, String nom, String prenom, String email,String motDePasse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse= motDePasse;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public  String getMotDePasse() {return  motDePasse;}


    public void recevoirNotification(String message) {
//        System.out.println("\u001B[32mNotification pour " + prenom + " " + nom + " : " + message + "\u001B[0m");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employe employe = (Employe) obj;
        return this.id == employe.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

}
