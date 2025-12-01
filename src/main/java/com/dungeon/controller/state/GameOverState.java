package com.dungeon.controller.state;

import com.dungeon.controller.GameContext;
import com.dungeon.ui.panels.AnimationPanel;
import javafx.application.Platform;

/**
 * État de game over
 */
public class GameOverState implements GameState {
    
    @Override
    public void enter(GameContext context) {
        Platform.runLater(() -> 
            context.getGameUI().getAnimationPanel().changeScene(
                AnimationPanel.SceneType.GAME_OVER, 
                0
            )
        );
        
        context.getUiService().afficherMessage(
            "💀 Game Over. Vous êtes mort après " + 
            context.getSallesParcourues() + " salles."
        );
        
        context.getJoueurService().supprimerSauvegarde();
        
        String[] options = {"Rejouer", "Quitter"};
        int choix = context.getUiService().attendreChoix(options);
        
        if (choix == 0) {
            context.getGameUI().getGamePanel().resetGame();
            context.setSallesParcourues(0);
            context.setState(new MenuState());
        } else {
            System.exit(0);
        }
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
        return "Game Over";
    }
}

