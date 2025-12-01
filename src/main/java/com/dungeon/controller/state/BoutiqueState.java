package com.dungeon.controller.state;

import com.dungeon.controller.GameContext;
import com.dungeon.model.item.arme.Arme;
import com.dungeon.model.salle.SalleBoutique;
import com.dungeon.ui.panels.AnimationPanel;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * État de la boutique de réparation
 */
public class BoutiqueState implements GameState {
    
    @Override
    public void enter(GameContext context) {
        Platform.runLater(() -> 
            context.getGameUI().getAnimationPanel().changeScene(
                AnimationPanel.SceneType.ITEM, 
                context.getNbChemins()
            )
        );
        
        context.getUiService().afficherMessage("╔══════════════════════════════════════╗");
        context.getUiService().afficherMessage("║   🏪  BOUTIQUE DE RÉPARATION  🏪    ║");
        context.getUiService().afficherMessage("╚══════════════════════════════════════╝");
        context.getUiService().afficherMessage("");
        context.getUiService().afficherMessage("Bienvenue dans ma boutique, voyageur !");
        context.getUiService().afficherMessage("Je peux réparer vos armes usées contre des pièces d'or.");
        context.getUiService().afficherMessage("");
        context.getUiService().afficherMessage("💰 Votre or : " + context.getJoueur().getPiecesOr() + " pièces");
        context.getUiService().afficherMessage("");
        
        // Collecter toutes les armes (équipée + inventaire)
        List<Arme> armesAReparer = new ArrayList<>();
        List<String> descriptions = new ArrayList<>();
        List<Integer> couts = new ArrayList<>();
        
        // Arme équipée
        if (context.getJoueur().getArmeEquipee() != null) {
            Arme armeEquipee = context.getJoueur().getArmeEquipee();
            int cout = SalleBoutique.calculerCoutReparation(
                armeEquipee.getDurabiliteMax(), 
                armeEquipee.getDurabilite()
            );
            
            if (cout > 0) {
                armesAReparer.add(armeEquipee);
                couts.add(cout);
                descriptions.add(String.format(
                    "⚔️ %s [ÉQUIPÉE] - Durabilité: %d/%d (%.0f%%) - Coût: %d or",
                    armeEquipee.getNom(),
                    armeEquipee.getDurabilite(),
                    armeEquipee.getDurabiliteMax(),
                    armeEquipee.getPourcentageDurabilite(),
                    cout
                ));
            }
        }
        
        // Armes dans l'inventaire
        for (int i = 0; i < context.getJoueur().getInventaire().getNombreItems(); i++) {
            if (context.getJoueur().getInventaire().getItem(i) instanceof Arme arme) {
                int cout = SalleBoutique.calculerCoutReparation(
                    arme.getDurabiliteMax(), 
                    arme.getDurabilite()
                );
                
                if (cout > 0) {
                    armesAReparer.add(arme);
                    couts.add(cout);
                    descriptions.add(String.format(
                        "🗡️ %s - Durabilité: %d/%d (%.0f%%) - Coût: %d or",
                        arme.getNom(),
                        arme.getDurabilite(),
                        arme.getDurabiliteMax(),
                        arme.getPourcentageDurabilite(),
                        cout
                    ));
                }
            }
        }
        
        if (armesAReparer.isEmpty()) {
            context.getUiService().afficherMessage("✅ Toutes vos armes sont en parfait état !");
            context.getUiService().afficherMessage("");
            context.getUiService().afficherMessage("Le marchand vous souhaite bonne route.");
            
            String[] options = {"Continuer"};
            context.getUiService().attendreChoix(options);
            
            context.setState(new ExplorationState());
            return;
        }
        
        // Ajouter l'option "Ne rien réparer"
        descriptions.add("❌ Ne rien réparer (continuer l'aventure)");
        
        context.getUiService().afficherMessage("Quelle arme voulez-vous réparer ?");
        
        int choix = context.getUiService().attendreChoix(descriptions.toArray(new String[0]));
        
        if (choix < 0 || choix >= armesAReparer.size()) {
            // Ne rien réparer
            context.getUiService().afficherMessage("Vous décidez de ne rien réparer pour le moment.");
            context.setState(new ExplorationState());
            return;
        }
        
        Arme armeChoisie = armesAReparer.get(choix);
        int coutReparation = couts.get(choix);
        
        // Vérifier si le joueur a assez d'or
        if (context.getJoueur().getPiecesOr() < coutReparation) {
            context.getUiService().afficherMessage("❌ Vous n'avez pas assez d'or pour cette réparation !");
            context.getUiService().afficherMessage(String.format(
                "   Il vous manque %d pièces.", 
                coutReparation - context.getJoueur().getPiecesOr()
            ));
            
            String[] options = {"Continuer sans réparer"};
            context.getUiService().attendreChoix(options);
            
            context.setState(new ExplorationState());
            return;
        }
        
        // Réparer l'arme
        context.getJoueur().retirerPiecesOr(coutReparation);
        armeChoisie.reparer();
        
        context.getUiService().afficherMessage("✅ " + armeChoisie.getNom() + " a été réparée !");
        context.getUiService().afficherMessage(String.format(
            "   Durabilité restaurée : %d/%d", 
            armeChoisie.getDurabilite(), 
            armeChoisie.getDurabiliteMax()
        ));
        context.getUiService().afficherMessage("💰 Or restant : " + context.getJoueur().getPiecesOr() + " pièces");
        context.getUiService().afficherMessage("");
        context.getUiService().afficherMessage("Le marchand vous souhaite bonne route !");
        
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
        return "Boutique";
    }
}

