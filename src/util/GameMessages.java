package util;

public final class GameMessages {
    public static final String[] GAME_OVER_ASCII = {
        "  ▄▄█████▄  ▄████▄  ██▄███▄    ▄████▄",
        " ██▀    ▀   ██▄▄▄██ ██▀  ▀██   ██▄▄▄██",
        " ██    ▄▄   ██▀▀▀██ ██    ██   ██▀▀▀██",
        "  ▀█████▀   ▀████▀  ███▄███▀   ▀████▀",
        "",
        "  ▄████▄  ██    ██ ▄████▄ ██████",
        " ██▄▄▄██  ██    ██ ██▄▄▄██ ██▄▄",
        " ██▀▀▀██   ██  ██  ██▀▀▀██ ██▀▀",
        "  ▀████▀     ██     ▀████▀ ██████"
    };

    public static final String[] ROOM_ASCII = {
        "╔═══╗\n║ ☠ ║\n╚═══╝",  // Ennemi
        "╔═♦═╗\n║ ★ ║\n╚═♦═╝",  // Boss
        "╔~✿~╗\n║ ♥ ║\n╚~✿~╝",  // Soin
        "╔═☯═╗\n║ ⚔ ║\n╚═☯═╝"   // Amélioration
    };

    public static final String[] COMBAT_ICONS = {
        "⚔️ ",  // Combat
        "🚪"    // Fuite
    };

    // Messages de combat
    public static final String MSG_VICTOIRE_ENNEMI = "Vous avez vaincu l'ennemi !";
    public static final String MSG_VICTOIRE_BOSS = "Vous avez vaincu le boss !";
    public static final String MSG_ATTAQUE_ENNEMI = "L'ennemi vous a attaqué. Il lui reste %d PV.";
    public static final String MSG_ATTAQUE_BOSS = "Le boss vous a attaqué. Il lui reste %d PV.";
    public static final String MSG_MORT = "Vous avez succombé à vos blessures...";
    public static final String MSG_VICTOIRE_FINALE = "Félicitations ! Vous avez survécu au donjon !";

    // Messages de salle
    public static final String MSG_SALLE_SOIN = "Vous trouvez une fontaine de soin. Vous récupérez 15%% de vos PV max.";
    public static final String MSG_SALLE_AMELIORATION = "Vous trouvez une salle d'entraînement. Votre attaque augmente de 5 !";
    public static final String MSG_FORMAT_SALLE = "=== SALLE %d ===";
    public static final String MSG_FORMAT_AVANCE = "Vous avancez vers %s...";

    // Messages de stats
    public static final String MSG_FORMAT_STATS = "PV: %d/%d | ATK: %d";
    public static final String MSG_FORMAT_SCORE = "Salles : %d | Ennemis : %d | Boss : %d";

    // Messages d'interface
    public static final String MSG_APPUYER_ENTREE = "Appuyez sur ENTRÉE pour continuer";
    public static final String MSG_CHOIX_COMBAT = "Continuer le combat";
    public static final String MSG_CHOIX_FUITE = "Reculer";

    private GameMessages() {
        // Empêche l'instanciation
    }
}
