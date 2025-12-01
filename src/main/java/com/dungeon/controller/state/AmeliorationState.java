package com.dungeon.controller.state;

import com.dungeon.controller.GameContext;
import com.dungeon.model.item.arme.Arme;
import com.dungeon.model.salle.SalleAmelioration;
import com.dungeon.ui.panels.AnimationPanel;
import javafx.application.Platform;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * État de salle d'amélioration/entraînement
 */
public class AmeliorationState implements GameState {
    private static final Logger logger = Logger.getLogger(AmeliorationState.class.getName());
    private List<Arme> armesDisponibles;
    private boolean choixArmeEffectue = false;

    @Override
    public void enter(GameContext context) {
        try {
            logger.info("Entrée dans AmeliorationState");

            context.getGameUI().getAnimationPanel().changeScene(
                AnimationPanel.SceneType.AMELIORATION,
                context.getNbChemins()
            );

            SalleAmelioration salle = (SalleAmelioration) context.getSalleActuelle();
            armesDisponibles = salle.getArmesDisponibles(context.getJoueur());

            if (armesDisponibles.isEmpty()) {
                // Pas d'armes disponibles, entraînement aux poings
                salle.entrer(context.getJoueur());
                context.getUiService().afficherMessage("⚒️ Salle d'entraînement");
                context.getUiService().afficherMessage("Vous vous entraînez avec vos poings ! Attaque +2");

                Platform.runLater(() ->
                    context.getGameUI().getGamePanel().getStatsPanel().updateStats(context.getJoueur())
                );

                choixArmeEffectue = true;
                String[] options = {"Continuer"};
                context.getUiService().attendreChoixAsync(options, context, this);
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
            context.getUiService().attendreChoixAsync(optionsArmes, context, this);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur dans AmeliorationState.enter", e);
            context.setState(new ExplorationState());
        }
    }

    @Override
    public void handleAction(GameContext context, int choix) {
        try {
            if (choixArmeEffectue) {
                // Deuxième choix : "Continuer" après l'entraînement
                context.setState(new ExplorationState());
                return;
            }

            // Premier choix : sélection de l'arme
            SalleAmelioration salle = (SalleAmelioration) context.getSalleActuelle();

            if (choix < 0 || choix >= armesDisponibles.size()) {
                // Passer l'entraînement
                context.getUiService().afficherMessage("Vous décidez de passer votre chemin.");
            } else {
                // S'entraîner avec l'arme choisie
                Arme armeChoisie = armesDisponibles.get(choix);
                String messageEntrainement = salle.entrainerAvecArme(context.getJoueur(), armeChoisie);

                for (String ligne : messageEntrainement.split("\n")) {
                    context.getUiService().afficherMessage(ligne);
                }

                Platform.runLater(() ->
                    context.getGameUI().getGamePanel().getStatsPanel().updateStats(context.getJoueur())
                );
            }

            // Proposer de continuer
            choixArmeEffectue = true;
            String[] options = {"Continuer"};
            context.getUiService().attendreChoixAsync(options, context, this);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur dans AmeliorationState.handleAction", e);
            e.printStackTrace();
        }
    }

    @Override
    public void exit(GameContext context) {
        logger.info("Sortie de AmeliorationState");
    }

    @Override
    public String getStateName() {
        return "Amélioration";
    }
}

