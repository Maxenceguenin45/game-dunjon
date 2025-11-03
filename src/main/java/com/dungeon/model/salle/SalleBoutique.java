package com.dungeon.model.salle;

import com.dungeon.model.personnage.Joueur;

/**
 * Salle boutique où le joueur peut réparer ses armes contre des pièces d'or
 */
public class SalleBoutique implements Salle {
    private static final int COUT_REPARATION_BASE = 10;
    
    public SalleBoutique() {
    }

    @Override
    public void entrer(Joueur joueur) {
        // La logique d'interaction avec la boutique sera gérée par le contrôleur
        // Cette méthode est appelée quand le joueur entre dans la salle
    }

    @Override
    public String getDescription() {
        return "Boutique de réparation - Réparez vos armes contre des pièces d'or";
    }

    /**
     * Calcule le coût de réparation pour une arme selon sa rareté et l'usure
     * @param durabiliteMax la durabilité maximale de l'arme
     * @param durabiliteActuelle la durabilité actuelle
     * @return le coût en pièces d'or
     */
    public static int calculerCoutReparation(int durabiliteMax, int durabiliteActuelle) {
        int pointsAReparer = durabiliteMax - durabiliteActuelle;
        if (pointsAReparer <= 0) {
            return 0;
        }
        // Formule : 10 pièces de base + 0.5 pièce par point de durabilité à réparer
        return COUT_REPARATION_BASE + (pointsAReparer / 2);
    }

    @Override
    public String toString() {
        return "Boutique de réparation";
    }
}

