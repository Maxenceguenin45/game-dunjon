package personnage;

import model.Inventaire;

import java.io.*;

/**
 * Représente le joueur dans le jeu
 */
public class Joueur implements Personnage {
    private String pseudo;
    private int pv;
    private int pvMax;
    private int attaque;
    private int ennemisTues;
    private int bossVaincus;
    private Inventaire inventaire;

    public Joueur(String pseudo, int pvMax, int attaque) {
        this.pseudo = pseudo;
        this.pvMax = pvMax;
        this.pv = pvMax;
        this.attaque = attaque;
        this.ennemisTues = 0;
        this.bossVaincus = 0;
        this.inventaire = new Inventaire();
    }

    // Getters
    public String getPseudo() {
        return pseudo;
    }

    @Override
    public int getPv() {
        return pv;
    }

    @Override
    public int getPvMax() {
        return pvMax;
    }

    @Override
    public int getAttaque() {
        return attaque;
    }

    public int getEnnemisTues() {
        return ennemisTues;
    }

    public int getBossVaincus() {
        return bossVaincus;
    }

    public Inventaire getInventaire() {
        return inventaire;
    }

    // Setters
    @Override
    public void setPv(int pv) {
        this.pv = Math.min(pv, pvMax);
    }

    public void setPvMax(int pvMax) {
        this.pvMax = pvMax;
    }

    @Override
    public void setAttaque(int attaque) {
        this.attaque = attaque;
    }

    public void setEnnemisTues(int ennemisTues) {
        this.ennemisTues = ennemisTues;
    }

    public void setBossVaincus(int bossVaincus) {
        this.bossVaincus = bossVaincus;
    }

    /**
     * Incrémente le compteur d'ennemis tués
     */
    public void incrementerEnnemisTues() {
        this.ennemisTues++;
    }

    /**
     * Incrémente le compteur de boss vaincus
     */
    public void incrementerBossVaincus() {
        this.bossVaincus++;
    }

    /**
     * Charge un joueur depuis un fichier
     * @param filename le nom du fichier
     * @return le joueur chargé
     * @throws IOException si une erreur d'I/O se produit
     */
    public static Joueur loadFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String pseudo = reader.readLine();
            int pv = Integer.parseInt(reader.readLine());
            int pvMax = Integer.parseInt(reader.readLine());
            int attaque = Integer.parseInt(reader.readLine());
            int ennemisTues = Integer.parseInt(reader.readLine());
            int bossVaincus = Integer.parseInt(reader.readLine());

            Joueur joueur = new Joueur(pseudo, pvMax, attaque);
            joueur.setPv(pv);
            joueur.setEnnemisTues(ennemisTues);
            joueur.setBossVaincus(bossVaincus);

            return joueur;
        }
    }

    /**
     * Sauvegarde le joueur dans un fichier
     * @param filename le nom du fichier
     * @throws IOException si une erreur d'I/O se produit
     */
    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(pseudo);
            writer.newLine();
            writer.write(String.valueOf(pv));
            writer.newLine();
            writer.write(String.valueOf(pvMax));
            writer.newLine();
            writer.write(String.valueOf(attaque));
            writer.newLine();
            writer.write(String.valueOf(ennemisTues));
            writer.newLine();
            writer.write(String.valueOf(bossVaincus));
            writer.newLine();
        }
    }

    @Override
    public String toString() {
        return String.format("%s (PV: %d/%d, ATK: %d)", pseudo, pv, pvMax, attaque);
    }
}

