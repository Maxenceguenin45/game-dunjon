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
     * @return Tableau des options disponibles
     */
    public String[] getOptionsCombat(boolean peutFuir) {
        if (peutFuir) {
            return new String[]{
                GameMessages.MSG_CHOIX_COMBAT,
                GameMessages.MSG_CHOIX_FUITE
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
     * Vérifie si le joueur a choisi de fuir.
     *
     * @param choix L'index du choix du joueur
     * @return True si le joueur fuit, false sinon
     */
    public boolean doitFuir(int choix) {
        return 1 == choix;
    }
}
