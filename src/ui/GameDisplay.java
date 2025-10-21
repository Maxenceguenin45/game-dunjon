package ui;

public class GameDisplay {
    // Constantes pour les couleurs ANSI
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    
    // Symboles pour le labyrinthe
    private static final char MUR = '█';
    private static final char COULOIR = '·';
    private static final char JOUEUR = '@';
    private static final String[] SYMBOLES_SALLE = {
        RED + "☠" + RESET,      // Ennemi
        RED + "★" + RESET,      // Boss
        GREEN + "♥" + RESET,    // Soin
        YELLOW + "⚔" + RESET    // Amélioration
    };

    private final int LARGEUR = 40;
    private final int HAUTEUR = 20;
    private final String[][] carte;

    public GameDisplay() {
        carte = new String[HAUTEUR][LARGEUR];
        initialiserCarte();
    }

    private void initialiserCarte() {
        // Remplir la carte avec des murs
        for (int y = 0; y < HAUTEUR; y++) {
            for (int x = 0; x < LARGEUR; x++) {
                if (y == 0 || y == HAUTEUR-1 || x == 0 || x == LARGEUR-1) {
                    carte[y][x] = String.valueOf(MUR);
                } else {
                    carte[y][x] = String.valueOf(COULOIR);
                }
            }
        }
    }

    public void afficherCarte(int nbChemins, String[] descriptions, int posJoueur) {
        // Effacer l'écran
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // Titre
        System.out.println(BLUE + "=== DONJON ===" + RESET);
        System.out.println("Q: Gauche | D: Droite | ENTRÉE: Valider\n");

        // Placer les salles et le joueur
        for (int i = 0; i < nbChemins; i++) {
            int x = 5 + (i * 10);
            int y = HAUTEUR / 2;
            
            // Dessiner la salle
            dessinerSalle(x, y, i, descriptions[i]);
            
            // Placer le joueur
            if (i == posJoueur) {
                carte[y][x-2] = String.valueOf(JOUEUR);
            }
        }

        // Afficher la carte
        for (int y = 0; y < HAUTEUR; y++) {
            for (int x = 0; x < LARGEUR; x++) {
                System.out.print(carte[y][x]);
            }
            System.out.println();
        }

        // Légende
        System.out.println("\nLégende :");
        System.out.println("@ : Vous êtes ici");
        System.out.println(RED + "☠ : Ennemi" + RESET);
        System.out.println(RED + "★ : Boss" + RESET);
        System.out.println(GREEN + "♥ : Soin" + RESET);
        System.out.println(YELLOW + "⚔ : Amélioration" + RESET + "\n");

        // Descriptions détaillées
        for (int i = 0; i < nbChemins; i++) {
            if (i == posJoueur) {
                System.out.println(BLUE + "→ " + RESET + descriptions[i] + " ← Vous êtes ici");
            } else {
                System.out.println("  " + descriptions[i]);
            }
        }
    }

    private void dessinerSalle(int x, int y, int type, String description) {
        // Dessiner une salle avec son symbole
        carte[y-1][x] = String.valueOf(MUR);
        carte[y+1][x] = String.valueOf(MUR);
        carte[y][x-1] = String.valueOf(MUR);
        carte[y][x+1] = String.valueOf(MUR);
        carte[y][x] = SYMBOLES_SALLE[getSalleType(description)];
    }

    private int getSalleType(String description) {
        if (description.contains("Boss")) return 1;
        if (description.contains("soin")) return 2;
        if (description.contains("Amélioration") || description.contains("entraînement")) return 3;
        return 0;
    }
}
