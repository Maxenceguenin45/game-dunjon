package model.salle;

/**
 * Interface pour les salles qui peuvent combattre (ennemis et boss)
 */
public interface CombattantSalle extends Salle {
    int getPv();
    int getAttaque();
}
