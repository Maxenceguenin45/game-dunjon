package com.dungeon.controller.state;

import com.dungeon.controller.GameContext;
import com.dungeon.model.item.arme.Arme;
import com.dungeon.model.salle.SalleItem;
import com.dungeon.ui.AnimationPanel;
import javafx.application.Platform;

/**
 * État de salle avec item/arme
 */
public class ItemState implements GameState {
    
    @Override
    public void enter(GameContext context) {
        Platform.runLater(() -> 
            context.getGameUI().getAnimationPanel().changeScene(
                AnimationPanel.SceneType.ITEM, 
                context.getNbChemins()
            )
        );
        
        SalleItem salleItem = (SalleItem) context.getSalleActuelle();
        
        if (salleItem.getItem() instanceof Arme) {
            gererArme(context, (Arme) salleItem.getItem());
        } else {
            gererAutreItem(context, salleItem);
        }
        
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
            
            int choix = context.getUiService().attendreChoix(options);
            
            if (choix == 0) {
                context.getJoueur().setArmeEquipee(arme);
                context.getUiService().afficherMessage("✅ Vous équipez " + arme.getNom() + " !");
                context.getUiService().afficherMessage("💡 Votre maîtrise repart de zéro avec cette nouvelle arme.");
                
                if (context.getJoueur().getInventaire().ajouterItem(armeActuelle)) {
                    context.getUiService().afficherMessage("📦 " + armeActuelle.getNom() + " ajoutée à l'inventaire.");
                } else {
                    context.getUiService().afficherMessage("⚠️ Inventaire plein ! " + armeActuelle.getNom() + " abandonnée.");
                }
            } else if (choix == 1) {
                context.getUiService().afficherMessage("🛡️ Vous gardez " + armeActuelle.getNom() + ".");
                
                if (context.getJoueur().getInventaire().ajouterItem(arme)) {
                    context.getUiService().afficherMessage("📦 " + arme.getNom() + " ajoutée à l'inventaire.");
                } else {
                    context.getUiService().afficherMessage("⚠️ Inventaire plein ! " + arme.getNom() + " abandonnée.");
                }
            } else {
                if (context.getJoueur().getInventaire().ajouterItem(arme)) {
                    context.getUiService().afficherMessage("📦 " + arme.getNom() + " ajoutée à l'inventaire.");
                } else {
                    context.getUiService().afficherMessage("⚠️ Inventaire plein ! " + arme.getNom() + " abandonnée.");
                }
            }
        } else {
            String[] options = {
                "⚔️ Équiper " + arme.getNom(),
                "📦 Mettre dans l'inventaire"
            };
            
            int choix = context.getUiService().attendreChoix(options);
            
            if (choix == 0) {
                context.getJoueur().setArmeEquipee(arme);
                context.getUiService().afficherMessage("✅ Vous équipez " + arme.getNom() + " !");
            } else {
                if (context.getJoueur().getInventaire().ajouterItem(arme)) {
                    context.getUiService().afficherMessage("📦 " + arme.getNom() + " ajoutée à l'inventaire.");
                } else {
                    context.getUiService().afficherMessage("⚠️ Inventaire plein ! " + arme.getNom() + " abandonnée.");
                }
            }
        }
        
        Platform.runLater(() -> 
            context.getGameUI().getGamePanel().getStatsPanel().updateStats(context.getJoueur())
        );
    }
    
    private void gererAutreItem(GameContext context, SalleItem salleItem) {
        context.getSalleActuelle().entrer(context.getJoueur());
        context.getUiService().afficherMessage("Vous avez trouvé un item !");
        
        Platform.runLater(() -> 
            context.getGameUI().getGamePanel().getStatsPanel().updateStats(context.getJoueur())
        );
        
        String[] options = {"Continuer"};
        context.getUiService().attendreChoix(options);
    }
}

