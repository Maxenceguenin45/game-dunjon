package com.dungeon.controller.state;

import com.dungeon.controller.GameContext;
import com.dungeon.model.personnage.Joueur;
import com.dungeon.model.salle.Salle;
import com.dungeon.ui.panels.AnimationPanel;
import javafx.application.Platform;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * État d'exploration - choix entre plusieurs chemins
 */
public class ExplorationState implements GameState {
    private static final Logger logger = Logger.getLogger(ExplorationState.class.getName());
    private static final int MAX_SALLES = 50;
    
    @Override
    public String getStateName() {
        return "Exploration";
    }

    @Override
    public void enter(GameContext context) {
        try {
            // Incrémenter le compteur de salles
            context.incrementerSallesParcourues();

            // Vérifications de fin de jeu
            Joueur joueur = context.getJoueur();
            if (joueur == null || joueur.getPv() <= 0) {
                return; // GameContext.setState s'occupera du changement d'état
            }

            if (context.getSallesParcourues() >= MAX_SALLES) {
                return; // GameContext.setState s'occupera du changement d'état
            }

            // Sauvegarder
            try {
                context.getJoueurService().sauvegarderJoueur(joueur);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Erreur de sauvegarde", e);
            }

            // Générer les chemins
            final int nbChemins = context.getSalleService().genererNombreChemins();
            final String[] descriptions = new String[nbChemins + 1]; // +1 pour l'option village
            final Salle[] chemins = context.getSalleService().genererChemins(
                nbChemins, context.getSallesParcourues(), descriptions
            );

            // Ajouter l'option pour aller au village
            descriptions[nbChemins] = "🏘️ Quitter le donjon et aller au village";

            // Mettre à jour le contexte
            context.setNbChemins(nbChemins);
            context.setCheminsPossibles(chemins);
            context.setDescriptionsSalles(descriptions);

            // Changer la scène et attendre le choix
            context.getGameUI().getAnimationPanel().changeScene(
                AnimationPanel.SceneType.EXPLORATION, nbChemins
            );
            context.getUiService().attendreChoixAsync(descriptions, context, this);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur dans enter()", e);
        }
    }
    
    @Override
    public void handleAction(GameContext context, int choix) {
        try {
            // Vérifier si le joueur veut aller au village
            if (choix == context.getNbChemins()) {
                context.getUiService().afficherMessage("");
                context.getUiService().afficherMessage("Vous quittez le donjon et vous dirigez vers le village...");
                context.getUiService().afficherMessage("");
                context.setState(new VillageState());
                return;
            }

            // Validation du choix
            if (choix < 0 || choix >= context.getNbChemins()) {
                context.getUiService().afficherMessage("⚠️ Choix invalide");
                return;
            }

            // Récupérer la salle choisie
            Salle[] chemins = context.getCheminsPossibles();
            if (chemins == null || choix >= chemins.length) {
                logger.warning("Chemins invalides");
                return;
            }

            Salle salleChoisie = chemins[choix];
            if (salleChoisie == null) {
                logger.warning("Salle choisie invalide");
                return;
            }

            // Mise à jour du contexte
            context.setSalleActuelle(salleChoisie);

            // Message de transition
            String message = String.format("=== Salle %d : %s ===",
                context.getSallesParcourues(),
                context.getDescriptionsSalles()[choix]
            );
            context.getUiService().afficherMessage(message);

            // Créer et appliquer le prochain état
            GameState nextState = SalleStateFactory.createState(salleChoisie);
            if (nextState != null) {
                context.setState(nextState);
            } else {
                logger.warning("Impossible de créer l'état pour la salle");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur dans handleAction", e);
            e.printStackTrace();
            context.getUiService().afficherMessage("⚠️ Une erreur est survenue...");
        }
    }
    @Override
    public void exit(GameContext context) {
        // Pas d'opération spécifique nécessaire lors de la sortie
    }
}
