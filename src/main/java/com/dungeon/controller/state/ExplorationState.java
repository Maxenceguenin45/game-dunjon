package com.dungeon.controller.state;

import com.dungeon.controller.GameContext;
import com.dungeon.model.salle.Salle;
import com.dungeon.ui.AnimationPanel;
import javafx.application.Platform;

/**
 * État d'exploration - choix entre plusieurs chemins
 */
public class ExplorationState implements GameState {
    private static final int MAX_SALLES = 50;
    
    @Override
    public void enter(GameContext context) {
        context.incrementerSallesParcourues();
        
        // Vérifier la fin du jeu
        if (context.getJoueur().getPv() <= 0) {
            context.setState(new GameOverState());
            return;
        }
        
        if (context.getSallesParcourues() >= MAX_SALLES) {
            context.setState(new VictoireState());
            return;
        }
        
        // Sauvegarder automatiquement
        try {
            context.getJoueurService().sauvegarderJoueur(context.getJoueur());
        } catch (Exception e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
        
        // Générer les chemins
        int nbChemins = context.getSalleService().genererNombreChemins();
        String[] descriptions = new String[nbChemins];
        Salle[] chemins = context.getSalleService().genererChemins(
            nbChemins, 
            context.getSallesParcourues(), 
            descriptions
        );
        
        context.setNbChemins(nbChemins);
        context.setCheminsPossibles(chemins);
        context.setDescriptionsSalles(descriptions);
        
        // Changer la scène
        Platform.runLater(() -> 
            context.getGameUI().getAnimationPanel().changeScene(
                AnimationPanel.SceneType.EXPLORATION, 
                nbChemins
            )
        );
        
        // Demander le choix
        int choix = context.getUiService().attendreChoix(descriptions);
        handleAction(context, choix);
    }
    
    @Override
    public void handleAction(GameContext context, int choix) {
        if (choix < 0) {
            // Abandon
            context.setState(new GameOverState());
            return;
        }
        
        Salle salleChoisie = context.getCheminsPossibles()[choix];
        context.setSalleActuelle(salleChoisie);
        
        context.getUiService().afficherMessage(
            "=== Salle " + context.getSallesParcourues() + 
            " : " + context.getDescriptionsSalles()[choix] + " ==="
        );
        
        // Passer à l'état approprié selon le type de salle
        context.setState(SalleStateFactory.createState(salleChoisie));
    }
    
    @Override
    public void exit(GameContext context) {
        // Attendre avant la prochaine salle
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Override
    public String getStateName() {
        return "Exploration";
    }
}

