package model;

import java.io.*;

public class Joueur {
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

    public String getPseudo() { return pseudo; }
    public int getPv() { return pv; }
    public int getPvMax() { return pvMax; }
    public int getAttaque() { return attaque; }
    public int getEnnemisTues() { return ennemisTues; }
    public int getBossVaincus() { return bossVaincus; }
    public Inventaire getInventaire() { return inventaire; }

    public void setPv(int pv) { this.pv = Math.min(pv, pvMax); }
    public void setPvMax(int pvMax) { this.pvMax = pvMax; }
    public void setAttaque(int attaque) { this.attaque = attaque; }
    public void setEnnemisTues(int ennemisTues) { this.ennemisTues = ennemisTues; }
    public void setBossVaincus(int bossVaincus) { this.bossVaincus = bossVaincus; }

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
}
