import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Joueur {
    private final String pseudo;
    private int pv;
    private final int pvMax;
    private int attaque;
    private int ennemisTues = 0;
    private int bossVaincus = 0;

    public Joueur(String pseudo, int pvMax, int attaque) {
        this.pseudo = pseudo;
        this.pvMax = pvMax;
        this.pv = pvMax;
        this.attaque = attaque;
    }

    // Ajout d'un constructeur pour restaurer les PV actuels (optionnel)
    public Joueur(String pseudo, int pvMax, int attaque, int pvActuel) {
        this.pseudo = pseudo;
        this.pvMax = pvMax;
        this.pv = pvActuel;
        this.attaque = attaque;
    }

    // Ajout d'un constructeur pour restaurer les compteurs
    public Joueur(String pseudo, int pvMax, int attaque, int pvActuel, int ennemisTues, int bossVaincus) {
        this.pseudo = pseudo;
        this.pvMax = pvMax;
        this.pv = pvActuel;
        this.attaque = attaque;
        this.ennemisTues = ennemisTues;
        this.bossVaincus = bossVaincus;
    }

    public void attaquer(Joueur cible) {
        cible.pv -= this.attaque;
        if (cible.pv < 0) {
            cible.pv = 0;
        }
    }

    public void soigner(int montant) {
        this.pv += montant;
        if (this.pv > this.pvMax) {
            this.pv = this.pvMax;
        }
    }

    public void augmenterAttaque(int montant) {
        this.attaque += montant;
    }

    public void setPv(int pv) {
        if (pv < 0) {
            this.pv = 0;
        } else if (pv > this.pvMax) {
            this.pv = this.pvMax;
        } else {
            this.pv = pv;
        }
    }

    public void setAttaque(int attaque) {
        if (attaque < 0) {
            this.attaque = 0;
        } else {
            this.attaque = attaque;
        }
    }

    public int getPv() {
        return pv;
    }

    public int getPvMax() {
        return pvMax;
    }

    public int getAttaque() {
        return attaque;
    }

    public String getPseudo() {
        return pseudo;
    }

    public int getEnnemisTues() {
        return ennemisTues;
    }

    public void setEnnemisTues(int ennemisTues) {
        this.ennemisTues = ennemisTues;
    }

    public int getBossVaincus() {
        return bossVaincus;
    }

    public void setBossVaincus(int bossVaincus) {
        this.bossVaincus = bossVaincus;
    }

    public void saveToFile(String filename) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            out.println(pseudo);
            out.println(pv);
            out.println(pvMax);
            out.println(attaque);
            out.println(ennemisTues);
            out.println(bossVaincus);
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    public static Joueur loadFromFile(String filename) {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String pseudo = in.readLine();
            int pv = Integer.parseInt(in.readLine());
            int pvMax = Integer.parseInt(in.readLine());
            int attaque = Integer.parseInt(in.readLine());
            int ennemisTues = 0;
            int bossVaincus = 0;
            String line = in.readLine();
            if (line != null) {
                ennemisTues = Integer.parseInt(line);
                line = in.readLine();
                if (line != null) {
                    bossVaincus = Integer.parseInt(line);
                }
            }
            return new Joueur(pseudo, pvMax, attaque, pv, ennemisTues, bossVaincus);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erreur lors du chargement : " + e.getMessage());
            return null;
        }
    }
}
