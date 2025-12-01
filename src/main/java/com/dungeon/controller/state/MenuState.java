package com.dungeon.controller.state;

import com.dungeon.controller.GameContext;
import com.dungeon.model.personnage.Joueur;
import com.dungeon.ui.panels.AnimationPanel;
import javafx.application.Platform;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * État du menu principal
 */
public class MenuState implements GameState {
    private static final Logger logger = Logger.getLogger(MenuState.class.getName());
    private boolean choixSauvegarde = false;
    private Joueur joueurSauvegarde = null;

    @Override
    public void enter(GameContext context) {
        logger.info("MenuState.enter() appelée");

        // Planifier l'initialisation du menu sur le thread JavaFX sans bloquer le thread de jeu
        Platform.runLater(() -> {
            logger.info("Initialisation du menu sur le thread JavaFX");
            try {
                // Afficher l'écran du menu
                context.getGameUI().getAnimationPanel().changeScene(AnimationPanel.SceneType.MENU, 0);

                // Vérifier s'il y a une sauvegarde
                boolean hasSave = context.getJoueurService().sauvegardeExiste();

                if (hasSave) {
                    try {
                        joueurSauvegarde = context.getJoueurService().chargerSauvegarde();
                        if (joueurSauvegarde != null && joueurSauvegarde.getPv() > 0) {
                            logger.info("Sauvegarde trouvée");
                            afficherMenuSauvegarde(context, joueurSauvegarde);

                            choixSauvegarde = true;
                            String[] options = {
                                "▶️ Reprendre la partie sauvegardée",
                                "🆕 Nouvelle partie (écrase la sauvegarde)"
                            };
                            context.getUiService().attendreChoixAsync(options, context, this);
                            return;
                        }
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Erreur lors du chargement de la sauvegarde", e);
                    }
                }

                // Créer une nouvelle partie
                logger.info("Création d'une nouvelle partie");
                creerNouvellePartie(context);

                choixSauvegarde = false;
                String[] options = {"Continuer"};
                context.getUiService().attendreChoixAsync(options, context, this);

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erreur lors de l'initialisation du menu", e);
                e.printStackTrace();
            }
        });
    }

    @Override
    public void handleAction(GameContext context, int choix) {
        logger.info("Gestion de l'action du menu: choix=" + choix);

        if (choixSauvegarde) {
            if (choix == 0) {
                context.getUiService().afficherMessage("✅ Chargement de la partie sauvegardée...");
                context.setJoueur(joueurSauvegarde);
                Platform.runLater(() ->
                    context.getGameUI().getGamePanel().getStatsPanel().updateStats(joueurSauvegarde)
                );
            } else {
                context.getUiService().afficherMessage("⚠️ Suppression de la sauvegarde précédente...");
                context.getJoueurService().supprimerSauvegarde();
                Joueur joueur = context.getJoueurService().creerJoueur("Aventurier");
                context.setJoueur(joueur);
                Platform.runLater(() ->
                    context.getGameUI().getGamePanel().getStatsPanel().updateStats(joueur)
                );
            }
        }

        logger.info("Passage à l'état ExplorationState");
        context.setState(new ExplorationState());
    }

    @Override
    public void exit(GameContext context) {
        logger.info("Sortie de MenuState");
    }

    @Override
    public String getStateName() {
        return "Menu";
    }

    private void afficherMenuSauvegarde(GameContext context, Joueur joueur) {
        context.getUiService().afficherMessage("╔══════════════════════════════════════╗");
        context.getUiService().afficherMessage("║   ⚔️  DUNGEON QUEST  ⚔️              ║");
        context.getUiService().afficherMessage("╚══════════════════════════════════════╝");
        context.getUiService().afficherMessage("");
        context.getUiService().afficherMessage("Une partie sauvegardée a été trouvée :");
        context.getUiService().afficherMessage("• Joueur : " + joueur.getPseudo());
        context.getUiService().afficherMessage("• Niveau : " + joueur.getNiveau());
        context.getUiService().afficherMessage("• PV : " + joueur.getPv() + "/" + joueur.getPvMax());
        context.getUiService().afficherMessage("• Attaque : " + joueur.getAttaque());
        context.getUiService().afficherMessage("• Or : " + joueur.getPiecesOr() + " pièces");
        context.getUiService().afficherMessage("");
    }

    private void creerNouvellePartie(GameContext context) {
        afficherBienvenue(context);

        Joueur joueur = context.getJoueurService().creerJoueur("Aventurier");
        context.setJoueur(joueur);

        Platform.runLater(() ->
            context.getGameUI().getGamePanel().getStatsPanel().updateStats(joueur)
        );

        afficherInfosJoueur(context, joueur);
    }

    private void afficherBienvenue(GameContext context) {
        context.getUiService().afficherMessage("╔══════════════════════════════════════╗");
        context.getUiService().afficherMessage("║   ⚔️  DUNGEON QUEST  ⚔️              ║");
        context.getUiService().afficherMessage("╚══════════════════════════════════════╝");
        context.getUiService().afficherMessage("");
        context.getUiService().afficherMessage("Bienvenue, aventurier !");
        context.getUiService().afficherMessage("");
        context.getUiService().afficherMessage("🆕 Création d'une nouvelle partie...");
        context.getUiService().afficherMessage("");
    }

    private void afficherInfosJoueur(GameContext context, Joueur joueur) {
        context.getUiService().afficherMessage("✅ Partie créée avec succès !");
        context.getUiService().afficherMessage("• Niveau : " + joueur.getNiveau());
        context.getUiService().afficherMessage("• PV : " + joueur.getPv() + "/" + joueur.getPvMax());
        context.getUiService().afficherMessage("• Attaque : " + joueur.getAttaque());
        context.getUiService().afficherMessage("• Or : " + joueur.getPiecesOr() + " pièces");
        context.getUiService().afficherMessage("");
        context.getUiService().afficherMessage("Bonne chance dans votre aventure !");
    }
}

