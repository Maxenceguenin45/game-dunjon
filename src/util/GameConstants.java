package util;

public final class GameConstants {
    // Dimensions
    public static final int LARGEUR_FENETRE = 800;
    public static final int HAUTEUR_FENETRE = 600;
    public static final int CELL_SIZE = 32;

    // Délais
    public static final int DELAI_ANIMATION_COMBAT = 500;
    public static final int DELAI_ENTRE_SALLES = 1000;

    // Combat
    public static final int PV_BASE = 100;
    public static final int ATK_BASE = 20;

    private GameConstants() {
        // Empêche l'instanciation
    }
}
