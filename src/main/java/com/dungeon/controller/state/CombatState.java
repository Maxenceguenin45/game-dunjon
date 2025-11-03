package com.dungeon.controller.state;

import com.dungeon.controller.GameContext;
import com.dungeon.model.personnage.Joueur;
import com.dungeon.model.salle.*;
import com.dungeon.ui.AnimationPanel;
import javafx.application.Platform;

/**
 * État de combat contre un ennemi ou un boss
 */
public class CombatState implements GameState {
    private final boolean estBoss;
    
    public CombatState(boolean estBoss) {
        this.estBoss = estBoss;
    }
    
    @Override
    public void enter(GameContext context) {
        // Changer la scène
        Platform.runLater(() -> {
            AnimationPanel.SceneType scene = estBoss ? 
                AnimationPanel.SceneType.BOSS : 
                AnimationPanel.SceneType.COMBAT;
            context.getGameUI().getAnimationPanel().changeScene(scene, context.getNbChemins());
        });
        
        CombattantSalle ennemi = (CombattantSalle) context.getSalleActuelle();
        context.setPvEnnemiAvantAction(ennemi.getPv());
        
        // Boucle de combat
        while (ennemi.getPv() > 0 && context.getJoueur().getPv() > 0) {
            // Attaquer l'ennemi
            context.getSalleActuelle().entrer(context.getJoueur());
            
            Platform.runLater(() -> 
                context.getGameUI().getAnimationPanel().startSceneCombatAnimation(130, 345)
            );
            
            String messageCombat = context.getSalleService().traiterCombat(
                context.getSalleActuelle(), 
                context.getPvEnnemiAvantAction()
            );
            
            if (messageCombat != null) {
                context.getUiService().afficherMessage(messageCombat);
                Platform.runLater(() -> 
                    context.getGameUI().getGamePanel().getStatsPanel().updateStats(context.getJoueur())
                );
                
                // Vérifier si l'ennemi est vaincu
                if (ennemi.getPv() <= 0) {
                    gererVictoire(context, ennemi);
                    context.setState(new ExplorationState());
                    return;
                }
                
                // Vérifier si le joueur est mort
                if (context.getJoueur().getPv() <= 0) {
                    context.setState(new GameOverState());
                    return;
                }
                
                // L'ennemi est blessé mais vivant - proposer des actions
                Platform.runLater(() -> 
                    context.getGameUI().getAnimationPanel().changeScene(
                        AnimationPanel.SceneType.ENNEMI_BLESSE, 
                        context.getNbChemins()
                    )
                );
                
                boolean actionExecutee = false;
                while (!actionExecutee && context.getJoueur().getPv() > 0) {
                    boolean aDesItems = !context.getJoueur().getInventaire().estVide();
                    String[] options = context.getCombatService().getOptionsCombat(
                        context.getNbChemins() > 1, 
                        aDesItems
                    );
                    
                    int decision = context.getUiService().attendreChoix(options);
                    
                    if (context.getCombatService().veutUtiliserItem(decision, aDesItems)) {
                        // Utiliser un item
                        boolean itemUtilise = context.getUiService().gererInventaireCombat(context.getJoueur());
                        if (itemUtilise) {
                            Platform.runLater(() -> 
                                context.getGameUI().getGamePanel().getStatsPanel().updateStats(context.getJoueur())
                            );
                        }
                    } else if (context.getCombatService().doitFuir(decision, aDesItems)) {
                        // Fuir
                        String messageRetraite = context.getCombatService().appliquerDegatsRetraite(context.getJoueur());
                        context.getUiService().afficherMessage(messageRetraite);
                        Platform.runLater(() -> 
                            context.getGameUI().getGamePanel().getStatsPanel().updateStats(context.getJoueur())
                        );
                        
                        if (context.getJoueur().getPv() <= 0) {
                            context.getUiService().afficherMessage("Vous êtes mort en fuyant...");
                            context.setState(new GameOverState());
                        } else {
                            context.setState(new ExplorationState());
                        }
                        return;
                    } else {
                        // Continuer le combat
                        actionExecutee = true;
                        context.setPvEnnemiAvantAction(ennemi.getPv());
                    }
                }
            }
        }
    }
    
    @Override
    public void handleAction(GameContext context, int choix) {
        // Actions gérées dans la boucle de combat
    }
    
    @Override
    public void exit(GameContext context) {
        // Rien à faire
    }
    
    @Override
    public String getStateName() {
        return estBoss ? "Combat Boss" : "Combat Ennemi";
    }
    
    private void gererVictoire(GameContext context, CombattantSalle ennemi) {
        Platform.runLater(() -> 
            context.getGameUI().getAnimationPanel().changeScene(
                AnimationPanel.SceneType.VICTOIRE, 
                context.getNbChemins()
            )
        );
        
        // Gagner de l'expérience
        int xpGagnee = Joueur.calculerXpGagnee(
            context.getPvEnnemiAvantAction(), 
            ennemi.getAttaque(), 
            estBoss
        );
        
        String messageXp = context.getNiveauService().gagnerExperience(context.getJoueur(), xpGagnee);
        context.getUiService().afficherMessage(messageXp);
        
        // Gagner de l'or
        int orGagne = Joueur.calculerOrGagne(
            context.getPvEnnemiAvantAction(), 
            ennemi.getAttaque(), 
            estBoss
        );
        context.getJoueur().ajouterPiecesOr(orGagne);
        context.getUiService().afficherMessage(String.format("💰 Vous gagnez %d pièces d'or !", orGagne));
        
        // Donner des items
        String messageItem;
        if (estBoss) {
            SalleBoss boss = (SalleBoss) ennemi;
            int difficulte = boss.getAttaque() + 100;
            messageItem = context.getItemService().donnerItemsBoss(context.getJoueur(), difficulte);
            context.getJoueur().incrementerBossVaincus();
        } else {
            SalleEnnemi salleEnnemi = (SalleEnnemi) ennemi;
            int difficulte = salleEnnemi.getAttaque() * 2;
            messageItem = context.getItemService().donnerItemEnnemi(context.getJoueur(), difficulte);
            context.getJoueur().incrementerEnnemisTues();
        }
        
        if (messageItem != null) {
            context.getUiService().afficherMessage(messageItem);
        }
        
        Platform.runLater(() -> 
            context.getGameUI().getGamePanel().getStatsPanel().updateStats(context.getJoueur())
        );
    }
}

