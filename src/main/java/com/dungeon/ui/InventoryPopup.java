package com.dungeon.ui;

import com.dungeon.model.Inventaire;
import com.dungeon.model.item.Item;
import com.dungeon.model.item.arme.Arme;
import com.dungeon.model.personnage.Joueur;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Fenêtre popup pour afficher l'inventaire
 */
public class InventoryPopup {
    
    public static Stage show(Joueur joueur, Stage parentStage) {
        Inventaire inventaire = joueur.getInventaire();
        Stage popup = new Stage();
        popup.initModality(Modality.NONE);
        popup.initOwner(parentStage);
        popup.initStyle(StageStyle.DECORATED);
        popup.setTitle("Inventaire");
        popup.setResizable(false);

        if (parentStage != null) {
            popup.setX(parentStage.getX() + parentStage.getWidth() + 10);
            popup.setY(parentStage.getY());
        }

        VBox root = new VBox(10);
        root.setPadding(new Insets(25));
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #f4e8d0, #e8dcc0);" +
            "-fx-border-color: #8b7355;" +
            "-fx-border-width: 4;" +
            "-fx-border-style: solid;" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;"
        );

        // Titre
        Label titre = new Label("INVENTAIRE");
        titre.setFont(Font.font("Serif", 28));
        titre.setTextFill(Color.rgb(50, 30, 20));
        titre.setAlignment(Pos.CENTER);
        titre.setStyle("-fx-font-weight: bold;");

        // Informations du joueur
        HBox infoBox = new HBox(20);
        infoBox.setAlignment(Pos.CENTER);

        Label nbItems = new Label(String.format("📦 Objets: %d/10", inventaire.getNombreItems()));
        nbItems.setFont(Font.font("Serif", 14));
        nbItems.setTextFill(Color.rgb(60, 40, 30));
        nbItems.setStyle("-fx-font-weight: bold;");

        Label piecesOr = new Label(String.format("💰 Or: %d pièces", joueur.getPiecesOr()));
        piecesOr.setFont(Font.font("Serif", 14));
        piecesOr.setTextFill(Color.rgb(218, 165, 32));
        piecesOr.setStyle("-fx-font-weight: bold;");

        infoBox.getChildren().addAll(nbItems, piecesOr);

        // Arme équipée
        VBox armeEquipeeBox = createArmeEquipeeBox(joueur, popup);

        // Onglets pour armes et autres items
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-background-color: transparent;");

        // Onglet Armes
        Tab armesTab = new Tab("⚔️ Armes");
        VBox armesContent = createArmesTab(joueur, popup);
        ScrollPane armesScroll = new ScrollPane(armesContent);
        armesScroll.setFitToWidth(true);
        armesScroll.setPrefHeight(350);
        armesScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        armesTab.setContent(armesScroll);

        // Onglet Autres items
        Tab autresTab = new Tab("🎒 Autres");
        VBox autresContent = createAutresTab(joueur, popup);
        ScrollPane autresScroll = new ScrollPane(autresContent);
        autresScroll.setFitToWidth(true);
        autresScroll.setPrefHeight(350);
        autresScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        autresTab.setContent(autresScroll);

        tabPane.getTabs().addAll(armesTab, autresTab);

        // Bouton fermer
        Button closeBtn = new Button("Fermer");
        closeBtn.setFont(Font.font("Serif", 14));
        closeBtn.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #c19a6b, #a67c52);" +
            "-fx-text-fill: black;" +
            "-fx-padding: 10px 25px;" +
            "-fx-font-weight: bold;" +
            "-fx-border-color: #8b6f47;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );
        closeBtn.setOnAction(e -> popup.close());
        
        HBox buttonBox = new HBox(closeBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        
        root.getChildren().addAll(titre, infoBox, armeEquipeeBox, tabPane, buttonBox);

        Scene scene = new Scene(root, 600, 750);
        popup.setScene(scene);
        popup.show();
        return popup;
    }
    
    private static VBox createArmeEquipeeBox(Joueur joueur, Stage popup) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(12));
        box.setStyle(
            "-fx-background-color: rgba(193, 154, 107, 0.3);" +
            "-fx-border-color: #c19a6b;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );

        Label titre = new Label("⚔️ ARME ÉQUIPÉE");
        titre.setFont(Font.font("Serif", 16));
        titre.setTextFill(Color.rgb(50, 30, 20));
        titre.setStyle("-fx-font-weight: bold;");

        if (joueur.getArmeEquipee() == null) {
            Label aucune = new Label("Aucune arme équipée");
            aucune.setFont(Font.font("Serif", 13));
            aucune.setStyle("-fx-font-style: italic;");
            aucune.setTextFill(Color.GRAY);
            box.getChildren().addAll(titre, aucune);
        } else {
            Arme arme = joueur.getArmeEquipee();
            Label nomArme = new Label(arme.getNom());
            nomArme.setFont(Font.font("Serif", 14));
            nomArme.setTextFill(getRareteColor(arme.getRarete()));
            nomArme.setStyle("-fx-font-weight: bold;");

            Label stats = new Label(String.format("ATK: %d | Durabilité: %d/%d (%.0f%%)",
                arme.calculerDegatsTotal(), arme.getDurabilite(), arme.getDurabiliteMax(),
                arme.getPourcentageDurabilite()));
            stats.setFont(Font.font("Serif", 12));
            stats.setTextFill(arme.estCassee() ? Color.RED : Color.rgb(60, 40, 30));

            Button detailsBtn = new Button("Voir détails");
            detailsBtn.setFont(Font.font("Serif", 11));
            detailsBtn.setStyle(
                "-fx-background-color: #8b7355;" +
                "-fx-text-fill: white;" +
                "-fx-padding: 5px 10px;"
            );
            detailsBtn.setOnAction(e -> WeaponDetailsPopup.show(arme, popup));

            box.getChildren().addAll(titre, nomArme, stats, detailsBtn);
        }

        return box;
    }

    private static VBox createArmesTab(Joueur joueur, Stage popup) {
        VBox content = new VBox(8);
        content.setPadding(new Insets(15));
        content.setStyle(
            "-fx-background-color: rgba(255, 250, 240, 0.7);" +
            "-fx-border-color: #a67c52;" +
            "-fx-border-width: 2;" +
            "-fx-border-style: dashed;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );

        List<Item> armes = new ArrayList<>();
        List<Integer> armesIndexes = new ArrayList<>();

        for (int i = 0; i < joueur.getInventaire().getNombreItems(); i++) {
            Item item = joueur.getInventaire().getItem(i);
            if (item instanceof Arme) {
                armes.add(item);
                armesIndexes.add(i);
            }
        }

        if (armes.isEmpty()) {
            Label vide = new Label("~ Aucune arme dans l'inventaire ~");
            vide.setFont(Font.font("Serif", 14));
            vide.setStyle("-fx-font-style: italic; -fx-padding: 30;");
            content.getChildren().add(vide);
        } else {
            for (int i = 0; i < armes.size(); i++) {
                Arme arme = (Arme) armes.get(i);
                int inventoryIndex = armesIndexes.get(i);
                HBox armeBox = createArmeBox(arme, inventoryIndex, joueur, popup);
                content.getChildren().add(armeBox);
            }
        }

        return content;
    }

    private static VBox createAutresTab(Joueur joueur, Stage popup) {
        VBox content = new VBox(8);
        content.setPadding(new Insets(15));
        content.setStyle(
            "-fx-background-color: rgba(255, 250, 240, 0.7);" +
            "-fx-border-color: #a67c52;" +
            "-fx-border-width: 2;" +
            "-fx-border-style: dashed;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );

        List<Item> autres = new ArrayList<>();
        List<Integer> autresIndexes = new ArrayList<>();

        for (int i = 0; i < joueur.getInventaire().getNombreItems(); i++) {
            Item item = joueur.getInventaire().getItem(i);
            if (!(item instanceof Arme)) {
                autres.add(item);
                autresIndexes.add(i);
            }
        }

        if (autres.isEmpty()) {
            Label vide = new Label("~ Aucun autre objet ~");
            vide.setFont(Font.font("Serif", 14));
            vide.setStyle("-fx-font-style: italic; -fx-padding: 30;");
            content.getChildren().add(vide);
        } else {
            for (int i = 0; i < autres.size(); i++) {
                Item item = autres.get(i);
                int inventoryIndex = autresIndexes.get(i);
                HBox itemBox = createItemBox(item, inventoryIndex, joueur, popup);
                content.getChildren().add(itemBox);
            }
        }

        return content;
    }

    private static HBox createArmeBox(Arme arme, int index, Joueur joueur, Stage popup) {
        HBox box = new HBox(10);
        box.setPadding(new Insets(12));
        box.setAlignment(Pos.CENTER_LEFT);

        boolean estEquipee = joueur.getArmeEquipee() == arme;
        String borderColor = estEquipee ? "#d4af37" : "#c4a777";
        String bgColor = estEquipee ? "rgba(212, 175, 55, 0.2)" : "rgba(250, 245, 235, 1)";

        box.setStyle(
            "-fx-background-color: " + bgColor + ";" +
            "-fx-border-color: " + borderColor + ";" +
            "-fx-border-width: " + (estEquipee ? "3" : "2") + ";" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );

        VBox info = new VBox(5);

        Label nom = new Label(arme.getNom() + (estEquipee ? " [ÉQUIPÉE]" : ""));
        nom.setFont(Font.font("Serif", 14));
        nom.setTextFill(getRareteColor(arme.getRarete()));
        nom.setStyle("-fx-font-weight: bold;");

        Label stats = new Label(String.format("ATK: %d | Maîtrise: Niv.%d (+%.0f%%)",
            arme.calculerDegatsTotal(), arme.getNiveauMaitrise(), arme.getBonusMaitrise()));
        stats.setFont(Font.font("Serif", 11));
        stats.setTextFill(Color.rgb(60, 40, 30));

        Label durabilite = new Label(String.format("Durabilité: %d/%d (%.0f%%)",
            arme.getDurabilite(), arme.getDurabiliteMax(), arme.getPourcentageDurabilite()));
        durabilite.setFont(Font.font("Serif", 11));
        Color duraColor = arme.estCassee() ? Color.RED :
                         (arme.getPourcentageDurabilite() < 30 ? Color.ORANGE : Color.rgb(34, 139, 34));
        durabilite.setTextFill(duraColor);

        info.getChildren().addAll(nom, stats, durabilite);

        // Boutons
        HBox boutons = new HBox(5);

        Button equiperBtn = new Button(estEquipee ? "Déséquiper" : "Équiper");
        equiperBtn.setFont(Font.font("Serif", 11));
        equiperBtn.setStyle(
            "-fx-background-color: " + (estEquipee ? "#d9534f" : "#5cb85c") + ";" +
            "-fx-text-fill: white;" +
            "-fx-padding: 6px 12px;" +
            "-fx-font-weight: bold;"
        );

        equiperBtn.setOnAction(e -> {
            if (estEquipee) {
                joueur.setArmeEquipee(null);
            } else {
                if (arme.estCassee()) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.initOwner(popup);
                    alert.setTitle("Arme cassée");
                    alert.setContentText("Cette arme est cassée ! Réparez-la dans une boutique avant de l'équiper.");
                    alert.showAndWait();
                    return;
                }
                joueur.setArmeEquipee(arme);
            }
            popup.close();
            show(joueur, popup.getOwner() != null ? (Stage) popup.getOwner() : null);
        });

        Button detailsBtn = new Button("Détails");
        detailsBtn.setFont(Font.font("Serif", 11));
        detailsBtn.setStyle(
            "-fx-background-color: #5bc0de;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 6px 12px;"
        );
        detailsBtn.setOnAction(e -> WeaponDetailsPopup.show(arme, popup));

        boutons.getChildren().addAll(equiperBtn, detailsBtn);

        box.getChildren().addAll(info, boutons);
        HBox.setHgrow(info, javafx.scene.layout.Priority.ALWAYS);

        return box;
    }

    private static HBox createItemBox(Item item, int index, Joueur joueur, Stage popup) {
        HBox box = new HBox(10);
        box.setPadding(new Insets(12));
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #faf5eb, #f0e6d2);" +
            "-fx-border-color: #c4a777;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );
        
        // Numéro
        Label numero = new Label(String.valueOf(index + 1) + ".");
        numero.setFont(Font.font("Serif", 18));
        numero.setTextFill(Color.rgb(100, 60, 30));
        numero.setMinWidth(30);
        numero.setStyle("-fx-font-weight: bold;");

        // Nom et rareté
        VBox info = new VBox(5);
        
        Label nom = new Label(item.getNom());
        nom.setFont(Font.font("Serif", 16));
        nom.setTextFill(getRareteColor(item.getRarete()));
        nom.setStyle("-fx-font-weight: bold;");
        
        Label rarete = new Label("[" + item.getRarete().getNom() + "]");
        rarete.setFont(Font.font("Serif", 12));
        rarete.setTextFill(getRareteColor(item.getRarete()));
        rarete.setStyle("-fx-font-style: italic;");
        
        Label description = new Label(item.getDescription());
        description.setFont(Font.font("Serif", 11));
        description.setTextFill(Color.rgb(60, 40, 30));
        description.setWrapText(true);
        
        info.getChildren().addAll(nom, rarete, description);
        
        // Bouton Utiliser
        Button useBtn = new Button(item.estConsommable() ? "Utiliser" : "Équiper");
        useBtn.setFont(Font.font("Serif", 12));
        useBtn.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #7fb069, #5a8c4f);" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8px 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-border-color: #4a6b3e;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );
        
        useBtn.setOnMouseEntered(e -> useBtn.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #8fc079, #6a9c5f);" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8px 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-border-color: #5a7b4e;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        ));
        
        useBtn.setOnMouseExited(e -> useBtn.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #7fb069, #5a8c4f);" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8px 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-border-color: #4a6b3e;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        ));
        
        useBtn.setOnAction(e -> {
            String message = item.utiliser(joueur);
            
            // Afficher le résultat dans une alerte
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.initOwner(popup);
            alert.setTitle("Item utilisé");
            alert.setHeaderText(null);
            alert.setContentText(message);
            
            // Style parchemin pour l'alerte
            alert.getDialogPane().setStyle(
                "-fx-background-color: linear-gradient(to bottom, #f4e8d0, #e8dcc0);"
            );
            
            alert.showAndWait();
            
            // Si l'item est consommable, le retirer et fermer la popup pour la rafraîchir
            if (item.estConsommable()) {
                joueur.getInventaire().retirerItem(index);
                popup.close();
                // Rouvrir l'inventaire mis à jour
                show(joueur, popup.getOwner() != null ? (Stage) popup.getOwner() : null);
            }
        });
        
        box.getChildren().addAll(numero, info, useBtn);
        HBox.setHgrow(info, javafx.scene.layout.Priority.ALWAYS);
        
        return box;
    }
    
    private static Color getRareteColor(com.dungeon.model.item.Rarete rarete) {
        return switch (rarete) {
            case COMMUN -> Color.rgb(105, 105, 105);
            case PEU_COMMUN -> Color.rgb(34, 139, 34);
            case RARE -> Color.rgb(30, 144, 255);
            case EPIQUE -> Color.rgb(138, 43, 226);
            case LEGENDAIRE -> Color.rgb(218, 165, 32);
        };
    }
}
