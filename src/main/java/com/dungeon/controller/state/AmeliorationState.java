package com.dungeon.controller.state;

import com.dungeon.controller.GameContext;
import com.dungeon.model.item.arme.Arme;
import com.dungeon.model.salle.SalleAmelioration;
import com.dungeon.ui.AnimationPanel;
import javafx.application.Platform;

import java.util.List;

/**
 * État de salle d'amélioration/entraînement
 */
public class AmeliorationState implements GameState {
    
    @Override
    public void enter(GameContext context) {
        Platform.runLater(() -> 
            context.getGameUI().getAnimationPanel().changeScene(
                AnimationPanel.SceneType.AMELIORATION, 
                context.getNbChemins()
            )
        );
        
        SalleAmelioration salle = (SalleAmelioration) context.getSalleActuelle();
        List<Arme> armesDisponibles = salle.getArmesDisponibles(context.getJoueur());
        
        if (armesDisponibles.isEmpty()) {
            salle.entrer(context.getJoueur());
            context.getUiService().afficherMessage("⚒️ Salle d'entraînement");
            context.getUiService().afficherMessage("Vous vous entraînez avec vos poings ! Attaque +2");
            
            Platform.runLater(() -> 
                context.getGameUI().getGamePanel().getStatsPanel().updateStats(context.getJoueur())
            );
            
            String[] options = {"Continuer"};
            context.getUiService().attendreChoix(options);
            
            context.setState(new ExplorationState());
            return;
        }
        
        // Proposer le choix d'arme
        String[] optionsArmes = new String[armesDisponibles.size() + 1];
        for (int i = 0; i < armesDisponibles.size(); i++) {
            Arme arme = armesDisponibles.get(i);
            optionsArmes[i] = String.format(
                "%s (Niv.%d, +%.0f%% bonus, %d/%d XP)",
                arme.getNom(),
                arme.getNiveauMaitrise(),
                arme.getBonusMaitrise(),
                arme.getExperienceMaitrise(),
                arme.getExperienceRequise()
            );
        }
        optionsArmes[armesDisponibles.size()] = "Passer l'entraînement";
        
        context.getUiService().afficherMessage("⚔️ Salle d'entraînement : Choisissez une arme à maîtriser");
        
        int choix = context.getUiService().attendreChoix(optionsArmes);
        
        if (choix < 0 || choix >= armesDisponibles.size()) {
            context.getUiService().afficherMessage("Vous décidez de passer votre chemin.");
        } else {
            Arme armeChoisie = armesDisponibles.get(choix);
            String messageEntrainement = salle.entrainerAvecArme(context.getJoueur(), armeChoisie);
            
            for (String ligne : messageEntrainement.split("\n")) {
                context.getUiService().afficherMessage(ligne);
            }
            
            Platform.runLater(() -> 
                context.getGameUI().getGamePanel().getStatsPanel().updateStats(context.getJoueur())
            );
        }
        
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
        return "Amélioration";
    }
}

