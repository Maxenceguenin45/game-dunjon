package model.salle;

import personnage.Joueur;

/**
 * Interface de base pour toutes les salles du donjon
 */
public interface Salle {
    void entrer(Joueur joueur);

    String getDescription();
}
