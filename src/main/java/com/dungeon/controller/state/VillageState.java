package com.dungeon.controller.state;

import com.dungeon.controller.GameContext;
import com.dungeon.model.item.Item;
import com.dungeon.model.item.arme.Arme;
import com.dungeon.model.item.armure.Armure;
import com.dungeon.ui.panels.AnimationPanel;
import javafx.application.Platform;

/**
 * État du village où le joueur peut vendre et réparer ses équipements
 */
public class VillageState implements GameState {

    private enum MenuType {
        PRINCIPAL, REPARATION, VENTE
    }

    private MenuType menuActuel = MenuType.PRINCIPAL;

    @Override
    public String getStateName() {
        return "Village";
    }

    @Override
    public void enter(GameContext context) {
        Platform.runLater(() ->
            context.getGameUI().getAnimationPanel().changeScene(
                AnimationPanel.SceneType.ITEM,
                context.getNbChemins()
            )
        );

        // Faire évoluer le marché à chaque visite
        context.getMarche().evoluer();

        afficherMenuPrincipal(context);
    }

    @Override
    public void exit(GameContext context) {
        // Nettoyage si nécessaire lors de la sortie de l'état
    }

    private void afficherMenuPrincipal(GameContext context) {
        menuActuel = MenuType.PRINCIPAL;

        context.getUiService().afficherMessage("╔══════════════════════════════════════╗");
        context.getUiService().afficherMessage("║        🏘️  VILLAGE  🏘️              ║");
        context.getUiService().afficherMessage("╚══════════════════════════════════════╝");
        context.getUiService().afficherMessage("");
        context.getUiService().afficherMessage("Bienvenue au village, " + context.getJoueur().getPseudo() + " !");
        context.getUiService().afficherMessage("💰 Votre or : " + context.getJoueur().getPiecesOr() + " pièces");
        context.getUiService().afficherMessage("");

        String[] options = {
            "🛠️ Réparer vos équipements",
            "💰 Vendre des objets",
            "📊 Consulter l'état du marché",
            "🗡️ Retourner au donjon"
        };

        context.getUiService().attendreChoixAsync(options, context, this);
    }

    @Override
    public void handleAction(GameContext context, int choix) {
        if (menuActuel == MenuType.PRINCIPAL) {
            switch (choix) {
                case 0 -> {
                    menuActuel = MenuType.REPARATION;
                    afficherMenuReparation(context);
                }
                case 1 -> {
                    menuActuel = MenuType.VENTE;
                    afficherMenuVente(context);
                }
                case 2 -> {
                    afficherEtatMarche(context);
                }
                case 3 -> {
                    context.getUiService().afficherMessage("");
                    context.getUiService().afficherMessage("Vous retournez au donjon...");
                    context.getUiService().afficherMessage("");
                    context.setState(new ExplorationState());
                }
            }
        }
        // Les menus REPARATION et VENTE sont gérés par leurs propres GameState anonymes
    }

    private void afficherMenuReparation(GameContext context) {
        context.getUiService().afficherMessage("");
        context.getUiService().afficherMessage("╔══════════════════════════════════════╗");
        context.getUiService().afficherMessage("║   🛠️  ATELIER DE RÉPARATION  🛠️     ║");
        context.getUiService().afficherMessage("╚══════════════════════════════════════╝");
        context.getUiService().afficherMessage("");
        context.getUiService().afficherMessage("💰 Votre or : " + context.getJoueur().getPiecesOr() + " pièces");
        context.getUiService().afficherMessage("");

        // Compter les équipements à réparer
        int nbARéparer = 0;

        // Vérifier arme équipée
        if (context.getJoueur().getArmeEquipee() != null) {
            Arme arme = context.getJoueur().getArmeEquipee();
            if (arme.getDurabilite() < arme.getDurabiliteMax()) {
                int cout = (arme.getDurabiliteMax() - arme.getDurabilite()) * 2;
                context.getUiService().afficherMessage(String.format(
                    "⚔️ %s [ÉQUIPÉE] - Durabilité: %d/%d - Coût: %d or",
                    arme.getNom(), arme.getDurabilite(), arme.getDurabiliteMax(), cout
                ));
                nbARéparer++;
            }
        }

        // Vérifier armure équipée
        if (context.getJoueur().getArmureEquipee() != null) {
            Armure armure = context.getJoueur().getArmureEquipee();
            if (armure.getDurabilite() < armure.getDurabiliteMax()) {
                int cout = (armure.getDurabiliteMax() - armure.getDurabilite()) * 3;
                context.getUiService().afficherMessage(String.format(
                    "🛡️ %s [ÉQUIPÉE] - Durabilité: %d/%d - Coût: %d or",
                    armure.getNom(), armure.getDurabilite(), armure.getDurabiliteMax(), cout
                ));
                nbARéparer++;
            }
        }

        // Vérifier inventaire
        for (int i = 0; i < context.getJoueur().getInventaire().getNombreItems(); i++) {
            Item item = context.getJoueur().getInventaire().getItem(i);
            if (item instanceof Arme arme && arme.getDurabilite() < arme.getDurabiliteMax()) {
                int cout = (arme.getDurabiliteMax() - arme.getDurabilite()) * 2;
                context.getUiService().afficherMessage(String.format(
                    "🗡️ %s - Durabilité: %d/%d - Coût: %d or",
                    arme.getNom(), arme.getDurabilite(), arme.getDurabiliteMax(), cout
                ));
                nbARéparer++;
            } else if (item instanceof Armure armure && armure.getDurabilite() < armure.getDurabiliteMax()) {
                int cout = (armure.getDurabiliteMax() - armure.getDurabilite()) * 3;
                context.getUiService().afficherMessage(String.format(
                    "🛡️ %s - Durabilité: %d/%d - Coût: %d or",
                    armure.getNom(), armure.getDurabilite(), armure.getDurabiliteMax(), cout
                ));
                nbARéparer++;
            }
        }

        if (nbARéparer == 0) {
            context.getUiService().afficherMessage("✅ Tous vos équipements sont en parfait état !");
        } else {
            context.getUiService().afficherMessage("");
            context.getUiService().afficherMessage("NOTE: Les réparations seront disponibles dans une future version.");
        }

        context.getUiService().afficherMessage("");
        String[] options = {"Retour au village"};
        menuActuel = MenuType.PRINCIPAL;
        context.getUiService().attendreChoixAsync(options, context, new GameState() {
            @Override
            public void enter(GameContext ctx) {}
            @Override
            public void exit(GameContext ctx) {}
            @Override
            public String getStateName() { return "VillageReparation"; }
            @Override
            public void handleAction(GameContext ctx, int c) {
                afficherMenuPrincipal(ctx);
            }
        });
    }

    private void afficherMenuVente(GameContext context) {
        context.getUiService().afficherMessage("");
        context.getUiService().afficherMessage("╔══════════════════════════════════════╗");
        context.getUiService().afficherMessage("║      💰  MARCHAND  💰               ║");
        context.getUiService().afficherMessage("╚══════════════════════════════════════╝");
        context.getUiService().afficherMessage("");
        context.getUiService().afficherMessage("Je rachète tous vos objets à bon prix !");
        context.getUiService().afficherMessage("💰 Votre or : " + context.getJoueur().getPiecesOr() + " pièces");
        context.getUiService().afficherMessage("");

        int nbItems = context.getJoueur().getInventaire().getNombreItems();

        if (nbItems == 0) {
            context.getUiService().afficherMessage("Votre inventaire est vide !");
            context.getUiService().afficherMessage("");
            String[] options = {"Retour au village"};
            menuActuel = MenuType.PRINCIPAL;
            context.getUiService().attendreChoixAsync(options, context, new GameState() {
                @Override
                public void enter(GameContext ctx) {}
                @Override
                public void exit(GameContext ctx) {}
                @Override
                public String getStateName() { return "VillageVente"; }
                @Override
                public void handleAction(GameContext ctx, int c) {
                    afficherMenuPrincipal(ctx);
                }
            });
            return;
        }

        // Afficher les objets disponibles à la vente
        context.getUiService().afficherMessage("Sélectionnez un objet à vendre :");
        context.getUiService().afficherMessage("");

        String[] options = new String[nbItems + 1];
        for (int i = 0; i < nbItems; i++) {
            Item item = context.getJoueur().getInventaire().getItem(i);
            int prix = calculerPrixVente(context, item);

            String symbole = "📦";
            if (item instanceof Arme) symbole = "⚔️";
            else if (item instanceof Armure) symbole = "🛡️";

            String durabilite = "";
            if (item instanceof Arme arme) {
                durabilite = String.format(" [%d/%d]", arme.getDurabilite(), arme.getDurabiliteMax());
            } else if (item instanceof Armure armure) {
                durabilite = String.format(" [%d/%d]", armure.getDurabilite(), armure.getDurabiliteMax());
            }

            options[i] = String.format("%s %s%s - %d or", symbole, item.getNom(), durabilite, prix);
        }
        options[nbItems] = "❌ Retour au village";

        menuActuel = MenuType.PRINCIPAL;
        context.getUiService().attendreChoixAsync(options, context, new GameState() {
            @Override
            public void enter(GameContext ctx) {}
            @Override
            public void exit(GameContext ctx) {}
            @Override
            public String getStateName() { return "VillageVente"; }
            @Override
            public void handleAction(GameContext ctx, int choix) {
                if (choix == nbItems) {
                    // Retour au village
                    afficherMenuPrincipal(ctx);
                } else {
                    // Vendre l'objet sélectionné
                    vendreObjet(ctx, choix);
                }
            }
        });
    }

    private void vendreObjet(GameContext context, int index) {
        Item item = context.getJoueur().getInventaire().getItem(index);
        if (item == null) {
            context.getUiService().afficherMessage("❌ Objet invalide !");
            afficherMenuVente(context);
            return;
        }

        // Vérifier si l'objet est équipé
        boolean estEquipe = false;
        if (item instanceof Arme && item.equals(context.getJoueur().getArmeEquipee())) {
            estEquipe = true;
        } else if (item instanceof Armure && item.equals(context.getJoueur().getArmureEquipee())) {
            estEquipe = true;
        }

        if (estEquipe) {
            context.getUiService().afficherMessage("⚠️ Vous ne pouvez pas vendre un objet équipé !");
            context.getUiService().afficherMessage("   Déséquipez-le d'abord depuis votre inventaire.");
            context.getUiService().afficherMessage("");

            String[] options = {"Retour au marché"};
            context.getUiService().attendreChoixAsync(options, context, new GameState() {
                @Override
                public void enter(GameContext ctx) {}
                @Override
                public void exit(GameContext ctx) {}
                @Override
                public String getStateName() { return "VillageVenteErreur"; }
                @Override
                public void handleAction(GameContext ctx, int c) {
                    afficherMenuVente(ctx);
                }
            });
            return;
        }

        int prix = calculerPrixVente(context, item);

        // Retirer l'objet de l'inventaire et ajouter l'or
        Item itemVendu = context.getJoueur().getInventaire().retirerItem(index);
        context.getJoueur().ajouterPiecesOr(prix);

        context.getUiService().afficherMessage("");
        context.getUiService().afficherMessage("✅ Vous avez vendu : " + itemVendu.getNom());
        context.getUiService().afficherMessage("💰 Vous recevez : " + prix + " pièces d'or");

        // Afficher l'influence du marché
        double demande = context.getMarche().getDemande(itemVendu.getRarete());
        String indicateurDemande;
        if (demande >= 1.3) {
            indicateurDemande = "🟢🟢 Excellente";
        } else if (demande >= 1.1) {
            indicateurDemande = "🟢 Bonne";
        } else if (demande >= 0.9) {
            indicateurDemande = "➡️ Normale";
        } else if (demande >= 0.7) {
            indicateurDemande = "🟡 Faible";
        } else {
            indicateurDemande = "🔴 Très faible";
        }

        context.getUiService().afficherMessage("📊 Demande " + itemVendu.getRarete() + " : " + indicateurDemande);
        context.getUiService().afficherMessage("💰 Total : " + context.getJoueur().getPiecesOr() + " pièces");
        context.getUiService().afficherMessage("");

        String[] options = {"Vendre autre chose", "Retour au village"};
        context.getUiService().attendreChoixAsync(options, context, new GameState() {
            @Override
            public void enter(GameContext ctx) {}
            @Override
            public void exit(GameContext ctx) {}
            @Override
            public String getStateName() { return "VillageVenteSuccess"; }
            @Override
            public void handleAction(GameContext ctx, int choix) {
                if (choix == 0) {
                    afficherMenuVente(ctx);
                } else {
                    afficherMenuPrincipal(ctx);
                }
            }
        });
    }

    private int calculerPrixVente(GameContext context, Item item) {
        int prixBase = switch (item.getRarete()) {
            case COMMUN -> 10;
            case PEU_COMMUN -> 25;
            case RARE -> 50;
            case EPIQUE -> 100;
            case LEGENDAIRE -> 250;
        };

        // Pour les armes et armures, ajuster selon la durabilité
        if (item instanceof Arme arme) {
            double ratio = arme.getDurabilite() / (double) arme.getDurabiliteMax();
            prixBase = (int) (prixBase * (0.5 + 0.5 * ratio));
        } else if (item instanceof Armure armure) {
            double ratio = armure.getDurabilite() / (double) armure.getDurabiliteMax();
            prixBase = (int) (prixBase * (0.5 + 0.5 * ratio));
        }

        // Appliquer les fluctuations du marché
        return context.getMarche().calculerPrixVente(prixBase, item.getRarete());
    }

    private void afficherEtatMarche(GameContext context) {
        context.getUiService().afficherMessage("");
        context.getUiService().afficherMessage(context.getMarche().getMessageMarche());
        context.getUiService().afficherMessage("");

        String[] options = {"Retour au village"};
        context.getUiService().attendreChoixAsync(options, context, new GameState() {
            @Override
            public void enter(GameContext ctx) {}
            @Override
            public void exit(GameContext ctx) {}
            @Override
            public String getStateName() { return "VillageEtatMarche"; }
            @Override
            public void handleAction(GameContext ctx, int c) {
                afficherMenuPrincipal(ctx);
            }
        });
    }
}

