package com.dungeon.controller.state;

import com.dungeon.controller.GameContext;
import com.dungeon.model.item.Item;
import com.dungeon.model.item.arme.Arme;
import com.dungeon.model.salle.SalleItem;
import com.dungeon.ui.panels.AnimationPanel;
import javafx.application.Platform;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * État de salle avec item/arme
 */
public class ItemState implements GameState {
    private static final Logger logger = Logger.getLogger(ItemState.class.getName());
    private Arme armeAChoisir = null;
    private boolean isArme = false;

    @Override
    public void enter(GameContext context) {
        try {
            logger.info("Entrée dans ItemState");

            context.getGameUI().getAnimationPanel().changeScene(
                AnimationPanel.SceneType.ITEM,
                context.getNbChemins()
            );

            SalleItem salleItem = (SalleItem) context.getSalleActuelle();
            Item item = salleItem.getItem();

            if (item instanceof Arme) {
                isArme = true;
                armeAChoisir = (Arme) item;
                gererArme(context, armeAChoisir);
            } else {
                isArme = false;
                gererAutreItem(context, salleItem);
                // Pour les items non-armes, on continue directement
                context.setState(new ExplorationState());
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur dans ItemState.enter", e);
            context.setState(new ExplorationState());
        }
    }
    
    @Override
    public void handleAction(GameContext context, int choix) {
        try {
            if (!isArme || armeAChoisir == null) {
                context.setState(new ExplorationState());
                return;
            }

            Arme armeActuelle = context.getJoueur().getArmeEquipee();

            if (choix == 0) {
                // Équiper la nouvelle arme
                context.getJoueur().setArmeEquipee(armeAChoisir);
                context.getUiService().afficherMessage("✅ Vous équipez " + armeAChoisir.getNom() + " !");
                context.getUiService().afficherMessage("💡 Votre maîtrise repart de zéro avec cette nouvelle arme.");

                if (armeActuelle != null) {
                    if (context.getJoueur().getInventaire().ajouterItem(armeActuelle)) {
                        context.getUiService().afficherMessage("📦 " + armeActuelle.getNom() + " ajoutée à l'inventaire.");
                    } else {
                        context.getUiService().afficherMessage("⚠️ Inventaire plein ! " + armeActuelle.getNom() + " abandonnée.");
                    }
                }

            } else if (choix == 1) {
                // Garder l'arme actuelle
                context.getUiService().afficherMessage("🛡️ Vous gardez " + armeActuelle.getNom() + ".");

                if (context.getJoueur().getInventaire().ajouterItem(armeAChoisir)) {
                    context.getUiService().afficherMessage("📦 " + armeAChoisir.getNom() + " ajoutée à l'inventaire.");
                } else {
                    context.getUiService().afficherMessage("⚠️ Inventaire plein ! " + armeAChoisir.getNom() + " abandonnée.");
                }

            } else {
                // Mettre dans l'inventaire
                if (context.getJoueur().getInventaire().ajouterItem(armeAChoisir)) {
                    context.getUiService().afficherMessage("📦 " + armeAChoisir.getNom() + " ajoutée à l'inventaire.");
                } else {
                    context.getUiService().afficherMessage("⚠️ Inventaire plein ! " + armeAChoisir.getNom() + " abandonnée.");
                }
            }

            Platform.runLater(() ->
                context.getGameUI().getGamePanel().getStatsPanel().updateStats(context.getJoueur())
            );

            context.setState(new ExplorationState());

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur dans ItemState.handleAction", e);
            e.printStackTrace();
        }
    }
    
    @Override
    public void exit(GameContext context) {
        logger.info("Sortie de ItemState");
    }
    
    @Override
    public String getStateName() {
        return "Item";
    }
    
    private void gererArme(GameContext context, Arme arme) {
        context.getUiService().afficherMessage("╔══════════════════════════════════════╗");
        context.getUiService().afficherMessage("║        🗡️  ARME DÉCOUVERTE  🗡️       ║");
        context.getUiService().afficherMessage("╚══════════════════════════════════════╝");
        context.getUiService().afficherMessage("");
        context.getUiService().afficherMessage("Vous trouvez : " + arme.getNom());
        context.getUiService().afficherMessage("• Rareté : " + arme.getRarete());
        context.getUiService().afficherMessage("• Dégâts : " + arme.calculerDegats());
        context.getUiService().afficherMessage("• Durabilité : " + arme.getDurabilite() + "/" + arme.getDurabiliteMax());
        context.getUiService().afficherMessage("• Type : " + arme.getTypeAttaque());
        context.getUiService().afficherMessage("");
        
        Arme armeActuelle = context.getJoueur().getArmeEquipee();
        
        if (armeActuelle != null) {
            context.getUiService().afficherMessage("Arme actuelle : " + armeActuelle.getNom());
            context.getUiService().afficherMessage("• Dégâts actuels : " + armeActuelle.calculerDegatsTotal());
            context.getUiService().afficherMessage("• Niveau de maîtrise : " + armeActuelle.getNiveauMaitrise());
            context.getUiService().afficherMessage("• Durabilité : " + armeActuelle.getDurabilite() + "/" + armeActuelle.getDurabiliteMax());
            context.getUiService().afficherMessage("");
            
            String[] options = {
                "⚔️ Équiper " + arme.getNom() + " (+" + arme.calculerDegats() + " dégâts)",
                "🛡️ Garder " + armeActuelle.getNom() + " (+" + armeActuelle.calculerDegatsTotal() + " dégâts)",
                "📦 Mettre dans l'inventaire"
            };
            
            context.getUiService().attendreChoixAsync(options, context, this);

        } else {
            // Pas d'arme actuelle, équiper directement
            context.getJoueur().setArmeEquipee(arme);
            context.getUiService().afficherMessage("✅ Vous équipez " + arme.getNom() + " !");

            Platform.runLater(() ->
                context.getGameUI().getGamePanel().getStatsPanel().updateStats(context.getJoueur())
            );

            context.setState(new ExplorationState());
        }
    }

    private void gererAutreItem(GameContext context, SalleItem salleItem) {
        context.getUiService().afficherMessage("╔══════════════════════════════════════╗");
        context.getUiService().afficherMessage("║       📦  OBJET DÉCOUVERT  📦        ║");
        context.getUiService().afficherMessage("╚══════════════════════════════════════╝");
        context.getUiService().afficherMessage("");

        // La salle gère elle-même l'ajout de l'item au joueur
        salleItem.entrer(context.getJoueur());

        context.getUiService().afficherMessage("Vous trouvez : " + salleItem.getItem().getNom());
        context.getUiService().afficherMessage("📦 " + salleItem.getItem().getNom() + " ajouté à votre inventaire !");

        Platform.runLater(() ->
            context.getGameUI().getGamePanel().getStatsPanel().updateStats(context.getJoueur())
        );
    }
}

