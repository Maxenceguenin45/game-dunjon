package com.dungeon.controller.state;

import com.dungeon.controller.GameContext;
import com.dungeon.model.personnage.Joueur;
import com.dungeon.model.salle.*;
import com.dungeon.ui.panels.AnimationPanel;
import javafx.application.Platform;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * État de combat contre un ennemi ou un boss
 */
public class CombatState implements GameState {
    private static final Logger logger = Logger.getLogger(CombatState.class.getName());
    private final boolean estBoss;

    public CombatState(boolean estBoss) {
        this.estBoss = estBoss;
    }

    @Override
    public void enter(GameContext context) {
        try {
            logger.info("Entrée dans CombatState (Boss: " + estBoss + ")");

            // Changer la scène
            AnimationPanel.SceneType scene = estBoss ?
                AnimationPanel.SceneType.BOSS :
                AnimationPanel.SceneType.COMBAT;
            context.getGameUI().getAnimationPanel().changeScene(scene, context.getNbChemins());

            CombattantSalle ennemi = (CombattantSalle) context.getSalleActuelle();
            context.setPvEnnemiAvantAction(ennemi.getPv());

            // Lancer le premier tour de combat
            executerTourCombat(context);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur dans enter()", e);
            context.setState(new ExplorationState());
        }
    }

    private void executerTourCombat(GameContext context) {
        try {
            CombattantSalle ennemi = (CombattantSalle) context.getSalleActuelle();

            // Vérifier si le combat est terminé
            if (ennemi.getPv() <= 0) {
                gererVictoire(context, ennemi);
                context.setState(new ExplorationState());
                return;
            }

            if (context.getJoueur().getPv() <= 0) {
                context.setState(new GameOverState());
                return;
            }

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
            }

            // Vérifier à nouveau après l'attaque
            if (ennemi.getPv() <= 0) {
                gererVictoire(context, ennemi);
                context.setState(new ExplorationState());
                return;
            }

            if (context.getJoueur().getPv() <= 0) {
                context.setState(new GameOverState());
                return;
            }

            // L'ennemi est blessé - proposer des actions
            context.getGameUI().getAnimationPanel().changeScene(
                AnimationPanel.SceneType.ENNEMI_BLESSE,
                context.getNbChemins()
            );

            boolean aDesItems = !context.getJoueur().getInventaire().estVide();
            String[] options = context.getCombatService().getOptionsCombat(
                context.getNbChemins() > 1,
                aDesItems
            );

            context.getUiService().attendreChoixAsync(options, context, this);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors du tour de combat", e);
            e.printStackTrace();
        }
    }

    @Override
    public void handleAction(GameContext context, int decision) {
        try {
            boolean aDesItems = !context.getJoueur().getInventaire().estVide();

            if (context.getCombatService().veutUtiliserItem(decision, aDesItems)) {
                // Utiliser un item
                boolean itemUtilise = context.getUiService().gererInventaireCombat(context.getJoueur());
                if (itemUtilise) {
                    Platform.runLater(() ->
                            context.getGameUI().getGamePanel().getStatsPanel().updateStats(context.getJoueur())
                    );
                }
                // Re-proposer les options après utilisation d'item
                boolean aDesItemsApres = !context.getJoueur().getInventaire().estVide();
                String[] options = context.getCombatService().getOptionsCombat(
                        context.getNbChemins() > 1,
                        aDesItemsApres
                );
                context.getUiService().attendreChoixAsync(options, context, this);

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

            } else {
                // Continuer le combat - préparer le prochain tour
                CombattantSalle ennemi = (CombattantSalle) context.getSalleActuelle();
                context.setPvEnnemiAvantAction(ennemi.getPv());
                executerTourCombat(context);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur dans handleAction", e);
            e.printStackTrace();
            context.setState(new ExplorationState());
        }
    }

    @Override
    public void exit(GameContext context) {
        logger.info("Sortie de CombatState");
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

        context.getJoueur().setPiecesOr(context.getJoueur().getPiecesOr() + orGagne);
        context.getUiService().afficherMessage(
            String.format("💰 Vous gagnez %d pièces d'or !", orGagne)
        );

        // Donner des items
        String messageItem;
        if (estBoss) {
            SalleBoss boss = (SalleBoss) ennemi;
            int difficulte = boss.getAttaque() + 100;
            messageItem = context.getItemService().donnerItemsBoss(context.getJoueur(), difficulte);
            context.getJoueur().incrementerBossVaincus();
            context.getUiService().afficherMessage("🏆 Boss vaincu !");
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

