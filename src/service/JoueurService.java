package service;

import model.Joueur;
import java.io.File;
import java.io.IOException;

public class JoueurService {
    private static final String SAVE_FILE = "save.txt";

    public Joueur creerJoueur(String pseudo) {
        return new Joueur(pseudo, 100, 20);
    }

    public boolean sauvegardeExiste() {
        return new File(SAVE_FILE).exists();
    }

    public Joueur chargerSauvegarde() throws IOException {
        return Joueur.loadFromFile(SAVE_FILE);
    }

    public String genererStatsJoueur(Joueur joueur) {
        if (null == joueur) {
            return "";
        }
        return String.format("PV: %d/%d | ATK: %d",
            joueur.getPv(), joueur.getPvMax(), joueur.getAttaque());
    }

    public String genererScoreFinal(Joueur joueur, int sallesParcourues) {
        if (null == joueur) {
            return "";
        }
        return String.format("Salles : %d | Ennemis : %d | Boss : %d",
            sallesParcourues, joueur.getEnnemisTues(), joueur.getBossVaincus());
    }
}
