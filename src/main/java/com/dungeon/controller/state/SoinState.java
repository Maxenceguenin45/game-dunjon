package com.dungeon.controller.state;

import com.dungeon.controller.GameContext;
import com.dungeon.ui.panels.AnimationPanel;
import javafx.application.Platform;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * État de salle de soin
 */
public class SoinState implements GameState {
    private static final Logger logger = Logger.getLogger(SoinState.class.getName());

    @Override
    public void enter(GameContext context) {
        try {
            logger.info("Entrée dans SoinState");

            context.getGameUI().getAnimationPanel().changeScene(
                AnimationPanel.SceneType.SOIN,
                context.getNbChemins()
            );

            int pvAvant = context.getJoueur().getPv();
            context.getSalleActuelle().entrer(context.getJoueur());

            int pvGagnes = context.getJoueur().getPv() - pvAvant;

            context.getUiService().afficherMessage("✚ Vous trouvez une fontaine de soin !");
            context.getUiService().afficherMessage(String.format(
                "Vous récupérez %d PV. PV actuels : %d/%d",
                pvGagnes,
                context.getJoueur().getPv(),
                context.getJoueur().getPvMax()
            ));

            Platform.runLater(() ->
                context.getGameUI().getGamePanel().getStatsPanel().updateStats(context.getJoueur())
            );

            String[] options = {"Continuer"};
            context.getUiService().attendreChoixAsync(options, context, this);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur dans SoinState.enter", e);
            context.setState(new ExplorationState());
        }
    }

    @Override
    public void handleAction(GameContext context, int choix) {
        try {
            logger.info("SoinState.handleAction: Passage à ExplorationState");
            context.setState(new ExplorationState());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur dans SoinState.handleAction", e);
            e.printStackTrace();
        }
    }

    @Override
    public void exit(GameContext context) {
        logger.info("Sortie de SoinState");
    }

    @Override
    public String getStateName() {
        return "Soin";
    }
}

