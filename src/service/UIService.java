package service;

import ui.IGamePanel;
import ui.GamePanel;
import ui.GameWindow;
import ui.AnimationPanel;
import util.GameMessages;
import personnage.Joueur;

public class UIService {
    public GameWindow creerFenetre() {
        return new GameWindow();
    }

    public void afficherMessage(IGamePanel panel, String message) {
        if (null != panel && null != message) {
            panel.setInfoMessage(message);
        }
    }

    public void afficherStats(IGamePanel panel, String stats) {
        if (null != panel && null != stats) {
            panel.setStats(stats);
        }
    }

    /**
     * Met à jour l'affichage des statistiques détaillées avec la barre de niveau
     */
    public void afficherStatsDetailles(IGamePanel panel, Joueur joueur) {
        if (panel instanceof GamePanel && joueur != null) {
            GamePanel gamePanel = (GamePanel) panel;
            gamePanel.setStatsDetailles(
                joueur.getNiveau(),
                joueur.getExperience(),
                joueur.getExperienceRequise(),
                joueur.getPv(),
                joueur.getPvMax(),
                joueur.getAttaque()
            );
        }
    }

    public String genererMessageGameOver(boolean victoire, String stats) {
        if (victoire) {
            return String.format("%s\n%s", GameMessages.MSG_VICTOIRE_FINALE, stats);
        }
        return String.format("%s\n%s", GameMessages.MSG_MORT, stats);
    }

    public String genererMessageSalle(int numero, String description) {
        return String.format("%s\n%s",
            String.format(GameMessages.MSG_FORMAT_SALLE, numero),
            String.format(GameMessages.MSG_FORMAT_AVANCE, description));
    }

    public void changerSceneAnimation(GameWindow window, AnimationPanel.SceneType scene) {
        if (null != window && null != window.getAnimationPanel()) {
            window.getAnimationPanel().setScene(scene);
        }
    }

    public void changerSceneAnimation(GameWindow window, AnimationPanel.SceneType scene, int nombreChoix) {
        if (null != window && null != window.getAnimationPanel()) {
            window.getAnimationPanel().setScene(scene, nombreChoix);
        }
    }

    // Nouveau: régler la précision visuelle de l'animation (1=normal, 2-3=plus précis)
    public void reglerQualiteAnimation(GameWindow window, int hdScale) {
        if (null != window && null != window.getAnimationPanel()) {
            window.getAnimationPanel().setHdScale(hdScale);
        }
    }
}
