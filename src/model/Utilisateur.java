package model;

public class Utilisateur {
    private String mail;
    private String nom;
    private String prenom;

    public Utilisateur(String mail, String nom, String prenom) {
        this.mail = mail;
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}