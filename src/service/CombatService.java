package service;

import ui.IGamePanel;
import util.GameConstants;
import util.GameMessages;
import exception.GameException;

/**
 * Service gérant les combats et leurs animations.
 */
public class CombatService {
    private final IGamePanel gamePanel;

    public CombatService(IGamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * Démarre l'animation de combat à la position spécifiée.
     *
     * @param x Position X de l'animation
     * @param y Position Y de l'animation
     */
    public void demarrerAnimation(int x, int y) {
        if (null != gamePanel) {
            gamePanel.startCombatAnimation(x + GameConstants.CELL_SIZE * 3,
                                         y + GameConstants.CELL_SIZE * 2);
        }
    }

    /**
     * Retourne les options disponibles pendant le combat.
     *
     * @param peutFuir True si le joueur peut fuir, false sinon
     * @param aDesItems True si le joueur a des items dans son inventaire
     * @return Tableau des options disponibles
     */
    public String[] getOptionsCombat(boolean peutFuir, boolean aDesItems) {
        if (peutFuir && aDesItems) {
            return new String[]{
                GameMessages.MSG_CHOIX_COMBAT,
                "Utiliser un item",
                GameMessages.MSG_CHOIX_FUITE
            };
        } else if (peutFuir) {
            return new String[]{
                GameMessages.MSG_CHOIX_COMBAT,
                GameMessages.MSG_CHOIX_FUITE
            };
        } else if (aDesItems) {
            return new String[]{
                GameMessages.MSG_CHOIX_COMBAT,
                "Utiliser un item"
            };
        }
        return new String[]{GameMessages.MSG_CHOIX_COMBAT};
    }

    /**
     * Attend la prochaine action du joueur.
     */
    public void attendreProchaineAction() {
        try {
            Thread.sleep(GameConstants.DELAI_ANIMATION_COMBAT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GameException("Animation interrompue", e);
        }
    }

    /**
     * Applique les dégâts de fuite au joueur.
     *
     * @param joueur Le joueur qui fuit
     * @return Message décrivant la fuite
     */
    public String appliquerDegatsRetraite(model.Joueur joueur) {
        int degatsRetraite = 10;
        joueur.setPv(joueur.getPv() - degatsRetraite);
        return "Vous battez en retraite ! Vous perdez " + degatsRetraite + " PV en fuyant.";
    }

    /**
     * Vérifie si le joueur a choisi d'utiliser un item.
     *
     * @param choix L'index du choix du joueur
     * @param aDesItems True si le joueur a des items
     * @return True si le joueur veut utiliser un item, false sinon
     */
    public boolean veutUtiliserItem(int choix, boolean aDesItems) {
        return aDesItems && choix == 1;
    }

    /**
     * Vérifie si le joueur a choisi de fuir.
     *
     * @param choix L'index du choix du joueur
     * @param aDesItems True si le joueur a des items
     * @return True si le joueur fuit, false sinon
     */
    public boolean doitFuir(int choix, boolean aDesItems) {
        if (aDesItems) {
            return choix == 2; // Fuir est en position 2 si on a des items
        }
        return choix == 1; // Fuir est en position 1 si on n'a pas d'items
    }
}
