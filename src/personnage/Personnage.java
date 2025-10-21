package personnage;

/**
 * Interface représentant un personnage du jeu (joueur ou ennemi)
 */
public interface Personnage {
    /**
     * Obtient les points de vie actuels du personnage
     * @return les PV actuels
     */
    int getPv();
    
    /**
     * Obtient les points de vie maximum du personnage
     * @return les PV max
     */
    int getPvMax();
    
    /**
     * Obtient l'attaque du personnage
     * @return la valeur d'attaque
     */
    int getAttaque();
    
    /**
     * Définit les points de vie du personnage
     * @param pv les nouveaux PV
     */
    void setPv(int pv);
    
    /**
     * Définit l'attaque du personnage
     * @param attaque la nouvelle valeur d'attaque
     */
    void setAttaque(int attaque);
    
    /**
     * Vérifie si le personnage est encore en vie
     * @return true si le personnage a des PV > 0
     */
    default boolean estVivant() {
        return getPv() > 0;
    }
    
    /**
     * Inflige des dégâts au personnage
     * @param degats les dégâts à infliger
     */
    default void recevoirDegats(int degats) {
        setPv(Math.max(0, getPv() - degats));
    }
    
    /**
     * Soigne le personnage
     * @param soin les points de vie à restaurer
     */
    default void soigner(int soin) {
        setPv(Math.min(getPvMax(), getPv() + soin));
    }
}

