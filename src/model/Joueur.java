package model;

public class Joueur {
    private String pseudo;
    private int pv;
    private int pvMax;
    private int attaque;
    private int ennemisTues;
    private int bossVaincus;

    public Joueur(String pseudo, int pvMax, int attaque) {
        this.pseudo = pseudo;
        this.pvMax = pvMax;
        this.pv = pvMax;
        this.attaque = attaque;
        this.ennemisTues = 0;
        this.bossVaincus = 0;
    }

    public String getPseudo() { return pseudo; }
    public int getPv() { return pv; }
    public int getPvMax() { return pvMax; }
    public int getAttaque() { return attaque; }
    public int getEnnemisTues() { return ennemisTues; }
    public int getBossVaincus() { return bossVaincus; }

    public void setPv(int pv) { this.pv = Math.min(pv, pvMax); }
    public void setAttaque(int attaque) { this.attaque = attaque; }
    public void setEnnemisTues(int ennemisTues) { this.ennemisTues = ennemisTues; }
    public void setBossVaincus(int bossVaincus) { this.bossVaincus = bossVaincus; }

    public static Joueur loadFromFile(String filename) {
        // TODO: Implémenter le chargement depuis un fichier
        return null;
    }
}
