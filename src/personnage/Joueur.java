package personnage;

import model.Inventaire;
import item.arme.Arme;

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

    // Arme équipée
    private Arme armeEquipee;

    // Système d'expérience et de niveau
    private int niveau;
    private int experience;
    private int experienceRequise;

    public Joueur(String pseudo, int pvMax, int attaque) {
        this.pseudo = pseudo;
        this.pvMax = pvMax;
        this.pv = pvMax;
        this.attaque = attaque;
        this.ennemisTues = 0;
        this.bossVaincus = 0;
        this.inventaire = new Inventaire();
        this.niveau = 1;
        this.experience = 0;
        this.experienceRequise = 100; // XP nécessaire pour le niveau 2
        this.armeEquipee = null;
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

    public int getNiveau() {
        return niveau;
    }

    public int getExperience() {
        return experience;
    }

    public int getExperienceRequise() {
        return experienceRequise;
    }

    public Arme getArmeEquipee() {
        return armeEquipee;
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

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setExperienceRequise(int experienceRequise) {
        this.experienceRequise = experienceRequise;
    }

    public void setArmeEquipee(Arme arme) {
        this.armeEquipee = arme;
    }

    /**
     * Calcule l'attaque totale du joueur avec le bonus de l'arme équipée
     */
    public int getAttaqueTotale() {
        if (armeEquipee == null) {
            return attaque;
        }

        // Attaque de base + dégâts de l'arme avec bonus de maîtrise
        return attaque + armeEquipee.calculerDegatsTotal();
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
     * Ajoute de l'expérience au joueur et gère les montées de niveau
     * @param xp l'expérience à ajouter
     * @return true si le joueur a gagné au moins un niveau
     */
    public boolean gagnerExperience(int xp) {
        this.experience += xp;
        boolean aGagneNiveau = false;

        // Gérer les montées de niveaux multiples
        while (this.experience >= this.experienceRequise) {
            monterNiveau();
            aGagneNiveau = true;
        }

        return aGagneNiveau;
    }

    /**
     * Fait monter le joueur d'un niveau et améliore ses stats
     */
    private void monterNiveau() {
        this.niveau++;
        this.experience -= this.experienceRequise;

        // Calculer l'XP requise pour le prochain niveau (formule exponentielle)
        this.experienceRequise = (int) (100 * Math.pow(1.5, niveau));

        // Amélioration des stats à chaque niveau
        int bonusPvMax = 10 + (niveau * 2); // +10, +12, +14, etc.
        int bonusAttaque = 3 + (niveau / 2); // +3, +3, +4, +4, +5, etc.

        this.pvMax += bonusPvMax;
        this.pv = this.pvMax; // Soigne complètement à chaque niveau
        this.attaque += bonusAttaque;
    }

    /**
     * Calcule l'XP gagnée en battant un ennemi
     * @param pvEnnemi les PV de l'ennemi
     * @param attaqueEnnemi l'attaque de l'ennemi
     * @param estBoss true si c'est un boss
     * @return l'XP gagnée
     */
    public static int calculerXpGagnee(int pvEnnemi, int attaqueEnnemi, boolean estBoss) {
        int xpBase = pvEnnemi + (attaqueEnnemi * 2);

        if (estBoss) {
            xpBase *= 3; // Les boss donnent 3x plus d'XP
        }

        return xpBase;
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

            // Charger les nouvelles données d'XP et niveau (compatibilité avec anciennes sauvegardes)
            int niveau = 1;
            int experience = 0;
            int experienceRequise = 100;

            String niveauLine = reader.readLine();
            if (niveauLine != null && !niveauLine.isEmpty()) {
                niveau = Integer.parseInt(niveauLine);
                experience = Integer.parseInt(reader.readLine());
                experienceRequise = Integer.parseInt(reader.readLine());
            }

            Joueur joueur = new Joueur(pseudo, pvMax, attaque);
            joueur.setPv(pv);
            joueur.setEnnemisTues(ennemisTues);
            joueur.setBossVaincus(bossVaincus);
            joueur.setNiveau(niveau);
            joueur.setExperience(experience);
            joueur.setExperienceRequise(experienceRequise);

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
            // Sauvegarder les données d'XP et niveau
            writer.write(String.valueOf(niveau));
            writer.newLine();
            writer.write(String.valueOf(experience));
            writer.newLine();
            writer.write(String.valueOf(experienceRequise));
            writer.newLine();
        }
    }

    @Override
    public String toString() {
        return String.format("%s (Niv.%d PV: %d/%d, ATK: %d, XP: %d/%d)",
            pseudo, niveau, pv, pvMax, attaque, experience, experienceRequise);
    }
}
