package com.dungeon.controller.state;

import com.dungeon.controller.GameContext;
import com.dungeon.ui.AnimationPanel;
import javafx.application.Platform;

/**
 * État de salle de soin
 */
public class SoinState implements GameState {
    
    @Override
    public void enter(GameContext context) {
        Platform.runLater(() -> 
            context.getGameUI().getAnimationPanel().changeScene(
                AnimationPanel.SceneType.SOIN, 
                context.getNbChemins()
            )
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
        context.getUiService().attendreChoix(options);
        
        context.setState(new ExplorationState());
    }
    
    @Override
    public void handleAction(GameContext context, int choix) {
        // Géré dans enter()
    }
    
    @Override
    public void exit(GameContext context) {
        // Rien à faire
    }
    
    @Override
    public String getStateName() {
        return "Soin";
    }
}

