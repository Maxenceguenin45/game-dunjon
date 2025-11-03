package com.dungeon.controller.state;

import com.dungeon.controller.GameContext;
import com.dungeon.model.personnage.Joueur;
import com.dungeon.ui.AnimationPanel;
import javafx.application.Platform;

/**
 * État du menu principal
 */
public class MenuState implements GameState {
    
    @Override
    public void enter(GameContext context) {
        Platform.runLater(() -> 
            context.getGameUI().getAnimationPanel().changeScene(AnimationPanel.SceneType.MENU, 0)
        );
        
        // Vérifier s'il y a une sauvegarde
        if (context.getJoueurService().sauvegardeExiste()) {
            chargerOuNouvellePartie(context);
        } else {
            creerNouvellePartie(context);
        }
    }
    
    @Override
    public void handleAction(GameContext context, int choix) {
        // Actions gérées directement dans enter()
    }
    
    @Override
    public void exit(GameContext context) {
        // Rien à faire
    }
    
    @Override
    public String getStateName() {
        return "Menu";
    }
    
    private void chargerOuNouvellePartie(GameContext context) {
        try {
            Joueur joueurSauvegarde = context.getJoueurService().chargerSauvegarde();
            if (joueurSauvegarde.getPv() > 0) {
                afficherMenuSauvegarde(context, joueurSauvegarde);
                
                String[] options = {
                    "▶️ Reprendre la partie sauvegardée",
                    "🆕 Nouvelle partie (écrase la sauvegarde)"
                };
                
                int choix = context.getUiService().attendreChoix(options);
                
                if (choix == 0) {
                    context.getUiService().afficherMessage("✅ Chargement de la partie sauvegardée...");
                    context.setJoueur(joueurSauvegarde);
                    Platform.runLater(() -> 
                        context.getGameUI().getGamePanel().getStatsPanel().updateStats(joueurSauvegarde)
                    );
                } else {
                    context.getUiService().afficherMessage("⚠️ Suppression de la sauvegarde précédente...");
                    context.getJoueurService().supprimerSauvegarde();
                    creerNouvellePartie(context);
                }
            } else {
                creerNouvellePartie(context);
            }
        } catch (Exception e) {
            context.getUiService().afficherMessage("❌ Erreur lors du chargement : " + e.getMessage());
            context.getUiService().afficherMessage("Démarrage d'une nouvelle partie...");
            creerNouvellePartie(context);
        }
        
        // Passer à l'état d'exploration
        context.setState(new ExplorationState());
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

