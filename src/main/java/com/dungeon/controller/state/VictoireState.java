package com.dungeon.controller.state;

import com.dungeon.controller.GameContext;
import com.dungeon.ui.panels.AnimationPanel;
import javafx.application.Platform;

/**
 * État de victoire - fin du donjon
 */
public class VictoireState implements GameState {
    
    @Override
    public void enter(GameContext context) {
        Platform.runLater(() -> 
            context.getGameUI().getAnimationPanel().changeScene(
                AnimationPanel.SceneType.VICTOIRE, 
                0
            )
        );
        
        context.getUiService().afficherMessage(
            "🎉 VICTOIRE ! Vous avez conquis " + 
            context.getSallesParcourues() + " salles !"
        );
        context.getUiService().afficherMessage("");
        context.getUiService().afficherMessage("Statistiques finales :");
        context.getUiService().afficherMessage("• Niveau : " + context.getJoueur().getNiveau());
        context.getUiService().afficherMessage("• PV : " + context.getJoueur().getPv() + "/" + context.getJoueur().getPvMax());
        context.getUiService().afficherMessage("• Attaque : " + context.getJoueur().getAttaque());
        context.getUiService().afficherMessage("• Ennemis tués : " + context.getJoueur().getEnnemisTues());
        context.getUiService().afficherMessage("• Boss vaincus : " + context.getJoueur().getBossVaincus());
        context.getUiService().afficherMessage("• Or restant : " + context.getJoueur().getPiecesOr());
        
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
        return "Victoire";
    }
}

