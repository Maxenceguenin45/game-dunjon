package com.dungeon.controller.state;

import com.dungeon.model.salle.*;

/**
 * Factory pour créer le bon état selon le type de salle
 */
public class SalleStateFactory {
    
    public static GameState createState(Salle salle) {
        if (salle instanceof SalleBoss) {
            return new CombatState(true);
        } else if (salle instanceof SalleEnnemi) {
            return new CombatState(false);
        } else if (salle instanceof SalleSoin) {
            return new SoinState();
        } else if (salle instanceof SalleItem) {
            return new ItemState();
        } else if (salle instanceof SalleAmelioration) {
            return new AmeliorationState();
        } else if (salle instanceof SalleBoutique) {
            return new BoutiqueState();
        }
        
        // Par défaut, retour à l'exploration
        return new ExplorationState();
    }
}

