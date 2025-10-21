public interface Salle {
    /**
     * Applique l'effet de la salle sur le joueur.
     * @param joueur le joueur qui entre dans la salle
     */
    void entrer(Joueur joueur);

    /**
     * Retourne une description de la salle.
     */
    String getDescription();
}

