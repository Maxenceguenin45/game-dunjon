package service;

import personnage.Joueur;

public class EnnemiService {
    public static void combattre(Joueur joueur, int pvEnnemi, int attaqueEnnemi) {
        // Le joueur attaque en premier
        pvEnnemi -= joueur.getAttaque();
        if (pvEnnemi > 0) {
            joueur.setPv(joueur.getPv() - attaqueEnnemi);
        }
    }

    public static void combattreBoss(Joueur joueur, int pvBoss, int attaqueBoss) {
        // Même logique que pour un ennemi, mais stats plus élevées
        combattre(joueur, pvBoss, attaqueBoss);
    }
}

