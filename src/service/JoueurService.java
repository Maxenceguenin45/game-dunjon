package service;

import personnage.Joueur;
import item.arme.Epee;

import java.io.File;
import java.io.IOException;

public class JoueurService {
    private static final String SAVE_FILE = "save.txt";

    public Joueur creerJoueur(String pseudo) {
        Joueur joueur = new Joueur(pseudo, 100, 20);

        // Donner une épée de départ au joueur
        Epee epeeDepart = Epee.debutant();
        joueur.setArmeEquipee(epeeDepart);

        return joueur;
    }

    public boolean sauvegardeExiste() {
        return new File(SAVE_FILE).exists();
    }

    public Joueur chargerSauvegarde() throws IOException {
        return Joueur.loadFromFile(SAVE_FILE);
    }

    public void sauvegarderJoueur(Joueur joueur) throws IOException {
        joueur.saveToFile(SAVE_FILE);
    }

    public void supprimerSauvegarde() {
        File saveFile = new File(SAVE_FILE);
        if (saveFile.exists()) {
            saveFile.delete();
        }
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
